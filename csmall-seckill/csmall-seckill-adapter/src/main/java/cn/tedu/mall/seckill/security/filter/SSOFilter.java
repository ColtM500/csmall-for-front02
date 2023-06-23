package cn.tedu.mall.seckill.security.filter;

import cn.tedu.mall.common.config.PrefixConfiguration;
import cn.tedu.mall.common.pojo.domain.CsmallAuthenticationInfo;
import cn.tedu.mall.common.utils.JwtTokenUtils;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <p>单点登录过滤器</p>
 */
@Component
@Slf4j
public class SSOFilter extends OncePerRequestFilter {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Value("${jwt.tokenHead}")
    private String jwtTokenHead;

    private static final String REQUEST_HEADER_AUTHORIZATION = "Authorization";

    @Override
    protected void doFilterInternal(
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        //准备操作hash的对象
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        //获取AUTHORIZATION头
        String authHeader = httpServletRequest.getHeader(REQUEST_HEADER_AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(jwtTokenHead)) {
            String authToken = authHeader.substring(jwtTokenHead.length()+1);
            CsmallAuthenticationInfo userInfo = jwtTokenUtils.getUserInfo(authToken);
            Long id = userInfo.getId();
            String userTokenKey = PrefixConfiguration.SSOPrefixConfiguration.USER_LOGIN_PREFIX + id;
            String redisToken = (String) operations.get(userTokenKey, "token");
            Boolean redisEnabled = (Boolean) operations.get(userTokenKey, "enabled");
            List<GrantedAuthority> redisAuthorities = (List<GrantedAuthority>) operations.get(userTokenKey, "authorities");
            Long redisExpiredTime = (Long) operations.get(userTokenKey, "expiredTime");
            //检查token是否相同,检查是否enabled,检查是否过期,进行续租操作
            if (redisToken != null && redisToken.equals(authToken)) {
                log.debug("token相同");
            }else {
                log.debug("token不同");
                SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            if (redisEnabled != null && redisEnabled) {
                log.debug("user enabled");
            }else {
                log.debug("user not enabled");
                SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            if (redisExpiredTime != null && redisExpiredTime > System.currentTimeMillis()) {
                log.debug("token未过期");
                //进入续期操作
                operations.put(userTokenKey, "expiredTime", System.currentTimeMillis() + jwtTokenUtils.getExpiration());
            }else {
                log.debug("token已过期");
                SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

                UsernamePasswordAuthenticationToken authentication = null;

            if (userInfo != null) {

                List<GrantedAuthority> authorities = redisAuthorities;

                authentication =
                    new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                /*throw new CoolSharkServiceException(ResponseCode.UNAUTHORIZED,"您的token不正确,请重新登录");*/
                SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
