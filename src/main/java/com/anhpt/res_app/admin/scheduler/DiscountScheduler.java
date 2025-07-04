package com.anhpt.res_app.admin.scheduler;

import com.anhpt.res_app.admin.service.AdminDiscountService;
import com.anhpt.res_app.admin.service.AdminDishService;
import com.anhpt.res_app.common.entity.Discount;
import com.anhpt.res_app.common.entity.Dish;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscountScheduler {
    private final AdminDishService adminDishService;
    private final AdminDiscountService adminDiscountService;

    @Scheduled(cron = "0 * * * * *")
    public void handleDiscount() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startRange = now.minusMinutes(1);

        log.debug("Bắt đầu xử lý discount tại thời điểm: {}", now);

        processDiscountStart(startRange, now);
        processDiscountEnd(startRange, now);
        processExpiredDiscounts(now);

        log.debug("Hoàn thành xử lý discount tại thời điểm: {}", now);
    }

    @Transactional
    public void processDiscountStart(LocalDateTime startRange, LocalDateTime now) {
        try {
            List<Discount> discountsStart = adminDiscountService.getByTimeStartBetween(startRange, now);
            if (!discountsStart.isEmpty()) {
                log.info("Tìm thấy {} discount bắt đầu", discountsStart.size());
                handleDiscountStart(discountsStart);
            }
        } catch (Exception e) {
            log.error("Lỗi khi xử lý discount bắt đầu: ", e);
            throw e; // Re-throw để rollback transaction này
        }
    }

    @Transactional
    public void processDiscountEnd(LocalDateTime startRange, LocalDateTime now) {
        try {
            List<Discount> discountsEnd = adminDiscountService.getByTimeEndBetween(startRange, now);
            if (!discountsEnd.isEmpty()) {
                log.info("Tìm thấy {} discount kết thúc", discountsEnd.size());
                handleDiscountEnd(discountsEnd);
            }
        } catch (Exception e) {
            log.error("Lỗi khi xử lý discount kết thúc: ", e);
            throw e;
        }
    }

    @Transactional
    public void processExpiredDiscounts(LocalDateTime now) {
        try {
            adminDiscountService.handleExpiredDiscounts(now);
        } catch (Exception e) {
            log.error("Lỗi khi xử lý discount hết hạn: ", e);
            throw e;
        }
    }

    
    private void handleDiscountStart(List<Discount> discounts) {
        if (!discounts.isEmpty()) {
            Map<Long, BigDecimal> dishPriceDiscountMap = discounts.stream()
                .collect(Collectors.toMap(
                    discount -> discount.getDish().getId(),
                    Discount::getPriceDiscount,
                    (existing, replacement) -> replacement
                ));
            if (!dishPriceDiscountMap.isEmpty()) {
                adminDishService.updatePriceDiscountByDishes(dishPriceDiscountMap);
                log.info("Đã cập nhật priceDiscount cho {} dish", dishPriceDiscountMap.size());
            }
        }
    }

    private void handleDiscountEnd(List<Discount> discounts) {
        if (!discounts.isEmpty()) {
            Map<Long, BigDecimal> dishPriceDiscountMap = new HashMap<>();
            for (Discount discount : discounts) {
                Dish dish = discount.getDish();
                if (dish != null) {
                    dishPriceDiscountMap.put(dish.getId(), null); // Gán null an toàn
                }
            }
            if (!dishPriceDiscountMap.isEmpty()) {
                adminDishService.updatePriceDiscountByDishes(dishPriceDiscountMap);
                adminDiscountService.deleteAllByDiscounts(discounts);
                log.info("Đã reset priceDiscount và xóa {} discount", discounts.size());
            }
        }
    }
}
