package tech.powerjob.pbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class PbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PbotApplication.class, args);
    }

}
