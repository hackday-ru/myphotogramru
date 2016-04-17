package ru.myphotogram.config;

import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by Роман on 17.04.2016.
 */
@Configuration
public class ServiceConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public InstagramService instagramService() {
        return new InstagramAuthService()
            .apiKey("ed5fd2c0d59142a4afcbe4aedb5bc86d")
            .apiSecret("8cae5a9c2e13430d9b262d9b74bd336e")
            .callback("https://myphotogramru.herokuapp.com/instagram/finish-auth")
            .build();
    }

}
