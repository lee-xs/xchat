package cn.alees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("cn.alees.mapper")
public class XChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(XChatApplication.class, args);
    }

}
