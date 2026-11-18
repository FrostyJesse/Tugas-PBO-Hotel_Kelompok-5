# LAPORAN TUGAS BESAR PEMROGRAMAN BERORIENTASI OBJEK (PBO)
## SISTEM RESERVASI HOTEL (HOTEL RESERVATION SYSTEM) BERBASIS JAVA SWING DAN MYSQL MENGGUNAKAN ARSITEKTUR MVC

---

> **Disusun Oleh (Kelompok X):**
> 1. **Nama Mahasiswa 1** (NIM: .....................) – *Peran: Database, Model OOP & DAO*
> 2. **Nama Mahasiswa 2** (NIM: .....................) – *Peran: GUI Java Swing, Master Data Tamu & Kamar*
> 3. **Nama Mahasiswa 3** (NIM: .....................) – *Peran: Transaksi Reservasi, Check-In/Out & Pembayaran*
>
> **Program Studi :** Teknik Informatika / Sistem Informasi  
> **Mata Kuliah   :** Pemrograman Berorientasi Objek (PBO)  
> **Dosen Pengampu:** ....................................................  
> **Tahun Akademik:** 2026 / 2027  

---

## KATA PENGANTAR

Puji syukur kami panjatkan ke hadirat Tuhan Yang Maha Esa atas rahmat dan karunia-Nya sehingga kami dapat menyelesaikan laporan Tugas Besar mata kuliah Pemrograman Berorientasi Objek (PBO) yang berjudul **"Sistem Reservasi Hotel (Hotel Reservation System) Berbasis Java Swing dan MySQL Menggunakan Arsitektur Model-View-Controller (MVC)"**.

Proyek ini bertujuan untuk mengimplementasikan prinsip-prinsip utama Pemrograman Berorientasi Objek (Encapsulation, Inheritance, Abstraction, dan Polymorphism) ke dalam aplikasi perangkat lunak desktop nyata yang terintegrasi dengan basis data relasional melalui JDBC.

Kami mengucapkan terima kasih kepada dosen pengampu, asisten laboratorium, serta rekan-rekan mahasiswa yang telah memberikan bimbingan dan dukungan dalam penyusunan tugas ini. Kami menyadari bahwa laporan ini masih memiliki keterbatasan. Oleh karena itu, kritik dan saran yang membangun sangat kami harapkan.

Kota, Tanggal Bulan Tahun  

*(Tim Penyusun)*

---

## DAFTAR ISI

- **KATA PENGANTAR**
- **DAFTAR ISI**
- **BAB I: PENDAHULUAN**
  - 1.1 Latar Belakang
  - 1.2 Rumusan Masalah
  - 1.3 Tujuan Proyek
  - 1.4 Batasan Masalah
- **BAB II: LANDASAN TEORI**
  - 2.1 Konsep Pemrograman Berorientasi Objek (4 Pilar OOP)
  - 2.2 Arsitektur Model-View-Controller (MVC)
  - 2.3 Desain Pola Data Access Object (DAO)
  - 2.4 Java Swing & Event-Driven Programming
  - 2.5 Java Database Connectivity (JDBC) & MySQL
- **BAB III: ANALISIS DAN PERANCANGAN SISTEM**
  - 3.1 Gambaran Umum Alur Sistem
  - 3.2 Perancangan Basis Data (Struktur Tabel)
  - 3.3 Class Diagram & Hubungan Antar Kelas
  - 3.4 Pembagian Kerja Kelompok
- **BAB IV: IMPLEMENTASI DAN PEMBAHASAN**
  - 4.1 Struktur Direktori & Package
  - 4.2 Pembuktian Penerapan 4 Pilar OOP
  - 4.3 Antarmuka Pengguna (GUI) & Alur Penggunaan
  - 4.4 Pengujian Fungsionalitas (*Black Box Testing*)
- **BAB V: PENUTUP**
  - 5.1 Kesimpulan
  - 5.2 Saran
- **DAFTAR PUSTAKA**
- **LAMPIRAN: REPOSITORY GIT & BUKTI COMMIT**

---

# BAB I: PENDAHULUAN

