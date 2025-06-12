package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.ComboVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComboVersionRepository extends JpaRepository<ComboVersion, Long> {
    Optional<ComboVersion> findByIdAndComboId(Long versionId, Long comboId);

    boolean existsByIdAndComboId(Long versionId, Long comboId);
}
