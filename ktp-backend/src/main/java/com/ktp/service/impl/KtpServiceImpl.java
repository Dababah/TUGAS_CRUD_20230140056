package com.ktp.service.impl;

import com.ktp.dto.KtpRequestDto;
import com.ktp.dto.KtpResponseDto;
import com.ktp.entity.Ktp;
import com.ktp.mapper.KtpMapper;
import com.ktp.repository.KtpRepository;
import com.ktp.service.KtpService;
import com.ktp.util.KtpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of KtpService.
 * Handles all business logic for KTP CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KtpServiceImpl implements KtpService {

    private final KtpRepository ktpRepository;
    private final KtpMapper ktpMapper;

    @Override
    public KtpResponseDto createKtp(KtpRequestDto requestDto) {
        log.info("Creating KTP with nomorKtp: {}", requestDto.getNomorKtp());

        // Validate nomor KTP format
        if (!KtpUtil.isValidNomorKtp(requestDto.getNomorKtp())) {
            throw new IllegalArgumentException("Format Nomor KTP tidak valid. Harus 16 digit angka.");
        }

        // Validate jenis kelamin
        if (!KtpUtil.isValidJenisKelamin(requestDto.getJenisKelamin())) {
            throw new IllegalArgumentException("Jenis kelamin tidak valid. Pilih 'Laki-laki' atau 'Perempuan'.");
        }

        // Check if nomorKtp already exists
        if (ktpRepository.existsByNomorKtp(requestDto.getNomorKtp())) {
            throw new IllegalArgumentException(
                    String.format(KtpUtil.KTP_NOMOR_EXISTED, requestDto.getNomorKtp()));
        }

        Ktp ktp = ktpMapper.toEntity(requestDto);
        Ktp saved = ktpRepository.save(ktp);
        log.info("KTP created successfully with id: {}", saved.getId());
        return ktpMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KtpResponseDto> getAllKtp() {
        log.info("Fetching all KTP records");
        return ktpRepository.findAll()
                .stream()
                .map(ktpMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public KtpResponseDto getKtpById(Integer id) {
        log.info("Fetching KTP with id: {}", id);
        Ktp ktp = ktpRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        String.format(KtpUtil.KTP_NOT_FOUND, id)));
        return ktpMapper.toResponseDto(ktp);
    }

    @Override
    public KtpResponseDto updateKtp(Integer id, KtpRequestDto requestDto) {
        log.info("Updating KTP with id: {}", id);

        // Validate nomor KTP format
        if (!KtpUtil.isValidNomorKtp(requestDto.getNomorKtp())) {
            throw new IllegalArgumentException("Format Nomor KTP tidak valid. Harus 16 digit angka.");
        }

        // Validate jenis kelamin
        if (!KtpUtil.isValidJenisKelamin(requestDto.getJenisKelamin())) {
            throw new IllegalArgumentException("Jenis kelamin tidak valid. Pilih 'Laki-laki' atau 'Perempuan'.");
        }

        // Find existing entity
        Ktp existingKtp = ktpRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        String.format(KtpUtil.KTP_NOT_FOUND, id)));

        // Check if the new nomorKtp is already used by another record
        if (ktpRepository.existsByNomorKtpAndIdNot(requestDto.getNomorKtp(), id)) {
            throw new IllegalArgumentException(
                    String.format(KtpUtil.KTP_NOMOR_EXISTED, requestDto.getNomorKtp()));
        }

        ktpMapper.updateEntityFromDto(existingKtp, requestDto);
        Ktp updated = ktpRepository.save(existingKtp);
        log.info("KTP updated successfully with id: {}", updated.getId());
        return ktpMapper.toResponseDto(updated);
    }

    @Override
    public void deleteKtp(Integer id) {
        log.info("Deleting KTP with id: {}", id);

        if (!ktpRepository.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException(
                    String.format(KtpUtil.KTP_NOT_FOUND, id));
        }

        ktpRepository.deleteById(id);
        log.info("KTP deleted successfully with id: {}", id);
    }
}
