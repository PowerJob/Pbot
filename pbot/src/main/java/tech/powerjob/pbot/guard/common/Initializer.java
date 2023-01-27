package tech.powerjob.pbot.guard.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Initializer
 *
 * @author tjq
 * @since 2023/1/27
 */
@Configuration
public class Initializer {

    @Bean
    @ConfigurationProperties(prefix = "powerjob.guard")
    public GuardConfig initGuardConfig() {
        return new GuardConfig();
    }
}
