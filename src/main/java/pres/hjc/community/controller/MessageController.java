package pres.hjc.community.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pres.hjc.community.dto.PageDTO;
import pres.hjc.community.entity.MessagePO;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.MessageService;
import pres.hjc.community.service.UserService;
import pres.hjc.community.tools.HostHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            HashMap<String, Object> hashMap = new HashMap<>();

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
        model.addAttribute("target" , getLetterTarget(convId));

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


}
