package ru.myphotogram.grabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.myphotogram.service.UserService;

@Component
public class PhotoGrabberScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoGrabberScheduler.class);

    private final UserService userService;
    private final Grabber dropboxGrabber;

    @Autowired
    public PhotoGrabberScheduler(UserService userService, Grabber dropboxGrabber) {
        this.userService = userService;
        this.dropboxGrabber = dropboxGrabber;
    }

    @Transactional
    @Scheduled(fixedDelay = 30000)
    public void grabPhotos() {
        userService.getUserWithAuthoritiesByLogin("user").ifPresent(user -> {
            LOGGER.info("Start scheduled grabber...");
            dropboxGrabber.grabPhotos(user);
            LOGGER.info("Scheduled grabber finished");
        });
    }

}
