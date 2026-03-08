CREATE TABLE `phim` (
  `ma_phim` int PRIMARY KEY AUTO_INCREMENT,
  `ten_phim` varchar(255) NOT NULL,
  `thoi_luong` int NOT NULL,
  `ngay_phat_hanh` date,
  `ngon_ngu` varchar(100),
  `trang_thai` tinyint NOT NULL,
  `gioi_han_tuoi` tinyint NOT NULL,
  `url_anh` varchar(255),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `dao_dien` (
  `ma_dao_dien` int PRIMARY KEY AUTO_INCREMENT,
  `ten_dao_dien` varchar(255) NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `the_loai` (
  `ma_the_loai` int PRIMARY KEY AUTO_INCREMENT,
  `ten_the_loai` varchar(100) UNIQUE NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `dao_dien_phim` (
  `ma_phim` int,
  `ma_dao_dien` int,
  PRIMARY KEY (`ma_phim`, `ma_dao_dien`)
);

CREATE TABLE `the_loai_phim` (
  `ma_phim` int,
  `ma_the_loai` int,
  PRIMARY KEY (`ma_phim`, `ma_the_loai`)
);

CREATE TABLE `rap` (
  `ma_rap` int PRIMARY KEY AUTO_INCREMENT,
  `ten_rap` varchar(255) NOT NULL,
  `dia_chi` varchar(255),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `loai_phong` (
  `ma_loai_phong` int PRIMARY KEY AUTO_INCREMENT,
  `ten_loai_phong` varchar(100) UNIQUE NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `phong_chieu` (
  `ma_phong` int PRIMARY KEY AUTO_INCREMENT,
  `ten_phong` varchar(255) NOT NULL,
  `ma_rap` int NOT NULL,
  `ma_loai_phong` int NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `loai_ghe` (
  `ma_loai_ghe` int PRIMARY KEY AUTO_INCREMENT,
  `ten_loai_ghe` varchar(255) UNIQUE NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `ghe` (
  `ma_ghe` int PRIMARY KEY AUTO_INCREMENT,
  `ma_phong` int NOT NULL,
  `ma_loai_ghe` int NOT NULL,
  `hang` varchar(10) NOT NULL,
  `cot` varchar(10) NOT NULL,
  `trang_thai` bit NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `suat_chieu` (
  `ma_suat_chieu` int PRIMARY KEY AUTO_INCREMENT,
  `ma_phim` int NOT NULL,
  `ma_phong` int NOT NULL,
  `ngon_ngu_trinh_chieu` tinyint NOT NULL,
  `tg_bat_dau` datetime NOT NULL,
  `tg_ket_thuc` datetime NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `bang_gia_ve` (
  `ma_bang_gia` int PRIMARY KEY AUTO_INCREMENT,
  `ma_loai_ghe` int NOT NULL,
  `ma_loai_phong` int NOT NULL,
  `gia` decimal(10,2) NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `ve` (
  `ma_ve` int PRIMARY KEY AUTO_INCREMENT,
  `ma_suat_chieu` int NOT NULL,
  `ma_ghe` int NOT NULL,
  `ma_bang_gia` int NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `loai_san_pham` (
  `ma_loai_san_pham` int PRIMARY KEY AUTO_INCREMENT,
  `ten_loai_san_pham` varchar(255) UNIQUE NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `san_pham` (
  `ma_san_pham` int PRIMARY KEY AUTO_INCREMENT,
  `ten_san_pham` varchar(255) NOT NULL,
  `ma_loai_san_pham` int NOT NULL,
  `gia_co_ban` decimal(10,2) NOT NULL,
  `so_luong_ton` int NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `url_anh` varchar(255),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `chuc_vu` (
  `ma_chuc_vu` int PRIMARY KEY AUTO_INCREMENT,
  `ten_chuc_vu` varchar(100) UNIQUE NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `nhan_vien` (
  `ma_nhan_vien` int PRIMARY KEY AUTO_INCREMENT,
  `ten_nhan_vien` varchar(255) NOT NULL,
  `gioi_tinh` bit,
  `so_dien_thoai` varchar(20) UNIQUE,
  `ngay_sinh` date,
  `email` varchar(255) UNIQUE,
  `ngay_vao_lam` date,
  `ma_chuc_vu` int NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `tai_khoan` (
  `ma_tai_khoan` int PRIMARY KEY AUTO_INCREMENT,
  `ma_nhan_vien` int UNIQUE NOT NULL,
  `ten_tai_khoan` varchar(255) UNIQUE NOT NULL,
  `mat_khau` varchar(255) NOT NULL,
  `trang_thai` bit NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `hoa_don` (
  `ma_hoa_don` int PRIMARY KEY AUTO_INCREMENT,
  `ma_nhan_vien` int NOT NULL,
  `phuong_thuc_thanh_toan` tinyint NOT NULL,
  `tong_tien` decimal(10,2) NOT NULL DEFAULT 0,
  `trang_thai` tinyint NOT NULL,
  `thoi_gian_thanh_toan` datetime,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `hoa_don_ve` (
  `ma_hoa_don` int,
  `ma_ve` int,
  `don_gia` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ma_hoa_don`, `ma_ve`)
);

CREATE TABLE `hoa_don_san_pham` (
  `ma_hoa_don` int,
  `ma_san_pham` int,
  `so_luong_tong` int NOT NULL DEFAULT 1,
  `don_gia` decimal(10,2) NOT NULL,
  `thanh_tien` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ma_hoa_don`, `ma_san_pham`)
);

CREATE UNIQUE INDEX `ghe_index_0` ON `ghe` (`ma_phong`, `hang`, `cot`);

CREATE UNIQUE INDEX `bang_gia_ve_index_1` ON `bang_gia_ve` (`ma_loai_ghe`, `ma_loai_phong`);

CREATE UNIQUE INDEX `ve_index_2` ON `ve` (`ma_suat_chieu`, `ma_ghe`);

ALTER TABLE `dao_dien_phim` ADD FOREIGN KEY (`ma_phim`) REFERENCES `phim` (`ma_phim`);

ALTER TABLE `dao_dien_phim` ADD FOREIGN KEY (`ma_dao_dien`) REFERENCES `dao_dien` (`ma_dao_dien`);

ALTER TABLE `the_loai_phim` ADD FOREIGN KEY (`ma_phim`) REFERENCES `phim` (`ma_phim`);

ALTER TABLE `the_loai_phim` ADD FOREIGN KEY (`ma_the_loai`) REFERENCES `the_loai` (`ma_the_loai`);

ALTER TABLE `phong_chieu` ADD FOREIGN KEY (`ma_rap`) REFERENCES `rap` (`ma_rap`);

ALTER TABLE `phong_chieu` ADD FOREIGN KEY (`ma_loai_phong`) REFERENCES `loai_phong` (`ma_loai_phong`);

ALTER TABLE `ghe` ADD FOREIGN KEY (`ma_phong`) REFERENCES `phong_chieu` (`ma_phong`);

ALTER TABLE `ghe` ADD FOREIGN KEY (`ma_loai_ghe`) REFERENCES `loai_ghe` (`ma_loai_ghe`);

ALTER TABLE `suat_chieu` ADD FOREIGN KEY (`ma_phim`) REFERENCES `phim` (`ma_phim`);

ALTER TABLE `suat_chieu` ADD FOREIGN KEY (`ma_phong`) REFERENCES `phong_chieu` (`ma_phong`);

ALTER TABLE `bang_gia_ve` ADD FOREIGN KEY (`ma_loai_ghe`) REFERENCES `loai_ghe` (`ma_loai_ghe`);

ALTER TABLE `bang_gia_ve` ADD FOREIGN KEY (`ma_loai_phong`) REFERENCES `loai_phong` (`ma_loai_phong`);

ALTER TABLE `ve` ADD FOREIGN KEY (`ma_suat_chieu`) REFERENCES `suat_chieu` (`ma_suat_chieu`);

ALTER TABLE `ve` ADD FOREIGN KEY (`ma_ghe`) REFERENCES `ghe` (`ma_ghe`);

ALTER TABLE `ve` ADD FOREIGN KEY (`ma_bang_gia`) REFERENCES `bang_gia_ve` (`ma_bang_gia`);

ALTER TABLE `san_pham` ADD FOREIGN KEY (`ma_loai_san_pham`) REFERENCES `loai_san_pham` (`ma_loai_san_pham`);

ALTER TABLE `nhan_vien` ADD FOREIGN KEY (`ma_chuc_vu`) REFERENCES `chuc_vu` (`ma_chuc_vu`);

ALTER TABLE `tai_khoan` ADD FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`);

ALTER TABLE `hoa_don` ADD FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`);

ALTER TABLE `hoa_don_ve` ADD FOREIGN KEY (`ma_hoa_don`) REFERENCES `hoa_don` (`ma_hoa_don`);

ALTER TABLE `hoa_don_ve` ADD FOREIGN KEY (`ma_ve`) REFERENCES `ve` (`ma_ve`);

ALTER TABLE `hoa_don_san_pham` ADD FOREIGN KEY (`ma_hoa_don`) REFERENCES `hoa_don` (`ma_hoa_don`);

ALTER TABLE `hoa_don_san_pham` ADD FOREIGN KEY (`ma_san_pham`) REFERENCES `san_pham` (`ma_san_pham`);
