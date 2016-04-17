package ru.myphotogram.grabber;

import com.dropbox.core.DbxRequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.repository.PhotoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("vkGrabber")
public class VKGrabber implements Grabber {

    private static final Logger LOGGER = LoggerFactory.getLogger(VKGrabber.class);
    private final PhotoRepository photoRepository;

    @Autowired
    public VKGrabber(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void grabPhotos(User user) {
        LOGGER.info("Start grabbing vk.com for {}...", user);
        DbxRequestConfig config = new DbxRequestConfig("myphotogram", "en_US");
        RestTemplate restTemplate = new RestTemplate();
        String url = URIBuilder
            .fromUri("https://api.vk.com/method/photos.getAll")
            .queryParam("access_token", "e20945e8e4731af7053fadcac0e14d966c304073768ccd3aaea577341609884bb57000d3b2778db8160b6")
            .queryParam("owner_id", "360697717")
            .queryParam("extended", "1")
            .build().toString();
        Map<String, List<Object>> vkArray = restTemplate.getForObject(url, Map.class);
        vkArray.get("response").stream()
            .filter(m -> m instanceof Map)
            .map(m -> (Map<String, Object>) m)
            .peek(m -> LOGGER.debug((String) m.get("src")))
            .forEach(m -> photoRepository.save(createPhotoFromMap(m, user)));
    }

    @Override
    public void setToken(String token) {

    }

    private Photo createPhotoFromMap(Map<String, Object> file, User user) {
        Photo photo = new Photo();
        photo.setUrl((String) file.get("src"));
        photo.setThumbnailUrl((String) file.get("src"));
        LocalDate creationDate = Optional.ofNullable(file.get("creationDate")).map(l -> LocalDate.ofEpochDay(Long.parseLong((String) l)))
            .orElse(LocalDate.now());
        photo.setCreationDate(creationDate);
        photo.setYear(creationDate.getYear());
        photo.setMonth(creationDate.getMonth().getValue());
        photo.setDay(creationDate.getDayOfMonth());
        photo.setUser(user);
        Map likes = (Map) file.get("likes");
        photo.setLikes((Integer) likes.get("count"));
        return photo;
    }

}
