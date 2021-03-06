package ru.myphotogram.grabber;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxDelta;
import com.dropbox.core.v1.DbxEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.repository.PhotoRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@Component
public class DropboxGrabber implements Grabber {

    private final String grabberId = "dropbox";

    private static final Logger LOGGER = LoggerFactory.getLogger(DropboxGrabber.class);
    private final PhotoRepository photoRepository;

    @Autowired
    public DropboxGrabber(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void grabPhotos(User user) {
        LOGGER.info("Start grabbing dropbox for {}...", user);
        DbxRequestConfig config = new DbxRequestConfig("myphotogram", "en_US");
        DbxClientV1 clientV1 = new DbxClientV1(config, "1LffmceDYhAAAAAAAAAAGnq29CjJCm2jefXwEDOMv0U75AKI-ds_-KrOKCgJ1kAB");
        try {
            String cursor = user.getDropboxCursor();
            DbxDelta<DbxEntry> delta;
            do {
                delta = clientV1.getDelta(cursor, true);
                delta.entries.stream()
                    .filter(entry -> entry.metadata.isFile() && Objects.nonNull(entry.metadata.asFile().photoInfo))
                    .peek(entry -> LOGGER.debug(entry.metadata.name))
                    .map(entry -> createPhotoFromFile(user, entry.metadata.asFile()))
                    .forEach(photoRepository::save);
                cursor = delta.cursor;
            } while (delta.hasMore);
            user.setDropboxCursor(cursor);
            LOGGER.info("Grabbing dropbox for {} finished", user);
        } catch (DbxException dbe) {
            LOGGER.error("Failed to grab photos from Dropbox", dbe);
        }
    }

    @Override
    public void setToken(String token) { }

    @Override
    public boolean hasToken() {
        return true;
    }

    private Photo createPhotoFromFile(User user, DbxEntry.File file) {
        DbxEntry.File.PhotoInfo photoInfo = file.photoInfo;
        Photo photo = new Photo();
        photo.setUrl("/dropbox/img" + file.path);
        photo.setThumbnailUrl("/dropbox/thumbnail" + file.path);
        LocalDate creationDate = Optional.ofNullable(photoInfo.timeTaken)
            .map(timeTaken -> timeTaken.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            .orElse(LocalDate.now());
        photo.setCreationDate(creationDate);
        photo.setYear(creationDate.getYear());
        photo.setMonth(creationDate.getMonth().getValue());
        photo.setDay(creationDate.getDayOfMonth());
        DbxEntry.File.Location location = photoInfo.location;
        if (Objects.nonNull(location)) {
            photo.setLatitude(location.latitude);
            photo.setLongitude(location.longitude);
        }
        photo.setUser(user);
        photo.setGrabberId(grabberId);
        return photo;
    }

}
