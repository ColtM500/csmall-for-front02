package cn.tedu.mall.sso.pojo.dto;

import cn.tedu.mall.common.validation.RegExpressions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

@ApiModel(value="前台用户登录DTO")
@Data
public class UserLoginDTO implements RegExpressions {
    /**
     * 验证不通过时的提示文本的前缀
     */
    private static final String MESSAGE_PREFIX = "登录失败！";

    @ApiModelProperty(value = "登录用户名", name = "username", example = "jackson", required = true)
    @NotNull(message = MESSAGE_PREFIX + "用户名不允许为空！")
    @Pattern(regexp = REGEXP_ADMIN_USERNAME, message = MESSAGE_PREFIX + MESSAGE_ADMIN_USERNAME)
    private String username;

    @ApiModelProperty(value = "登录密码", name = "password", example = "123456", required = true)
    @NotNull(message = MESSAGE_PREFIX + "密码不允许为空！")
    @Pattern(regexp = REGEXP_ADMIN_PASSWORD, message = MESSAGE_PREFIX + MESSAGE_ADMIN_PASSWORD)
    private String password;

}
