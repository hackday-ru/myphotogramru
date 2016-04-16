package ru.myphotogram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.myphotogram.domain.User;
import ru.myphotogram.grabber.DropboxGrabber;
import ru.myphotogram.grabber.Grabber;
import ru.myphotogram.service.UserService;

import javax.inject.Inject;

/**
 * Created by Eugene on 16.04.16.
 */
@Controller
public class InitController {

    @Autowired
    @Qualifier("dropboxGrabber")
    private Grabber dropboxGrabber;

    @Autowired
    private UserService service;

    @RequestMapping(value = "init", method = RequestMethod.GET)
    public String init() {
        User user = service.getUserWithAuthoritiesByLogin("user").get();
        dropboxGrabber.grabPhotos(user);
        return "timeline";
    }
}
