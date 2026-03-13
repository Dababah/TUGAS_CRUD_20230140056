package com.ktp.mapper;

import com.ktp.dto.KtpRequestDto;
import com.ktp.dto.KtpResponseDto;
import com.ktp.entity.Ktp;
import org.springframework.stereotype.Component;

/**
 * Mapper class to convert between Ktp entity and DTO objects.
 * Handles the transformation layer for the KTP domain.
 */
@Component
public class KtpMapper {

    /**
     * Convert KtpRequestDto to Ktp entity.
     *
     * @param requestDto the request DTO
     * @return Ktp entity
     */
    public Ktp toEntity(KtpRequestDto requestDto) {
        if (requestDto == null)
            return null;

        Ktp ktp = new Ktp();
        ktp.setNomorKtp(requestDto.getNomorKtp());
        ktp.setNamaLengkap(requestDto.getNamaLengkap());
        ktp.setAlamat(requestDto.getAlamat());
        ktp.setTanggalLahir(requestDto.getTanggalLahir());
        ktp.setJenisKelamin(requestDto.getJenisKelamin());
        return ktp;
    }

    /**
     * Convert Ktp entity to KtpResponseDto.
     *
     * @param ktp the Ktp entity
     * @return KtpResponseDto
     */
    public KtpResponseDto toResponseDto(Ktp ktp) {
        if (ktp == null)
            return null;

        KtpResponseDto responseDto = new KtpResponseDto();
        responseDto.setId(ktp.getId());
        responseDto.setNomorKtp(ktp.getNomorKtp());
        responseDto.setNamaLengkap(ktp.getNamaLengkap());
        responseDto.setAlamat(ktp.getAlamat());
        responseDto.setTanggalLahir(ktp.getTanggalLahir());
        responseDto.setJenisKelamin(ktp.getJenisKelamin());
        return responseDto;
    }

    /**
     * Update an existing Ktp entity from KtpRequestDto.
     *
     * @param existingKtp the entity to update
     * @param requestDto  the new data
     */
    public void updateEntityFromDto(Ktp existingKtp, KtpRequestDto requestDto) {
        if (existingKtp == null || requestDto == null)
            return;

        existingKtp.setNomorKtp(requestDto.getNomorKtp());
        existingKtp.setNamaLengkap(requestDto.getNamaLengkap());
        existingKtp.setAlamat(requestDto.getAlamat());
        existingKtp.setTanggalLahir(requestDto.getTanggalLahir());
        existingKtp.setJenisKelamin(requestDto.getJenisKelamin());
    }
}
