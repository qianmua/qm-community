package pres.hjc.community.tools;

import org.springframework.stereotype.Component;
import pres.hjc.community.entity.UserPO;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  20:04
 * @description : 持有 用户 容器 cache
 */
@Component
public class HostHolder {
    private ThreadLocal<UserPO> usersPO = new ThreadLocal<>();

    /**
     * 缓存
     * @param usersPO usersPO
     */
    public void setUsersPO(UserPO usersPO) {
        this.usersPO.set(usersPO);
    }

    /**
     * get
     * @return UserPO
     */
    public UserPO getUsersPO() {
        return this.usersPO.get();
    }

    /**
     * 清除
     * //
     * 内存泄漏 0.0 ~
     */
    public void clear(){
        this.usersPO.remove();
    }
}
