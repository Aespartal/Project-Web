package es.project.repository;

import es.project.domain.Image;
import es.project.domain.ImageExt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.DoubleStream;

/**
 * Spring Data JPA repository for the Image entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageRepositoryCustom extends JpaRepository<ImageExt, Long>, JpaSpecificationExecutor<ImageExt> {

    @Query(value = "SELECT i.*, COUNT(li.id) as likes FROM public.image i \n" +
        "        LEFT JOIN public.like_image li ON i.id = li.image_id  \n" +
        "        WHERE i.is_private = false \n" +
        "        GROUP BY i.id\n" +
        "    ORDER BY COUNT(li.id) DESC LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}", nativeQuery = true)
    Page<ImageExt> findPopularImages(Pageable pageable);

    @Query(value = "SELECT i.*, COUNT(li.id) as likes FROM public.image i \n" +
        "        LEFT JOIN public.like_image li ON i.id = li.image_id  \n" +
        "        WHERE i.is_private = false \n" +
        "        GROUP BY i.id\n" +
        "    ORDER BY i.id DESC LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}", nativeQuery = true)
    Page<ImageExt> findRecentImages(Pageable page);

    @Query(value = "SELECT i.*, COUNT(li.id) as likes, " +
        "(\n" +
        "    SELECT COUNT(li.id) > 0 FROM public.like_image li WHERE li.image_id = i.id AND li.extended_user_id = :id\n" +
        ") as is_favourited FROM public.image i \n" +
        "        LEFT JOIN public.like_image li ON i.id = li.image_id  \n" +
        "        WHERE i.is_private = false \n" +
        "        GROUP BY i.id\n" +
        "    ORDER BY COUNT(li.id) DESC LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}", nativeQuery = true)
    Page<ImageExt> findPopularImagesForUser(Pageable pageable, long id);

    @Query(value = "SELECT i.*, COUNT(li.id) as likes, \n" +
        "(\n" +
        "    SELECT COUNT(li.id) > 0 FROM public.like_image li WHERE li.image_id = i.id AND li.extended_user_id = :id\n" +
        ") as is_favourited FROM public.image i \n" +
        "        LEFT JOIN public.like_image li ON i.id = li.image_id  \n" +
        "        WHERE i.is_private = false \n" +
        "        GROUP BY i.id\n" +
        "    ORDER BY i.id DESC LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}", nativeQuery = true)
    Page<ImageExt> findRecentImagesForUser(Pageable page, long id);
}
