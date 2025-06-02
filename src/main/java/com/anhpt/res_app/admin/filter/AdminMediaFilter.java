package com.anhpt.res_app.admin.filter;

import com.anhpt.res_app.admin.dto.request.media.MediaSearchRequest;
import com.anhpt.res_app.common.entity.Media;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
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

            // Filter by search term
            if (StringUtils.hasText(request.getSearchTerm())) {
                predicates.add(cb.like(
                    cb.lower(root.get("originName")),
                    "%" + request.getSearchTerm().toLowerCase().trim() + "%"
                ));
            }
            // Filter by type (IMAGE/VIDEO)
            if (StringUtils.hasText(request.getType())) {
                String mimeTypePrefix = request.getType().equalsIgnoreCase("IMAGE") ? "image/" : "video/";
                predicates.add(cb.like(root.get("mimeType"), mimeTypePrefix + "%"));
            }
            // Add sorting
            if (query.getResultType().equals(Media.class)) {
                query.orderBy(createSortOrder(root, cb, request));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private List<Order> createSortOrder(Root<Media> root, CriteriaBuilder cb, MediaSearchRequest request) {
        List<Order> orders = new ArrayList<>();
        // Default sort by createdAt DESC
        if (!StringUtils.hasText(request.getSortByCreatedAt()) || 
            request.getSortByCreatedAt().equalsIgnoreCase("DESC")) {
            orders.add(cb.desc(root.get("createdAt")));
        } else {
            orders.add(cb.asc(root.get("createdAt")));
        }
        // Add additional sorting criteria here if needed
        // Example: orders.add(cb.asc(root.get("originName")));
        
        return orders;
    }
} 