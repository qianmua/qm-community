package pres.hjc.community.event;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pres.hjc.community.entity.MessagePO;
import pres.hjc.community.service.MessageService;
import pres.hjc.community.tools.KafkaCommunityConstant;
import pres.hjc.community.vo.EventVO;

import java.util.Date;
import java.util.HashMap;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/7  17:26
 * @description :
 */
@Component
@Slf4j
public class EventConsumer implements KafkaCommunityConstant {

    @Autowired
    private MessageService messageService;

    /**
     * 消费
     * 订阅 消费 主题
     */
    @KafkaListener( topics = {TOPIC_COMMENT , TOPIC_FOLLOW , TOPIC_LIKE})
    public void handleCommentMessage(ConsumerRecord consumerRecord){
        if (consumerRecord == null || consumerRecord.value() == null){
            log.error("消息为空 ： null");
            return;
        }

        // 原始数据
        EventVO eventVO = JSONObject.parseObject(consumerRecord.value().toString(), EventVO.class);
        if (eventVO == null){
            log.error("消息 格式 错误");
            return;
        }

        // 发送通知
        val messagePO = new MessagePO();
        messagePO.setFromId(SYSTEM_USER_ID);
        messagePO.setToId(eventVO.getEntityUserId());
        messagePO.setConversationId(eventVO.getTopic());
        messagePO.setStatus(0);
        messagePO.setCreateTime(new Date());

        // VO
        val content = new HashMap<String, Object>();
        content.put("userId", eventVO.getUserId());
        content.put("entityType", eventVO.getEntityType());
        content.put("entityId", eventVO.getEntityId());

        if (!eventVO.getData().isEmpty()){
            eventVO.getData().forEach(content::put);
        }
        // 推送消息
        messagePO.setContent(JSONObject.toJSONString(content));

//        messageService.insertMessage( messagePO);
    }


}
