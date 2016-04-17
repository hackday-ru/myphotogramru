package ru.myphotogram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.myphotogram.domain.Authority;
import ru.myphotogram.domain.User;
import ru.myphotogram.grabber.Grabber;
import ru.myphotogram.repository.AuthorityRepository;
import ru.myphotogram.service.UserService;

/**
 * Created by Eugene on 17.04.16.
 */
@Component
public class InitApplicationListener {

    @Autowired
    @Qualifier("dropboxGrabber")
    private Grabber dropboxGrabber;

    @Autowired
    @Qualifier("instagramGrabber")
    private Grabber instagramGrabber;

    @Autowired
    @Qualifier("vkGrabber")
    private Grabber vkGrabber;

    @Autowired
    private UserService service;

    @Autowired
    private AuthorityRepository authorityRepository;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        Authority authority = new Authority();
        authority.setName("ROLE_USER");
        authorityRepository.save(authority);
        User user = service.createUserInformation("user", "password", "User", "User", "1@1.1", "en");
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(user, "user"));
        dropboxGrabber.grabPhotos(user);
        instagramGrabber.grabPhotos(user);
        vkGrabber.grabPhotos(user);
    }
}
