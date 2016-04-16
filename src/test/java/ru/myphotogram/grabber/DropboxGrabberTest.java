package ru.myphotogram.grabber;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.repository.PhotoRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DropboxGrabberTest {

    @Mock private User user;
    @Mock private PhotoRepository photoRepository;
    @InjectMocks private DropboxGrabber dropboxGrabber;

    @Test
    public void grabPhotos() throws Exception {
        dropboxGrabber.grabPhotos(user);
        verify(photoRepository).save(any(Photo.class));
    }

}