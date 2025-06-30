package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminDiscountMapper;
import com.anhpt.res_app.admin.dto.request.discount.DiscountCreateRequest;
import com.anhpt.res_app.admin.dto.request.discount.DiscountSearchRequest;
import com.anhpt.res_app.admin.dto.request.discount.DiscountUpdateRequest;
import com.anhpt.res_app.admin.dto.response.discount.DiscountResponse;
import com.anhpt.res_app.admin.filter.AdminDiscountFilter;
import com.anhpt.res_app.admin.validation.AdminDiscountValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Discount;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DiscountRepository;
import com.anhpt.res_app.common.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminDiscountService {
    // Repository
    private final DiscountRepository discountRepository;
    private final DishRepository dishRepository;
    // Validation
    private final AdminDiscountValidation adminDiscountValidation;
    // Mapper
    private final AdminDiscountMapper adminDiscountMapper;
    // Filter
    private final AdminDiscountFilter adminDiscountFilter;

    public DiscountResponse create(Long dishId, DiscountCreateRequest request) {
        adminDiscountValidation.validateCreate(dishId, request);
        // Lấy Dish
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Dish"));
        // Tạo Discount
        Discount discount = new Discount();
        discount.setDish(dish);
        discount.setPriceDiscount(request.getPriceDiscount());
        discount.setTimeStart(request.getTimeStart());
        discount.setTimeEnd(request.getTimeEnd());
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());
        discount = discountRepository.save(discount);
        return adminDiscountMapper.toDiscountResponse(discount);
    }

    public DiscountResponse update(Long discountId, DiscountUpdateRequest request) {
        adminDiscountValidation.validateUpdate(discountId, request);
        Discount discount = discountRepository.findById(discountId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Discount"));
        if (request.getTimeStart() != null) discount.setTimeStart(request.getTimeStart());
        if (request.getTimeEnd() != null) discount.setTimeEnd(request.getTimeEnd());
        if (request.getPriceDiscount() != null) discount.setPriceDiscount(request.getPriceDiscount());
        discount.setUpdatedAt(LocalDateTime.now());
        discount = discountRepository.save(discount);
        return adminDiscountMapper.toDiscountResponse(discount);
    }

    public void delete(Long discountId) {
        adminDiscountValidation.validateDelete(discountId);
        Discount discount = discountRepository.findById(discountId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Discount"));
        // Loại bỏ priceDiscount nếu đã được thiết lập
        Dish dish = discount.getDish();
        if (dish.getPriceDiscount() != null) {
            dish.setPriceDiscount(null);
            dish.setUpdatedAt(LocalDateTime.now());
            dishRepository.save(dish);
        }
        // Xóa Discount
        discountRepository.delete(discount);
    }

    public PageResponse<DiscountResponse> get(DiscountSearchRequest request) {
        adminDiscountValidation.validateSearch(request);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Discount> discounts = discountRepository.findAll(adminDiscountFilter.search(request), pageable);
        if (discounts.getTotalPages() > 0 && request.getPage() > discounts.getTotalPages()) {
            throw new IllegalArgumentException("Trang không tồn tại");
        }
        List<DiscountResponse> discountResponses = discounts.stream()
            .map(adminDiscountMapper::toDiscountResponse)
            .collect(Collectors.toList());
        return new PageResponse<>(
            discountResponses,
            request.getPage(),
            request.getSize(),
            discounts.getTotalElements(),
            discounts.getTotalPages()
        );
    }

    // Tìm kiếm các bản ghi có timeStart trùng với thời gian hiện tại trong bảng Discount
    public List<Discount> getByTimeStartBetween(LocalDateTime startTime, LocalDateTime endTime) {
        List<Discount> discounts =  discountRepository.findByTimeStartBetween(startTime, endTime);
        if (discounts.isEmpty()) {
            return Collections.emptyList();
        }
        return discounts;
    }
    // Tìm kiếm các bản ghi có timeEnd trùng với thời gian hiện tại trong bảng Discount
    public List<Discount> getByTimeEndBetween(LocalDateTime startTime, LocalDateTime endTime) {
        List<Discount> discounts =  discountRepository.findByTimeEndBetween(startTime, endTime);
        if (discounts.isEmpty()) {
            return Collections.emptyList();
        }
        return discounts;
    }

    // Tìm kiếm và xử lý các discount đã hết hạn
    @Transactional
    public void handleExpiredDiscounts(LocalDateTime currentTime) {
        List<Discount> expiredDiscounts = discountRepository.findExpiredDiscounts(currentTime);
        if (!expiredDiscounts.isEmpty()) {
            log.info("Tìm thấy {} discount đã hết hạn, bắt đầu xử lý", expiredDiscounts.size());
            // Reset priceDiscount cho các dish có discount hết hạn
            expiredDiscounts.forEach(discount -> {
                Dish dish = discount.getDish();
                if (dish != null) {
                    dish.setPriceDiscount(null);
                    dish.setUpdatedAt(LocalDateTime.now());
                    dishRepository.save(dish);
                    log.debug("Đã reset priceDiscount cho dish: {}", dish.getName());
                }
            });
            // Xóa các discount đã hết hạn
            discountRepository.deleteAll(expiredDiscounts);
            log.info("Đã xóa {} discount hết hạn", expiredDiscounts.size());
        }
    }

    // Xóa toàn bộ các Discount
    public void deleteAllByDiscounts(List<Discount> discounts) {
        discountRepository.deleteAll(discounts);
    }
}
