package cn.zeroeden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: Zero
 * @time: 2023/5/16
 * @description:
 */

@SpringBootApplication
@EnableTransactionManagement
public class BilibiliApp {
    public static void main(String[] args) {
        SpringApplication.run(BilibiliApp.class, args);
    }
}
