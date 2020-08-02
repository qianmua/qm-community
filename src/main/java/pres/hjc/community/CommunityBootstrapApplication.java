package pres.hjc.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  12:51
 * @description : 启动引导类
 */
@SpringBootApplication
@MapperScan(basePackages = {"pres.hjc.community.dao"})
public class CommunityBootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityBootstrapApplication.class , args);
    }

}
