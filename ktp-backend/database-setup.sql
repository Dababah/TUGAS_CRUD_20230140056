-- ============================================================
-- Database Setup Script for KTP CRUD Application
-- ============================================================
-- Run this script in MySQL before starting the Spring Boot app
-- ============================================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS spring
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Use the database
USE spring;

-- Create KTP table
CREATE TABLE IF NOT EXISTS ktp (
    id            INT          NOT NULL AUTO_INCREMENT,
    nomorKtp      VARCHAR(16)  NOT NULL UNIQUE COMMENT 'NIK 16 digit',
    namaLengkap   VARCHAR(255) NOT NULL COMMENT 'Nama lengkap sesuai KTP',
    alamat        VARCHAR(500) NOT NULL COMMENT 'Alamat tempat tinggal',
    tanggalLahir  DATE         NOT NULL COMMENT 'Tanggal lahir (YYYY-MM-DD)',
    jenisKelamin  VARCHAR(20)  NOT NULL COMMENT 'Laki-laki / Perempuan',
    PRIMARY KEY (id),
    INDEX idx_nomorKtp (nomorKtp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- Insert sample data (opsional, untuk testing)
-- ============================================================
INSERT INTO ktp (nomorKtp, namaLengkap, alamat, tanggalLahir, jenisKelamin) VALUES
('3275011234560001', 'Budi Santoso',     'Jl. Merdeka No. 1, Jakarta Selatan',   '1990-05-15', 'Laki-laki'),
('3275019876540002', 'Siti Rahayu',      'Jl. Sudirman No. 45, Bandung',          '1995-08-20', 'Perempuan'),
('3275011111110003', 'Ahmad Fauzi',      'Jl. Gatot Subroto No. 12, Surabaya',   '1988-12-01', 'Laki-laki');

-- Verify
SELECT * FROM ktp;
