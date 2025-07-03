package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.discount.DiscountCreateRequest;
import com.anhpt.res_app.admin.dto.request.discount.DiscountSearchRequest;
import com.anhpt.res_app.admin.dto.request.discount.DiscountUpdateRequest;
import com.anhpt.res_app.common.entity.Discount;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DiscountRepository;
import com.anhpt.res_app.common.repository.DishRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminDiscountValidation {
    private final DiscountRepository discountRepository;
    private final DishRepository dishRepository;

    public void validateCreate(Long dishId, DiscountCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra Dish tồn tại
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Dish"));
        // Kiểm tra Discount tồn tại Dish
        if (discountRepository.existsByDishId(dishId)) {
            log.warn("DishId {} đã có Discount", dishId);
            throw new IllegalArgumentException("Món ăn đã có Discount");
        }
        // Kiểm tra thời gian bắt đầu và thời gian kết thúc hợp lệ
        LocalDateTime timeStart = request.getTimeStart();
        LocalDateTime timeEnd = request.getTimeEnd();
        LocalDateTime currentTime = LocalDateTime.now();
        // Kiểm tra thời gian bắt đầu phải lớn hơn thời điểm hiện tại
        if (timeStart.isBefore(currentTime) || timeStart.isEqual(currentTime)) {
            String field = FieldNameUtil.getFieldName(DiscountCreateRequest::getTimeStart);
            errors.put(field, "Thời gian bắt đầu phải lớn hơn thời điểm hiện tại");
        }
        if (timeStart.isAfter(timeEnd)) {
            String field = FieldNameUtil.getFieldName(DiscountCreateRequest::getTimeStart);
            errors.put(field, "Thời gian bắt đầu phải trước thời gian kết thúc");
        }
        // Kiểm tra giá khuyến mãi hợp lệ
        if (request.getPriceDiscount().compareTo(dish.getPrice()) >= 0) {
            String field = FieldNameUtil.getFieldName(DiscountCreateRequest::getPriceDiscount);
            errors.put(field, "Giá khuyến mãi phải nhỏ hơn giá món ăn");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdate(Long discountId, DiscountUpdateRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra request không rỗng
        if (request.isEmpty()) {
            throw new IllegalArgumentException("Yêu cầu không hợp lệ");
        }
        // Kiểm tra Discount tồn tại
        Discount discount = discountRepository.findById(discountId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Discount"));

        LocalDateTime currentTime = LocalDateTime.now();

        // Kiểm tra thời gian bắt đầu và thời gian kết thúc hợp lệ
        if (request.getTimeStart() != null && request.getTimeEnd() == null) {
            LocalDateTime timeStart = request.getTimeStart();
            LocalDateTime timeEnd = discount.getTimeEnd();

            // Kiểm tra thời gian bắt đầu phải lớn hơn thời điểm hiện tại
            if (timeStart.isBefore(currentTime) || timeStart.isEqual(currentTime)) {
                String field = FieldNameUtil.getFieldName(DiscountUpdateRequest::getTimeStart);
                errors.put(field, "Thời gian bắt đầu phải lớn hơn thời điểm hiện tại");
            }

            if (timeStart.isAfter(timeEnd)) {
                String field = FieldNameUtil.getFieldName(DiscountUpdateRequest::getTimeStart);
                errors.put(field, "Thời gian bắt đầu phải trước thời gian kết thúc");
            }
        }
        if (request.getTimeEnd() != null && request.getTimeStart() == null) {
            LocalDateTime timeEnd = request.getTimeEnd();
            LocalDateTime timeStart = discount.getTimeStart();
            if (timeEnd.isBefore(timeStart)) {
                String field = FieldNameUtil.getFieldName(DiscountUpdateRequest::getTimeEnd);
                errors.put(field, "Thời gian kết thúc phải sau thời gian bắt đầu");
            }
        }
        if (request.getTimeStart() != null && request.getTimeEnd() != null) {
            LocalDateTime timeStart = request.getTimeStart();
            LocalDateTime timeEnd = request.getTimeEnd();

            // Kiểm tra thời gian bắt đầu phải lớn hơn thời điểm hiện tại
            if (timeStart.isBefore(currentTime) || timeStart.isEqual(currentTime)) {
                String field = FieldNameUtil.getFieldName(DiscountUpdateRequest::getTimeStart);
                errors.put(field, "Thời gian bắt đầu phải lớn hơn thời điểm hiện tại");
            }

            if (timeStart.isAfter(timeEnd)) {
                String field = FieldNameUtil.getFieldName(DiscountUpdateRequest::getTimeStart);
                errors.put(field, "Thời gian bắt đầu phải trước thời gian kết thúc");
            }
        }
        // Kiểm tra giá khuyến mãi hợp lệ
        if (request.getPriceDiscount() != null) {
            if (request.getPriceDiscount().compareTo(discount.getDish().getPrice()) >= 0) {
                String field = FieldNameUtil.getFieldName(DiscountUpdateRequest::getPriceDiscount);
                errors.put(field, "Giá khuyến mãi phải nhỏ hơn giá món ăn");
            }
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateDelete(Long discountId) {
        // Kiểm tra Discount tồn tại
        Discount discount = discountRepository.findById(discountId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Discount"));
    }

    public void validateSearch(DiscountSearchRequest request) {
    }
}
