package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Post;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.enums.status.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    // Admin
    boolean existsByTitle(String title);
    boolean existsBySlug(String slug);
    Optional<Post> findByTitle(String title);
    Optional<Post> findByIdAndUser(Long postId, User user);

    // Web
    Page<Post> findByStatus(PostStatus status, Pageable pageable);
    Optional<Post> findBySlugAndStatus(String slug, PostStatus status);

    @Query("""
        SELECT p FROM Post p
        JOIN p.tagPosts tp
        JOIN tp.tag t
        WHERE p.status = :status
        AND t.slug = :tagSlug
    """)
    Page<Post> findByStatusAndTagSlug(
        @Param("status") PostStatus status,
        @Param("tagSlug") String tagSlug,
        Pageable pageable);

    @Query("""
        SELECT p FROM Post p 
        WHERE p.status = :status 
        AND p.id <> :postId 
        AND EXISTS (
            SELECT 1 FROM TagPost tp 
            WHERE tp.post = p 
            AND tp.tag.id IN :tagIds
        )
        ORDER BY p.publishedAt DESC
    """)
    List<Post> findPostsRelated(
        @Param("postId") Long postId,
        @Param("tagIds") Set<Long> tagIds,
        @Param("status") PostStatus status,
        Pageable pageable);

    @Query("""
        SELECT p FROM Post p 
        WHERE p.status = :status 
        AND p.id NOT IN :postIds
        ORDER BY p.publishedAt DESC
    """)
    List<Post> findByStatusOrderByPublishedAt(
        @Param("postIds") Set<Long> postIds,
        @Param("status") PostStatus status,
        Pageable pageable
    );
}
