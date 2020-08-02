package pres.hjc.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pres.hjc.community.CommunityBootstrapApplication;
import pres.hjc.community.dao.UserMapper;

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

    @Test
    public void testUserInfo(){
        System.out.println(userMapper.selectById(0));
    }
}
