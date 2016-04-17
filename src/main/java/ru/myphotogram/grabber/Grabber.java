package ru.myphotogram.grabber;

import ru.myphotogram.domain.User;

public interface Grabber {

    void grabPhotos(User user);
    void setToken(String token);
    boolean hasToken();

}
