package com.anhpt.res_app.common.api;


import com.anhpt.res_app.common.dto.request.socket.DeskMessageMultiRequest;
import com.anhpt.res_app.common.dto.request.socket.DeskMessageSingleRequest;
import com.anhpt.res_app.common.dto.response.socket.DeskMessageResponse;
import com.anhpt.res_app.common.enums.status.DeskMessageStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.service.RedisService;
import com.anhpt.res_app.common.utils.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageApi {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RedisService redisService;

    @MessageMapping("/send-message")
    @SendTo("/topic")
    public String sendMessage(Message<?> message, String greet) {
        Long userId = SecurityContextUtil.getCurrentUserIdFromMessage(message);
        if (userId == null) {
            throw new ResourceNotFoundException("Không tìm thấy userId");
        }
        System.out.println("Message From Client with userId: " + userId + "-" + greet);
        greet = userId + "-" + greet;
        return greet;
    }

    @MessageMapping("/view-table")
    @SendTo("/topic")
    public List<DeskMessageResponse> handleViewDesk(Message<?> message, DeskMessageSingleRequest request) {
        System.out.println(request.getDeskNumber());
        Long userId = SecurityContextUtil.getCurrentUserIdFromMessage(message);
        if (userId == null) {
            throw new ResourceNotFoundException("Không tìm thấy userId");
        }
        // Kiểm tra xem bàn có được giữ trong Redis hay không -> Kiểm tra bàn có đang được giữ bởi người dùng hiện tại hay không
        Long userIdResult =  redisService.get(String.valueOf(request.getDeskNumber()), Long.class);
        if (userIdResult != null) {
            if (userIdResult.equals(userId)) {
                log.error("UserId: {} đã chọn bàn DeskNumber: {}", userId, request.getDeskNumber());
                return Collections.emptyList();
            }
            log.error("DeskNumber: {} đã được chọn bởi UserId: {}", request.getDeskNumber(), userIdResult);
        }
        // TODO: Kiểm tra xem bàn có được đặt trong DB hay không
        // Tiến hành chọn bàn
        redisService.set(String.valueOf(request.getDeskNumber()), userId);
        DeskMessageResponse deskMessageResponse = DeskMessageResponse.builder()
            .deskNumber(request.getDeskNumber())
            .userId(userId)
            .status(DeskMessageStatus.VIEW)
            .build();
        return new ArrayList<DeskMessageResponse>(List.of(deskMessageResponse));
    }

    @MessageMapping("/remove-view-table")
    @SendTo("/topic")
    public List<DeskMessageResponse> handleRemoveViewDesk(Message<?> message, DeskMessageSingleRequest request) {
        System.out.println(request.getDeskNumber());
        Long userId = SecurityContextUtil.getCurrentUserIdFromMessage(message);
        if (userId == null) {
            throw new ResourceNotFoundException("Không tìm thấy User");
        }
        // Kiểm tra xem bàn có được giữ bởi người dùng trong Redis hay không
        Long userIdResult = redisService.get(String.valueOf(request.getDeskNumber()), Long.class);
        if (userIdResult != null) {
            if (!userIdResult.equals(userId)) {
                log.error("UserId: {} không chọn xem DeskNumber: {}", userId, request.getDeskNumber());
                return Collections.emptyList();
            } else {
                // Tiến hành bỏ chọn bàn
                boolean result = redisService.delete(String.valueOf(request.getDeskNumber()));
                if (result) {
                    DeskMessageResponse deskMessageResponse = DeskMessageResponse.builder()
                        .deskNumber(request.getDeskNumber())
                        .userId(userId)
                        .status(DeskMessageStatus.EMPTY)
                        .build();
                    return new ArrayList<DeskMessageResponse>(List.of(deskMessageResponse));
                }
            }
        }
        return Collections.emptyList();
    }

    @MessageMapping("/switch-view-table")
    @SendTo("/topic")
    public List<DeskMessageResponse> handleSwitchViewDesk(Message<?> message, DeskMessageMultiRequest request) {
        System.out.println(request.getOldDesk() + "-" + request.getNewDesk());
        Long userId = SecurityContextUtil.getCurrentUserIdFromMessage(message);
        if (userId == null) {
            throw new ResourceNotFoundException("Không tìm thấy userId");
        }
        // Kiểm tra xem bàn cũ có được giữ bởi người dùng trong Redis hay không
        Long userIdResultWithOldDesk = redisService.get(String.valueOf(request.getOldDesk()), Long.class);
        if (userIdResultWithOldDesk == null || !userIdResultWithOldDesk.equals(userId)) {
            log.error("UserId: {} không chọn xem DeskNumber: {}", userId, request.getOldDesk());
            return Collections.emptyList();
        }
        // Kiểm tra xem bàn mới có được giữ trong Redis/DB hay không
        Long userIdResultWithNewDesk = redisService.get(String.valueOf(request.getNewDesk()), Long.class);
        if (userIdResultWithNewDesk != null) {
            if (!userIdResultWithNewDesk.equals(userId)) {
                log.error("DeskNumber: {} đã được chọn bởi user hiện tại UserId: {}", request.getNewDesk(), userId);
            } else {
                log.error("DeskNumber: {} đã được chọn bởi user khác userId: {}", request.getNewDesk(), userIdResultWithNewDesk);
            }
            return Collections.emptyList();
        }
        // Thực hiện lock bàn mới và tiến hành tạo bàn ghi tương ứng trong Redis
        redisService.set(String.valueOf(request.getNewDesk()), userId);
        if (!redisService.hasKey(String.valueOf(request.getNewDesk()))) {
            return Collections.emptyList();
        }
        // Nếu thất bại không trả về gì | hoặc trả về message lỗi cho user
        // Nếu ghi thành công, tiến hành xóa bàn cũ trong Redis
        redisService.delete(String.valueOf(request.getOldDesk()));
        if (redisService.hasKey(String.valueOf(request.getOldDesk()))) {
            return Collections.emptyList();
        }
        // Trả về message: giải phòng bàn và chọn bàn mới
        DeskMessageResponse messageOldDesk = DeskMessageResponse.builder()
            .deskNumber(request.getOldDesk())
            .userId(userId)
            .status(DeskMessageStatus.EMPTY)
            .build();
        DeskMessageResponse messageNewDesk = DeskMessageResponse.builder()
            .deskNumber(request.getNewDesk())
            .userId(userId)
            .status(DeskMessageStatus.VIEW)
            .build();
        return new ArrayList<DeskMessageResponse>(List.of(messageOldDesk, messageNewDesk));
    }
}
