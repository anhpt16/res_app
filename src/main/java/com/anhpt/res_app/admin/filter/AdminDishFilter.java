package com.anhpt.res_app.admin.filter;

import com.anhpt.res_app.admin.dto.request.dish.DishSearchRequest;
import com.anhpt.res_app.admin.dto.request.dish.DishUpdateRequest;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.enums.status.DishStatus;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AdminDishFilter {
    public Specification<Dish> search(DishSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Lọc theo tên
            if (StringUtils.hasText(request.getName())) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + request.getName().toLowerCase().trim() + "%"
                ));
            }
            // Lọc theo danh mục
            if (request.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), request.getCategoryId()));
            }
            // Lọc theo trạng thái
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), DishStatus.fromCode(request.getStatus())));
            }
            // Điều kiện sắp xếp
            List<Order> orders = new ArrayList<>();
            // Sắp xếp theo createdAt
            if (StringUtils.hasText(request.getSortByCreatedAt())) {
                orders.add(request.getSortByCreatedAt().equalsIgnoreCase("DESC") ?
                    criteriaBuilder.desc(root.get("createdAt")) :
                    criteriaBuilder.asc(root.get("createdAt")));
            }
            query.orderBy(orders);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
