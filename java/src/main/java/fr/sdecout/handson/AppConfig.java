package fr.sdecout.handson;

import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.jooq.conf.RenderNameCase.LOWER;

@Configuration
public class AppConfig {

    @Bean
    DefaultConfigurationCustomizer configurationCustomizer() {
        return config -> config.settings().withRenderNameCase(LOWER);
    }

}
