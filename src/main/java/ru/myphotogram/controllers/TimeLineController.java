package ru.myphotogram.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.service.TimelineService;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
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
        User user = null;
        Map<Integer, List<Photo>> photos = service.photos(user);

        List<Photo> photosQ = new ArrayList<>();
        photosQ.add(getPhoto());
        photosQ.add(getPhoto());
        photosQ.add(getPhoto());
        photos.put(2016, photosQ);

        List<Photo> photosQ1 = new ArrayList<>();
        photosQ1.add(getPhoto());
        photosQ1.add(getPhoto());
        photosQ1.add(getPhoto());
        photosQ1.add(getPhoto());
        photos.put(2015, photosQ1);

        model.addAttribute("photos", photos);
        return "timeline";

    }

    private Photo getPhoto() {
        Photo p = new Photo();
        p.setCreationDate(LocalDate.now());
        p.setUrl("http://127.0.0.1:8080/assets/images/development_ribbon.png");
        return p;
    }

    @RequestMapping(value = "timeline/year/{year}", method = RequestMethod.GET)
    public String timeLineYear(@PathVariable("year") int year, Model model) {
        User user = null;
        Map<Integer, List<Photo>> photos = service.photos(user, year);
        model.addAttribute("photos", photos);
        return "timeline";
    }

    @RequestMapping(value = "timeline/year/{year}/month/{month}", method = RequestMethod.GET)
    public String timeLineMonth(@PathVariable("year") int year, @PathVariable("month") int month, Model model) {
        User user = null;
        Map<Integer, List<Photo>> photos = service.photos(user, year, month);
        model.addAttribute("photos", photos);
        return "timeline";
    }

}
