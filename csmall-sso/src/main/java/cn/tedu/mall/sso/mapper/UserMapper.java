package cn.tedu.mall.sso.mapper;

import cn.tedu.mall.pojo.ums.model.User;
import cn.tedu.mall.pojo.ums.vo.UserVO;


public interface UserMapper {
    User findByUsername(String username);

    UserVO findById(Long id);
}
