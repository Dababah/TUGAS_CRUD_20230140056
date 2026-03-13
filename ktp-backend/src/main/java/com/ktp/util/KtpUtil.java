package com.ktp.util;

/**
 * Utility class containing constants and helper methods
 * used across the KTP application.
 */
public class KtpUtil {

    // Message constants
    public static final String KTP_NOT_FOUND = "Data KTP dengan id %d tidak ditemukan";
    public static final String KTP_NOMOR_EXISTED = "Nomor KTP '%s' sudah terdaftar";
    public static final String KTP_CREATED = "Data KTP berhasil ditambahkan";
    public static final String KTP_UPDATED = "Data KTP berhasil diperbarui";
    public static final String KTP_DELETED = "Data KTP berhasil dihapus";
    public static final String KTP_FETCHED = "Data KTP berhasil diambil";
    public static final String KTP_LIST_FETCHED = "Daftar KTP berhasil diambil";

    // Valid jenis kelamin values
    public static final String LAKI_LAKI = "Laki-laki";
    public static final String PEREMPUAN = "Perempuan";

    private KtpUtil() {
        // Utility class — prevent instantiation
    }

    /**
     * Validate jenis kelamin value.
     *
     * @param jenisKelamin the value to validate
     * @return true if valid
     */
    public static boolean isValidJenisKelamin(String jenisKelamin) {
        return LAKI_LAKI.equalsIgnoreCase(jenisKelamin) ||
                PEREMPUAN.equalsIgnoreCase(jenisKelamin);
    }

    /**
     * Validate NIK format (16 numeric digits).
     *
     * @param nomorKtp the NIK to validate
     * @return true if valid format
     */
    public static boolean isValidNomorKtp(String nomorKtp) {
        return nomorKtp != null && nomorKtp.matches("\\d{16}");
    }
}
