package ru.myphotogram.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.myphotogram.grabber.Grabber;

import javax.inject.Inject;

/**
 * Created by Eugene on 16.04.16.
 */
@Controller
public class SourcesController {

    @Inject
    Grabber instagramGrabber;

    @RequestMapping(value = "sources", method = RequestMethod.GET)
    public String sources(Model model) {
        model.addAttribute("hasToken", instagramGrabber.hasToken());
        return "sources";

    }
}
