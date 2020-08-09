package pres.hjc.community.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pres.hjc.community.entity.UserPO;

import java.util.Collection;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/9  16:05
 * @description :
 */
public class UserDetailDTO extends UserPO implements UserDetails {


    /**
     * 权限集
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * ture 账号未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * true 未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * ture 凭证未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * true 账号 可用
     */
    @Override
    public boolean isEnabled() {
        return false;
    }
}
