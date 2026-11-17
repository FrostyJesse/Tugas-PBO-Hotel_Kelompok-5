-- ==========================================================
-- SISTEM RESERVASI HOTEL (HOTEL RESERVATION SYSTEM)
-- Script Database MySQL & Data Awal
-- ==========================================================

CREATE DATABASE IF NOT EXISTS hotel_reservation;
USE hotel_reservation;

-- 1. Tabel users
DROP TABLE IF EXISTS pembayaran;
DROP TABLE IF EXISTS reservasi;
DROP TABLE IF EXISTS kamar;
DROP TABLE IF EXISTS tamu;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS fasilitas;

CREATE TABLE users (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'RESEPSIONIS'
) ENGINE=InnoDB;

-- 2. Tabel tamu
CREATE TABLE tamu (
    id_tamu INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    nomor_identitas VARCHAR(50) NOT NULL UNIQUE,
    no_telepon VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    alamat TEXT
) ENGINE=InnoDB;

-- 3. Tabel kamar
CREATE TABLE kamar (
    id_kamar INT AUTO_INCREMENT PRIMARY KEY,
    nomor_kamar VARCHAR(20) NOT NULL UNIQUE,
    tipe_kamar VARCHAR(30) NOT NULL,
    harga DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Tersedia',
    kapasitas INT NOT NULL DEFAULT 2
) ENGINE=InnoDB;

-- 4. Tabel reservasi
CREATE TABLE reservasi (
    id_reservasi INT AUTO_INCREMENT PRIMARY KEY,
    id_tamu INT NOT NULL,
    id_kamar INT NOT NULL,
    tanggal_checkin DATE NOT NULL,
    tanggal_checkout DATE NOT NULL,
    jumlah_tamu INT NOT NULL,
    status_reservasi VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_harga DOUBLE NOT NULL,
    CONSTRAINT fk_reservasi_tamu FOREIGN KEY (id_tamu) REFERENCES tamu(id_tamu) ON DELETE CASCADE,
    CONSTRAINT fk_reservasi_kamar FOREIGN KEY (id_kamar) REFERENCES kamar(id_kamar) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 5. Tabel pembayaran
CREATE TABLE pembayaran (
    id_pembayaran INT AUTO_INCREMENT PRIMARY KEY,
    id_reservasi INT NOT NULL,
    tanggal_pembayaran DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    jumlah_bayar DOUBLE NOT NULL,
    metode_pembayaran VARCHAR(30) NOT NULL,
    status_pembayaran VARCHAR(20) NOT NULL,
    CONSTRAINT fk_pembayaran_reservasi FOREIGN KEY (id_reservasi) REFERENCES reservasi(id_reservasi) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6. Tabel fasilitas (opsional pendukung tipe kamar)
CREATE TABLE fasilitas (
    id_fasilitas INT AUTO_INCREMENT PRIMARY KEY,
    nama_fasilitas VARCHAR(100) NOT NULL,
    biaya DOUBLE NOT NULL,
    tipe_kamar VARCHAR(30) NOT NULL
) ENGINE=InnoDB;

-- ==========================================================
-- SEED DATA AWAL (DEMO & TESTING)
-- ==========================================================

-- Data Akun Petugas
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'ADMIN'),
('resepsionis', 'hotel123', 'RESEPSIONIS');

-- Data Tamu Awal
INSERT INTO tamu (nama, nomor_identitas, no_telepon, email, alamat) VALUES 
('Budi Santoso', '3271012304900001', '081234567890', 'budi@gmail.com', 'Jl. Sudirman No. 45, Jakarta'),
('Siti Aminah', '3271012304900002', '082198765432', 'siti@yahoo.com', 'Jl. Diponegoro No. 12, Bandung'),
('Dewi Lestari', '3271012304900003', '081377889900', 'dewi@gmail.com', 'Jl. Malioboro No. 8, Yogyakarta');

-- Data Kamar Awal (Standard, Deluxe, Suite)
INSERT INTO kamar (nomor_kamar, tipe_kamar, harga, status, kapasitas) VALUES 
('101', 'Standard', 350000, 'Tersedia', 2),
('102', 'Standard', 350000, 'Tersedia', 2),
('201', 'Deluxe', 550000, 'Tersedia', 3),
('202', 'Deluxe', 550000, 'Tersedia', 3),
('301', 'Suite', 950000, 'Tersedia', 4),
('302', 'Suite', 950000, 'Tersedia', 4);

-- Data Fasilitas
INSERT INTO fasilitas (nama_fasilitas, biaya, tipe_kamar) VALUES 
('Wi-Fi High Speed & AC', 0, 'Standard'),
('Breakfast Buffet & Mini Bar', 50000, 'Deluxe'),
('Private Jacuzzi, Welcome Drink, & Airport Shuttle', 150000, 'Suite');

-- Data Reservasi Contoh
INSERT INTO reservasi (id_tamu, id_kamar, tanggal_checkin, tanggal_checkout, jumlah_tamu, status_reservasi, total_harga) VALUES 
(1, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 2, 'PENDING', 700000);

-- Data Pembayaran Contoh
INSERT INTO pembayaran (id_reservasi, tanggal_pembayaran, jumlah_bayar, metode_pembayaran, status_pembayaran) VALUES 
(1, NOW(), 700000, 'Transfer', 'Lunas');
