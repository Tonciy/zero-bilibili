package cn.zeroeden;

import cn.zeroeden.service.websocket.WebSocketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: Zero
 * @time: 2023/5/16
 * @description:
 */

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
public class BilibiliApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext ap = SpringApplication.run(BilibiliApp.class, args);
        // 多例模式下注入单例bean
        WebSocketService.setApplicationContext(ap);
    }
}
