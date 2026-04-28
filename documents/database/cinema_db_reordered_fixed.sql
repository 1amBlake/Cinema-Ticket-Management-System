-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: cinema_db
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP DATABASE IF EXISTS `cinema_db`;

CREATE DATABASE IF NOT EXISTS `cinema_db`
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE `cinema_db`;

SET FOREIGN_KEY_CHECKS = 0;

--
-- Table structure for table `chuc_vu`
--

DROP TABLE IF EXISTS `chuc_vu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chuc_vu` (
  `ma_chuc_vu` int NOT NULL AUTO_INCREMENT,
  `ten_chuc_vu` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_chuc_vu`),
  UNIQUE KEY `ten_chuc_vu` (`ten_chuc_vu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chuc_vu`
--

LOCK TABLES `chuc_vu` WRITE;
/*!40000 ALTER TABLE `chuc_vu` DISABLE KEYS */;
/*!40000 ALTER TABLE `chuc_vu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dao_dien`
--

DROP TABLE IF EXISTS `dao_dien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dao_dien` (
  `ma_dao_dien` int NOT NULL AUTO_INCREMENT,
  `ten_dao_dien` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_dao_dien`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dao_dien`
--

LOCK TABLES `dao_dien` WRITE;
/*!40000 ALTER TABLE `dao_dien` DISABLE KEYS */;
/*!40000 ALTER TABLE `dao_dien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loai_ghe`
--

DROP TABLE IF EXISTS `loai_ghe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loai_ghe` (
  `ma_loai_ghe` int NOT NULL AUTO_INCREMENT,
  `ten_loai_ghe` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_loai_ghe`),
  UNIQUE KEY `ten_loai_ghe` (`ten_loai_ghe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loai_ghe`
--

LOCK TABLES `loai_ghe` WRITE;
/*!40000 ALTER TABLE `loai_ghe` DISABLE KEYS */;
/*!40000 ALTER TABLE `loai_ghe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loai_phong`
--

DROP TABLE IF EXISTS `loai_phong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loai_phong` (
  `ma_loai_phong` int NOT NULL AUTO_INCREMENT,
  `ten_loai_phong` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_loai_phong`),
  UNIQUE KEY `ten_loai_phong` (`ten_loai_phong`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loai_phong`
--

LOCK TABLES `loai_phong` WRITE;
/*!40000 ALTER TABLE `loai_phong` DISABLE KEYS */;
/*!40000 ALTER TABLE `loai_phong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loai_san_pham`
--

DROP TABLE IF EXISTS `loai_san_pham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loai_san_pham` (
  `ma_loai_san_pham` int NOT NULL AUTO_INCREMENT,
  `ten_loai_san_pham` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_loai_san_pham`),
  UNIQUE KEY `ten_loai_san_pham` (`ten_loai_san_pham`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loai_san_pham`
--

LOCK TABLES `loai_san_pham` WRITE;
/*!40000 ALTER TABLE `loai_san_pham` DISABLE KEYS */;
/*!40000 ALTER TABLE `loai_san_pham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `the_loai`
--

DROP TABLE IF EXISTS `the_loai`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `the_loai` (
  `ma_the_loai` int NOT NULL AUTO_INCREMENT,
  `ten_the_loai` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_the_loai`),
  UNIQUE KEY `ten_the_loai` (`ten_the_loai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `the_loai`
--

LOCK TABLES `the_loai` WRITE;
/*!40000 ALTER TABLE `the_loai` DISABLE KEYS */;
/*!40000 ALTER TABLE `the_loai` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rap`
--

DROP TABLE IF EXISTS `rap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rap` (
  `ma_rap` int NOT NULL AUTO_INCREMENT,
  `ten_rap` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dia_chi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_rap`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rap`
--

LOCK TABLES `rap` WRITE;
/*!40000 ALTER TABLE `rap` DISABLE KEYS */;
/*!40000 ALTER TABLE `rap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phim`
--

DROP TABLE IF EXISTS `phim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phim` (
  `ma_phim` int NOT NULL AUTO_INCREMENT,
  `ten_phim` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thoi_luong` int NOT NULL,
  `ngay_phat_hanh` date DEFAULT NULL,
  `ngon_ngu` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trang_thai` tinyint NOT NULL,
  `gioi_han_tuoi` tinyint NOT NULL,
  `url_anh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_phim`),
  CONSTRAINT `chk_phim_gioi_han_tuoi` CHECK ((`gioi_han_tuoi` in (0,1,2,3,4,5))),
  CONSTRAINT `chk_phim_trang_thai` CHECK ((`trang_thai` in (0,1,2))),
  CONSTRAINT `phim_chk_1` CHECK ((`thoi_luong` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phim`
--

LOCK TABLES `phim` WRITE;
/*!40000 ALTER TABLE `phim` DISABLE KEYS */;
/*!40000 ALTER TABLE `phim` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bang_gia_ve`
--

DROP TABLE IF EXISTS `bang_gia_ve`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bang_gia_ve` (
  `ma_bang_gia` int NOT NULL AUTO_INCREMENT,
  `ma_loai_ghe` int NOT NULL,
  `ma_loai_phong` int NOT NULL,
  `gia` decimal(10,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_bang_gia`),
  UNIQUE KEY `uq_bang_gia_ve` (`ma_loai_ghe`,`ma_loai_phong`),
  KEY `fk_bang_gia_ve_loai_phong` (`ma_loai_phong`),
  CONSTRAINT `fk_bang_gia_ve_loai_ghe` FOREIGN KEY (`ma_loai_ghe`) REFERENCES `loai_ghe` (`ma_loai_ghe`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_bang_gia_ve_loai_phong` FOREIGN KEY (`ma_loai_phong`) REFERENCES `loai_phong` (`ma_loai_phong`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `bang_gia_ve_chk_1` CHECK ((`gia` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bang_gia_ve`
--

LOCK TABLES `bang_gia_ve` WRITE;
/*!40000 ALTER TABLE `bang_gia_ve` DISABLE KEYS */;
/*!40000 ALTER TABLE `bang_gia_ve` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhan_vien`
--

DROP TABLE IF EXISTS `nhan_vien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhan_vien` (
  `ma_nhan_vien` int NOT NULL AUTO_INCREMENT,
  `ten_nhan_vien` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gioi_tinh` tinyint DEFAULT NULL,
  `so_dien_thoai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ngay_sinh` date DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ngay_vao_lam` date DEFAULT NULL,
  `ma_chuc_vu` int NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_nhan_vien`),
  UNIQUE KEY `so_dien_thoai` (`so_dien_thoai`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_nhan_vien_chuc_vu` (`ma_chuc_vu`),
  CONSTRAINT `fk_nhan_vien_chuc_vu` FOREIGN KEY (`ma_chuc_vu`) REFERENCES `chuc_vu` (`ma_chuc_vu`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_nhan_vien_gioi_tinh` CHECK (((`gioi_tinh` in (0,1)) or (`gioi_tinh` is null))),
  CONSTRAINT `chk_nhan_vien_trang_thai` CHECK ((`trang_thai` in (0,1,2)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhan_vien`
--

LOCK TABLES `nhan_vien` WRITE;
/*!40000 ALTER TABLE `nhan_vien` DISABLE KEYS */;
/*!40000 ALTER TABLE `nhan_vien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phong_chieu`
--

DROP TABLE IF EXISTS `phong_chieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phong_chieu` (
  `ma_phong` int NOT NULL AUTO_INCREMENT,
  `ten_phong` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ma_rap` int NOT NULL,
  `ma_loai_phong` int NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_phong`),
  UNIQUE KEY `uq_phong_chieu_rap_ten` (`ma_rap`,`ten_phong`),
  KEY `fk_phong_chieu_loai_phong` (`ma_loai_phong`),
  CONSTRAINT `fk_phong_chieu_loai_phong` FOREIGN KEY (`ma_loai_phong`) REFERENCES `loai_phong` (`ma_loai_phong`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_phong_chieu_rap` FOREIGN KEY (`ma_rap`) REFERENCES `rap` (`ma_rap`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_phong_chieu_trang_thai` CHECK ((`trang_thai` in (0,1,2)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phong_chieu`
--

LOCK TABLES `phong_chieu` WRITE;
/*!40000 ALTER TABLE `phong_chieu` DISABLE KEYS */;
/*!40000 ALTER TABLE `phong_chieu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `san_pham`
--

DROP TABLE IF EXISTS `san_pham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `san_pham` (
  `ma_san_pham` int NOT NULL AUTO_INCREMENT,
  `ten_san_pham` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ma_loai_san_pham` int NOT NULL,
  `gia_co_ban` decimal(10,2) NOT NULL,
  `so_luong_ton` int NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `url_anh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_san_pham`),
  KEY `fk_san_pham_loai_san_pham` (`ma_loai_san_pham`),
  CONSTRAINT `fk_san_pham_loai_san_pham` FOREIGN KEY (`ma_loai_san_pham`) REFERENCES `loai_san_pham` (`ma_loai_san_pham`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_san_pham_trang_thai` CHECK ((`trang_thai` in (0,1,2))),
  CONSTRAINT `san_pham_chk_1` CHECK ((`gia_co_ban` >= 0)),
  CONSTRAINT `san_pham_chk_2` CHECK ((`so_luong_ton` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `san_pham`
--

LOCK TABLES `san_pham` WRITE;
/*!40000 ALTER TABLE `san_pham` DISABLE KEYS */;
/*!40000 ALTER TABLE `san_pham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dao_dien_phim`
--

DROP TABLE IF EXISTS `dao_dien_phim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dao_dien_phim` (
  `ma_phim` int NOT NULL,
  `ma_dao_dien` int NOT NULL,
  PRIMARY KEY (`ma_phim`,`ma_dao_dien`),
  KEY `fk_dao_dien_phim_dao_dien` (`ma_dao_dien`),
  CONSTRAINT `fk_dao_dien_phim_dao_dien` FOREIGN KEY (`ma_dao_dien`) REFERENCES `dao_dien` (`ma_dao_dien`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_dao_dien_phim_phim` FOREIGN KEY (`ma_phim`) REFERENCES `phim` (`ma_phim`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dao_dien_phim`
--

LOCK TABLES `dao_dien_phim` WRITE;
/*!40000 ALTER TABLE `dao_dien_phim` DISABLE KEYS */;
/*!40000 ALTER TABLE `dao_dien_phim` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `the_loai_phim`
--

DROP TABLE IF EXISTS `the_loai_phim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `the_loai_phim` (
  `ma_phim` int NOT NULL,
  `ma_the_loai` int NOT NULL,
  PRIMARY KEY (`ma_phim`,`ma_the_loai`),
  KEY `fk_the_loai_phim_the_loai` (`ma_the_loai`),
  CONSTRAINT `fk_the_loai_phim_phim` FOREIGN KEY (`ma_phim`) REFERENCES `phim` (`ma_phim`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_the_loai_phim_the_loai` FOREIGN KEY (`ma_the_loai`) REFERENCES `the_loai` (`ma_the_loai`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `the_loai_phim`
--

LOCK TABLES `the_loai_phim` WRITE;
/*!40000 ALTER TABLE `the_loai_phim` DISABLE KEYS */;
/*!40000 ALTER TABLE `the_loai_phim` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tai_khoan`
--

DROP TABLE IF EXISTS `tai_khoan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tai_khoan` (
  `ma_tai_khoan` int NOT NULL AUTO_INCREMENT,
  `ma_nhan_vien` int NOT NULL,
  `ten_tai_khoan` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mat_khau` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_tai_khoan`),
  UNIQUE KEY `ma_nhan_vien` (`ma_nhan_vien`),
  UNIQUE KEY `ten_tai_khoan` (`ten_tai_khoan`),
  CONSTRAINT `fk_tai_khoan_nhan_vien` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_tai_khoan_trang_thai` CHECK ((`trang_thai` in (0,1)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tai_khoan`
--

LOCK TABLES `tai_khoan` WRITE;
/*!40000 ALTER TABLE `tai_khoan` DISABLE KEYS */;
/*!40000 ALTER TABLE `tai_khoan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ghe`
--

DROP TABLE IF EXISTS `ghe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ghe` (
  `ma_ghe` int NOT NULL AUTO_INCREMENT,
  `ma_phong` int NOT NULL,
  `ma_loai_ghe` int NOT NULL,
  `hang` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cot` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_ghe`),
  UNIQUE KEY `uq_ghe_phong_hang_cot` (`ma_phong`,`hang`,`cot`),
  KEY `fk_ghe_loai_ghe` (`ma_loai_ghe`),
  CONSTRAINT `fk_ghe_loai_ghe` FOREIGN KEY (`ma_loai_ghe`) REFERENCES `loai_ghe` (`ma_loai_ghe`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_ghe_phong` FOREIGN KEY (`ma_phong`) REFERENCES `phong_chieu` (`ma_phong`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_ghe_trang_thai` CHECK ((`trang_thai` in (0,1)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ghe`
--

LOCK TABLES `ghe` WRITE;
/*!40000 ALTER TABLE `ghe` DISABLE KEYS */;
/*!40000 ALTER TABLE `ghe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suat_chieu`
--

DROP TABLE IF EXISTS `suat_chieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suat_chieu` (
  `ma_suat_chieu` int NOT NULL AUTO_INCREMENT,
  `ma_phim` int NOT NULL,
  `ma_phong` int NOT NULL,
  `ngon_ngu_trinh_chieu` tinyint NOT NULL,
  `tg_bat_dau` datetime NOT NULL,
  `tg_ket_thuc` datetime NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_suat_chieu`),
  KEY `fk_suat_chieu_phim` (`ma_phim`),
  KEY `fk_suat_chieu_phong` (`ma_phong`),
  CONSTRAINT `fk_suat_chieu_phim` FOREIGN KEY (`ma_phim`) REFERENCES `phim` (`ma_phim`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_suat_chieu_phong` FOREIGN KEY (`ma_phong`) REFERENCES `phong_chieu` (`ma_phong`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_suat_chieu_ngon_ngu_trinh_chieu` CHECK ((`ngon_ngu_trinh_chieu` in (0,1,2,3))),
  CONSTRAINT `chk_suat_chieu_time` CHECK ((`tg_ket_thuc` > `tg_bat_dau`)),
  CONSTRAINT `chk_suat_chieu_trang_thai` CHECK ((`trang_thai` in (0,1,2,3)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suat_chieu`
--

LOCK TABLES `suat_chieu` WRITE;
/*!40000 ALTER TABLE `suat_chieu` DISABLE KEYS */;
/*!40000 ALTER TABLE `suat_chieu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ve`
--

DROP TABLE IF EXISTS `ve`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ve` (
  `ma_ve` int NOT NULL AUTO_INCREMENT,
  `ma_suat_chieu` int NOT NULL,
  `ma_ghe` int NOT NULL,
  `ma_bang_gia` int NOT NULL,
  `trang_thai` tinyint NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_ve`),
  UNIQUE KEY `uq_ve_suat_chieu_ghe` (`ma_suat_chieu`,`ma_ghe`),
  KEY `fk_ve_ghe` (`ma_ghe`),
  KEY `fk_ve_bang_gia` (`ma_bang_gia`),
  CONSTRAINT `fk_ve_bang_gia` FOREIGN KEY (`ma_bang_gia`) REFERENCES `bang_gia_ve` (`ma_bang_gia`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_ve_ghe` FOREIGN KEY (`ma_ghe`) REFERENCES `ghe` (`ma_ghe`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_ve_suat_chieu` FOREIGN KEY (`ma_suat_chieu`) REFERENCES `suat_chieu` (`ma_suat_chieu`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_ve_trang_thai` CHECK ((`trang_thai` in (0,1,2,3)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ve`
--

LOCK TABLES `ve` WRITE;
/*!40000 ALTER TABLE `ve` DISABLE KEYS */;
/*!40000 ALTER TABLE `ve` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Table structure for table `hoa_don`
--

DROP TABLE IF EXISTS `hoa_don`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoa_don` (
  `ma_hoa_don` int NOT NULL AUTO_INCREMENT,
  `ma_nhan_vien` int NOT NULL,
  `phuong_thuc_thanh_toan` tinyint NOT NULL,
  `trang_thai` tinyint NOT NULL DEFAULT '0',
  `thoi_gian_thanh_toan` datetime DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ma_hoa_don`),
  KEY `fk_hoa_don_nhan_vien` (`ma_nhan_vien`),
  CONSTRAINT `fk_hoa_don_nhan_vien` FOREIGN KEY (`ma_nhan_vien`) REFERENCES `nhan_vien` (`ma_nhan_vien`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_hoa_don_phuong_thuc_thanh_toan` CHECK ((`phuong_thuc_thanh_toan` in (0,1))),
  CONSTRAINT `chk_hoa_don_trang_thai` CHECK ((`trang_thai` in (0,1,2,3)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoa_don`
--

LOCK TABLES `hoa_don` WRITE;
/*!40000 ALTER TABLE `hoa_don` DISABLE KEYS */;
/*!40000 ALTER TABLE `hoa_don` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoa_don_ve`
--

DROP TABLE IF EXISTS `hoa_don_ve`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoa_don_ve` (
  `ma_hoa_don` int NOT NULL,
  `ma_ve` int NOT NULL,
  `don_gia` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ma_hoa_don`,`ma_ve`),
  KEY `fk_hoa_don_ve_ve` (`ma_ve`),
  CONSTRAINT `fk_hoa_don_ve_hoa_don` FOREIGN KEY (`ma_hoa_don`) REFERENCES `hoa_don` (`ma_hoa_don`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_hoa_don_ve_ve` FOREIGN KEY (`ma_ve`) REFERENCES `ve` (`ma_ve`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `hoa_don_ve_chk_1` CHECK ((`don_gia` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoa_don_ve`
--

LOCK TABLES `hoa_don_ve` WRITE;
/*!40000 ALTER TABLE `hoa_don_ve` DISABLE KEYS */;
/*!40000 ALTER TABLE `hoa_don_ve` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoa_don_san_pham`
--

DROP TABLE IF EXISTS `hoa_don_san_pham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoa_don_san_pham` (
  `ma_hoa_don` int NOT NULL,
  `ma_san_pham` int NOT NULL,
  `so_luong_tong` int NOT NULL DEFAULT '1',
  `don_gia` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ma_hoa_don`,`ma_san_pham`),
  KEY `fk_hoa_don_san_pham_san_pham` (`ma_san_pham`),
  CONSTRAINT `fk_hoa_don_san_pham_hoa_don` FOREIGN KEY (`ma_hoa_don`) REFERENCES `hoa_don` (`ma_hoa_don`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_hoa_don_san_pham_san_pham` FOREIGN KEY (`ma_san_pham`) REFERENCES `san_pham` (`ma_san_pham`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `hoa_don_san_pham_chk_1` CHECK ((`so_luong_tong` > 0)),
  CONSTRAINT `hoa_don_san_pham_chk_2` CHECK ((`don_gia` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoa_don_san_pham`
--

LOCK TABLES `hoa_don_san_pham` WRITE;
/*!40000 ALTER TABLE `hoa_don_san_pham` DISABLE KEYS */;
/*!40000 ALTER TABLE `hoa_don_san_pham` ENABLE KEYS */;
UNLOCK TABLES;

SET FOREIGN_KEY_CHECKS = 1;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-21 10:31:21
