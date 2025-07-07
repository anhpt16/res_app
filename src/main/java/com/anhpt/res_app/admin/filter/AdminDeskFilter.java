package com.anhpt.res_app.admin.filter;

import com.anhpt.res_app.admin.dto.request.desk.DeskDurationSearchRequest;
import com.anhpt.res_app.admin.dto.request.desk.DeskSearchRequest;
import com.anhpt.res_app.common.entity.Desk;
import com.anhpt.res_app.common.entity.DeskDuration;
import com.anhpt.res_app.common.enums.DeskPosition;
import com.anhpt.res_app.common.enums.DeskSeat;
import com.anhpt.res_app.common.enums.DeskType;
import com.anhpt.res_app.common.enums.status.DeskDurationStatus;
import com.anhpt.res_app.common.enums.status.DeskStatus;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminDeskFilter {
    public Specification<Desk> search(DeskSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Thêm lọc trạng thái nếu có
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), DeskStatus.fromCode(request.getStatus())));
            }
            // Thêm lọc số chỗ ngồi nếu có
            if (StringUtils.hasText(request.getSeat())) {
                predicates.add(criteriaBuilder.equal(root.get("seat"), DeskSeat.fromCode(request.getSeat())));
            }
            // Thêm lọc loại bàn nếu có
            if (StringUtils.hasText(request.getType())) {
                predicates.add(criteriaBuilder.equal(root.get("type"), DeskType.fromCode(request.getType())));
            }
            // Thêm lọc vị trí bàn nếu có
            if (StringUtils.hasText(request.getPosition())) {
                predicates.add(criteriaBuilder.equal(root.get("position"), DeskPosition.fromCode(request.getPosition())));
            }
            // Sắp xếp
            List<Order> orders = new ArrayList<>();
            // Sắp xếp theo createdAt
            if (StringUtils.hasText(request.getSortByCreatedAt())) {
                orders.add(request.getSortByCreatedAt().equalsIgnoreCase("DESC") ?
                    criteriaBuilder.desc(root.get("createdAt")) :
                    criteriaBuilder.asc(root.get("createdAt")));
            }
            // Sắp xếp theo số bàn (id) nếu có
            if (StringUtils.hasText(request.getSortByNumber())) {
                orders.add(request.getSortByNumber().equalsIgnoreCase("DESC") ?
                    criteriaBuilder.desc(root.get("number")) :
                    criteriaBuilder.asc(root.get("number")));
            }
            query.orderBy(orders);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<DeskDuration> deskDurationSearch(Integer deskNumber, DeskDurationSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("desk").get("number"), deskNumber));
            // Thêm lọc trạng thái nếu có
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), DeskDurationStatus.fromCode(request.getStatus())));
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
