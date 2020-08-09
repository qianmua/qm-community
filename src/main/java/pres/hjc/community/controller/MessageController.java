package pres.hjc.community.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import pres.hjc.community.dto.PageDTO;
import pres.hjc.community.entity.MessagePO;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.MessageService;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.HostHolder;
import pres.hjc.community.tools.KafkaCommunityConstant;

import java.util.*;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/5  12:15
 * @description :
 */
@Controller
@RequestMapping("/message")
public class MessageController implements KafkaCommunityConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    /*@GetMapping("/letter")
    public String getLetterView(){
        return "site/letter";
    }*/

    /**
     * 私信 列表
     * @param model model
     * @param pageDTO  pageDTO
     * @return views
     */
    @GetMapping("/letter/list")
    public String getLetterList(Model model , PageDTO pageDTO){

        UserPO po = hostHolder.getUsersPO();

        pageDTO.setLimit(5);
        /*message/ */
        /*分页路径*/
        pageDTO.setPath("/message/letter/list");
        pageDTO.setRows(messageService.selectConversationCount(po.getId()));

        // 会话列表
        // list<Message>
        val pos = messageService.selectConversations(po.getId(), pageDTO.getOffset(), pageDTO.getLimit());

        // 私信 数量
        ArrayList<Map<String, Object>> cov = new ArrayList<>();

        pos.forEach(v1 -> {
            HashMap<String, Object> hashMap = new HashMap<>(8);

            hashMap.put("conversation" , v1);
            // 未读消息
            hashMap.put("unreadCount" , messageService.selectLetterUnreadCount(po.getId() , v1.getConversationId()));

            //包含消息数
            // 总消息数
            hashMap.put("letterCount" , messageService.selectLetterCount(v1.getConversationId()));

            // 用户信息
            int targetId = po.getId() == v1.getFromId() ? v1.getToId() : v1.getFromId();
            hashMap.put("target" , userService.selectById(targetId));

            cov.add(hashMap);

        });

        // 未读消息 总数
        val unreadCount = messageService.selectLetterUnreadCount(po.getId(), null);
        model.addAttribute("letterUnreadCount" , unreadCount);
        // 未读 通知
        model.addAttribute("noticeUnreadCount",messageService.selectNoticeUnreadCount(po.getId() , null));

        model.addAttribute("conversations" , cov);

        return "site/letter";
    }

    /**
     * 消息查看
     * @param convId convId
     * @param pageDTO pageDTO
     * @return views
     */
    @GetMapping("/letter/detail/{convId}")
    public String getLetterDetail(@PathVariable String convId , PageDTO pageDTO ,Model model){
        pageDTO.setLimit(5);
        pageDTO.setPath("/message/letter/detail/" + convId);
        // 当前消息
        //消息数
        pageDTO.setRows(messageService.selectLetterCount(convId));

        //私信列表
        List<MessagePO> pos = messageService.selectLetters(convId, pageDTO.getOffset(), pageDTO.getLimit());

        //VO
        ArrayList<Map<String, Object>> letters = new ArrayList<>();

        pos.forEach(v1 -> {
            val map = new HashMap<String, Object>(2);

            map.put("letter" , v1);
            map.put("fromUser" , userService.selectById(v1.getFromId()));

            letters.add(map);
        });

        model.addAttribute("letters" , letters);
        // 目标
        model.addAttribute("target" , getLetterTarget(convId));

        // 消费私信
        val ids = getLetterIds(pos);
        if (!ids.isEmpty()){
            messageService.updateStatus(ids);
        }

        return "site/letter-detail";
    }



    /**
     * 得到私信目标
     * ID_ID
     * @param conversationId conversationId
     * @return
     */
    private UserPO getLetterTarget(String conversationId){

        // String[]
        val strings = conversationId.split("_");

        val d0 = Integer.parseInt(strings[0]);
        val d1 = Integer.parseInt(strings[1]);
        // 目标是d1
        //或者 do
        // 双方互相
        return hostHolder.getUsersPO().getId() == d0 ?
                userService.selectById(d1) : userService.selectById(d0);
    }


    /**
     * 发送私信
     * @param toName toName
     * @param content content
     * @return status
     */
    @PostMapping("/letter/send")
    @ResponseBody
    public String sendLetter(String toName , String content){
        val userPO = userService.selectByName(toName);

        if (null == userPO){
            return CommunityUtil.getJSONString(404 , "目标用户不存在");
        }
        // 构造基本 数据
        val messagePO = new MessagePO();
        messagePO.setFromId(hostHolder.getUsersPO().getId());
        messagePO.setToId(userPO.getId());

        // 会话ID
        messagePO.setConversationId(
                messagePO.getFromId() < messagePO.getToId() ?
                        messagePO.getFromId() + "_" + messagePO.getToId():
                        messagePO.getToId() + "_" + messagePO.getFromId()
        );

        messagePO.setContent( content);
        messagePO.setCreateTime(new Date());

        messageService.insertMessage(messagePO);

        return CommunityUtil.getJSONString(0);
    }


    /**
     * 通知列表
     * @param model model
     * @return views
     */
    @GetMapping("/notice/list")
    public String getNoticeList(Model model){
        val usersPO = hostHolder.getUsersPO();
        // 通知 类型

        HashMap<String, Object> msgVo;
        //评论
        var messagePO = messageService.selectLatestNotice(usersPO.getId(), TOPIC_COMMENT);
        msgVo = getMsgEntity(usersPO.getId(), TOPIC_COMMENT , messagePO);
        model.addAttribute("commentNotice" , msgVo.isEmpty() ? null : msgVo);
        //点赞
        messagePO = messageService.selectLatestNotice(usersPO.getId() , TOPIC_LIKE);
        msgVo = getMsgEntity(usersPO.getId(), TOPIC_LIKE , messagePO);
        model.addAttribute("likeNotice" , msgVo.isEmpty() ? null : msgVo);

        ///关注
        messagePO = messageService.selectLatestNotice(usersPO.getId() , TOPIC_FOLLOW);
        msgVo = getMsgEntity(usersPO.getId(), TOPIC_FOLLOW , messagePO);
        model.addAttribute("followNotice" , msgVo.isEmpty() ? null : msgVo);

        // 未读 消息总数
        model.addAttribute("letterUnreadCount" , messageService.selectLetterUnreadCount(usersPO.getId() , null));
        //未读 通知 总数
        model.addAttribute("noticeUnreadCount",messageService.selectNoticeUnreadCount(usersPO.getId() , null));

        return "site/notice";

    }

    private HashMap<String, Object> getMsgEntity(int userId, String type, MessagePO messagePO) {
        HashMap<String, Object> msgVo = new HashMap<>(8);
        if (messagePO != null){
            msgVo.put("message" , messagePO);
            // 转义
            // 正义
            val s = HtmlUtils.htmlUnescape(messagePO.getContent());
            HashMap hashMap = JSONObject.parseObject(s, HashMap.class);
            msgVo.put("user" , userService.selectById( (Integer)hashMap.get("userId") ));
            msgVo.put("entityType" , hashMap.get("entityType"));
            msgVo.put("entityId" , hashMap.get("entityId"));
            msgVo.put("postId" , hashMap.get("postId"));
            // 消息数量
            msgVo.put("count" ,messageService.selectNoticeCount(userId , type));

            // 未读 数量
            msgVo.put("unread" , messageService.selectNoticeUnreadCount(userId , type));

        }
        return msgVo;
    }

    /**
     * 通知 详情 页面
     * @param topic topic [comment , like , follow]
     * @param pageDTO pageDTO
     * @param model model
     * @return views
     */
    @GetMapping("/notice/detail/{topic}")
    public String getNoticeDetail(@PathVariable String topic ,PageDTO pageDTO ,Model model){
        val usersPO = hostHolder.getUsersPO();

        pageDTO.setLimit(10);
        pageDTO.setPath("/message/notice/detail/" + topic);
        pageDTO.setRows(messageService.selectNoticeCount(usersPO.getId() , topic));
        val pos = messageService.selectNotices(usersPO.getId(), topic, pageDTO.getOffset(), pageDTO.getLimit());
        val noticeVOList = new ArrayList<Map<String, Object>>();
        pos.forEach(v1 -> {
            val vo = new HashMap<String , Object>(8);

            val htmlUnescape = HtmlUtils.htmlUnescape(v1.getContent());
            val hashMap = JSONObject.parseObject(htmlUnescape, HashMap.class);

            vo.put("notice" , v1);
            vo.put("user" , userService.selectById( (Integer) hashMap.get("userId") ));
            vo.put("entityType" , hashMap.get("entityType") );
            vo.put("entityId" , hashMap.get("entityId") );
            vo.put("postId" , hashMap.get("postId") );
            // 通知发起者
            vo.put("fromUser" , userService.selectById( v1.getFromId()) );

            noticeVOList.add(vo);

        });

        model.addAttribute("notices" , noticeVOList);

        // 已读
        val letterIds = getLetterIds(pos);
        if (!letterIds.isEmpty()){
            messageService.updateStatus(letterIds);
        }

        return "site/notice-detail";

    }


    /**
     * 消费私信
     * @param letterList letterList
     * @return
     */
    private List<Integer> getLetterIds(List<MessagePO> letterList){
        val list = new ArrayList<Integer>();
        letterList.forEach(v1 -> {
            // 未消费列表
            if (hostHolder.getUsersPO().getId() == v1.getToId() && v1.getStatus() == 0){
                list.add(v1.getId());
            }
        });
        return list;
    }


}
