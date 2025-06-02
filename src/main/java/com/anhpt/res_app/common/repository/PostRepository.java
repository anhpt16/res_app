package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Post;
import com.anhpt.res_app.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    boolean existsByTitle(String title);
    boolean existsBySlug(String slug);
    Optional<Post> findByTitle(String title);

    Optional<Post> findByIdAndUser(Long postId, User user);
}