### 1.1 Latar Belakang
Industri perhotelan membutuhkan pengelolaan data operasional yang cepat, akurat, dan terstruktur. Pencatatan manual atau sistem yang tidak terintegrasi sering menimbulkan kendala seperti pemesanan ganda (*room double-booking*), kesulitan pelacakan status kamar (tersedia, terisi, atau maintenance), perhitungan tarif inap yang rentan keliru, serta lambatnya proses pembuatan kwitansi dan laporan pendapatan.

Mata kuliah Pemrograman Berorientasi Objek (PBO) menekankan bagaimana kode perangkat lunak dapat dimodelkan mendekati entitas dunia nyata melalui kelas, objek, pewarisan, enkapsulasi, abstraksi, dan polimorfisme. Melalui proyek ini, dibangun aplikasi desktop **Hotel Reservation System** menggunakan bahasa pemrograman **Java**, antarmuka grafis **Java Swing**, basis data **MySQL**, dan arsitektur **Model-View-Controller (MVC)** yang bersih dan terstandarisasi.

### 1.2 Rumusan Masalah
1. Bagaimana mengimplementasikan 4 pilar OOP (Encapsulation, Inheritance, Abstraction, dan Polymorphism) pada sistem reservasi hotel?
2. Bagaimana memisahkan logika data, antarmuka pengguna, dan alur aplikasi menggunakan arsitektur MVC dan DAO?
3. Bagaimana mengotomatisasi perhitungan tarif kamar hotel sesuai tipe kamar dan durasi menginap (diskon bertingkat)?
4. Bagaimana mengelola siklus operasional kamar mulai dari pemesanan, *check-in*, *check-out*, hingga transaksi pembayaran?

### 1.3 Tujuan Proyek
1. Memenuhi tugas mata kuliah Pemrograman Berorientasi Objek dengan standar rekayasa perangkat lunak yang baik.
2. Membangun sistem yang mampu menangani CRUD data tamu dan kamar hotel.
3. Mengimplementasikan hierarki kamar (`Kamar`, `Standard`, `Deluxe`, `Suite`) dan strategi diskon menggunakan konsep Polimorfisme dan Abstraksi.
4. Menyediakan antarmuka desktop modern yang intuitif untuk staf resepsionis hotel.

### 1.4 Batasan Masalah
1. Aplikasi berbasis desktop dan berjalan pada sistem operasi Windows menggunakan runtime Java (JDK).
2. Penyimpanan data permanen menggunakan basis data MySQL lokal (XAMPP).
3. Pengguna sistem adalah petugas resepsionis/admin hotel yang telah terautentikasi melalui login.

---

# BAB II: LANDASAN TEORI

### 2.1 Konsep Pemrograman Berorientasi Objek (4 Pilar OOP)
1. **Encapsulation (Enkapsulasi):** Teknik menyembunyikan data internal (*data hiding*) dengan mendeklarasikan atribut kelas bertipe `private` dan menyediakan akses melalui method getter dan setter yang dilengkapi validasi data.
2. **Inheritance (Pewarisan):** Mekanisme di mana suatu kelas (*subclass*) mewarisi atribut dan method dari kelas lain (*superclass*) menggunakan kata kunci `extends`.
3. **Abstraction (Abstraksi):** Menyembunyikan detail implementasi yang rumit dan hanya menampilkan fungsionalitas esensial kepada pengguna melalui `abstract class` dan `interface`.
4. **Polymorphism (Polimorfisme):** Kemampuan suatu objek atau method untuk memiliki banyak bentuk, dicapai melalui:
   - **Method Overriding:** Subclass mendefinisikan ulang method yang telah dideklarasikan pada superclass.
   - **Method Overloading:** Beberapa method dalam kelas yang sama memiliki nama yang identik namun berbeda parameter (jumlah atau tipe parameter).

### 2.2 Arsitektur Model-View-Controller (MVC)
Arsitektur MVC membagi tanggung jawab komponen aplikasi menjadi tiga bagian:
* **Model:** Merepresentasikan entitas data, aturan validasi, dan logika bisnis.
* **View:** Bertanggung jawab murni pada tampilan antarmuka grafis (GUI) tanpa memuat kueri SQL.
* **Controller:** Menghubungkan View dan Model/DAO serta menangani event aksi dari pengguna.

