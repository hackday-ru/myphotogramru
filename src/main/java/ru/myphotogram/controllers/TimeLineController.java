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
import ru.myphotogram.service.TimelineService;
import ru.myphotogram.service.UserService;

import java.security.Principal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TimeLineController {

    private final TimelineService timelineService;
    private final UserService userService;
    private Comparator<Integer> c = (o1, o2) -> -o1.compareTo(o2);
    private Comparator<String> c2 = (o1, o2) -> -o1.compareTo(o2);

    @Autowired
    public TimeLineController(TimelineService timelineService, UserService userService) {
        this.timelineService = timelineService;
        this.userService = userService;
    }

    @RequestMapping(value = {"/", "timeline"}, method = RequestMethod.GET)
    public String timeLine(Model model, Authentication authentication, Principal principal) {
        User user = userService.getUserWithAuthoritiesByLogin("user").get();
        Map<Integer, List<Photo>> photos = timelineService.photos(user);

        TreeMap treeMap = new TreeMap(c);
        treeMap.putAll(photos);
        model.addAttribute("photos", treeMap);

        return "timeline";
    }

    @RequestMapping(value = "timeline/year/{year}", method = RequestMethod.GET)
    public String timeLineYear(@PathVariable("year") int year, Model model) {
        User user = userService.getUserWithAuthoritiesByLogin("user").get();

        TreeMap<Integer, List<Photo>> treeMap = new TreeMap(c);
        treeMap.putAll(timelineService.photos(user, year));

        Map<String, List<Photo>> map = treeMap.entrySet().stream().collect(
            Collectors.toMap(
                (e) -> Month.of(e.getKey()).getDisplayName(TextStyle.FULL, Locale.US),
                Map.Entry::getValue,
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                TreeMap::new)
        );

        model.addAttribute("photos", map);
        model.addAttribute("year", year);

        return "timeline-year";
    }

    @RequestMapping(value = "timeline/year/{year}/month/{month}", method = RequestMethod.GET)
    public String timeLineMonth(@PathVariable("year") int year, @PathVariable("month") String month, Model model) {
        User user = userService.getUserWithAuthoritiesByLogin("user").get();
        Map<Integer, List<Photo>> photos = timelineService.photos(user, year, Month.valueOf(month.toUpperCase()).getValue());

        TreeMap treeMap = new TreeMap(c);
        treeMap.putAll(photos);

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("photos", treeMap);
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

}
