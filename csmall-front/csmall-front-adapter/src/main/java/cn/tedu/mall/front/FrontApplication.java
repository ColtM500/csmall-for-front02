package cn.tedu.mall.front;

import cn.tedu.mall.common.config.MallCommonConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("cn.tedu.mall.front.mapper")
@Import(MallCommonConfiguration.class)
public class FrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontApplication.class, args);
    }
}
