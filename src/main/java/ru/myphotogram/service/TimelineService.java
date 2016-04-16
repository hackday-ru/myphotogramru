package ru.myphotogram.service;

import org.springframework.stereotype.Service;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.repository.PhotoRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Eugene on 16.04.16.
 */
@Service
public class TimelineService {

    @Inject
    PhotoRepository photoRepository;

    public Map<Integer, List<Photo>> photos(User user) {
        List<Photo> photos = photoRepository.findPhotos(user);
        Map<Integer, List<Photo>> collect = photos.stream().collect(Collectors.groupingBy((Photo::getYear)));
        return reduce(collect);

    }

    public Map<Integer, List<Photo>> photos(User user, int year) {
        List<Photo> photos = photoRepository.findPhotos(user, year);
        Map<Integer, List<Photo>> collect = photos.stream().collect(Collectors.groupingBy((Photo::getMonth)));
        return reduce(collect);

    }

    public Map<Integer, List<Photo>> photos(User user, int year, int month) {
        List<Photo> photos = photoRepository.findPhotos(user, year, month);
        Map<Integer, List<Photo>> collect = photos.stream().collect(Collectors.groupingBy((Photo::getDay)));
        return reduce(collect);
    }

    public List<Photo> photos(User user, int year, int month, int day) {
        return photoRepository.findPhotos(user, year, month, day);
    }

    private Map<Integer, List<Photo>> reduce(Map<Integer, List<Photo>> collect) {
        return collect.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> e.getValue().stream().limit(5).collect(Collectors.toList())));
    }
}
