DROP DATABASE IF EXISTS cinema_db;
CREATE DATABASE cinema_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE cinema_db;


-- 1. PHIM

CREATE TABLE phim (
    ma_phim INT AUTO_INCREMENT PRIMARY KEY,
    ten_phim VARCHAR(255) NOT NULL,
    thoi_luong INT NOT NULL CHECK (thoi_luong > 0),
    ngay_phat_hanh DATE,
    ngon_ngu VARCHAR(100),
    trang_thai TINYINT NOT NULL,
    gioi_han_tuoi TINYINT NOT NULL,
    url_anh VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_phim_trang_thai
        CHECK (trang_thai IN (0, 1, 2)),
    CONSTRAINT chk_phim_gioi_han_tuoi
        CHECK (gioi_han_tuoi IN (0, 1, 2, 3, 4, 5))
) ENGINE=InnoDB;

CREATE TABLE dao_dien (
    ma_dao_dien INT AUTO_INCREMENT PRIMARY KEY,
    ten_dao_dien VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE the_loai (
    ma_the_loai INT AUTO_INCREMENT PRIMARY KEY,
    ten_the_loai VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE dao_dien_phim (
    ma_phim INT NOT NULL,
    ma_dao_dien INT NOT NULL,
    PRIMARY KEY (ma_phim, ma_dao_dien),
    CONSTRAINT fk_dao_dien_phim_phim
        FOREIGN KEY (ma_phim) REFERENCES phim(ma_phim)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_dao_dien_phim_dao_dien
        FOREIGN KEY (ma_dao_dien) REFERENCES dao_dien(ma_dao_dien)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE the_loai_phim (
    ma_phim INT NOT NULL,
    ma_the_loai INT NOT NULL,
    PRIMARY KEY (ma_phim, ma_the_loai),
    CONSTRAINT fk_the_loai_phim_phim
        FOREIGN KEY (ma_phim) REFERENCES phim(ma_phim)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_the_loai_phim_the_loai
        FOREIGN KEY (ma_the_loai) REFERENCES the_loai(ma_the_loai)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- 2. RAP / PHONG CHIEU
-- =========================
CREATE TABLE rap (
    ma_rap INT AUTO_INCREMENT PRIMARY KEY,
    ten_rap VARCHAR(255) NOT NULL,
    dia_chi VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE loai_phong (
    ma_loai_phong INT AUTO_INCREMENT PRIMARY KEY,
    ten_loai_phong VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE phong_chieu (
    ma_phong INT AUTO_INCREMENT PRIMARY KEY,
    ten_phong VARCHAR(255) NOT NULL,
    ma_rap INT NOT NULL,
    ma_loai_phong INT NOT NULL,
    trang_thai TINYINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_phong_chieu_rap_ten UNIQUE (ma_rap, ten_phong),
    CONSTRAINT chk_phong_chieu_trang_thai
        CHECK (trang_thai IN (0, 1, 2)),
    CONSTRAINT fk_phong_chieu_rap
        FOREIGN KEY (ma_rap) REFERENCES rap(ma_rap)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_phong_chieu_loai_phong
        FOREIGN KEY (ma_loai_phong) REFERENCES loai_phong(ma_loai_phong)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- 3. GHE
-- =========================
CREATE TABLE loai_ghe (
    ma_loai_ghe INT AUTO_INCREMENT PRIMARY KEY,
    ten_loai_ghe VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE ghe (
    ma_ghe INT AUTO_INCREMENT PRIMARY KEY,
    ma_phong INT NOT NULL,
    ma_loai_ghe INT NOT NULL,
    hang VARCHAR(10) NOT NULL,
    cot VARCHAR(10) NOT NULL,
    trang_thai TINYINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_ghe_phong_hang_cot UNIQUE (ma_phong, hang, cot),
    CONSTRAINT chk_ghe_trang_thai
        CHECK (trang_thai IN (0, 1)),
    CONSTRAINT fk_ghe_phong
        FOREIGN KEY (ma_phong) REFERENCES phong_chieu(ma_phong)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_ghe_loai_ghe
        FOREIGN KEY (ma_loai_ghe) REFERENCES loai_ghe(ma_loai_ghe)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- 4. SUAT CHIEU
-- =========================
CREATE TABLE suat_chieu (
    ma_suat_chieu INT AUTO_INCREMENT PRIMARY KEY,
    ma_phim INT NOT NULL,
    ma_phong INT NOT NULL,
    ngon_ngu_trinh_chieu TINYINT NOT NULL,
    tg_bat_dau DATETIME NOT NULL,
    tg_ket_thuc DATETIME NOT NULL,
    trang_thai TINYINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_suat_chieu_time
        CHECK (tg_ket_thuc > tg_bat_dau),
    CONSTRAINT chk_suat_chieu_ngon_ngu_trinh_chieu
        CHECK (ngon_ngu_trinh_chieu IN (0, 1, 2, 3)),
    CONSTRAINT chk_suat_chieu_trang_thai
        CHECK (trang_thai IN (0, 1, 2, 3)),
    CONSTRAINT fk_suat_chieu_phim
        FOREIGN KEY (ma_phim) REFERENCES phim(ma_phim)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_suat_chieu_phong
        FOREIGN KEY (ma_phong) REFERENCES phong_chieu(ma_phong)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- 5. VE
-- =========================
CREATE TABLE bang_gia_ve (
    ma_bang_gia INT AUTO_INCREMENT PRIMARY KEY,
    ma_loai_ghe INT NOT NULL,
    ma_loai_phong INT NOT NULL,
    gia DECIMAL(10,2) NOT NULL CHECK (gia >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_bang_gia_ve UNIQUE (ma_loai_ghe, ma_loai_phong),
    CONSTRAINT fk_bang_gia_ve_loai_ghe
        FOREIGN KEY (ma_loai_ghe) REFERENCES loai_ghe(ma_loai_ghe)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_bang_gia_ve_loai_phong
        FOREIGN KEY (ma_loai_phong) REFERENCES loai_phong(ma_loai_phong)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE ve (
    ma_ve INT AUTO_INCREMENT PRIMARY KEY,
    ma_suat_chieu INT NOT NULL,
    ma_ghe INT NOT NULL,
    ma_bang_gia INT NOT NULL,
    trang_thai TINYINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_ve_suat_chieu_ghe UNIQUE (ma_suat_chieu, ma_ghe),
    CONSTRAINT chk_ve_trang_thai
        CHECK (trang_thai IN (0, 1, 2, 3)),
    CONSTRAINT fk_ve_suat_chieu
        FOREIGN KEY (ma_suat_chieu) REFERENCES suat_chieu(ma_suat_chieu)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_ve_ghe
        FOREIGN KEY (ma_ghe) REFERENCES ghe(ma_ghe)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_ve_bang_gia
        FOREIGN KEY (ma_bang_gia) REFERENCES bang_gia_ve(ma_bang_gia)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- 6. SAN PHAM
-- =========================
CREATE TABLE loai_san_pham (
    ma_loai_san_pham INT AUTO_INCREMENT PRIMARY KEY,
    ten_loai_san_pham VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE san_pham (
    ma_san_pham INT AUTO_INCREMENT PRIMARY KEY,
    ten_san_pham VARCHAR(255) NOT NULL,
    ma_loai_san_pham INT NOT NULL,
    gia_co_ban DECIMAL(10,2) NOT NULL CHECK (gia_co_ban >= 0),
    so_luong_ton INT NOT NULL CHECK (so_luong_ton >= 0),
    trang_thai TINYINT NOT NULL,
    url_anh VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_san_pham_trang_thai
        CHECK (trang_thai IN (0, 1, 2)),
    CONSTRAINT fk_san_pham_loai_san_pham
        FOREIGN KEY (ma_loai_san_pham) REFERENCES loai_san_pham(ma_loai_san_pham)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- 7. NHAN VIEN / TAI KHOAN
-- =========================
CREATE TABLE chuc_vu (
    ma_chuc_vu INT AUTO_INCREMENT PRIMARY KEY,
    ten_chuc_vu VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE nhan_vien (
    ma_nhan_vien INT AUTO_INCREMENT PRIMARY KEY,
    ten_nhan_vien VARCHAR(255) NOT NULL,
    gioi_tinh TINYINT,
    so_dien_thoai VARCHAR(20) UNIQUE,
    ngay_sinh DATE,
    email VARCHAR(255) UNIQUE,
    ngay_vao_lam DATE,
    ma_chuc_vu INT NOT NULL,
    trang_thai TINYINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_nhan_vien_gioi_tinh
        CHECK (gioi_tinh IN (0, 1) OR gioi_tinh IS NULL),
    CONSTRAINT chk_nhan_vien_trang_thai
        CHECK (trang_thai IN (0, 1, 2)),
    CONSTRAINT fk_nhan_vien_chuc_vu
        FOREIGN KEY (ma_chuc_vu) REFERENCES chuc_vu(ma_chuc_vu)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE tai_khoan (
    ma_tai_khoan INT AUTO_INCREMENT PRIMARY KEY,
    ma_nhan_vien INT NOT NULL UNIQUE,
    ten_tai_khoan VARCHAR(255) NOT NULL UNIQUE,
    mat_khau VARCHAR(255) NOT NULL,
    trang_thai TINYINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_tai_khoan_trang_thai
        CHECK (trang_thai IN (0, 1)),
    CONSTRAINT fk_tai_khoan_nhan_vien
        FOREIGN KEY (ma_nhan_vien) REFERENCES nhan_vien(ma_nhan_vien)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;
-- =========================
-- 8. HOA DON
-- =========================
CREATE TABLE hoa_don (
    ma_hoa_don INT AUTO_INCREMENT PRIMARY KEY,
    ma_nhan_vien INT NOT NULL,
    phuong_thuc_thanh_toan TINYINT NOT NULL,
    trang_thai TINYINT NOT NULL,
    thoi_gian_thanh_toan DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_hoa_don_phuong_thuc_thanh_toan
        CHECK (phuong_thuc_thanh_toan IN (0, 1)),
    CONSTRAINT chk_hoa_don_trang_thai
        CHECK (trang_thai IN (0, 1)),
    CONSTRAINT fk_hoa_don_nhan_vien
        FOREIGN KEY (ma_nhan_vien) REFERENCES nhan_vien(ma_nhan_vien)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE hoa_don_ve (
    ma_hoa_don INT NOT NULL,
    ma_ve INT NOT NULL,
    don_gia DECIMAL(10,2) NOT NULL CHECK (don_gia >= 0),
    PRIMARY KEY (ma_hoa_don, ma_ve),
    CONSTRAINT fk_hoa_don_ve_hoa_don
        FOREIGN KEY (ma_hoa_don) REFERENCES hoa_don(ma_hoa_don)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_hoa_don_ve_ve
        FOREIGN KEY (ma_ve) REFERENCES ve(ma_ve)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE hoa_don_san_pham (
    ma_hoa_don INT NOT NULL,
    ma_san_pham INT NOT NULL,
    so_luong_tong INT NOT NULL DEFAULT 1 CHECK (so_luong_tong > 0),
    don_gia DECIMAL(10,2) NOT NULL CHECK (don_gia >= 0),
    PRIMARY KEY (ma_hoa_don, ma_san_pham),
    CONSTRAINT fk_hoa_don_san_pham_hoa_don
        FOREIGN KEY (ma_hoa_don) REFERENCES hoa_don(ma_hoa_don)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_hoa_don_san_pham_san_pham
        FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;
