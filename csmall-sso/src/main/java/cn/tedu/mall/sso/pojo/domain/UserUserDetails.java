package cn.tedu.mall.sso.pojo.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class UserUserDetails implements Serializable, UserDetails {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String phone;
    private String email;
    private Integer isEnable;
    private Integer rewardPoint;
    private String lastLoginIp;
    private Integer loginCount;
    private Date gmtLastLogin;
    private Date gmtCreate;
    private Date gmtModified;
    private List<String> authorities;

    public UserUserDetails() {
        authorities =new ArrayList<>();
        authorities.add("ROLE_user");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities=new ArrayList<>();
        if (this.authorities==null||this.authorities.size()==0){
            return null;
        }
        for (String authority : this.authorities) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
