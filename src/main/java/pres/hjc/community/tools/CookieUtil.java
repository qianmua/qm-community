package pres.hjc.community.tools;

import lombok.val;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  18:48
 * @description :
 */
public class CookieUtil {

    /**
     * get value
     * @param request request
     * @param key key
     * @return value
     */
    public static String getValue(HttpServletRequest request, String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        val cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }
}
