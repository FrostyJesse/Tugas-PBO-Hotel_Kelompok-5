# HOTEL RESERVATION SYSTEM (SISTEM RESERVASI HOTEL)
**Proyek Mata Kuliah Pemrograman Berorientasi Objek (PBO)**

Aplikasi desktop manajemen hotel profesional berbasis **Java Swing**, **MySQL (JDBC)**, dan arsitektur **MVC (Model-View-Controller)**.

---

## 📌 1. Struktur Direktori Proyek

```text
TUGAS PBO_RESERVASI HOTEL/
├── .classpath                          # Konfigurasi Build Path Eclipse
├── .project                            # Metadata Proyek Eclipse
├── database/
│   └── schema.sql                      # Script DDL MySQL & Data Awal
├── lib/
│   └── mysql-connector-j-8.3.0.jar     # Driver JDBC MySQL
├── bin/                                # Hasil kompilasi (.class)
└── src/
    ├── Main.java                       # Titik masuk utama aplikasi (Entry Point)
    ├── model/                          # Domain Model & Aturan Bisnis (OOP)
    │   ├── Diskon.java                 # Interface Diskon (Pilar Abstraction)
    │   ├── DiskonDurasi.java           # Strategi Diskon Durasi Menginap (1-2: 0%, 3-5: 5%, >5: 10%)
    │   ├── DiskonMember.java           # Strategi Diskon Pelanggan Loyal (VIP)
    │   ├── Kamar.java                  # Abstract Class Kamar (Pilar Abstraction & Encapsulation)
    │   ├── Standard.java               # Subclass Standard (Pilar Inheritance & Polymorphism)
    │   ├── Deluxe.java                 # Subclass Deluxe (+ Fasilitas Sarapan Buffet)
    │   ├── Suite.java                  # Subclass Suite (+ Fasilitas Jacuzzi & VIP Lounge)
    │   ├── User.java                   # Model Akun Petugas Hotel
    │   ├── Tamu.java                   # Model Tamu & Validasi Enkapsulasi
    │   ├── Fasilitas.java              # Model Fasilitas Kamar
    │   ├── Reservasi.java              # Model Transaksi Pemesanan & Relasi Asosiasi
    │   ├── Pembayaran.java             # Model Transaksi Pelunasan Tagihan
    │   ├── CheckIn.java                # Logika Operasional Registrasi Masuk Kamar
    │   └── CheckOut.java               # Logika Operasional Kepulangan & Ringkasan Tagihan
    ├── dao/                            # Data Access Object (Kueri CRUD JDBC)
    │   ├── UserDAO.java
    │   ├── TamuDAO.java
    │   ├── KamarDAO.java
    │   ├── ReservasiDAO.java
    │   └── PembayaranDAO.java
    ├── view/                           # Antarmuka Pengguna (Java Swing GUI)
    │   ├── LoginView.java              # Form Autentikasi Petugas
    │   ├── MainDashboardView.java      # Frame Utama: JMenuBar, Sidebar, CardLayout
    │   ├── DashboardView.java          # Panel Kartu Statistik & Metrik Operasional
    │   ├── TamuView.java               # Panel Form & JTable CRUD Tamu
    │   ├── KamarView.java              # Panel Form & JTable CRUD Kamar
    │   ├── ReservasiView.java          # Panel Form Reservasi, Date Spinner, Check-In/Out
    │   └── PembayaranView.java         # Panel Form Transaksi Pembayaran & Cetak Kwitansi
    ├── controller/                     # Pengatur Alur Logika & Event Handling
    │   ├── LoginController.java
    │   ├── DashboardController.java
    │   ├── TamuController.java
    │   ├── KamarController.java
    │   ├── ReservasiController.java
    │   └── PembayaranController.java
    ├── exception/                      # Custom Exceptions
    │   ├── ReservasiException.java     # Error operasional reservasi, clash, & kamar
    │   └── DataValidationException.java# Error validasi input pengguna
    └── util/                           # Utilitas Pendukung
        ├── DatabaseConnection.java     # Singleton Koneksi MySQL JDBC
        ├── UIUtils.java                # Styling Warna, Font, Tombol & JTable
        └── TestKoneksi.java            # Tool CLI untuk Menguji Koneksi Database
```

---

## 🚀 2. Cara Menjalankan di Eclipse

1. **Jalankan MySQL**:
   - Buka **XAMPP Control Panel**, lalu klik tombol **Start** pada modul **MySQL** (port default 3306).
2. **Import Database**:
   - Buka browser ke `http://localhost/phpmyadmin`.
   - Buat database baru bernama `hotel_reservation` (atau langsung klik tab **Import** lalu pilih file `database/schema.sql`).
3. **Buka di Eclipse**:
   - Buka Eclipse IDE.
   - Pilih menu **File** -> **Open Projects from File System...**
   - Klik **Directory...** lalu pilih folder ini: `c:\Users\timot\Downloads\TUGAS PBO_RESERVASI HOTEL`.
   - Klik **Finish**.
4. **Verifikasi Library MySQL Connector**:
   - Klik kanan project di Eclipse -> **Properties** -> **Java Build Path** -> tab **Libraries**.
   - Pastikan file `lib/mysql-connector-j-8.3.0.jar` sudah terdaftar. (Jika belum, klik *Add JARs...* dan pilih file tersebut).
5. **Jalankan Aplikasi**:
   - Buka file `src/Main.java`.
   - Klik kanan -> **Run As** -> **Java Application**.

---

## 🔑 3. Akun Login Bawaan (Seed Data)

| Username | Password | Role |
| :--- | :--- | :--- |
| **admin** | `admin123` | ADMIN |
| **resepsionis** | `hotel123` | RESEPSIONIS |

---

## 💡 4. Pemenuhan 4 Pilar OOP

1. **Encapsulation**:
   - Semua atribut kelas model bertipe `private`.
   - Getter & setter memiliki validasi ketat (contoh: harga tidak boleh negatif, email harus valid, kapasitas minimal 1).
2. **Inheritance**:
   - `Kamar` adalah parent abstract class.
   - `Standard`, `Deluxe`, dan `Suite` adalah child class yang memanggil constructor parent menggunakan `super(...)`.
3. **Abstraction**:
   - `public abstract class Kamar` mendefinisikan method abstract `hitungTarif(int jumlahMalam)`.
   - `public interface Diskon` mendefinisikan kontrak perhitungan diskon yang diimplementasikan oleh `DiskonDurasi` dan `DiskonMember`.
4. **Polymorphism**:
   - **Method Overriding**: `hitungTarif(int jumlahMalam)` diimplementasikan secara unik pada setiap kelas turunan (`Standard`, `Deluxe`, `Suite`).
   - **Method Overloading**: `hitungTarif(int jumlahMalam)` dan `hitungTarif(int jumlahMalam, double diskonPersen)`.
