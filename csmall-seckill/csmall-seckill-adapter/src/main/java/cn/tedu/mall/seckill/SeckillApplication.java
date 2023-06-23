package cn.tedu.mall.seckill;

import cn.tedu.mall.common.config.MallCommonConfiguration;
import cn.tedu.mall.seckill.timer.SeckillInitializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("cn.tedu.mall.seckill.mapper")
@Import(MallCommonConfiguration.class)
@Slf4j
@EnableDubbo
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication springApplication=new SpringApplication(SeckillApplication.class);
        springApplication.addListeners(new ApplicationListener<ApplicationReadyEvent>() {
            @Override public void onApplicationEvent(ApplicationReadyEvent event) {
                log.info("秒杀服务启动,加载初始化");
                SeckillInitializer initializer = event.getApplicationContext().getBean(SeckillInitializer.class);
                initializer.startInit();
                initializer.scheduleStock();
            }
        });
        ConfigurableApplicationContext run = springApplication.run(args);

    }
}
