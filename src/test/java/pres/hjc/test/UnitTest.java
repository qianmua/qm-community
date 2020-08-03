package pres.hjc.test;

import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pres.hjc.community.CommunityBootstrapApplication;
import pres.hjc.community.dao.UserMapper;
import pres.hjc.community.tools.MailClientUtil;

import java.util.ArrayList;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  13:11
 * @description :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityBootstrapApplication.class)
public class UnitTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    MailClientUtil mailClientUtil;

    /**
     * 模板生成
     */
    @Autowired
    TemplateEngine templateEngine;

    @Test
    public void testUserInfo(){
        System.out.println(userMapper.selectById(0));
    }

    /**
     * 测试模板生成
     */
    @Test
    public void genTemp(){
        Context context = new Context();

        context.setVariable("username" , "qianmu");
        context.setVariable("message" , "guguguugugu");
        String process = templateEngine.process("/mail/demoTemplate", context);
        System.out.println(process);
    }



    @Test
    public void t1(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);

        val iterator = integers.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
