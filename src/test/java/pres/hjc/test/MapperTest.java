package pres.hjc.test;

import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pres.hjc.community.CommunityBootstrapApplication;
//import pres.hjc.community.dao.LoginTicketMapper;
import pres.hjc.community.entity.LoginTicketPO;

import java.util.Date;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  18:21
 * @description :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityBootstrapApplication.class)
public class MapperTest {


    @Autowired
//    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testAdd(){

        val po = new LoginTicketPO();

        po.setId(101);
        po.setTicket("qianmuna");
        po.setStatus(0);
        po.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 24 * 24));

//        val i = loginTicketMapper.insertLoginTicket(po);


    }


    @Test
    public void queryTic(){
//        val qianmuna = loginTicketMapper.selectByTicket("qianmuna");
        // success
//        System.out.println(qianmuna);
    }
}
