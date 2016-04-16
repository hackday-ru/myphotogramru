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
        return photos.stream().collect(Collectors.groupingBy((Photo::getYear)));
    }

    public Map<Integer, List<Photo>> photos(User user, int year) {
        List<Photo> photos = photoRepository.findPhotos(user, year);
        return photos.stream().collect(Collectors.groupingBy((Photo::getMonth)));
    }

    public Map<Integer, List<Photo>> photos(User user, int year, int month) {
        List<Photo> photos = photoRepository.findPhotos(user, year, month);
        return photos.stream().collect(Collectors.groupingBy((Photo::getDay)));
    }

    public List<Photo> photos(User user, int year, int month, int day) {
        return photoRepository.findPhotos(user, year, month, day);
    }
}
