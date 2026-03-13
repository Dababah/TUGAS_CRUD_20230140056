package com.ktp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for KTP response payload.
 * Used for returning KTP data to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KtpResponseDto {

    private Integer id;
    private String nomorKtp;
    private String namaLengkap;
    private String alamat;
    private LocalDate tanggalLahir;
    private String jenisKelamin;
}
