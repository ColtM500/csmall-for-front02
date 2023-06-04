package cn.tedu.mall.sso.service.impl;

import cn.tedu.mall.common.pojo.domain.CsmallAuthenticationInfo;
import cn.tedu.mall.common.utils.JwtTokenUtils;
import cn.tedu.mall.pojo.ums.vo.UserVO;
import cn.tedu.mall.sso.mapper.UserMapper;
import cn.tedu.mall.sso.pojo.vo.UserInfoVO;
import cn.tedu.mall.sso.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserInfoServiceImpl implements IUserInfoService {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserMapper userMapper;
    private static final String jwtTokenHead ="Bearer";
    @Override
    public UserInfoVO userInfo(String authToken) {
        String token=authToken.substring(jwtTokenHead.length());
        log.info("获取token:{}",token);
        CsmallAuthenticationInfo userInfo = jwtTokenUtils.getUserInfo(token);
        String type=userInfo.getUserType();
        Long id=userInfo.getId();
        //准备返回数据UserInfoVO
        UserInfoVO userInfoVO=new UserInfoVO();
            UserVO userVO=userMapper.findById(id);
            userInfoVO.setUserId(id);
            userInfoVO.setNickname(userVO.getNickname());
            userInfoVO.setPhone(userVO.getPhone());
            userInfoVO.setUsername(userVO.getUsername());
        return userInfoVO;
    }
}
