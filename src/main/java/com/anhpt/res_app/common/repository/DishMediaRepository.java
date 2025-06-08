package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishMediaRepository extends JpaRepository<DishMedia, Long> {

    List<DishMedia> findByDish(Dish dish);
}
