package com.ktp.repository;

import com.ktp.entity.Ktp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Ktp entity.
 * Extends JpaRepository to provide CRUD operations via Spring Data JPA.
 */
@Repository
public interface KtpRepository extends JpaRepository<Ktp, Integer> {

    /**
     * Find KTP by nomor KTP (NIK).
     *
     * @param nomorKtp the NIK to search for
     * @return Optional containing the KTP if found
     */
    Optional<Ktp> findByNomorKtp(String nomorKtp);

    /**
     * Check if a KTP with given nomor KTP exists.
     *
     * @param nomorKtp the NIK to check
     * @return true if exists, false otherwise
     */
    boolean existsByNomorKtp(String nomorKtp);

    /**
     * Check if a KTP with given nomor KTP exists but is NOT the given id.
     * Used for update validation.
     *
     * @param nomorKtp the NIK to check
     * @param id       the id to exclude
     * @return true if another record uses this NIK
     */
    boolean existsByNomorKtpAndIdNot(String nomorKtp, Integer id);
}
