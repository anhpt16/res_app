package com.anhpt.res_app.admin.filter;

import com.anhpt.res_app.admin.dto.request.setup.DishSetupSearchRequest;
import com.anhpt.res_app.common.entity.DishSetup;
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
public class AdminDishSetupFilter {
    
    public Specification<DishSetup> search(DishSetupSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getCurrentStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("currentStatus"), request.getCurrentStatus()));
            }
            if (request.getNextStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("nextStatus"), request.getNextStatus()));
            }
            // Sắp xếp
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
