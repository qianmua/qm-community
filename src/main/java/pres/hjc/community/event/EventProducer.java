package pres.hjc.community.event;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pres.hjc.community.vo.EventVO;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/7  17:24
 * @description : 消息 生产者
 */
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 处理 事件
     * 谁发的事件
     * @param eventVO eventVO
     */
    public void fireEvent(EventVO eventVO){
        // 发布到主题
        kafkaTemplate.send(eventVO.getTopic() , JSONObject.toJSONString(eventVO));
    }


}
