package com.ktp.controller;

import com.ktp.dto.ApiResponseDto;
import com.ktp.dto.KtpRequestDto;
import com.ktp.dto.KtpResponseDto;
import com.ktp.service.KtpService;
import com.ktp.util.KtpUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for KTP CRUD operations.
 * Handles HTTP requests and delegates to KtpService.
 */
@RestController
@RequestMapping("/ktp")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class KtpController {

    private final KtpService ktpService;

    /**
     * POST /ktp - Create a new KTP record.
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<KtpResponseDto>> createKtp(
            @Valid @RequestBody KtpRequestDto requestDto) {
        log.info("POST /ktp - Creating new KTP");
        try {
            KtpResponseDto response = ktpService.createKtp(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDto.success(KtpUtil.KTP_CREATED, response));
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }

    /**
     * GET /ktp - Get all KTP records.
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<KtpResponseDto>>> getAllKtp() {
        log.info("GET /ktp - Fetching all KTPs");
        List<KtpResponseDto> ktpList = ktpService.getAllKtp();
        return ResponseEntity.ok(ApiResponseDto.success(KtpUtil.KTP_LIST_FETCHED, ktpList));
    }

    /**
     * GET /ktp/{id} - Get a KTP by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<KtpResponseDto>> getKtpById(@PathVariable Integer id) {
        log.info("GET /ktp/{} - Fetching KTP by id", id);
        try {
            KtpResponseDto ktp = ktpService.getKtpById(id);
            return ResponseEntity.ok(ApiResponseDto.success(KtpUtil.KTP_FETCHED, ktp));
        } catch (EntityNotFoundException e) {
            log.warn("KTP not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }

    /**
     * PUT /ktp/{id} - Update a KTP by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<KtpResponseDto>> updateKtp(
            @PathVariable Integer id,
            @Valid @RequestBody KtpRequestDto requestDto) {
        log.info("PUT /ktp/{} - Updating KTP", id);
        try {
            KtpResponseDto updated = ktpService.updateKtp(id, requestDto);
            return ResponseEntity.ok(ApiResponseDto.success(KtpUtil.KTP_UPDATED, updated));
        } catch (EntityNotFoundException e) {
            log.warn("KTP not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }

    /**
     * DELETE /ktp/{id} - Delete a KTP by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteKtp(@PathVariable Integer id) {
        log.info("DELETE /ktp/{} - Deleting KTP", id);
        try {
            ktpService.deleteKtp(id);
            return ResponseEntity.ok(ApiResponseDto.success(KtpUtil.KTP_DELETED, null));
        } catch (EntityNotFoundException e) {
            log.warn("KTP not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }

    /**
     * Handle validation errors from @Valid annotation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<List<String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        log.warn("Validation errors: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(false, "Validasi gagal", errors));
    }

    /**
     * Handle unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDto.error("Terjadi kesalahan pada server: " + ex.getMessage()));
    }
}
