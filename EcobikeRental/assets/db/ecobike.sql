-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th12 13, 2021 lúc 03:53 PM
-- Phiên bản máy phục vụ: 10.4.17-MariaDB
-- Phiên bản PHP: 8.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `ecobike`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bike`
--

CREATE TABLE `bike` (
  `bike_id` varchar(200) NOT NULL,
  `type` varchar(100) NOT NULL,
  `price` int(11) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `dock_id` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `bike`
--

INSERT INTO `bike` (`bike_id`, `type`, `price`, `status`, `dock_id`) VALUES
('B01', 'Standard Bike', 1000, 1, 'D02'),
('B02', 'Twin Bike', 1375, 1, 'D02'),
('B03', 'Standard Bike', 1000, 1, 'D03'),
('B04', 'Standard E-bike', 1750, 1, 'D04'),
('B05', 'Standard Bike', 1000, 0, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bike_rent`
--

CREATE TABLE `bike_rent` (
  `bike_id` varchar(100) NOT NULL,
  `rent_time` datetime NOT NULL,
  `return_time` datetime DEFAULT NULL,
  `rent_dock` varchar(100) NOT NULL,
  `return_dock` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `bike_rent`
--

INSERT INTO `bike_rent` (`bike_id`, `rent_time`, `return_time`, `rent_dock`, `return_dock`) VALUES
('B05', '2021-12-13 21:51:35', NULL, 'D05', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `creditcard`
--

CREATE TABLE `creditcard` (
  `card_id` varchar(100) NOT NULL,
  `owner` varchar(100) NOT NULL,
  `card_code` varchar(100) NOT NULL,
  `cvv_code` varchar(100) NOT NULL,
  `date_expired` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `dock`
--

CREATE TABLE `dock` (
  `dock_id` varchar(100) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `station_id` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `dock`
--

INSERT INTO `dock` (`dock_id`, `status`, `station_id`) VALUES
('D01', 0, 'ST02'),
('D02', 0, 'ST01'),
('D03', 0, 'ST03'),
('D04', 0, 'ST03'),
('D05', 1, 'ST02');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `standard_ebike`
--

CREATE TABLE `standard_ebike` (
  `sebike_id` varchar(100) NOT NULL,
  `time_limit` int(11) NOT NULL,
  `battery` int(11) NOT NULL DEFAULT 100
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `station`
--

CREATE TABLE `station` (
  `station_id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(250) NOT NULL,
  `area` int(11) NOT NULL,
  `bike_quantity` int(11) NOT NULL,
  `empty_docks` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `station`
--

INSERT INTO `station` (`station_id`, `name`, `address`, `area`, `bike_quantity`, `empty_docks`) VALUES
('ST01', 'Station 01', 'district 01, HN', 200, 1, 0),
('ST02', 'Station 02', 'ABC', 300, 1, 1),
('ST03', 'Station 03', 'district 02, HN', 200, 2, 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `transaction`
--

CREATE TABLE `transaction` (
  `transaction_id` int(11) NOT NULL,
  `content` varchar(200) NOT NULL,
  `amount` int(11) NOT NULL,
  `create_at` date NOT NULL,
  `bike_id` varchar(100) NOT NULL,
  `card_id` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `user_id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`user_id`, `name`) VALUES
('U01', 'admin');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `bike`
--
ALTER TABLE `bike`
  ADD PRIMARY KEY (`bike_id`),
  ADD KEY `dock_id` (`dock_id`);

--
-- Chỉ mục cho bảng `bike_rent`
--
ALTER TABLE `bike_rent`
  ADD PRIMARY KEY (`bike_id`);

--
-- Chỉ mục cho bảng `creditcard`
--
ALTER TABLE `creditcard`
  ADD PRIMARY KEY (`card_id`);

--
-- Chỉ mục cho bảng `dock`
--
ALTER TABLE `dock`
  ADD PRIMARY KEY (`dock_id`),
  ADD KEY `station_id` (`station_id`);

--
-- Chỉ mục cho bảng `standard_ebike`
--
ALTER TABLE `standard_ebike`
  ADD KEY `sebike_id` (`sebike_id`);

--
-- Chỉ mục cho bảng `station`
--
ALTER TABLE `station`
  ADD PRIMARY KEY (`station_id`);

--
-- Chỉ mục cho bảng `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `card_id` (`card_id`),
  ADD KEY `transaction_ibfk_1` (`bike_id`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `transaction`
--
ALTER TABLE `transaction`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `bike`
--
ALTER TABLE `bike`
  ADD CONSTRAINT `bike_ibfk_1` FOREIGN KEY (`dock_id`) REFERENCES `dock` (`dock_id`);

--
-- Các ràng buộc cho bảng `bike_rent`
--
ALTER TABLE `bike_rent`
  ADD CONSTRAINT `bike_rent_ibfk_1` FOREIGN KEY (`bike_id`) REFERENCES `bike` (`bike_id`);

--
-- Các ràng buộc cho bảng `dock`
--
ALTER TABLE `dock`
  ADD CONSTRAINT `dock_ibfk_1` FOREIGN KEY (`station_id`) REFERENCES `station` (`station_id`);

--
-- Các ràng buộc cho bảng `standard_ebike`
--
ALTER TABLE `standard_ebike`
  ADD CONSTRAINT `standard_ebike_ibfk_1` FOREIGN KEY (`sebike_id`) REFERENCES `bike` (`bike_id`);

--
-- Các ràng buộc cho bảng `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`bike_id`) REFERENCES `bike_rent` (`bike_id`),
  ADD CONSTRAINT `transaction_ibfk_2` FOREIGN KEY (`card_id`) REFERENCES `creditcard` (`card_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
