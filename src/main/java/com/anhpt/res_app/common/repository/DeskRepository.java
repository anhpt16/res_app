package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeskRepository extends JpaRepository<Desk, Integer>, JpaSpecificationExecutor<Desk> {
}
