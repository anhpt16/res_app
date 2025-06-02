package com.anhpt.res_app.admin.filter;

import com.anhpt.res_app.admin.dto.request.post.PostSearchRequest;
import com.anhpt.res_app.common.entity.Post;
import com.anhpt.res_app.common.entity.Tag;
import com.anhpt.res_app.common.entity.TagPost;
import com.anhpt.res_app.common.entity.User;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminPostFilter {
    public Specification<Post> searchPost(PostSearchRequest request, User user) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Lọc ra các bản ghi của user đang đăng nhập
            if (request.getIsMyPost() != null && request.getIsMyPost()) {
                predicates.add(criteriaBuilder.equal(root.get("user"), user));
            }
            // Lọc theo từ khóa tìm kiếm
            if (StringUtils.hasText(request.getSearchTerm())) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + request.getSearchTerm().toLowerCase().trim() + "%"
                ));
            }
            // Lọc theo trạng thái bài viết
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            // Lọc theo thẻ
            if (StringUtils.hasText(request.getTagId())) {
                Join<Post, TagPost> tagPostJoin = root.join("tagPosts", JoinType.INNER);
                Join<TagPost, Tag> tagJoin = tagPostJoin.join("tag", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(tagJoin.get("id"), Long.parseLong(request.getTagId())));
            }
            // Add sorting
            if (query.getResultType().equals(Post.class)) {
                query.orderBy(createSortOrder(root, criteriaBuilder, request));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public List<Order> createSortOrder(Root<Post> root, CriteriaBuilder criteriaBuilder, PostSearchRequest request) {
        List<Order> orders = new ArrayList<>();
        if (StringUtils.hasText(request.getSortByCreatedAt()) && request.getSortByCreatedAt().equals("DESC")) {
            orders.add(criteriaBuilder.desc(root.get("createdAt")));
        } else {
            orders.add(criteriaBuilder.asc(root.get("createdAt")));
        }
        return orders;
    }
}
