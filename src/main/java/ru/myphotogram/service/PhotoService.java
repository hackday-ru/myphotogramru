package ru.myphotogram.service;

import ru.myphotogram.domain.Photo;
import ru.myphotogram.repository.PhotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Photo.
 */
@Service
@Transactional
public class PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoService.class);
    
    @Inject
    private PhotoRepository photoRepository;
    
    /**
     * Save a photo.
     * 
     * @param photo the entity to save
     * @return the persisted entity
     */
    public Photo save(Photo photo) {
        log.debug("Request to save Photo : {}", photo);
        Photo result = photoRepository.save(photo);
        return result;
    }

    /**
     *  Get all the photos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Photo> findAll(Pageable pageable) {
        log.debug("Request to get all Photos");
        Page<Photo> result = photoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one photo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Photo findOne(Long id) {
        log.debug("Request to get Photo : {}", id);
        Photo photo = photoRepository.findOne(id);
        return photo;
    }

    /**
     *  Delete the  photo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Photo : {}", id);
        photoRepository.delete(id);
    }
}
