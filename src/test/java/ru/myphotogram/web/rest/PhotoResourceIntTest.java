package ru.myphotogram.web.rest;

import ru.myphotogram.MyphotogramApp;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.repository.PhotoRepository;
import ru.myphotogram.service.PhotoService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PhotoResource REST controller.
 *
 * @see PhotoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyphotogramApp.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";
    private static final String DEFAULT_THUMBNAIL_URL = "AAAAA";
    private static final String UPDATED_THUMBNAIL_URL = "BBBBB";

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Boolean DEFAULT_HIDDEN = false;
    private static final Boolean UPDATED_HIDDEN = true;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LOGITUDE = 1D;
    private static final Double UPDATED_LOGITUDE = 2D;

    private static final Integer DEFAULT_LIKES = 1;
    private static final Integer UPDATED_LIKES = 2;

    @Inject
    private PhotoRepository photoRepository;

    @Inject
    private PhotoService photoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoMockMvc;

    private Photo photo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoResource photoResource = new PhotoResource();
        ReflectionTestUtils.setField(photoResource, "photoService", photoService);
        this.restPhotoMockMvc = MockMvcBuilders.standaloneSetup(photoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photo = new Photo();
        photo.setUrl(DEFAULT_URL);
        photo.setThumbnailUrl(DEFAULT_THUMBNAIL_URL);
        photo.setWidth(DEFAULT_WIDTH);
        photo.setHeight(DEFAULT_HEIGHT);
        photo.setHidden(DEFAULT_HIDDEN);
        photo.setCreationDate(DEFAULT_CREATION_DATE);
        photo.setLatitude(DEFAULT_LATITUDE);
        photo.setLogitude(DEFAULT_LOGITUDE);
        photo.setLikes(DEFAULT_LIKES);
    }

    @Test
    @Transactional
    public void createPhoto() throws Exception {
        int databaseSizeBeforeCreate = photoRepository.findAll().size();

        // Create the Photo

        restPhotoMockMvc.perform(post("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photo)))
                .andExpect(status().isCreated());

        // Validate the Photo in the database
        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeCreate + 1);
        Photo testPhoto = photos.get(photos.size() - 1);
        assertThat(testPhoto.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testPhoto.getThumbnailUrl()).isEqualTo(DEFAULT_THUMBNAIL_URL);
        assertThat(testPhoto.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testPhoto.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testPhoto.isHidden()).isEqualTo(DEFAULT_HIDDEN);
        assertThat(testPhoto.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPhoto.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testPhoto.getLogitude()).isEqualTo(DEFAULT_LOGITUDE);
        assertThat(testPhoto.getLikes()).isEqualTo(DEFAULT_LIKES);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setUrl(null);

        // Create the Photo, which fails.

        restPhotoMockMvc.perform(post("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photo)))
                .andExpect(status().isBadRequest());

        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkThumbnailUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setThumbnailUrl(null);

        // Create the Photo, which fails.

        restPhotoMockMvc.perform(post("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photo)))
                .andExpect(status().isBadRequest());

        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHiddenIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setHidden(null);

        // Create the Photo, which fails.

        restPhotoMockMvc.perform(post("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photo)))
                .andExpect(status().isBadRequest());

        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setCreationDate(null);

        // Create the Photo, which fails.

        restPhotoMockMvc.perform(post("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photo)))
                .andExpect(status().isBadRequest());

        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPhotos() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photos
        restPhotoMockMvc.perform(get("/api/photos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].thumbnailUrl").value(hasItem(DEFAULT_THUMBNAIL_URL.toString())))
                .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
                .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
                .andExpect(jsonPath("$.[*].hidden").value(hasItem(DEFAULT_HIDDEN.booleanValue())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].logitude").value(hasItem(DEFAULT_LOGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES)));
    }

    @Test
    @Transactional
    public void getPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get the photo
        restPhotoMockMvc.perform(get("/api/photos/{id}", photo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photo.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.thumbnailUrl").value(DEFAULT_THUMBNAIL_URL.toString()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.hidden").value(DEFAULT_HIDDEN.booleanValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.logitude").value(DEFAULT_LOGITUDE.doubleValue()))
            .andExpect(jsonPath("$.likes").value(DEFAULT_LIKES));
    }

    @Test
    @Transactional
    public void getNonExistingPhoto() throws Exception {
        // Get the photo
        restPhotoMockMvc.perform(get("/api/photos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhoto() throws Exception {
        // Initialize the database
        photoService.save(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo
        Photo updatedPhoto = new Photo();
        updatedPhoto.setId(photo.getId());
        updatedPhoto.setUrl(UPDATED_URL);
        updatedPhoto.setThumbnailUrl(UPDATED_THUMBNAIL_URL);
        updatedPhoto.setWidth(UPDATED_WIDTH);
        updatedPhoto.setHeight(UPDATED_HEIGHT);
        updatedPhoto.setHidden(UPDATED_HIDDEN);
        updatedPhoto.setCreationDate(UPDATED_CREATION_DATE);
        updatedPhoto.setLatitude(UPDATED_LATITUDE);
        updatedPhoto.setLogitude(UPDATED_LOGITUDE);
        updatedPhoto.setLikes(UPDATED_LIKES);

        restPhotoMockMvc.perform(put("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPhoto)))
                .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photos.get(photos.size() - 1);
        assertThat(testPhoto.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPhoto.getThumbnailUrl()).isEqualTo(UPDATED_THUMBNAIL_URL);
        assertThat(testPhoto.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPhoto.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPhoto.isHidden()).isEqualTo(UPDATED_HIDDEN);
        assertThat(testPhoto.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPhoto.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPhoto.getLogitude()).isEqualTo(UPDATED_LOGITUDE);
        assertThat(testPhoto.getLikes()).isEqualTo(UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void deletePhoto() throws Exception {
        // Initialize the database
        photoService.save(photo);

        int databaseSizeBeforeDelete = photoRepository.findAll().size();

        // Get the photo
        restPhotoMockMvc.perform(delete("/api/photos/{id}", photo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
