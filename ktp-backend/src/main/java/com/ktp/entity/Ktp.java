package com.ktp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entity class representing a KTP (Kartu Tanda Penduduk / Indonesian ID Card).
 * Maps to the 'ktp' table in the 'spring' database.
 */
@Entity
@Table(name = "ktp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ktp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nomorKtp", unique = true, nullable = false, length = 16)
    private String nomorKtp;

    @Column(name = "namaLengkap", nullable = false, length = 255)
    private String namaLengkap;

    @Column(name = "alamat", nullable = false, length = 500)
    private String alamat;

    @Column(name = "tanggalLahir", nullable = false)
    private LocalDate tanggalLahir;

    @Column(name = "jenisKelamin", nullable = false, length = 20)
    private String jenisKelamin;
}