### 2.3 Desain Pola Data Access Object (DAO)
Pola DAO memisahkan logika persistensi basis data dari kode logika bisnis. Seluruh perintah SQL (`INSERT`, `SELECT`, `UPDATE`, `DELETE`) diisolasi di dalam kelas DAO sehingga memudahkan pemeliharaan kode.

### 2.4 Java Swing & Layout Manager
Java Swing adalah pustaka GUI bawaan Java. Aplikasi ini menggunakan kombinasi Layout Manager seperti `BorderLayout`, `CardLayout` (untuk pergantian tab menu), `GridBagLayout` (untuk form presisi), dan `FlowLayout` guna memastikan antarmuka tetap rapi saat ukuran jendela diubah (*responsive resizing*).

---

# BAB III: ANALISIS DAN PERANCANGAN SISTEM

### 3.1 Gambaran Umum Alur Sistem

```text
       [ Start Aplikasi ]
               ↓
        [ Halaman Login ] ── (Validasi akun dari tabel 'users')
               ↓ (Sukses)
      [ Dashboard Utama ]
         ├── [ Kelola Data Tamu ]  (CRUD + Pencarian Data Tamu)
         ├── [ Kelola Data Kamar ] (CRUD + Status Kamar)
         ├── [ Reservasi Kamar ]   (Pilih Tamu & Kamar, Hitung Diskon Otomatis)
         ├── [ Check-In ]          (Ubah status Reservasi → CHECKED_IN & Kamar → Terisi)
         ├── [ Check-Out ]         (Ubah status Reservasi → CHECKED_OUT & Kamar → Tersedia)
         └── [ Pembayaran ]        (Proses Lunas/DP, Cetak Kwitansi Resmi)
```

### 3.2 Perancangan Basis Data
Basis data yang digunakan bernama `hotel_reservation` yang terdiri dari 6 tabel:

1. **Tabel `users`**: Menyimpan kredensial akun staf hotel (`id_user`, `username`, `password`, `role`).
2. **Tabel `tamu`**: Menyimpan data pelanggan hotel (`id_tamu`, `nama`, `nomor_identitas`, `no_telepon`, `email`, `alamat`).
3. **Tabel `kamar`**: Menyimpan data kamar (`id_kamar`, `nomor_kamar`, `tipe_kamar`, `harga`, `status`, `kapasitas`).
4. **Tabel `reservasi`**: Mencatat transaksi booking kamar (`id_reservasi`, `id_tamu`, `id_kamar`, `tanggal_checkin`, `tanggal_checkout`, `jumlah_tamu`, `status_reservasi`, `total_harga`).
5. **Tabel `pembayaran`**: Mencatat pelunasan biaya (`id_pembayaran`, `id_reservasi`, `tanggal_pembayaran`, `jumlah_bayar`, `metode_pembayaran`, `status_pembayaran`).
6. **Tabel `fasilitas`**: Menyimpan fasilitas penunjang per tipe kamar (`id_fasilitas`, `nama_fasilitas`, `biaya`, `tipe_kamar`).

### 3.3 Class Diagram
Class diagram menunjukkan relasi:
- `Kamar` (Abstract Superclass) diwarisi oleh `Standard`, `Deluxe`, dan `Suite`.
- `Diskon` (Interface) diimplementasikan oleh `DiskonDurasi` dan `DiskonMember`.
- `Reservasi` berasosiasi dengan `Tamu`, `Kamar`, `CheckIn`, `CheckOut`, dan `Pembayaran`.

