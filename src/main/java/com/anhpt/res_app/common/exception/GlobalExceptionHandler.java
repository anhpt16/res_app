package com.anhpt.res_app.common.exception;

import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.exception.file.FileDeleteException;
import com.anhpt.res_app.common.exception.file.FileInvalidException;
import com.anhpt.res_app.common.exception.file.FileUploadException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Exception hệ thống
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handlePathParamValidationExceptions(
        HandlerMethodValidationException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach(err -> {
            if (err instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else if (err instanceof ObjectError objectError) {
                errors.put(objectError.getObjectName(), objectError.getDefaultMessage());
            }
        });
        ApiResponse<Map<String, String>> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            false,
            "Tham số không hợp lệ",
            errors
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<Map<String, String>> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            false,
            "Dữ liệu không hợp lệ",
            errors
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
        IllegalArgumentException ex
    ) {
        log.warn("Tham số không hợp lệ: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            false,
            "Giá trị không hợp lệ. Vui lòng thử lại",
            null
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleUnexpectedException(
        Exception ex
    ) {
        log.error("Lỗi không xác định: ", ex);
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            false,
            "Đã xảy ra lỗi. Vui lòng thử lại sau",
            null
        );
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
        ConstraintViolationException ex
    ) {
        log.warn("Lỗi kiểm tra dữ liệu: {}", ex.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            false,
            "Dữ liệu không hợp lệ",
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex
    ) {
        log.warn("Yêu cầu thất bại: {}", ex.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            false,
            "Yêu cầu không hợp lệ",
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(
        AccessDeniedException ex
    ) {
        log.warn("Truy cập bị từ chối: {}", ex.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.FORBIDDEN.value(),
            false,
            "Truy cập bị từ chối",
            null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // Exception cơ bản
    @ExceptionHandler(MultiDuplicateException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMultiDuplicateException(
        MultiDuplicateException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getErrors().forEach((key, value) -> errors.put(key, value));
        ApiResponse<Map<String, String>> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            false,
            "Dữ liệu không hợp lệ",
            errors
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
        ResourceNotFoundException ex
    ) {
        log.warn("Không tìm thấy tài nguyên: {}", ex.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.NOT_FOUND.value(),
            false,
            "Tài nguyên không tồn tại",
            null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbiddenActionException(
        ForbiddenActionException ex
    ) {
        log.warn("Hành động không được cho phép: {}", ex.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            false,
            "Hành động không được cho phép",
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(
        BadCredentialsException ex
    ) {
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            false,
            "Tài khoản hoặc mật khẩu không đúng",
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Exception xử lý File
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiResponse<Object>> handleFileUploadException(
        FileUploadException ex
    ) {
        log.warn("Upload thất bại: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            false,
            "Lỗi khi tải file",
            null
        );
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
    @ExceptionHandler(FileDeleteException.class)
    public ResponseEntity<ApiResponse<Object>> handleFileDeleteException(
        FileDeleteException ex
    ) {
        log.warn("Xóa thất bại: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            false,
            "Lỗi khi xóa file",
            null
        );
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
    @ExceptionHandler(FileInvalidException.class)
    public ResponseEntity<ApiResponse<Object>> handleFileInvalidException(
        FileInvalidException ex
    ) {
        log.warn("Upload thất bại: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            false,
            "File không hợp lệ",
            null
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    // Exception - Media
    @ExceptionHandler(MediaNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleMediaNotFoundException(
        MediaNotFoundException ex
    ) {
        log.warn("Không tìm thấy file: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.BAD_REQUEST.value(),
            false,
            "File không hợp lệ",
            null
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    @ExceptionHandler(MediaException.class)
    public ResponseEntity<ApiResponse<Object>> handleMediaException(
        Exception ex
    ) {
        log.error("Lỗi không xác định: ", ex);
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            false,
            "Đã xảy ra lỗi. Vui lòng thử lại sau",
            null
        );
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }


    
}
