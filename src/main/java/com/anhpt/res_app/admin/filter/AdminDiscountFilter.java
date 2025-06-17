package com.anhpt.res_app.admin.filter;

import com.anhpt.res_app.admin.dto.request.discount.DiscountSearchRequest;
import com.anhpt.res_app.common.entity.Discount;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminDiscountFilter {
    public Specification<Discount> search(DiscountSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
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
