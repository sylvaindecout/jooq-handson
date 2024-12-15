package fr.sdecout.handson

import org.jooq.conf.RenderNameCase.LOWER
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun configurationCustomizer() = DefaultConfigurationCustomizer { it.settings().withRenderNameCase(LOWER) }

}
