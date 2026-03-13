package com.ktp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for KTP request payload.
 * Used for POST and PUT operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KtpRequestDto {

    @NotBlank(message = "Nomor KTP tidak boleh kosong")
    @Size(min = 16, max = 16, message = "Nomor KTP harus 16 digit")
    private String nomorKtp;

    @NotBlank(message = "Nama lengkap tidak boleh kosong")
    @Size(max = 255, message = "Nama lengkap maksimal 255 karakter")
    private String namaLengkap;

    @NotBlank(message = "Alamat tidak boleh kosong")
    @Size(max = 500, message = "Alamat maksimal 500 karakter")
    private String alamat;

    @NotNull(message = "Tanggal lahir tidak boleh kosong")
    private LocalDate tanggalLahir;

    @NotBlank(message = "Jenis kelamin tidak boleh kosong")
    private String jenisKelamin;
}
