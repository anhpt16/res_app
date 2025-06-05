package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Collection;
import com.anhpt.res_app.common.enums.status.CollectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long>, JpaSpecificationExecutor<Collection> {
    boolean existsByMediaId(Long mediaId);

    Page<Collection> findByStatusOrderByPublishedAtDesc(CollectionStatus status, Pageable pageable);
}
