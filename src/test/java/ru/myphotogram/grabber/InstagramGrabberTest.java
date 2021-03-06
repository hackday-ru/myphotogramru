package ru.myphotogram.grabber;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.repository.PhotoRepository;

import java.sql.Timestamp;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InstagramGrabberTest {

    @Mock
    private User user;
    @Mock
    private PhotoRepository photoRepository;
    @InjectMocks
    private InstagramGrabber instagramGrabber;

    @Test
    public void test() {
        instagramGrabber.grabPhotos(user);
        verify(photoRepository).save(any(Photo.class));
    }

    @Test
    public void checkConvertLongToDate() {
        Long time = Long.valueOf("1460834504000");
        Timestamp timestamp = new Timestamp(time);
        timestamp.toLocalDateTime().toLocalDate();
    }
}
