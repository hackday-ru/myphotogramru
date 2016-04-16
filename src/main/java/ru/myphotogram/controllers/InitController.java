package ru.myphotogram.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.myphotogram.domain.User;
import ru.myphotogram.grabber.DropboxGrabber;
import ru.myphotogram.service.UserService;

import javax.inject.Inject;

/**
 * Created by Eugene on 16.04.16.
 */
@Controller
public class InitController {

    @Inject
    DropboxGrabber grabber;

    @Inject
    UserService service;

    @RequestMapping(value = "init", method = RequestMethod.GET)
    public String init() {
        User user = service.getUserWithAuthoritiesByLogin("user").get();
        grabber.grabPhotos(user);
        return "timeline";
    }
}
