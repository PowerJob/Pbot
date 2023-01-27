package tech.powerjob.pbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@ComponentScan("tech.powerjob.pbot.guard.persistence")
@EntityScan("tech.powerjob.pbot.guard.persistence.model.*")
@EnableJpaRepositories("tech.powerjob.pbot.guard.persistence.repository.*")

public class PbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PbotApplication.class, args);
    }

}
