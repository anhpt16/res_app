package com.anhpt.res_app.admin.filter;

import com.anhpt.res_app.admin.dto.request.user.UserSearchRequest;
import com.anhpt.res_app.common.entity.User;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminUserFilter {
    public Specification<User> search(UserSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Tìm kiếm theo tên nếu có
            if (StringUtils.hasText(request.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            // Tìm kiếm theo tên đăng nhập nếu có
            if (StringUtils.hasText(request.getUsername())) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + request.getUsername() + "%"));
            }
            // Tìm kiếm theo email nếu có
            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }
            // Tìm kiếm theo số điện thoại nếu có
            if (StringUtils.hasText(request.getPhone())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }
            // Tìm kiếm theo roleId nếu có
            if (request.getRoleId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role").get("id"), request.getRoleId()));
            }

            // Tìm kiếm theo trạng thái nếu có
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            // Sắp xếp theo createdAt
            List<Order> orders = new ArrayList<>();
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
