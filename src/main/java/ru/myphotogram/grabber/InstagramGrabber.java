package ru.myphotogram.grabber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.repository.PhotoRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Component
public class InstagramGrabber implements Grabber{

    private final String grabberId = "instagram";
    private static final String API_URL = "https://api.instagram.com/v1";
    private static final String IMAGES_URL = API_URL+"/users/self/media/recent/";
    private static final Logger LOGGER = LoggerFactory.getLogger(InstagramGrabber.class);
    private final PhotoRepository photoRepository;
    private String token;

    @Autowired
    public InstagramGrabber(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void grabPhotos(User user) {
        if (token == null) {
            LOGGER.info("Instagram unauthorized skipping", user);
            return;
        }
        LOGGER.info("Start grabbing instagram for {}...", user);
        try {
            URL url = new URL(IMAGES_URL + "?access_token=" + token);
            //TODO pass real token
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            String response = streamToString(urlConnection.getInputStream());
            JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            for(int i = 0; i < jsonArray.length(); i++){
                Photo photo = createPhotoFromJSON((JSONObject) jsonArray.get(i), user);
                Optional<Photo> existentPhoto = photoRepository.findByUrl(photo.getUrl());
                if (existentPhoto.isPresent()) {
                    existentPhoto.get().setLikes(photo.getLikes());
                } else {
                    photoRepository.save(createPhotoFromJSON((JSONObject) jsonArray.get(i), user));
                }
            }
        } catch(Exception e){
            LOGGER.error("Instagram",e);
        }
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean hasToken() {
        return Objects.nonNull(token);
    }

    private String streamToString(InputStream is) throws IOException {
        String str = "";
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
            } finally {
                is.close();
            }
            str = sb.toString();
        }
        return str;
    }

    private String getNextBatchOfMedia() {
        return null;
    }

    private Photo createPhotoFromJSON(JSONObject jsonObject, User user) throws JSONException {
        Photo photo = new Photo();
        photo.setThumbnailUrl(jsonObject.getJSONObject("images").getJSONObject("thumbnail").getString("url"));
        photo.setUrl(jsonObject.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
        LocalDate creationDate = Optional.ofNullable(jsonObject.getString("created_time"))
            .map(timeTaken -> new Timestamp(Long.valueOf(timeTaken)*1000).toLocalDateTime().toLocalDate())
            .orElse(LocalDate.now());
        photo.setCreationDate(creationDate);
        photo.setYear(creationDate.getYear());
        photo.setMonth(creationDate.getMonth().getValue());
        photo.setDay(creationDate.getDayOfMonth());
        JSONObject likes = jsonObject.getJSONObject("likes");
        String count = likes.getString("count");
        if (Objects.nonNull(count)) {
            photo.setLikes(Integer.valueOf(count));
        }
        String location = jsonObject.getString("location");
        if (Objects.nonNull(location)) {
            //TODO set location if returns not null
        }
        photo.setUser(user);
        photo.setGrabberId(grabberId);
        return photo;
    }
}
