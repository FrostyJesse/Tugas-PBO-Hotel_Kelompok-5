# -*- coding: utf-8 -*-
"""
Script lengkap untuk menghasilkan Laporan_Tugas_Besar_PBO_Reservasi_Hotel.docx
Sesuai format outline tugas PBO.
"""

import docx
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

def create_report_docx(output_path):
    doc = Document()
    
    # Page Margins 1 inch
    for section in doc.sections:
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)
        
    def set_cell_shading(cell, color_hex):
        shading_elm = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{color_hex}"/>')
        cell._tc.get_or_add_tcPr().append(shading_elm)

    def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
        tcPr = cell._tc.get_or_add_tcPr()
        tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
        tcPr.append(tcMar)

    def add_code_block(code_text):
        tbl = doc.add_table(rows=1, cols=1)
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        cell = tbl.cell(0, 0)
        set_cell_shading(cell, "F4F6F9")
        set_cell_margins(cell, top=100, bottom=100, left=180, right=180)
        
        tcPr = cell._tc.get_or_add_tcPr()
        borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:top w:val="none"/><w:left w:val="single" w:sz="24" w:space="0" w:color="1F4E79"/><w:bottom w:val="none"/><w:right w:val="none"/></w:tcBorders>')
        tcPr.append(borders)
        
        p = cell.paragraphs[0]
        p.paragraph_format.space_before = Pt(2)
        p.paragraph_format.space_after = Pt(2)
        p.paragraph_format.line_spacing = 1.05
        run = p.add_run(code_text.strip())
        run.font.name = "Consolas"
        run.font.size = Pt(9.0)
        run.font.color.rgb = RGBColor(35, 45, 55)
        
        doc.add_paragraph().paragraph_format.space_after = Pt(4)

    def style_heading(text, level):
        h = doc.add_heading(text, level=level)
        h.paragraph_format.keep_with_next = True
        run = h.runs[0]
        run.font.name = "Segoe UI"
        if level == 1:
            run.font.size = Pt(15)
            run.font.bold = True
            run.font.color.rgb = RGBColor(31, 78, 120) # Navy
            h.paragraph_format.space_before = Pt(16)
            h.paragraph_format.space_after = Pt(6)
        elif level == 2:
            run.font.size = Pt(12.5)
            run.font.bold = True
            run.font.color.rgb = RGBColor(46, 117, 182) # Blue
            h.paragraph_format.space_before = Pt(12)
            h.paragraph_format.space_after = Pt(4)
        elif level == 3:
            run.font.size = Pt(11)
            run.font.bold = True
            run.font.color.rgb = RGBColor(50, 60, 70)
            h.paragraph_format.space_before = Pt(8)
            h.paragraph_format.space_after = Pt(2)
        return h

    def add_p(text, bold_prefix="", space_after=5):
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(0)
        p.paragraph_format.space_after = Pt(space_after)
        p.paragraph_format.line_spacing = 1.15
        if bold_prefix:
            r_pre = p.add_run(bold_prefix)
            r_pre.font.name = "Segoe UI"
            r_pre.font.size = Pt(10)
            r_pre.font.bold = True
            r_pre.font.color.rgb = RGBColor(30, 40, 50)
        r_body = p.add_run(text)
        r_body.font.name = "Segoe UI"
        r_body.font.size = Pt(10)
        r_body.font.color.rgb = RGBColor(50, 60, 70)
        return p

    def create_table(headers, data_rows):
        tbl = doc.add_table(rows=len(data_rows) + 1, cols=len(headers))
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        
        # Header Row
        hdr_cells = tbl.rows[0].cells
        for i, header_text in enumerate(headers):
            hdr_cells[i].text = header_text
            set_cell_shading(hdr_cells[i], "1F4E79")
            set_cell_margins(hdr_cells[i], top=100, bottom=100, left=140, right=140)
            p = hdr_cells[i].paragraphs[0]
            p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            for r in p.runs:
                r.font.name = "Segoe UI"
                r.font.size = Pt(9.5)
                r.font.bold = True
                r.font.color.rgb = RGBColor(255, 255, 255)
                
        # Data Rows
        for row_idx, row_data in enumerate(data_rows):
            row_cells = tbl.rows[row_idx + 1].cells
            bg_color = "F8FAFC" if row_idx % 2 == 1 else "FFFFFF"
            for col_idx, cell_value in enumerate(row_data):
                row_cells[col_idx].text = str(cell_value)
                set_cell_shading(row_cells[col_idx], bg_color)
                set_cell_margins(row_cells[col_idx], top=70, bottom=70, left=120, right=120)
                p = row_cells[col_idx].paragraphs[0]
                if col_idx == 0 or cell_value in ["VALID", "[x]", "Lunas", "Tersedia", "CHECKED_IN", "CHECKED_OUT"]:
                    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
                else:
                    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
                for r in p.runs:
                    r.font.name = "Segoe UI"
                    r.font.size = Pt(9)
                    r.font.color.rgb = RGBColor(40, 50, 60)
                    if cell_value == "VALID" or cell_value == "[x]":
                        r.font.bold = True
                        r.font.color.rgb = RGBColor(39, 174, 96)
                        
        for row in tbl.rows:
            for cell in row.cells:
                tcPr = cell._tc.get_or_add_tcPr()
                borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:top w:val="single" w:sz="4" w:space="0" w:color="D0D5DD"/><w:left w:val="none"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="D0D5DD"/><w:right w:val="none"/></w:tcBorders>')
                tcPr.append(borders)
                
        doc.add_paragraph().paragraph_format.space_after = Pt(6)
        return tbl

    # ==========================================
    # HALAMAN SAMPUL / COVER
    # ==========================================
    p_cover_pre = doc.add_paragraph()
    p_cover_pre.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_cover_pre.paragraph_format.space_before = Pt(40)
    p_cover_pre.paragraph_format.space_after = Pt(10)
    r = p_cover_pre.add_run("LAPORAN TUGAS BESAR")
    r.font.name = "Segoe UI"
    r.font.size = Pt(14)
    r.font.bold = True
    r.font.color.rgb = RGBColor(100, 110, 120)

    p_cover_title = doc.add_paragraph()
    p_cover_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_cover_title.paragraph_format.space_after = Pt(12)
    r = p_cover_title.add_run("SISTEM RESERVASI HOTEL\n(HOTEL RESERVATION SYSTEM)")
    r.font.name = "Segoe UI"
    r.font.size = Pt(22)
    r.font.bold = True
    r.font.color.rgb = RGBColor(31, 78, 120)

    p_cover_sub = doc.add_paragraph()
    p_cover_sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_cover_sub.paragraph_format.space_after = Pt(50)
    r = p_cover_sub.add_run("Aplikasi Desktop Manajemen Hotel Berbasis Java Swing dan MySQL\nMenerapkan 4 Pilar OOP dan Arsitektur Model-View-Controller (MVC)")
    r.font.name = "Segoe UI"
    r.font.size = Pt(11)
    r.font.italic = True
    r.font.color.rgb = RGBColor(70, 80, 90)

    # Box Anggota Kelompok
    p_team_title = doc.add_paragraph()
    p_team_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_team_title.paragraph_format.space_after = Pt(8)
    r = p_team_title.add_run("Disusun Oleh (Kelompok):")
    r.font.name = "Segoe UI"
    r.font.size = Pt(11)
    r.font.bold = True

    team_data = [
        ["1", "Jesse Timothy Halomoan Damanik", "21120124120041", "Database, Model OOP, & DAO Layer"],
        ["2", "Anggota Kelompok 2", "2112012413xxxx", "GUI Java Swing, Login, Tamu & Kamar"],
        ["3", "Anggota Kelompok 3", "2112012413xxxx", "Reservasi, Check-In/Out, & Pembayaran"]
    ]
    create_table(["No", "Nama Mahasiswa", "NIM", "Peran / Fokus Tanggung Jawab"], team_data)

    p_inst = doc.add_paragraph()
    p_inst.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_inst.paragraph_format.space_before = Pt(60)
    p_inst.paragraph_format.space_after = Pt(4)
    r = p_inst.add_run("MATA KULIAH PEMROGRAMAN BERORIENTASI OBJEK (PBO)\nPROGRAM STUDI TEKNIK KOMPUTER / INFORMATIKA\nFAKULTAS TEKNIK - UNIVERSITAS DIPONEGORO\n2026")
    r.font.name = "Segoe UI"
    r.font.size = Pt(11)
    r.font.bold = True
    r.font.color.rgb = RGBColor(31, 78, 120)

    doc.add_page_break()

    # ==========================================
    # 1. DESKRIPSI PROYEK DAN TUJUAN
    # ==========================================
    style_heading("1. Deskripsi Proyek dan Tujuan", 1)
    add_p(
        "Sistem Reservasi Hotel (Hotel Reservation System) adalah aplikasi desktop interaktif yang dibangun menggunakan bahasa pemrograman Java dan antarmuka Java Swing. Aplikasi ini dirancang khusus untuk memenuhi standar mata kuliah Pemrograman Berorientasi Objek (PBO) dengan menerapkan keempat pilar OOP secara substantif, memisahkan logika antarmuka dan bisnis melalui arsitektur Model-View-Controller (MVC), serta mengintegrasikan penyimpanan permanen menggunakan basis data MySQL via JDBC driver.",
        bold_prefix="Latar Belakang & Ringkasan: "
    )
    add_p(
        "Aplikasi digunakan oleh petugas resepsionis maupun admin hotel untuk mengelola data tamu, data kamar, transaksi pemesanan (reservasi), registrasi check-in, proses check-out beserta kalkulasi denda dan ringkasan tagihan, serta transaksi pelunasan pembayaran dengan metode tunai maupun non-tunai (QRIS, Transfer, Debit).",
        bold_prefix="Cakupan Operasional: "
    )
    add_p(
        "Tujuan utama proyek ini adalah: (1) Mengimplementasikan Enkapsulasi, Pewarisan, Abstraksi, dan Polimorfisme secara nyata; (2) Membangun struktur kode modular dengan pemisahan package (model, view, controller, dao, exception, util); (3) Mencegah kesalahan data menggunakan validasi komprehensif dan Custom Exception; serta (4) Menyediakan antarmuka pengguna desktop yang responsif, stabil, dan mudah digunakan.",
        bold_prefix="Tujuan Spesifik: "
    )

    # ==========================================
    # 2. PENERAPAN 4 PILAR OOP
    # ==========================================
    style_heading("2. Penerapan 4 Pilar Pemrograman Berorientasi Objek (OOP)", 1)
    
    style_heading("2.1 Encapsulation (Enkapsulasi)", 2)
    add_p("Seluruh kelas model (User, Tamu, Kamar, Reservasi, Pembayaran, Fasilitas, CheckIn, CheckOut) menerapkan prinsip data hiding dengan mendeklarasikan setiap atribut bertipe private. Akses data hanya diberikan melalui method getter dan setter yang dilengkapi logika validasi agar nilai objek selalu konsisten dan terbebas dari nilai tidak valid (seperti harga negatif, durasi 0 hari, atau string kosong).")
    
    style_heading("2.2 Inheritance (Pewarisan)", 2)
    add_p("Pewarisan diterapkan pada hierarki pengelolaan kamar hotel. Kamar berperan sebagai kelas induk abstrak (abstract superclass), sedangkan Standard, Deluxe, dan Suite berperan sebagai kelas turunan (subclasses). Seluruh kelas turunan memanfaatkan pemanggilan constructor superclass menggunakan super(...) untuk menginisialisasi atribut dasar seperti idKamar, nomorKamar, hargaDasar, status, dan kapasitas.")

    style_heading("2.3 Abstraction (Abstraksi)", 2)
    add_p("Abstraksi direalisasikan melalui dua mekanisme: (1) Abstract Class Kamar yang mendefinisikan abstract method public abstract double hitungTarif(int jumlahMalam); yang memaksa setiap kelas turunan menyediakan rumus perhitungan tarif sesuai spesifikasinya; serta (2) Interface Diskon yang mendefinisikan kontrak public double hitungDiskon(double total); yang diimplementasikan oleh kelas DiskonDurasi (diskon bertingkat berdasarkan lama menginap) dan DiskonMember (diskon pelanggan VIP/keanggotaan).")

    style_heading("2.4 Polymorphism (Polimorfisme)", 2)
    add_p("Polimorfisme diterapkan secara menyeluruh melalui dua teknik:")
    add_p("Subclass meng-override method hitungTarif(int jumlahMalam) dari kelas induk Kamar. Tipe Standard menghitung tarif murni (harga dasar x malam); tipe Deluxe menambahkan biaya sarapan buffet (+Rp 50.000/malam); sedangkan tipe Suite menambahkan fasilitas premium jacuzzi dan airport shuttle (+Rp 150.000/malam). Pemanggilan kamar.hitungTarif(malam) menghasilkan perhitungan dinamis sesuai objek riil yang diinstansiasi.", bold_prefix="a. Method Overriding: ")
    add_p("Method hitungTarif disediakan dalam dua bentuk parameter: (1) hitungTarif(int jumlahMalam) untuk tarif reguler, dan (2) hitungTarif(int jumlahMalam, double diskonPersen) untuk perhitungan tarif langsung dengan persentase potongan harga khusus.", bold_prefix="b. Method Overloading: ")

    # ==========================================
    # 3. ARSITEKTUR SISTEM (MVC & DAO)
    # ==========================================
    style_heading("3. Arsitektur Sistem (MVC dan DAO)", 1)
    add_p("Aplikasi dirancang dengan pemisahan tanggung jawab (Separation of Concerns) yang ketat melalui pola arsitektur Model-View-Controller (MVC) dan Data Access Object (DAO):")
    mvc_data = [
        ["Model", "Menyimpan data, aturan validasi objek, dan logika bisnis (Kamar, Tamu, Reservasi, Pembayaran, CheckIn, CheckOut). Bebas dari kode UI dan SQL."],
        ["View", "Menangani antarmuka grafis Java Swing (JTable, form input, dialog konfirmasi, menu bar). Tidak memuat kueri SQL maupun kalkulasi bisnis."],
        ["Controller", "Mengontrol alur aplikasi, menangani event listener tombol/tabel, memvalidasi input view, dan memanggil DAO untuk persistensi data."],
        ["DAO (Data Access Object)", "Mengisolasi seluruh kueri SQL (PreparedStatement, ResultSet, CRUD) ke database MySQL. Memetakan hasil kueri ke objek model polimorfik."],
        ["Util & Exception", "Menyediakan koneksi database tunggal (DatabaseConnection), styling GUI terpusat (UIUtils), dan penanganan galat kustom."]
    ]
    create_table(["Komponen Arsitektur", "Peran dan Tanggung Jawab"], mvc_data)

    # ==========================================
    # 4. PERANCANGAN BASIS DATA & CLASS DIAGRAM
    # ==========================================
    style_heading("4. Perancangan Basis Data dan Class Diagram", 1)
    
    style_heading("4.1 Struktur Tabel Basis Data (MySQL)", 2)
    add_p("Basis data hotel_reservation terdiri dari 6 tabel yang saling berelasi:")
    db_tables = [
        ["users", "id_user (PK), username, password, role", "Menyimpan data akun petugas resepsionis & admin."],
        ["tamu", "id_tamu (PK), nama, nomor_identitas, no_telepon, email, alamat", "Menyimpan data identitas pelanggan hotel."],
        ["kamar", "id_kamar (PK), nomor_kamar, tipe_kamar, harga, status, kapasitas", "Menyimpan inventaris kamar dan ketersediaan."],
        ["reservasi", "id_reservasi (PK), id_tamu (FK), id_kamar (FK), checkin, checkout, status, total", "Mencatat transaksi booking dan jadwal menginap."],
        ["pembayaran", "id_pembayaran (PK), id_reservasi (FK), tanggal, jumlah, metode, status", "Mencatat riwayat pembayaran, DP, dan pelunasan."],
        ["fasilitas", "id_fasilitas (PK), nama_fasilitas, biaya, tipe_kamar", "Menyimpan daftar fasilitas kamar hotel."]
    ]
    create_table(["Nama Tabel", "Struktur Kolom Utama", "Fungsi dan Keterangan"], db_tables)

    style_heading("4.2 Class Diagram dan Relasi Antar Objek", 2)
    add_p("Hubungan antar kelas dalam proyek ini meliputi:")
    add_p("Standard, Deluxe, dan Suite mewarisi kelas abstrak Kamar (Generalization/Inheritance).", bold_prefix="1. Inheritance: ")
    add_p("DiskonDurasi dan DiskonMember mengimplementasikan interface Diskon (Realization).", bold_prefix="2. Realization: ")
    add_p("Reservasi memiliki referensi ke objek Tamu dan Kamar. Pembayaran memiliki referensi ke Reservasi.", bold_prefix="3. Association: ")
    add_p("Kelas CheckIn dan CheckOut menerima objek Reservasi untuk memanipulasi status operasional kamar dan membuat tagihan.", bold_prefix="4. Dependency/Usage: ")

    # ==========================================
    # 5. IMPLEMENTASI KODE SUMBER
    # ==========================================
    style_heading("5. Implementasi Kode Sumber (Source Code Explanation)", 1)
    
    style_heading("5.1 Hierarki Kamar (Kamar.java, Standard.java, Deluxe.java, Suite.java)", 2)
    add_p("Kelas Kamar adalah kelas abstrak yang mendefinisikan atribut dasar kamar, abstract method hitungTarif, dan method overloading:")
    add_code_block("""// Kamar.java (Abstract Class)
public abstract class Kamar {
    private int idKamar;
    private String nomorKamar;
    private String tipeKamar;
    private double harga;
    private String status;
    private int kapasitas;

    public abstract double hitungTarif(int jumlahMalam);

    // Overloading method hitungTarif dengan persentase diskon
    public double hitungTarif(int jumlahMalam, double diskonPersen) {
        double total = hitungTarif(jumlahMalam);
        return total - (total * diskonPersen);
    }
}""")
    add_p("Kelas turunan Deluxe menambahkan biaya fasilitas sarapan dan meng-override hitungTarif:")
    add_code_block("""// Deluxe.java (Subclass)
public class Deluxe extends Kamar {
    private double biayaFasilitas = 50000.0;

    public Deluxe(int id, String no, double harga, String status, int kapasitas) {
        super(id, no, "Deluxe", harga, status, kapasitas);
    }

    @Override
    public double hitungTarif(int jumlahMalam) {
        return (getHarga() * jumlahMalam) + (biayaFasilitas * jumlahMalam);
    }
}""")

    style_heading("5.2 Interface Diskon & Implementasinya (Diskon.java, DiskonDurasi.java, DiskonMember.java)", 2)
    add_p("Interface Diskon mengatur standardisasi perhitungan potongan harga:")
    add_code_block("""// Diskon.java (Interface)
public interface Diskon {
    double hitungDiskon(double total);
    String getNamaDiskon();
    double getPersentase();
}""")
    add_p("DiskonDurasi mengimplementasikan aturan diskon durasi: 1-2 malam = 0%, 3-5 malam = 5%, >5 malam = 10%:")
    add_code_block("""// DiskonDurasi.java
public class DiskonDurasi implements Diskon {
    private int jumlahMalam;

    public DiskonDurasi(int jumlahMalam) { this.jumlahMalam = jumlahMalam; }

    @Override
    public double getPersentase() {
        if (jumlahMalam >= 6) return 0.10;
        if (jumlahMalam >= 3) return 0.05;
        return 0.0;
    }

    @Override
    public double hitungDiskon(double total) {
        return total * getPersentase();
    }
}""")

    style_heading("5.3 Model Tamu.java dan Fasilitas.java", 2)
    add_p("Model Tamu mengenkapsulasi data tamu seperti NIK, nomor telepon, dan email dengan validasi format:")
    add_code_block("""// Tamu.java
public void setEmail(String email) {
    if (email == null || !email.contains("@")) {
        throw new IllegalArgumentException("Format email tidak valid (harus mengandung @)");
    }
    this.email = email.trim();
}""")

    style_heading("5.4 Model Reservasi.java dan Pembayaran.java", 2)
    add_p("Model Reservasi mengintegrasikan hitungan jumlah malam dan kalkulasi polimorfik tarif kamar:")
    add_code_block("""// Reservasi.java
public int hitungJumlahMalam() {
    long days = ChronoUnit.DAYS.between(tanggalCheckin, tanggalCheckout);
    return days <= 0 ? 1 : (int) days;
}

public void kalkulasiTotal() {
    if (kamar != null) {
        int malam = hitungJumlahMalam();
        double subtotal = kamar.hitungTarif(malam);
        Diskon diskon = new DiskonDurasi(malam);
        this.totalHarga = subtotal - diskon.hitungDiskon(subtotal);
    }
}""")

    style_heading("5.5 Model CheckIn.java dan CheckOut.java", 2)
    add_p("Kelas CheckIn dan CheckOut memvalidasi siklus status kamar dan membuat ringkasan kwitansi akhir:")
    add_code_block("""// CheckIn.java
public void prosesCheckIn() throws ReservasiException {
    if ("CHECKED_IN".equalsIgnoreCase(reservasi.getStatusReservasi())) {
        throw new ReservasiException("Reservasi ini sudah dalam status CHECKED_IN!");
    }
    if ("Terisi".equalsIgnoreCase(reservasi.getKamar().getStatus())) {
        throw new ReservasiException("Kamar sedang terisi oleh tamu lain!");
    }
    reservasi.getKamar().setStatus("Terisi");
    reservasi.setStatusReservasi("CHECKED_IN");
}""")

    style_heading("5.6 Model Pengguna / User.java", 2)
    add_p("Model User menyimpan informasi kredensial login staf hotel dengan hak akses role (ADMIN atau RESEPSIONIS):")
    add_code_block("""// User.java
public class User {
    private int idUser;
    private String username;
    private String password;
    private String role; // ADMIN, RESEPSIONIS

    public User(int idUser, String username, String password, String role) {
        setIdUser(idUser);
        setUsername(username);
        setPassword(password);
        setRole(role);
    }
}""")

    style_heading("5.7 DatabaseConnection.java dan UIUtils.java", 2)
    add_p("DatabaseConnection mengelola koneksi JDBC MySQL berbasis Singleton pattern:")
    add_code_block("""// DatabaseConnection.java
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_reservation?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}""")

    style_heading("5.8 Custom Exception (ReservasiException.java, DataValidationException.java)", 2)
    add_p("Disediakan dua custom exception turunan dari java.lang.Exception untuk membedakan kesalahan operasional hotel dan validasi formulir:")
    add_code_block("""// ReservasiException.java
public class ReservasiException extends Exception {
    public ReservasiException(String message) { super(message); }
}

// DataValidationException.java
public class DataValidationException extends Exception {
    public DataValidationException(String message) { super(message); }
}""")

    style_heading("5.9 Lapisan DAO: UserDAO.java dan TamuDAO.java", 2)
    add_p("UserDAO menangani autentikasi login pengguna, sedangkan TamuDAO menangani CRUD lengkap data tamu:")
    add_code_block("""// UserDAO.java
public User login(String username, String password) throws SQLException {
    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
    try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
        stmt.setString(1, username);
        stmt.setString(2, password);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new User(rs.getInt("id_user"), rs.getString("username"), 
                                rs.getString("password"), rs.getString("role"));
            }
        }
    }
    return null;
}""")

    style_heading("5.10 KamarDAO.java, ReservasiDAO.java, PembayaranDAO.java", 2)
    add_p("KamarDAO menginstansiasi objek kamar secara polimorfik berdasarkan kolom tipe_kamar saat membaca database:")
    add_code_block("""// KamarDAO.java - Factory Polimorfik
public static Kamar mapResultSetToKamar(ResultSet rs) throws SQLException {
    String tipe = rs.getString("tipe_kamar");
    int id = rs.getInt("id_kamar");
    String no = rs.getString("nomor_kamar");
    double harga = rs.getDouble("harga");
    String status = rs.getString("status");
    int kapasitas = rs.getInt("kapasitas");

    if ("Deluxe".equalsIgnoreCase(tipe)) return new Deluxe(id, no, harga, status, kapasitas);
    if ("Suite".equalsIgnoreCase(tipe)) return new Suite(id, no, harga, status, kapasitas);
    return new Standard(id, no, harga, status, kapasitas);
}""")

    style_heading("5.11 Kelas Controller (MVC Flow)", 2)
    add_p("Kelas Controller mengisolasi penanganan aksi tombol GUI dari View. Contoh pada ReservasiController saat Check-In diproses:")
    add_code_block("""// ReservasiController.java
private void prosesCheckIn() {
    try {
        int id = Integer.parseInt(view.getIdReservasi());
        Reservasi r = reservasiDAO.getReservasiById(id);
        CheckIn checkIn = new CheckIn(r);
        checkIn.prosesCheckIn();

        reservasiDAO.updateStatusReservasi(r.getIdReservasi(), "CHECKED_IN");
        kamarDAO.updateStatusKamar(r.getKamar().getIdKamar(), "Terisi");

        JOptionPane.showMessageDialog(view, "Check-In Berhasil! Status kamar diubah menjadi Terisi.");
        loadAllData();
    } catch (ReservasiException ex) {
        JOptionPane.showMessageDialog(view, ex.getMessage(), "Operasional Ditolak", JOptionPane.WARNING_MESSAGE);
    }
}""")

    style_heading("5.12 Main.java", 2)
    add_p("Main.java adalah titik masuk (entry point) aplikasi yang mengatur tema Look and Feel dan memanggil form login pada Event Dispatch Thread (EDT):")
    add_code_block("""// Main.java
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            UserDAO userDAO = new UserDAO();
            new LoginController(loginView, userDAO);
            loginView.setVisible(true);
        });
    }
}""")

    # ==========================================
    # 6. CARA MENJALANKAN APLIKASI SERTA LINK GITHUB
    # ==========================================
    style_heading("6. Cara Menjalankan Aplikasi Serta Link ke Github", 1)
    
    style_heading("6.1 Link ke Github & Riwayat Commit", 2)
    add_p("Seluruh kode sumber proyek telah dikelola menggunakan version control Git dengan riwayat commit bertahap:")
    add_p("https://github.com/USERNAME-ANDA/hotel-reservation-system (dapat disesuaikan dengan remote repository kelompok)", bold_prefix="Tautan Repository: ")
    add_p("Berikut adalah log riwayat commit yang membuktikan pembagian kerja dan pengembangan bertahap:")
    add_code_block("""* d2bb7f8 - feat(transaksi): add reservation, check-in, check-out summary, payment receipt, and main dashboard integration
* 59ae67d - feat(view): build Swing GUI and MVC controllers for Login, Dashboard, Tamu, and Kamar CRUD
* 47fcae3 - feat(dao): implement JDBC Data Access Objects for User, Tamu, Kamar, Reservasi, and Pembayaran
* 3b80dbd - feat(model): add core OOP hierarchy, room polymorphism, discount strategy, and database schema""")

    style_heading("6.2 Ringkasan Pemenuhan Spesifikasi Tugas", 2)
    add_p("Tabel berikut merangkum pemenuhan seluruh 28 kriteria tugas proyek PBO:")
    checklist_data = [
        ["1", "Java Swing & Eclipse", "[x]", "Kompatibel penuh dengan Eclipse IDE (.project, .classpath)"],
        ["2", "Arsitektur MVC", "[x]", "Terpisah: model/, view/, controller/, dao/, util/, exception/"],
        ["3", "Minimal 8 Class Model/Logika", "[x]", "Terdapat 14 kelas model/logika (Kamar, Tamu, Reservasi, dll)"],
        ["4", "Encapsulation", "[x]", "Semua atribut private dengan setter validasi ketat"],
        ["5", "Inheritance", "[x]", "Hierarki Kamar -> Standard, Deluxe, Suite dengan super()"],
        ["6", "Abstraction", "[x]", "Abstract class Kamar dan Interface Diskon"],
        ["7", "Polymorphism (Overriding)", "[x]", "hitungTarif(malam) di-override pada setiap tipe kamar"],
        ["8", "Polymorphism (Overloading)", "[x]", "hitungTarif(malam) dan hitungTarif(malam, diskon)"],
        ["9", "Collection & Generics", "[x]", "List<Tamu>, List<Kamar>, List<Reservasi>, List<Pembayaran>"],
        ["10", "Custom Exception", "[x]", "ReservasiException dan DataValidationException"],
        ["11", "Database Permanen & JDBC", "[x]", "MySQL database hotel_reservation via DatabaseConnection"],
        ["12", "Minimal 3 Tampilan GUI", "[x]", "Tersedia 6 tampilan (Login, Dashboard, Tamu, Kamar, Reservasi, Bayar)"],
        ["13", "CRUD Lengkap Minimal 2 Entitas", "[x]", "CRUD lengkap pada Tamu, Kamar, Reservasi, dan Pembayaran"],
        ["14", "Variasi Komponen Swing", "[x]", "JTable, JTextField, JPasswordField, JComboBox, JRadioButton, JSpinner, JCheckBox"],
        ["15", "Search & Dialog Konfirmasi", "[x]", "Live search bar dan JOptionPane konfirmasi hapus"]
    ]
    create_table(["No", "Kriteria Persyaratan Proyek", "Status", "Keterangan Implementasi"], checklist_data)

    style_heading("6.3 Pengujian Fungsionalitas (Black Box Testing)", 2)
    add_p("Pengujian fungsionalitas dilakukan untuk memastikan seluruh alur sistem berjalan tanpa error:")
    testing_data = [
        ["1", "Autentikasi Login", "Input password salah", "Muncul pesan 'Username atau password salah'", "VALID"],
        ["2", "Autentikasi Login", "Input kredensial admin benar", "Berhasil login dan membuka Dashboard Utama", "VALID"],
        ["3", "CRUD Tamu", "Menambah data tamu baru", "Data tersimpan di database dan muncul di JTable", "VALID"],
        ["4", "Validasi Form", "Menyimpan email tanpa '@'", "Muncul dialog peringatan validasi email", "VALID"],
        ["5", "CRUD Kamar", "Update harga kamar", "Harga berhasil diperbarui di database dan tabel", "VALID"],
        ["6", "Kalkulasi Diskon", "Reservasi selama 5 malam", "Diskon hemat 5% otomatis terhitung memotong total", "VALID"],
        ["7", "Alur Check-In", "Check-in kamar yang tersedia", "Status reservasi CHECKED_IN & status kamar Terisi", "VALID"],
        ["8", "Alur Check-Out", "Check-out tamu aktif", "Status kamar kembali Tersedia & tampil dialog kwitansi", "VALID"],
        ["9", "Pembayaran", "Pilih metode QRIS dan bayar", "Status pembayaran Lunas & cetak kwitansi resmi", "VALID"]
    ]
    create_table(["No", "Modul Pengujian", "Skenario Uji", "Hasil yang Diharapkan", "Hasil Uji"], testing_data)

    doc.save(output_path)
    print(f"Document successfully created at: {output_path}")

if __name__ == "__main__":
    out_file = r"C:\Users\timot\Downloads\TUGAS PBO_RESERVASI HOTEL\Laporan_Tugas_Besar_PBO_Reservasi_Hotel.docx"
    create_report_docx(out_file)
