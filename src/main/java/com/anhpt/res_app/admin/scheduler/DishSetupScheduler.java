package com.anhpt.res_app.admin.scheduler;

import com.anhpt.res_app.admin.service.AdminDishService;
import com.anhpt.res_app.admin.service.AdminDishSetupService;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishSetup;
import com.anhpt.res_app.common.repository.DishSetupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final DishSetupRepository dishSetupRepository;
    /**
     * - Món ăn sắp ra mắt (active -> published): cho khách hàng đặt trước món ăn tính từ thời điểm phát hành
     * - Món ăn sắp kết thúc (published -> inactive): không cho khách hàng đặt món ăn sau thời điểm kết thúc
     * - Hệ thống sẽ so sánh dựa trên các bản ghi của bảng DishSetup: Tại một thời điểm, sau khi lấy ra danh sách món ăn ở trạng thái phát hành
     *   hệ thống sẽ tìm kiếm thêm các món ăn thỏa mãn điều kiện trong bảng DishSetup:
     *   + Kiểm tra danh sách món ăn sắp ra mắt, sắp kết thúc.
     *   + Món ăn có thời điểm phát hành <= thời điểm hiện tại thì được phép thêm vào danh sách các món ăn ở trạng thái phát hành
     *   + Món ăn có thời điểm kết thúc < thời điểm hiện tại thì xóa khỏi danh sách món ăn ở trạng thái phát hành
     *  - Lưu ý: nếu hệ thống lỗi (khiến khoảng thời gian chuyển trạng thái của món ăn chưa được thực hiện trong thời gian đó)
     *    Các món ăn chưa chuyển trạng thái vẫn tồn tại trong bảng, khiến việc kiểm tra món ăn sắp ra mắt và sắp kết thúc vẫn được thực hiện
     *    Lúc này phải thực hiện kiểm tra các bản ghi cũ (các bản ghi có mốc thời gian trước thời điểm hiện tại mà chưa bị xóa)
     *    --> Phải thực hiện tiến hành chuyển trạng thái cho bản ghi khi phát hiện sau đó mới được xóa bản ghi để không xảy ra lỗi.
     */
    // Chạy lúc 00:00 hàng ngày
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void handleStateTransitions() {
        // Lấy thời gian hiện tại (yyyy-mm-dd 00:00:00)
        LocalDateTime now = (LocalDate.now()).atStartOfDay();
        // Lấy các bản ghi có milestone <= với thời điểm hiện tại trong bảng Setup
        List<DishSetup> dishSetups = adminDishSetupService.getDishSetupsByMilestone(now);
        if (!dishSetups.isEmpty()) {
            // Tạo map dish -> setup
            Map<Long, DishSetup> dishesMapBySetups = dishSetups.stream()
                .filter(setup -> setup.getDish() != null)
                .collect(Collectors.toMap(
                    setup -> setup.getDish().getId(),
                    Function.identity()
                ));
            if (!dishesMapBySetups.isEmpty()) {
                // Gọi Service chuyển trạng thái của các Dish
                adminDishService.updateDishSetupStatus(dishesMapBySetups);
                // Sau khi chuyển trạng thái, xóa bản ghi hiện tại trong bảng Setup
                dishSetupRepository.deleteAll(dishSetups);
            }
        }
    }

}
