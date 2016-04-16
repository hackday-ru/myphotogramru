package ru.myphotogram.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Eugene on 16.04.16.
 */
@Controller
public class TimeLineController {

    @RequestMapping(value = {"/", "timeline"}, method = RequestMethod.GET)
    public String timeLine() {
        return "timeline";

    }
}
