package ru.myphotogram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.repository.AuthorityRepository;
import ru.myphotogram.service.TimelineService;
import ru.myphotogram.service.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TimeLineController {

    private final TimelineService timelineService;
    private final UserService userService;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public TimeLineController(TimelineService timelineService, UserService userService, AuthorityRepository authorityRepository) {
        this.timelineService = timelineService;
        this.userService = userService;
        this.authorityRepository = authorityRepository;
    }

    @RequestMapping(value = {"/", "timeline"}, method = RequestMethod.GET)
    public String timeLine(Model model, Authentication authentication, Principal principal) {
        User user = userService.getUserWithAuthoritiesByLogin("user").get();
        Map<Integer, List<Photo>> photos = timelineService.photos(user);

        Comparator<Integer> c = (o1, o2) -> -o1.compareTo(o2);
        TreeMap treeMap = new TreeMap(c);
        treeMap.putAll(photos);
        model.addAttribute("photos", treeMap);

        return "timeline";
    }

    @RequestMapping(value = "timeline/year/{year}", method = RequestMethod.GET)
    public String timeLineYear(@PathVariable("year") int year, Model model) {
        User user = userService.getUserWithAuthoritiesByLogin("user").get();
        Map<Integer, List<Photo>> photos = timelineService.photos(user, year);
        Map<String, List<Photo>> map = photos.entrySet().stream().collect(Collectors.toMap((e) -> Month.of(e.getKey()).getDisplayName(TextStyle.FULL, Locale.US), Map.Entry::getValue));
        model.addAttribute("year", year);
        model.addAttribute("photos", map);
        return "timeline-year";
    }

    @RequestMapping(value = "timeline/year/{year}/month/{month}", method = RequestMethod.GET)
    public String timeLineMonth(@PathVariable("year") int year, @PathVariable("month") String month, Model model) {
        User user = userService.getUserWithAuthoritiesByLogin("user").get();
        Map<Integer, List<Photo>> photos = timelineService.photos(user, year, Month.valueOf(month.toUpperCase()).getValue());
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("photos", photos);
        return "timeline-month";
    }

    @RequestMapping(value = "timeline/year/{year}/month/{month}/day/{day}", method = RequestMethod.GET)
    public String timeLineMonth(@PathVariable("year") int year, @PathVariable("month") String month, @PathVariable("day") int day, Model model) {
        User user = userService.getUserWithAuthoritiesByLogin("user").get();
        List<Photo> photos = timelineService.photos(user, year, Month.valueOf(month.toUpperCase()).getValue(), day);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("day", day);
        model.addAttribute("photos", photos);
        return "timeline-day";
    }

    private Photo getPhoto(User user) {
        Photo p = new Photo();
        p.setUser(user);
        p.setCreationDate(LocalDate.now());
        p.setUrl("http://127.0.0.1:8080/assets/images/development_ribbon.png");
        return p;
    }

}
