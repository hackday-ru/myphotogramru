package ru.myphotogram.service;

import org.springframework.stereotype.Service;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.repository.PhotoRepository;

import javax.inject.Inject;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class FavsService {

    @Inject
    PhotoRepository photoRepository;

    public Map<String, Photo> photos(User user) {
        return reduce(photoRepository.findPhotos(user, 2016));
    }

    private Map<String, Photo> reduce(List<Photo> photos) {
        Map<String, Photo> result = new HashMap<>();
        for (Photo photo : photos) {
            if (photo.getLikes() == null || photo.getLikes() <= 0) continue;
            String month = Month.of(photo.getMonth()).getDisplayName(TextStyle.FULL, Locale.US);
            if (result.containsKey(month)) {
                if (result.get(month).getLikes() < photo.getLikes()) {
                    result.put(month, photo);
                }
            } else {
                result.put(month, photo);
            }
        }
        return result;
    }

}

