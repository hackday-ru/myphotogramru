package ru.myphotogram.repository;

import ru.myphotogram.domain.Photo;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Photo entity.
 */
public interface PhotoRepository extends JpaRepository<Photo,Long> {

    @Query("select photo from Photo photo where photo.user.login = ?#{principal.username}")
    List<Photo> findAllForCurrentUser();

}
