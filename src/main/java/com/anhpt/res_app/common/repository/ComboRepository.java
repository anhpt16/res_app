package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Combo;
import com.anhpt.res_app.common.enums.status.ComboStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long>, JpaSpecificationExecutor<Combo> {
    boolean existsByName(String name);

    Optional<Combo> findByName(String name);

    Page<Combo> findAll(Specification<Combo> search, Pageable pageable);

    List<Combo> findByStatus(ComboStatus published, Sort publishedAt);
}
