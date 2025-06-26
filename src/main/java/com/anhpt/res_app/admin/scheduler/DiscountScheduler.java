package com.anhpt.res_app.admin.scheduler;

import com.anhpt.res_app.admin.service.AdminDiscountService;
import com.anhpt.res_app.admin.service.AdminDishService;
import com.anhpt.res_app.common.entity.Discount;
import com.anhpt.res_app.common.entity.Dish;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscountScheduler {
    private final AdminDishService adminDishService;
    private final AdminDiscountService adminDiscountService;

    // Chạy mỗi phút
    @Scheduled(cron = "0 * * * * *")
    public void handleDiscount() {
        // Lấy thời gian hiện tại (yyyy-mm-dd hh:mm:ss)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startRange = now.minusMinutes(1);
        // Tìm kiếm các bản ghi có timeStart trùng với thời gian hiện tại trong bảng Discount
        List<Discount> discountsStart = adminDiscountService.getByTimeStartBetween(startRange, now);
        handleDiscountStart(discountsStart);
        // Tìm kiếm các bản ghi có timeEnd trùng với thời gian hiện tại trong bảng Discount
        List<Discount> discountsEnd = adminDiscountService.getByTimeEndBetween(startRange, now);
        handleDiscountEnd(discountsEnd);
    }

    private void handleDiscountStart(List<Discount> discounts) {
        if (!discounts.isEmpty()) {
            // Tạo map dish -> priceDiscount
            Map<Dish, BigDecimal> dishPriceDiscountMap = discounts.stream()
                .collect(Collectors.toMap(
                    Discount::getDish,
                    Discount::getPriceDiscount,
                    (existing, replacement) -> replacement // Nếu có duplicate key, lấy giá trị mới
                ));
            if (!dishPriceDiscountMap.isEmpty()) {
                // Gọi service cập nhật priceDiscount cho các Dish
                adminDishService.updatePriceDiscountByDishes(dishPriceDiscountMap);
            }
        }
    }

    private void handleDiscountEnd(List<Discount> discounts) {
        if (!discounts.isEmpty()) {
            // Tạo map dish -> priceDiscount
            Map<Dish, BigDecimal> dishPriceDiscountMap = discounts.stream()
                .collect(Collectors.toMap(
                    Discount::getDish,
                    null,
                    (existing, replacement) -> replacement // Nếu có duplicate key, lấy giá trị mới
                ));
            if (!dishPriceDiscountMap.isEmpty()) {
                // Gọi service cập nhật priceDiscount cho các Dish
                adminDishService.updatePriceDiscountByDishes(dishPriceDiscountMap);
                // Sau khi cập nhật priceDiscount, xóa bản ghi này trong Discount
                adminDiscountService.deleteAllByDiscounts(discounts);
            }
        }
    }
}
