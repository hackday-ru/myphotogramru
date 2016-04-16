package ru.myphotogram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.myphotogram.domain.Photo;

import java.util.List;

/**
 * Spring Data JPA repository for the Photo entity.
 */
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query("select photo from Photo photo where photo.user.login = ?#{principal.username}")
    List<Photo> findPhotos();

    @Query("select photo from Photo photo where photo.user.login = ?#{principal.username} and photo.year = :year")
    List<Photo> findPhotos(@Param("year") int year);

    @Query("select photo from Photo photo where photo.user.login = ?#{principal.username} and photo.year = :year and month = :month")
    List<Photo> findPhotos(@Param("year") int year, @Param("month") int month);

}
