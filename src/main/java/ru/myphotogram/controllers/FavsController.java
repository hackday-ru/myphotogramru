package ru.myphotogram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.service.FavsService;
import ru.myphotogram.service.UserService;

import java.util.Map;

/**
 * Created by Eugene on 16.04.16.
 */
@Controller
public class FavsController {

    @Autowired
    private UserService userService;

    @Autowired
    private FavsService favsService;

    @RequestMapping(value = "favs", method = RequestMethod.GET)
    public String geo(Model model) {
        User user = userService.getUserWithAuthoritiesByLogin("user").get();
        Map<String, Photo> photos = favsService.photos(user);
        model.addAttribute("favs", photos);
        return "favorites";
    }

}
