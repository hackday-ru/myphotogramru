package ru.myphotogram.web.rest;

import ru.myphotogram.Application;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.repository.PhotoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
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
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoResourceTest {

    private static final String DEFAULT_URL = "SAMPLE_TEXT";
    private static final String UPDATED_URL = "UPDATED_TEXT";
    private static final String DEFAULT_THUMBNAIL_URL = "SAMPLE_TEXT";
    private static final String UPDATED_THUMBNAIL_URL = "UPDATED_TEXT";

    private static final Integer DEFAULT_WIDTH = 0;
    private static final Integer UPDATED_WIDTH = 1;

    private static final Integer DEFAULT_HEIGHT = 0;
    private static final Integer UPDATED_HEIGHT = 1;

    private static final Integer DEFAULT_LIKES = 0;
    private static final Integer UPDATED_LIKES = 1;

    @Inject
    private PhotoRepository photoRepository;

    private MockMvc restPhotoMockMvc;

    private Photo photo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoResource photoResource = new PhotoResource();
        ReflectionTestUtils.setField(photoResource, "photoRepository", photoRepository);
        this.restPhotoMockMvc = MockMvcBuilders.standaloneSetup(photoResource).build();
    }

    @Before
    public void initTest() {
        photo = new Photo();
        photo.setUrl(DEFAULT_URL);
        photo.setThumbnailUrl(DEFAULT_THUMBNAIL_URL);
        photo.setWidth(DEFAULT_WIDTH);
        photo.setHeight(DEFAULT_HEIGHT);
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
        assertThat(testPhoto.getLikes()).isEqualTo(DEFAULT_LIKES);
    }

    @Test
    @Transactional
    public void getAllPhotos() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photos
        restPhotoMockMvc.perform(get("/api/photos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].thumbnailUrl").value(hasItem(DEFAULT_THUMBNAIL_URL.toString())))
                .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
                .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
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
        photoRepository.saveAndFlush(photo);

		int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo
        photo.setUrl(UPDATED_URL);
        photo.setThumbnailUrl(UPDATED_THUMBNAIL_URL);
        photo.setWidth(UPDATED_WIDTH);
        photo.setHeight(UPDATED_HEIGHT);
        photo.setLikes(UPDATED_LIKES);
        restPhotoMockMvc.perform(put("/api/photos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photo)))
                .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photos.get(photos.size() - 1);
        assertThat(testPhoto.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPhoto.getThumbnailUrl()).isEqualTo(UPDATED_THUMBNAIL_URL);
        assertThat(testPhoto.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testPhoto.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPhoto.getLikes()).isEqualTo(UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void deletePhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

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
