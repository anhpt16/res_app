package com.anhpt.res_app.admin.scheduler;

import com.anhpt.res_app.admin.service.AdminDishService;
import com.anhpt.res_app.admin.service.AdminDishSetupService;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishSetup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DishSetupScheduler {
    private final AdminDishService adminDishService;
    private final AdminDishSetupService adminDishSetupService;

    // Chạy lúc 00:00 hàng ngày
    @Scheduled(cron = "0 0 0 * * *")
    public void handleStateTransitions() {
        // Lấy thời gian hiện tại (yyyy-mm-dd 00:00:00)
        LocalDateTime now = (LocalDate.now()).atStartOfDay();
        // Lấy các bản ghi có milestone trùng khớp với thời điểm hiện tại trong bảng Setup
        List<DishSetup> dishSetups = adminDishSetupService.getDishSetupsByMilestone(now);
        if (!dishSetups.isEmpty()) {
            // Tạo map dish -> setup
            Map<Dish, DishSetup> dishesMapBySetups = dishSetups.stream()
                .filter(setup -> setup.getDish() != null)
                .collect(Collectors.toMap(
                    DishSetup::getDish,
                    Function.identity()
                ));
            if (!dishesMapBySetups.isEmpty()) {
                // Gọi Service chuyển trạng thái của các Dish
                adminDishService.updateDishSetupStatus(dishesMapBySetups);
                // Sau khi chuyển trạng thái, xóa bản ghi hiện tại trong bảng Setup
                adminDishSetupService.deleteDishSetups(dishSetups);
            }
        }
    }

}