### 3.4 Pembagian Peran Kelompok
| No | Anggota | Fokus Tanggung Jawab | Output Kode |
| :---: | :--- | :--- | :--- |
| 1 | **Mahasiswa 1** | Basis data, Koneksi JDBC, Desain OOP Hierarki Kamar, Interface Diskon, dan DAO Layer | `schema.sql`, `DatabaseConnection.java`, `Kamar.java`, `Standard.java`, `Deluxe.java`, `Suite.java`, `Diskon.java`, `*DAO.java` |
| 2 | **Mahasiswa 2** | Antarmuka Grafis (Swing), Styling Tema, Modul Login, Dashboard Statistik, CRUD Tamu & Kamar | `UIUtils.java`, `LoginView.java`, `DashboardView.java`, `TamuView.java`, `KamarView.java`, Controller terkait |
| 3 | **Mahasiswa 3** | Transaksi Reservasi, Integrasi Diskon, Logika Operasional Check-In/Check-Out, dan Modul Pembayaran | `Reservasi.java`, `CheckIn.java`, `CheckOut.java`, `Pembayaran.java`, `ReservasiView.java`, `PembayaranView.java`, Controller terkait |

---

# BAB IV: IMPLEMENTASI DAN PEMBAHASAN

### 4.1 Struktur Direktori & Package

```text
src/
├── model/       # Entitas logika & domain OOP
├── view/        # Seluruh antarmuka Java Swing (JTable, Form, Dialog)
├── controller/  # Penghubung event handling MVC
├── dao/         # Query kueri CRUD ke MySQL
├── exception/   # Custom Exception penanganan error
├── util/        # Koneksi database & utility UI
└── Main.java    # Entry point aplikasi
```

### 4.2 Pembuktian Penerapan 4 Pilar OOP

#### A. Encapsulation (Enkapsulasi)
Semua atribut pada kelas model dideklarasikan bertipe `private`, serta setter memiliki validasi ketat.
*Contoh cuplikan kode pada `Kamar.java`:*
```java
private double harga;

public void setHarga(double harga) {
    if (harga < 0) {
        throw new IllegalArgumentException("Harga tidak boleh negatif");
    }
    this.harga = harga;
}
```

#### B. Inheritance (Pewarisan)
Kelas `Standard`, `Deluxe`, dan `Suite` mewarisi kelas `Kamar` dan memanggil konstruktor induk melalui `super()`.
*Contoh cuplikan kode pada `Deluxe.java`:*
```java
public class Deluxe extends Kamar {
    private double biayaFasilitas = 50000.0;

    public Deluxe(int idKamar, String nomorKamar, double harga, String status, int kapasitas) {
        super(idKamar, nomorKamar, "Deluxe", harga, status, kapasitas);
    }
}
```

#### C. Abstraction (Abstraksi)
Diterapkan melalui kelas abstrak `Kamar` dan interface `Diskon` yang diimplementasikan oleh beberapa kelas.
*Contoh cuplikan kode pada `Diskon.java`:*
```java
public interface Diskon {
    double hitungDiskon(double total);
    String getNamaDiskon();
    double getPersentase();
}
```

#### D. Polymorphism (Polimorfisme)
* **Method Overriding**: Method `hitungTarif(int jumlahMalam)` dihitung secara berbeda pada setiap tipe kamar:
  - Standard: $\text{Harga dasar} \times \text{Malam}$
  - Deluxe: $(\text{Harga dasar} + \text{Biaya fasilitas}) \times \text{Malam}$
  - Suite: $(\text{Harga dasar} + \text{Biaya fasilitas premium}) \times \text{Malam}$
* **Method Overloading**: Tersedia dua variasi method `hitungTarif`:
  ```java
  public abstract double hitungTarif(int jumlahMalam);
  public double hitungTarif(int jumlahMalam, double diskonPersen);
  ```

### 4.3 Antarmuka Pengguna (GUI)
Aplikasi memiliki 6 tampilan grafis utama:
1. **Login View**: Mengotentikasi pengguna menggunakan `JPasswordField` dan `JCheckBox`.
2. **Dashboard View**: Menampilkan 6 kartu statistik real-time dari database.
3. **Data Tamu View**: Form input lengkap dengan tabel `JTable`, live search, dan konfirmasi hapus.
4. **Data Kamar View**: Pengelolaan data kamar beserta status dan tipe kamar.
5. **Reservasi View**: Dilengkapi `JSpinner` tanggal (format `yyyy-MM-dd`), panel ringkasan otomatis tarif polimorfik, tombol *Check-In*, dan tombol *Check-Out*.
6. **Pembayaran View**: Dilengkapi `JRadioButton` metode bayar (`Cash`, `Transfer`, `Debit`, `QRIS`) dan fitur Cetak Kwitansi Resmi.

