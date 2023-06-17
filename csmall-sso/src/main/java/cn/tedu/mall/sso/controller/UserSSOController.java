package cn.tedu.mall.sso.controller;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.sso.pojo.bo.UserLoginBO;
import cn.tedu.mall.sso.pojo.dto.UserLoginDTO;
import cn.tedu.mall.sso.pojo.vo.TokenVO;
import cn.tedu.mall.sso.security.service.user.IUserSSOService;
import cn.tedu.mall.sso.utils.LoginUtils;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 实现前台用户登录的请求和登出请求
 */
@RestController
@RequestMapping("/user/sso")
@Api(tags = "前台管理用户认证")
@Slf4j
public class UserSSOController {
    @Autowired
    private IUserSSOService userSSOService;
    @Value("${jwt.tokenHead}")
    private String jwtTokenHead;
    @ApiOperation(value = "前台单点登录认证登录")
    @PostMapping("/login")
    public JsonResult<TokenVO> doLogin(@Valid UserLoginDTO userLoginDTO, @ApiIgnore HttpServletRequest request){
        UserLoginBO userLoginBO=assembleUserLoginBO(request,userLoginDTO);
        String token = userSSOService.doLogin(userLoginBO);
        TokenVO tokenVO = new TokenVO();
        tokenVO.setTokenHeader(jwtTokenHead);
        tokenVO.setTokenValue(token);
        return JsonResult.ok(tokenVO);
    }

    private UserLoginBO assembleUserLoginBO(HttpServletRequest request, UserLoginDTO dto) {
        String ip = LoginUtils.getIpAddress(request);
        log.info("远程ip地址:{}",ip);
        String sessionId = request.getSession().getId();
        UserLoginBO userLoginBO = new UserLoginBO();
        userLoginBO.setIp(ip);
        String userAgent = request.getHeader("User-Agent");
        userLoginBO.setUserAgent(userAgent.substring(0,userAgent.indexOf("(")).trim());
        userLoginBO.setUsername(dto.getUsername());
        userLoginBO.setPassword(dto.getPassword());
        log.debug("封装了userbo对象:{}", JSON.toJSONString(userLoginBO));
        return userLoginBO;
    }

    /**
     * <p>登出logout</p>
     * <p>没有任何实际业务逻辑</p>
     */
    @ApiOperation(value = "前台单点登录认证登出")
    @PostMapping("/logout")
    public JsonResult doLogout(@RequestHeader(name = "Authorization") String token){
        userSSOService.doLogout(token);
        return JsonResult.ok();
    }
}
