package ru.myphotogram.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.repository.PhotoRepository;
import ru.myphotogram.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Photo.
 */
@RestController
@RequestMapping("/api")
public class PhotoResource {

    private final Logger log = LoggerFactory.getLogger(PhotoResource.class);

    @Inject
    private PhotoRepository photoRepository;

    /**
     * POST  /photos -> Create a new photo.
     */
    @RequestMapping(value = "/photos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Photo photo) throws URISyntaxException {
        log.debug("REST request to save Photo : {}", photo);
        if (photo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new photo cannot already have an ID").build();
        }
        photoRepository.save(photo);
        return ResponseEntity.created(new URI("/api/photos/" + photo.getId())).build();
    }

    /**
     * PUT  /photos -> Updates an existing photo.
     */
    @RequestMapping(value = "/photos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Photo photo) throws URISyntaxException {
        log.debug("REST request to update Photo : {}", photo);
        if (photo.getId() == null) {
            return create(photo);
        }
        photoRepository.save(photo);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /photos -> get all the photos.
     */
    @RequestMapping(value = "/photos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Photo>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Photo> page = photoRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/photos", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /photos/:id -> get the "id" photo.
     */
    @RequestMapping(value = "/photos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Photo> get(@PathVariable Long id) {
        log.debug("REST request to get Photo : {}", id);
        return Optional.ofNullable(photoRepository.findOne(id))
            .map(photo -> new ResponseEntity<>(
                photo,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photos/:id -> delete the "id" photo.
     */
    @RequestMapping(value = "/photos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Photo : {}", id);
        photoRepository.delete(id);
    }
}
