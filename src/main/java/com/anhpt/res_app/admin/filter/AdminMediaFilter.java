package com.anhpt.res_app.admin.filter;

import com.anhpt.res_app.admin.dto.request.media.MediaSearchRequest;
import com.anhpt.res_app.common.entity.Media;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AdminMediaFilter {
    public Specification<Media> searchMedia(MediaSearchRequest request, Long userId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            // Điều kiện tìm kiếm
            // Tiếm kiếm theo tên
            if (StringUtils.hasText(request.getSearchTerm())) {
                predicates.add(cb.like(
                    cb.lower(root.get("originName")),
                    "%" + request.getSearchTerm().toLowerCase().trim() + "%"
                ));
            }
            // Tìm kiếm theo loại
            if (StringUtils.hasText(request.getType())) {
                String mimeTypePrefix = request.getType().equalsIgnoreCase("IMAGE") ? "image/" : "video/";
                predicates.add(cb.like(root.get("mimeType"), mimeTypePrefix + "%"));
            }

            // Điều kiện sắp xếp
            List<Order> orders = new ArrayList<>();
            // Sắp xếp theo createdAt
            if (StringUtils.hasText(request.getSortByCreatedAt())) {
                orders.add(request.getSortByCreatedAt().equalsIgnoreCase("DESC") ?
                    cb.desc(root.get("createdAt")) :
                    cb.asc(root.get("createdAt")));
            }

            query.orderBy(orders);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
} 