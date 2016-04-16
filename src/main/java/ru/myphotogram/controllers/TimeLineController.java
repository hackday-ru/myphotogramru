package ru.myphotogram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.service.TimelineService;
import ru.myphotogram.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class TimeLineController {

    private final TimelineService timelineService;
    private final UserService userService;

    @Autowired
    public TimeLineController(TimelineService timelineService, UserService userService) {
        this.timelineService = timelineService;
        this.userService = userService;
    }

    @RequestMapping(value = {"/", "timeline"}, method = RequestMethod.GET)
    public String timeLine(Model model, Authentication authentication) {
        User user = null;
        if (authentication.getPrincipal() == null) {
            user = userService.createUserInformation("user", "password", "User", "User", "1@1.1", "en");
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, "user"));
        }
        Map<Integer, List<Photo>> photos = timelineService.photos(user);

        List<Photo> photosQ = new ArrayList<>();
        photosQ.add(getPhoto(user));
        photosQ.add(getPhoto(user));
        photosQ.add(getPhoto(user));
        photos.put(2016, photosQ);

        List<Photo> photosQ1 = new ArrayList<>();
        photosQ1.add(getPhoto(user));
        photosQ1.add(getPhoto(user));
        photosQ1.add(getPhoto(user));
        photosQ1.add(getPhoto(user));
        photos.put(2015, photosQ1);

        model.addAttribute("photos", photos);
        return "timeline";

    }

    private Photo getPhoto(User user) {
        Photo p = new Photo();
        p.setUser(user);
        p.setCreationDate(LocalDate.now());
        p.setUrl("http://127.0.0.1:8080/assets/images/development_ribbon.png");
        return p;
    }

    @RequestMapping(value = "timeline/year/{year}", method = RequestMethod.GET)
    public String timeLineYear(@PathVariable("year") int year, Model model) {
        User user = null;
        Map<Integer, List<Photo>> photos = timelineService.photos(user, year);
        model.addAttribute("photos", photos);
        return "timeline";
    }

    @RequestMapping(value = "timeline/year/{year}/month/{month}", method = RequestMethod.GET)
    public String timeLineMonth(@PathVariable("year") int year, @PathVariable("month") int month, Model model) {
        User user = null;
        Map<Integer, List<Photo>> photos = timelineService.photos(user, year, month);
        model.addAttribute("photos", photos);
        return "timeline";
    }

}
