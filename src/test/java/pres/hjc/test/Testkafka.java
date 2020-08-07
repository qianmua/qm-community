package pres.hjc.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import pres.hjc.community.CommunityBootstrapApplication;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/7  13:45
 * @description :
 */
@RunWith(Runner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityBootstrapApplication.class)
public class Testkafka {

//    @Autowired
//    private KafkaProvider kafkaProvider;

    @Test
    public void testkafka(){

    }
}

@Component
class KafkaProvider{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic , String content){
        kafkaTemplate.send(topic, content);
    }
}

class KafkaComsumer{
    @KafkaListener( topics = "test")
    public void kafkaCons(ConsumerRecord consumerRecord){
        System.out.println(consumerRecord.value());
    }
}
