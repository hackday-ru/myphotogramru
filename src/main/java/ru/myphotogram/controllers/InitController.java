package ru.myphotogram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.myphotogram.domain.Authority;
import ru.myphotogram.domain.User;
import ru.myphotogram.grabber.Grabber;
import ru.myphotogram.repository.AuthorityRepository;
import ru.myphotogram.service.UserService;

/**
 * Created by Eugene on 16.04.16.
 */
@Controller
public class InitController {

    @Autowired
    @Qualifier("dropboxGrabber")
    private Grabber dropboxGrabber;

    @Autowired
    @Qualifier("instagramGrabber")
    private Grabber instagramGrabber;

    @Autowired
    private UserService service;

    @Autowired
    private AuthorityRepository authorityRepository;


    @RequestMapping(value = "init", method = RequestMethod.GET)
    public String init() {
        Authority authority = new Authority();
        authority.setName("ROLE_USER");
        authorityRepository.save(authority);
        User user = service.createUserInformation("user", "password", "User", "User", "1@1.1", "en");
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(user, "user"));
        dropboxGrabber.grabPhotos(user);
        instagramGrabber.grabPhotos(user);
        return "timeline";
    }
}
