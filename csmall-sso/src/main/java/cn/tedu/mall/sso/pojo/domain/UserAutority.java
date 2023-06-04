package cn.tedu.mall.sso.pojo.domain;

import java.io.Serializable;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
@Data
public class UserAutority implements Serializable, GrantedAuthority {
    private String authority;
}