### 4.4 Pengujian Fungsionalitas (*Black Box Testing*)

| No | Komponen yang Diuji | Skenario Uji | Hasil yang Diharapkan | Hasil Pengujian |
| :---: | :--- | :--- | :--- | :---: |
| 1 | **Login Sistem** | Memasukkan username & password salah | Muncul dialog error "Username atau password salah" | **VALID** |
| 2 | **Login Sistem** | Memasukkan kredensial admin yang benar | Berhasil masuk ke jendela Dashboard utama | **VALID** |
| 3 | **Validasi Data Tamu** | Menyimpan tamu dengan nomor telepon kosong | Muncul dialog peringatan "Nomor telepon tidak boleh kosong!" | **VALID** |
| 4 | **CRUD Kamar** | Menambah kamar baru tipe Suite | Kamar tersimpan di MySQL dan langsung muncul di `JTable` | **VALID** |
| 5 | **Perhitungan Tarif** | Memilih menginap 4 malam | Diskon 5% otomatis terhitung dan memotong total tagihan | **VALID** |
| 6 | **Operasional Check-In** | Klik Check-In pada reservasi aktif | Status reservasi menjadi `CHECKED_IN`, status kamar menjadi `Terisi` | **VALID** |
| 7 | **Operasional Check-Out** | Klik Check-Out pada tamu | Status reservasi menjadi `CHECKED_OUT`, kamar kembali `Tersedia`, dan muncul kwitansi | **VALID** |
| 8 | **Pencarian Data** | Mengetikkan kata kunci pada search bar | Tabel memfilter baris yang sesuai secara seketika | **VALID** |

---

# BAB V: PENUTUP

### 5.1 Kesimpulan
1. Sistem Reservasi Hotel telah berhasil dibangun menggunakan bahasa pemrograman Java dengan menerapkan 4 pilar OOP (Encapsulation, Inheritance, Abstraction, Polymorphism) secara komprehensif.
2. Pola arsitektur MVC dan DAO berhasil diterapkan untuk memisahkan data, antarmuka grafis Swing, dan logika transaksi basis data MySQL secara modular dan terstruktur.
3. Fitur operasional lengkap seperti manajemen tamu, kamar, pemesanan, *check-in*, *check-out*, hingga kwitansi pembayaran telah berjalan dengan baik dan tervalidasi melalui pengujian *black-box*.

### 5.2 Saran
1. Sistem dapat dikembangkan lebih lanjut dengan menambahkan fitur ekspor laporan berkala ke format PDF atau Excel.
2. Penambahan integrasi payment gateway otomatis untuk metode pembayaran digital.

---

## DAFTAR PUSTAKA

1. Deitel, P., & Deitel, H. (2017). *Java How to Program, Early Objects (11th Edition)*. Pearson.
2. Horstmann, C. S. (2018). *Core Java Volume I--Fundamentals (11th Edition)*. Prentice Hall.
3. Oracle Corporation. (2024). *Java Platform, Standard Edition Documentation & Swing API*. Oracle.
4. Schildt, H. (2018). *Java: The Complete Reference (11th Edition)*. McGraw-Hill Education.

---

## LAMPIRAN: RIWAYAT COMMIT GIT

Bukti pengerjaan bertahap pada repository Git:
```text
* d2bb7f8 - feat(transaksi): add reservation, check-in, check-out summary, payment receipt, and main dashboard integration
* 59ae67d - feat(view): build Swing GUI and MVC controllers for Login, Dashboard, Tamu, and Kamar CRUD
* 47fcae3 - feat(dao): implement JDBC Data Access Objects for User, Tamu, Kamar, Reservasi, and Pembayaran
* 3b80dbd - feat(model): add core OOP hierarchy, room polymorphism, discount strategy, and database schema
```
