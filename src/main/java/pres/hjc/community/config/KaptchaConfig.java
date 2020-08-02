package pres.hjc.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  17:48
 * @description : 验证码生成
 */
@Configuration
public class KaptchaConfig {


    /**
     * 执行验证码 生成
     * @return Producer img
     */
    @Bean
    public Producer kaptchaProducer(){
        Properties properties = new Properties();

        // config
        properties.setProperty("kaptcha.image.width" , "100");
        properties.setProperty("kaptcha.image.height" , "40");
        properties.setProperty("kaptcha.textproducer.font.size" , "32");
        properties.setProperty("kaptcha.textproducer.font.color" , "0,0,0");
        properties.setProperty("kaptcha.textproducer.char.String" , "0123456789QWERTYIOPASDFGHJKLZXCVBNM");

        // 字符数
        properties.setProperty("kaptcha.textproducer.char.length" , "4");
        // 干扰
        properties.setProperty("kaptcha.noise.impl" , "com.google.code.kaptcha.impl.NoNoise");

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
