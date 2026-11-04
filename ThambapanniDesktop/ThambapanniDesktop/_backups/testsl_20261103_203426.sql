-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: testsl
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `testsl`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `testsl` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `testsl`;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Spices','Traditional Sri Lankan spices and seasonings','2025-08-05 09:20:10'),(2,'Tea','Ceylon tea varieties from different regions','2025-08-05 09:20:10'),(3,'Ayurvedic Products','Traditional herbal medicines and wellness products','2025-08-05 09:20:10'),(4,'Handicrafts','Traditional Sri Lankan handicrafts and artwork','2025-08-05 09:20:10'),(5,'Textiles','Traditional fabrics and clothing','2025-08-05 09:20:10'),(6,'Coconut Products','Pure coconut oil, desiccated coconut, and coconut-based products','2025-08-05 09:20:10'),(7,'Gemstones','Authentic Sri Lankan precious and semi-precious stones','2025-08-05 09:20:10'),(8,'Essential Oils','Natural essential oils extracted from Sri Lankan plants','2025-08-05 09:20:10'),(9,'Spices','Traditional Sri Lankan spices and seasonings','2025-08-05 09:28:27'),(10,'Tea','Ceylon tea varieties from different regions','2025-08-05 09:28:27'),(11,'Ayurvedic Products','Traditional herbal medicines and wellness products','2025-08-05 09:28:27'),(12,'Handicrafts','Traditional Sri Lankan handicrafts and artwork','2025-08-05 09:28:27'),(13,'Textiles','Traditional fabrics and clothing','2025-08-05 09:28:27'),(14,'Coconut Products','Pure coconut oil, desiccated coconut, and coconut-based products','2025-08-05 09:28:27'),(15,'Gemstones','Authentic Sri Lankan precious and semi-precious stones','2025-08-05 09:28:27'),(16,'Essential Oils','Natural essential oils extracted from Sri Lankan plants','2025-08-05 09:28:27'),(17,'Spices','Traditional Sri Lankan spices and seasonings','2025-08-05 09:37:52'),(18,'Tea','Ceylon tea varieties from different regions','2025-08-05 09:37:52'),(19,'Ayurvedic Products','Traditional herbal medicines and wellness products','2025-08-05 09:37:52'),(20,'Handicrafts','Traditional Sri Lankan handicrafts and artwork','2025-08-05 09:37:52'),(21,'Textiles','Traditional fabrics and clothing','2025-08-05 09:37:52'),(22,'Coconut Products','Pure coconut oil, desiccated coconut, and coconut-based products','2025-08-05 09:37:52'),(23,'Gemstones','Authentic Sri Lankan precious and semi-precious stones','2025-08-05 09:37:52'),(24,'Essential Oils','Natural essential oils extracted from Sri Lankan plants','2025-08-05 09:37:52'),(25,'ayurwedic','','2025-08-05 09:41:17'),(26,'Spices','Traditional Sri Lankan spices and seasonings','2025-08-05 09:45:44'),(27,'Tea','Ceylon tea varieties from different regions','2025-08-05 09:45:44'),(28,'Ayurvedic Products','Traditional herbal medicines and wellness products','2025-08-05 09:45:44'),(29,'Handicrafts','Traditional Sri Lankan handicrafts and artwork','2025-08-05 09:45:44'),(30,'Textiles','Traditional fabrics and clothing','2025-08-05 09:45:44'),(31,'Coconut Products','Pure coconut oil, desiccated coconut, and coconut-based products','2025-08-05 09:45:44'),(32,'Gemstones','Authentic Sri Lankan precious and semi-precious stones','2025-08-05 09:45:44'),(33,'Essential Oils','Natural essential oils extracted from Sri Lankan plants','2025-08-05 09:45:44');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_items` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`item_id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `order_status` enum('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') DEFAULT 'PENDING',
  `shipping_address` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`order_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(200) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock_quantity` int(11) DEFAULT 0,
  `category_id` int(11) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `origin` varchar(100) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`product_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Ceylon Cinnamon Sticks','Premium quality true cinnamon from Sri Lanka, sweet and delicate flavor',15.99,100,1,'images/cinnamon.jpg','Kandy','2025-08-05 09:20:10','2025-08-05 09:20:10'),(2,'Black Pepper Whole','Organic black peppercorns with intense flavor and aroma',12.50,80,1,'images/pepper.jpg','Matale','2025-08-05 09:20:10','2025-08-05 09:20:10'),(3,'Cardamom Pods','Green cardamom pods, perfect for tea and desserts',25.00,60,1,'images/cardamom.jpg','Kandy','2025-08-05 09:20:10','2025-08-05 09:20:10'),(4,'Turmeric Powder','Pure turmeric powder with high curcumin content',8.99,120,1,'images/turmeric.jpg','Galle','2025-08-05 09:20:10','2025-08-05 09:20:10'),(5,'Ceylon Black Tea PEKOE','High-grown Ceylon black tea with rich flavor',18.99,150,2,'images/black_tea.jpg','Nuwara Eliya','2025-08-05 09:20:10','2025-08-05 09:20:10'),(6,'Earl Grey Ceylon Tea','Ceylon black tea blended with bergamot oil',22.50,90,2,'images/earl_grey.jpg','Dimbula','2025-08-05 09:20:10','2025-08-05 09:20:10'),(7,'Green Tea Sencha Style','Ceylon green tea processed in Japanese style',20.00,75,2,'images/green_tea.jpg','Uva','2025-08-05 09:20:10','2025-08-05 09:20:10'),(8,'White Tea Silver Tips','Premium white tea with delicate flavor',45.00,40,2,'images/white_tea.jpg','Nuwara Eliya','2025-08-05 09:20:10','2025-08-05 09:20:10'),(9,'Ceylon Cinnamon Sticks','Premium quality true cinnamon from Sri Lanka, sweet and delicate flavor',12.00,100,1,NULL,'Kandy','2025-08-05 09:28:28','2025-08-05 09:39:57'),(11,'Cardamom Pods','Green cardamom pods, perfect for tea and desserts',25.00,60,1,'images/cardamom.jpg','Kandy','2025-08-05 09:28:28','2025-08-05 09:28:28'),(12,'Turmeric Powder','Pure turmeric powder with high curcumin content',8.99,120,1,'images/turmeric.jpg','Galle','2025-08-05 09:28:28','2025-08-05 09:28:28'),(13,'Ceylon Black Tea PEKOE','High-grown Ceylon black tea with rich flavor',18.99,150,2,'images/black_tea.jpg','Nuwara Eliya','2025-08-05 09:28:28','2025-08-05 09:28:28'),(14,'Earl Grey Ceylon Tea','Ceylon black tea blended with bergamot oil',22.50,90,2,'images/earl_grey.jpg','Dimbula','2025-08-05 09:28:28','2025-08-05 09:28:28'),(15,'Green Tea Sencha Style','Ceylon green tea processed in Japanese style',20.00,75,2,'images/green_tea.jpg','Uva','2025-08-05 09:28:28','2025-08-05 09:28:28'),(16,'White Tea Silver Tips','Premium white tea with delicate flavor',45.00,40,2,'images/white_tea.jpg','Nuwara Eliya','2025-08-05 09:28:28','2025-08-05 09:28:28'),(17,'Ceylon Cinnamon Sticks','Premium quality true cinnamon from Sri Lanka, sweet and delicate flavor',15.99,100,1,'images/cinnamon.jpg','Kandy','2025-08-05 09:37:52','2025-08-05 09:37:52'),(18,'Black Pepper Whole','Organic black peppercorns with intense flavor and aroma',12.50,80,1,'images/pepper.jpg','Matale','2025-08-05 09:37:52','2025-08-05 09:37:52'),(19,'Cardamom Pods','Green cardamom pods, perfect for tea and desserts',25.00,60,1,'images/cardamom.jpg','Kandy','2025-08-05 09:37:52','2025-08-05 09:37:52'),(20,'Turmeric Powder','Pure turmeric powder with high curcumin content',8.99,120,1,'images/turmeric.jpg','Galle','2025-08-05 09:37:52','2025-08-05 09:37:52'),(21,'Ceylon Black Tea PEKOE','High-grown Ceylon black tea with rich flavor',18.99,150,2,'images/black_tea.jpg','Nuwara Eliya','2025-08-05 09:37:52','2025-08-05 09:37:52'),(22,'Earl Grey Ceylon Tea','Ceylon black tea blended with bergamot oil',22.50,90,2,'images/earl_grey.jpg','Dimbula','2025-08-05 09:37:52','2025-08-05 09:37:52'),(23,'Green Tea Sencha Style','Ceylon green tea processed in Japanese style',20.00,75,2,'images/green_tea.jpg','Uva','2025-08-05 09:37:52','2025-08-05 09:37:52'),(24,'White Tea Silver Tips','Premium white tea with delicate flavor',45.00,40,2,'images/white_tea.jpg','Nuwara Eliya','2025-08-05 09:37:52','2025-08-05 09:37:52'),(25,'skin care cream','it is good for dry',450.00,1,3,NULL,'','2025-08-05 09:39:36','2025-08-05 09:39:36'),(26,'Ceylon Cinnamon Sticks','Premium quality true cinnamon from Sri Lanka, sweet and delicate flavor',15.99,100,1,'images/cinnamon.jpg','Kandy','2025-08-05 09:45:44','2025-08-05 09:45:44'),(27,'Black Pepper Whole','Organic black peppercorns with intense flavor and aroma',12.50,80,1,'images/pepper.jpg','Matale','2025-08-05 09:45:44','2025-08-05 09:45:44'),(28,'Cardamom Pods','Green cardamom pods, perfect for tea and desserts',25.00,60,1,'images/cardamom.jpg','Kandy','2025-08-05 09:45:44','2025-08-05 09:45:44'),(29,'Turmeric Powder','Pure turmeric powder with high curcumin content',8.99,120,1,'images/turmeric.jpg','Galle','2025-08-05 09:45:44','2025-08-05 09:45:44'),(30,'Ceylon Black Tea PEKOE','High-grown Ceylon black tea with rich flavor',18.99,150,2,'images/black_tea.jpg','Nuwara Eliya','2025-08-05 09:45:44','2025-08-05 09:45:44'),(31,'Earl Grey Ceylon Tea','Ceylon black tea blended with bergamot oil',22.50,90,2,'images/earl_grey.jpg','Dimbula','2025-08-05 09:45:44','2025-08-05 09:45:44'),(32,'Green Tea Sencha Style','Ceylon green tea processed in Japanese style',20.00,75,2,'images/green_tea.jpg','Uva','2025-08-05 09:45:44','2025-08-05 09:45:44'),(33,'White Tea Silver Tips','Premium white tea with delicate flavor',45.00,40,2,'images/white_tea.jpg','Nuwara Eliya','2025-08-05 09:45:44','2025-08-05 09:45:44');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `user_type` enum('ADMIN','CUSTOMER') DEFAULT 'CUSTOMER',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin123','admin@thambapanni.lk','System Administrator',NULL,NULL,'ADMIN','2025-08-05 09:10:43','2025-08-05 09:10:43'),(2,'customer1','password123','customer1@email.com','Saman Perera','+94771234567','123 Galle Road, Colombo 03','CUSTOMER','2025-08-05 09:10:43','2025-08-05 09:10:43'),(6,'sahan','sahan5678','sahanperera@gmail.com','sahan perera','0765789345','sfyuyuy','CUSTOMER','2025-08-05 09:43:08','2025-08-05 09:43:08');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'testsl'
--

--
-- Dumping routines for database 'testsl'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-03 20:34:26
