package pres.hjc.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pres.hjc.community.CommunityBootstrapApplication;
import pres.hjc.community.filter.WorldsFilter;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/3  17:27
 * @description :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityBootstrapApplication.class)
public class TestFilterSensitive {

    @Autowired
    private WorldsFilter worldsFilter;

    @Test
    public void f1(){
        String str1 = "请不要赌博哦";

        String filter = worldsFilter.filter(str1);
        System.out.println(filter);
    }
}
