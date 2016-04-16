package ru.myphotogram.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.myphotogram.MyphotogramApp;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.repository.PhotoRepository;
import ru.myphotogram.service.PhotoService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
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

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

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
        photo.setYear(DEFAULT_YEAR);
        photo.setMonth(DEFAULT_MONTH);
        photo.setDay(DEFAULT_DAY);
        photo.setLatitude(DEFAULT_LATITUDE);
        photo.setLongitude(DEFAULT_LONGITUDE);
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
        assertThat(testPhoto.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testPhoto.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testPhoto.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testPhoto.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testPhoto.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
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
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setYear(null);

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
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setMonth(null);

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
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setDay(null);

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
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
                .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
                .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
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
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
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
        updatedPhoto.setYear(UPDATED_YEAR);
        updatedPhoto.setMonth(UPDATED_MONTH);
        updatedPhoto.setDay(UPDATED_DAY);
        updatedPhoto.setLatitude(UPDATED_LATITUDE);
        updatedPhoto.setLongitude(UPDATED_LONGITUDE);
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
        assertThat(testPhoto.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPhoto.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testPhoto.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testPhoto.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPhoto.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
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
