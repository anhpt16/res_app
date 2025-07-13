package com.anhpt.res_app.common.api;

import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.reservation.ReservationOptionResponse;
import com.anhpt.res_app.common.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reservation")
@RequiredArgsConstructor
@Validated
public class ReservationApi {
    private final ReservationService reservationService;

    @GetMapping("/init-options")
    public ResponseEntity<ApiResponse<ReservationOptionResponse>> initReservationOption() {
        ReservationOptionResponse reservationOptionResponse = reservationService.initReservationOptions();
        ApiResponse<ReservationOptionResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy lựa chọn thành công",
            reservationOptionResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
