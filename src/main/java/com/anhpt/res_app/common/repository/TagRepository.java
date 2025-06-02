package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Optional<Tag> findById(Long id);
    Optional<Tag> findByName(String name);
}
