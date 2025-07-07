package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.DeskMedia;
import com.anhpt.res_app.common.entity.key.DeskMediaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeskMediaRepository extends JpaRepository<DeskMedia, DeskMediaId> {
}
