package pres.hjc.community.tools;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  16:18
 * @description :
 */
public class CommunityUtil {


    /**
     * uuid
     * @return
     */
    public static String UUID(){
        return UUID.randomUUID().toString().replace("-" , "");

    }

    /**
     * md5
     * @param password password
     * @return str
     */
    public static String MD5(String password){
        return (StringUtils.isEmpty(password)) ? null : DigestUtils.md5DigestAsHex(password.getBytes());
    }

    /**
     * json 转换器
     * @param code code 编码
     * @param msg msg  提示信息
     * @param map map 数据
     * @return json
     */
    public static String getJSONString(int code , String msg , Map<String ,Object> map){
        JSONObject object = new JSONObject();

        object.put("code" , code);
        object.put("msg" , msg);
        if (!map.isEmpty()){
            map.forEach(object::put);
        }
        return object.toJSONString();
    }

    /**
     * json 转换器
     * @param code 编码
     * @param msg 提示信息
     * @return json
     */
    public static String getJSONString(int code , String msg){
        return getJSONString(code, msg, new HashMap<>(1)) ;
    }

    /**
     * json 转换器
     * @param code 编码
     * @return json
     */
    public static String getJSONString(int code){
        return getJSONString(code , null , new HashMap<>(1));
    }

}
