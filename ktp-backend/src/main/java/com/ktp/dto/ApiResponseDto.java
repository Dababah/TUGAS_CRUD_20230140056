package com.ktp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API Response wrapper for consistent response format.
 *
 * @param <T> the type of the data payload
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponseDto<T> success(String message, T data) {
        return new ApiResponseDto<>(true, message, data);
    }

    public static <T> ApiResponseDto<T> error(String message) {
        return new ApiResponseDto<>(false, message, null);
    }
}
