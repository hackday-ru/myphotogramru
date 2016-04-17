package ru.myphotogram.controllers;

import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import ru.myphotogram.grabber.Grabber;
import ru.myphotogram.repository.AuthorityRepository;
import ru.myphotogram.repository.UserRepository;

import javax.inject.Inject;

/**
 * Created by Роман on 17.04.2016.
 */
@Controller
public class InstagramController {

    @Inject
    InstagramService instagramService;

    @Inject
    Grabber instagramGrabber;

    @Inject
    UserRepository repository;

    @RequestMapping(path = "instagram/signin")
    public RedirectView singin() {
        return new RedirectView(instagramService.getAuthorizationUrl());
    }

    @RequestMapping(path = "instagram/finish-auth")
    public RedirectView finishAuth(@RequestParam String code) {
        instagramGrabber.setToken(instagramService.getAccessToken(new Verifier(code)).getToken());
        instagramGrabber.grabPhotos(repository.findOneByEmail("1@1.1").get());
        return new RedirectView("/timeline");
    }

}
