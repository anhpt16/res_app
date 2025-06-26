package com.anhpt.res_app.common.config.security;

import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ErrorFilter {
    public static void writeErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        ApiResponse<Object> errorResponse = new ApiResponse<>(
            status.value(),
            false,
            message,
            null
        );
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}
