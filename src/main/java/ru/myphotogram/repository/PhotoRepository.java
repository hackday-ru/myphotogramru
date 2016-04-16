package ru.myphotogram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the Photo entity.
 */
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query("select photo from Photo photo where photo.user = :user")
    List<Photo> findPhotos(@Param("user") User user);

    @Query("select photo from Photo photo where photo.user = :user and photo.year = :year")
    List<Photo> findPhotos(@Param("user") User user, @Param("year") int year);

    @Query("select photo from Photo photo where photo.user = :user and photo.year = :year and month = :month")
    List<Photo> findPhotos(@Param("user") User user, @Param("year") int year, @Param("month") int month);

    @Query("select photo from Photo photo where photo.user = :user and photo.year = :year and month = :month and day = :day")
    List<Photo> findPhotos(@Param("user") User user, @Param("year") int year, @Param("month") int month, @Param("day") int day);

}
