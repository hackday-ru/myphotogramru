package ru.myphotogram.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.service.TimelineService;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Created by Eugene on 16.04.16.
 */
@Controller
public class TimeLineController {

    @Inject
    TimelineService service;

    @RequestMapping(value = {"/", "timeline"}, method = RequestMethod.GET)
    public String timeLine(Model model) {
        Map<Integer, List<Photo>> photos = service.photos();
        model.addAttribute("photos", photos);
        return "timeline";

    }

    @RequestMapping(value = "timeline/year/{year}", method = RequestMethod.GET)
    public String timeLineYear(@PathVariable("year") int year, Model model) {
        Map<Integer, List<Photo>> photos = service.photos(year);
        model.addAttribute("photos", photos);
        return "timeline";
    }

    @RequestMapping(value = "timeline/year/{year}/month/{month}", method = RequestMethod.GET)
    public String timeLineMonth(@PathVariable("year") int year, @PathVariable("month") int month, Model model) {
        Map<Integer, List<Photo>> photos = service.photos(year, month);
        model.addAttribute("photos", photos);
        return "timeline";
    }

}
