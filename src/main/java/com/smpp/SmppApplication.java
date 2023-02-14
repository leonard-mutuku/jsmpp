package com.smpp;

import com.smpp.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SmppApplication {

    public static void main(String[] args) {
        final ApplicationContext ctx = SpringApplication.run(SmppApplication.class, args);
        final ApplicationProperties confs = ctx.getBean(ApplicationProperties.class);
        System.out.println("configs:\n" + confs.getSmpp().toString() + "\n" + confs.getClient().toString());
    }

}
