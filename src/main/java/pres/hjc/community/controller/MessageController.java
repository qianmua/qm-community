package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pres.hjc.community.dto.PageDTO;
import pres.hjc.community.entity.MessagePO;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.MessageService;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.CommunityUtil;
import pres.hjc.community.tools.HostHolder;

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
public class MessageController {

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


}
