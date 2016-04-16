package ru.myphotogram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.service.GeoLineService;
import ru.myphotogram.service.UserService;

import java.util.List;
import java.util.Map;

/**
 * Created by Eugene on 16.04.16.
 */
@Controller
public class GeoController {

    @Autowired
    private UserService userService;

    @Autowired
    private GeoLineService geoLineService;

    @RequestMapping(value = "geo", method = RequestMethod.GET)
    public String geo(Model model) {
        User user = userService.getUserWithAuthoritiesByLogin("user").get();
        Map<String, List<Photo>> photos = geoLineService.photos(user);
        model.addAttribute("photos", photos);
        return "geo";
    }

    @RequestMapping(value = "geo/album/{album}", method = RequestMethod.GET)
    public String geoAlbum(@PathVariable("album") String album, Model model) {
        User user = userService.getUserWithAuthoritiesByLogin("user").get();
        Map<String, List<Photo>> photos = geoLineService.photos(user);
        model.addAttribute("photos", photos.get(album));
        return "geo-album";
    }

}
