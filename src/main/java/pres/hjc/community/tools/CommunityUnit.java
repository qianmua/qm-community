package pres.hjc.community.tools;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  16:18
 * @description :
 */
public class CommunityUnit {


    /**
     * uuid
     * @return
     */
    public static String UUID(){
        return UUID.randomUUID().toString().replace("-" , "");

    }

    /**
     * md5
     * @param password
     * @return
     */
    public static String MD5(String password){
        return (StringUtils.isEmpty(password)) ? null : DigestUtils.md5DigestAsHex(password.getBytes());
    }


}
