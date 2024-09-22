package net.cechacek.examples.users;

import lombok.RequiredArgsConstructor;
import net.cechacek.examples.users.domain.User;
import net.cechacek.examples.users.security.JwtProperties;
import net.cechacek.examples.users.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
class ApplicationConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(10); //reload messages every 10 seconds
        return messageSource;
    }

    @Bean
    public CommandLineRunner run(UserService userService) {
        return args -> {
            userService.create(new User(null, "John Doe", "john@example.com", "secret"));
        };
    }
}
