package ru.myphotogram.service;

import org.springframework.stereotype.Service;
import ru.myphotogram.domain.Photo;
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

    public Map<Integer, List<Photo>> photos() {
        List<Photo> photos = photoRepository.findPhotos(null);
        return photos.stream().collect(Collectors.groupingBy((Photo::getYear)));
    }

    public Map<Integer, List<Photo>> photos(int year) {
        List<Photo> photos = photoRepository.findPhotos(null, year);
        return photos.stream().collect(Collectors.groupingBy((Photo::getMonth)));
    }

    public Map<Integer, List<Photo>> photos(int year, int month) {
        List<Photo> photos = photoRepository.findPhotos(null, year, month);
        return photos.stream().collect(Collectors.groupingBy((Photo::getDay)));
    }
}
