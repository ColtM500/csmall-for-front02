package cn.tedu.test.tedu.front;

import cn.tedu.mall.common.config.MallCommonConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(MallCommonConfiguration.class)
@SpringBootApplication
@MapperScan("cn.tedu.mall.front.mapper")
public class FrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontApplication.class, args);
    }
}
