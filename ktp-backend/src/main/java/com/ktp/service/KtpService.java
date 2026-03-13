package com.ktp.service;

import com.ktp.dto.KtpRequestDto;
import com.ktp.dto.KtpResponseDto;

import java.util.List;

/**
 * Service interface defining business operations for KTP.
 */
public interface KtpService {

    /**
     * Create a new KTP record.
     *
     * @param requestDto the data to save
     * @return the saved KTP as response DTO
     */
    KtpResponseDto createKtp(KtpRequestDto requestDto);

    /**
     * Retrieve all KTP records.
     *
     * @return list of all KTPs
     */
    List<KtpResponseDto> getAllKtp();

    /**
     * Retrieve a KTP by its ID.
     *
     * @param id the ID to search
     * @return the found KTP as response DTO
     */
    KtpResponseDto getKtpById(Integer id);

    /**
     * Update an existing KTP by its ID.
     *
     * @param id         the ID of the KTP to update
     * @param requestDto the new data
     * @return the updated KTP as response DTO
     */
    KtpResponseDto updateKtp(Integer id, KtpRequestDto requestDto);

    /**
     * Delete a KTP by its ID.
     *
     * @param id the ID of the KTP to delete
     */
    void deleteKtp(Integer id);
}
