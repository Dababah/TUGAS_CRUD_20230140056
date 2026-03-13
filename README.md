# Tugas CRUD KTP — Spring Boot + MySQL + jQuery

## 📌 Informasi Tugas
| Info | Detail |
|------|--------|
| **Mata Kuliah** | Deployment Perangkat Lunak |
| **Tugas** | CRUD KTP |
| **Stack** | Spring Boot 3.2 · MySQL · HTML/CSS/JS · jQuery Ajax |

---

## 📁 Struktur Proyek

```
TUGAS KTP/
├── ktp-backend/               # Spring Boot REST API
│   ├── src/main/java/com/ktp/
│   │   ├── KtpBackendApplication.java   # Entry point
│   │   ├── controller/
│   │   │   └── KtpController.java       # REST Controller
│   │   ├── service/
│   │   │   └── KtpService.java          # Service interface
│   │   ├── service/impl/
│   │   │   └── KtpServiceImpl.java      # Service implementation
│   │   ├── repository/
│   │   │   └── KtpRepository.java       # Spring Data JPA
│   │   ├── entity/
│   │   │   └── Ktp.java                 # JPA Entity
│   │   ├── dto/
│   │   │   ├── KtpRequestDto.java       # Request DTO
│   │   │   ├── KtpResponseDto.java      # Response DTO
│   │   │   └── ApiResponseDto.java      # Generic API wrapper
│   │   ├── mapper/
│   │   │   └── KtpMapper.java           # Entity ↔ DTO mapper
│   │   └── util/
│   │       └── KtpUtil.java             # Constants & helpers
│   ├── src/main/resources/
│   │   └── application.properties       # DB config
│   ├── database-setup.sql               # MySQL setup script
│   └── pom.xml                          # Maven dependencies
│
├── ktp-frontend/              # Client-side web app
│   ├── index.html             # Main HTML page
│   ├── style.css              # Premium dark theme CSS
│   └── app.js                 # jQuery Ajax logic
│
└── README.md
```

---

## 🗄️ Database Schema

**Database:** `spring`  
**Tabel:** `ktp`

| Kolom | Tipe | Keterangan |
|-------|------|-----------|
| `id` | INT, PK, AUTO_INCREMENT | Primary Key |
| `nomorKtp` | VARCHAR(16), UNIQUE | NIK 16 digit |
| `namaLengkap` | VARCHAR(255) | Nama sesuai KTP |
| `alamat` | VARCHAR(500) | Alamat tempat tinggal |
| `tanggalLahir` | DATE | Format YYYY-MM-DD |
| `jenisKelamin` | VARCHAR(20) | `Laki-laki` atau `Perempuan` |

---

## ⚙️ Setup & Menjalankan

### 1. Persiapan Database MySQL

```sql
-- Buka MySQL dan jalankan:
SOURCE ktp-backend/database-setup.sql;
```

Atau secara manual:
```sql
CREATE DATABASE IF NOT EXISTS spring CHARACTER SET utf8mb4;
```

### 2. Konfigurasi Koneksi Database

Edit `ktp-backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

### 3. Menjalankan Spring Boot Backend

```bash
cd ktp-backend
mvn spring-boot:run
```

> ✅ Server berjalan di: `http://localhost:8080`

### 4. Membuka Frontend

Buka file `ktp-frontend/index.html` langsung di browser — **tidak perlu server tambahan**.

> ⚠️ Pastikan Spring Boot sudah berjalan sebelum membuka frontend.

---

## 🔌 REST API Endpoints

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| `POST` | `/ktp` | Tambah KTP baru |
| `GET` | `/ktp` | Ambil semua KTP |
| `GET` | `/ktp/{id}` | Ambil KTP by ID |
| `PUT` | `/ktp/{id}` | Update KTP by ID |
| `DELETE` | `/ktp/{id}` | Hapus KTP by ID |

### Contoh Request Body (POST / PUT)

```json
{
  "nomorKtp": "3275011234560001",
  "namaLengkap": "Budi Santoso",
  "alamat": "Jl. Merdeka No. 1, Jakarta Selatan",
  "tanggalLahir": "1990-05-15",
  "jenisKelamin": "Laki-laki"
}
```

### Contoh Response (Success)

```json
{
  "success": true,
  "message": "Data KTP berhasil ditambahkan",
  "data": {
    "id": 1,
    "nomorKtp": "3275011234560001",
    "namaLengkap": "Budi Santoso",
    "alamat": "Jl. Merdeka No. 1, Jakarta Selatan",
    "tanggalLahir": "1990-05-15",
    "jenisKelamin": "Laki-laki"
  }
}
```

### Contoh Response (Error)

```json
{
  "success": false,
  "message": "Nomor KTP '3275011234560001' sudah terdaftar",
  "data": null
}
```

---

## 🎯 Fitur Validasi & Error Handling

### Backend (Spring Boot)
- ✅ Validasi `@NotBlank`, `@Size`, `@NotNull` via Jakarta Validation
- ✅ NIK harus tepat 16 digit angka
- ✅ NIK bersifat `UNIQUE` — tidak bisa duplikat
- ✅ Jenis kelamin hanya `Laki-laki` atau `Perempuan`
- ✅ Data tidak ditemukan → HTTP 404 dengan pesan jelas
- ✅ Duplikasi NIK → HTTP 400 dengan pesan jelas
- ✅ Global exception handler
- ✅ CORS enabled (`@CrossOrigin(origins = "*")`)

### Frontend (jQuery)
- ✅ Validasi NIK real-time (hanya digit, max 16)
- ✅ Validasi semua field sebelum submit
- ✅ Notifikasi toast (sukses/error/warning/info)
- ✅ Modal konfirmasi sebelum hapus
- ✅ Pencarian real-time (nama / NIK / alamat)
- ✅ Statistik: total, laki-laki, perempuan
- ✅ Loading indicator saat request sedang berlangsung
- ✅ Semua operasi CRUD tanpa refresh halaman (Ajax)

---

## 🏗️ Arsitektur & Package

```
com.ktp
├── KtpBackendApplication     # Main class (@SpringBootApplication)
├── controller/               # HTTP layer — menerima & merespons request
├── service/                  # Interface business logic
├── service/impl/             # Implementasi service
├── repository/               # Spring Data JPA repos
├── entity/                   # JPA Entities (mapped ke tabel DB)
├── dto/                      # Data Transfer Objects
├── mapper/                   # Entity ↔ DTO conversion
└── util/                     # Constants & helper methods
```

---

## 🔧 Teknologi yang Digunakan

### Backend
- **Java 17** / Spring Boot 3.2.3
- **Spring Web** (REST Controller)
- **Spring Data JPA** (Repository layer)
- **Spring Validation** (Input validation)
- **MySQL Connector/J** (JDBC driver)
- **Lombok** (Boilerplate reduction)

### Frontend
- **HTML5** (Semantic markup)
- **Vanilla CSS** (Premium dark theme, animations)
- **jQuery 3.7.1** (DOM manipulation)
- **jQuery Ajax** (Async HTTP requests)
- **Google Fonts** (Inter typeface)

---

## 📸 Tampilan Website

> Screenshot dapat ditambahkan di sini setelah aplikasi dijalankan.

---

## 👨‍💻 Informasi Pengembang

| | |
|-|-|
| **Nama** | *(Nama Mahasiswa)* |
| **NIM** | *(NIM Mahasiswa)* |
| **Mata Kuliah** | Deployment Perangkat Lunak |
| **Semester** | 6 |
