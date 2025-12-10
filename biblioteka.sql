-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Dec 10, 2025 at 06:31 PM
-- Server version: 8.0.31
-- PHP Version: 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `biblioteka`
--

-- --------------------------------------------------------

--
-- Table structure for table `autori`
--

DROP TABLE IF EXISTS `autori`;
CREATE TABLE IF NOT EXISTS `autori` (
  `ID_AUTORA` int NOT NULL AUTO_INCREMENT,
  `IME_AUTORA` varchar(100) NOT NULL,
  `PREZIME_AUTORA` varchar(100) NOT NULL,
  `GODINA_RODJENJA` date DEFAULT NULL,
  PRIMARY KEY (`ID_AUTORA`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `autori`
--

INSERT INTO `autori` (`ID_AUTORA`, `IME_AUTORA`, `PREZIME_AUTORA`, `GODINA_RODJENJA`) VALUES
(1, 'Ivo', 'Andrić', '1892-10-10'),
(2, 'Meša', 'Selimović', '1910-04-26'),
(3, 'Dositej', 'Obradović', '1892-02-17'),
(4, 'Anton', 'Čehov', '2024-07-04'),
(10, 'Fjodor', 'Dostojevski', '2024-07-10'),
(7, 'Branko', 'Ćopić', '2024-07-23'),
(23, 'Janko', 'Veselinović', '2024-08-14'),
(24, 'Lorens', 'Bergrin', '2024-08-13');

-- --------------------------------------------------------

--
-- Table structure for table `bibliotekar`
--

DROP TABLE IF EXISTS `bibliotekar`;
CREATE TABLE IF NOT EXISTS `bibliotekar` (
  `ID_BIBLIOTEKARA` int NOT NULL AUTO_INCREMENT,
  `IME` varchar(50) NOT NULL,
  `PREZIME` varchar(50) NOT NULL,
  `KORISNICKO_IME` varchar(50) NOT NULL,
  `LOZINKA` varchar(20) NOT NULL,
  PRIMARY KEY (`ID_BIBLIOTEKARA`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `bibliotekar`
--

INSERT INTO `bibliotekar` (`ID_BIBLIOTEKARA`, `IME`, `PREZIME`, `KORISNICKO_IME`, `LOZINKA`) VALUES
(1, 'Ivana', 'Petrović', 'ivanapetrovic', '123'),
(2, 'Anđela', 'Kalinić', 'andjelakalinic', '456'),
(4, 'Ivona', 'Coric', 'ive', '123');

-- --------------------------------------------------------

--
-- Table structure for table `izdavaci`
--

DROP TABLE IF EXISTS `izdavaci`;
CREATE TABLE IF NOT EXISTS `izdavaci` (
  `ID_IZDAVACA` int NOT NULL AUTO_INCREMENT,
  `NAZIV_IZDAVACA` varchar(100) NOT NULL,
  `KONTAKT_TELEFON` varchar(30) DEFAULT NULL,
  `ID_MJESTA` int DEFAULT NULL,
  PRIMARY KEY (`ID_IZDAVACA`),
  KEY `ID_MJESTA` (`ID_MJESTA`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `izdavaci`
--

INSERT INTO `izdavaci` (`ID_IZDAVACA`, `NAZIV_IZDAVACA`, `KONTAKT_TELEFON`, `ID_MJESTA`) VALUES
(4, 'Laguna', '057/232-777', 10),
(2, 'Vulkan', '055/234-727', 1),
(5, 'BuyBook', '057/222-888', 3);

-- --------------------------------------------------------

--
-- Table structure for table `izdavanje`
--

DROP TABLE IF EXISTS `izdavanje`;
CREATE TABLE IF NOT EXISTS `izdavanje` (
  `ID_IZDAVANJA` int NOT NULL AUTO_INCREMENT,
  `DATUM_IZDAVANJA` date NOT NULL,
  `DATUM_VRACANJA` date NOT NULL,
  `ID_KORISNIKA` int DEFAULT NULL,
  `ID_KNJIGE` int DEFAULT NULL,
  `ID_BIBLIOTEKARA` int DEFAULT NULL,
  PRIMARY KEY (`ID_IZDAVANJA`),
  KEY `ID_KORISNIKA` (`ID_KORISNIKA`),
  KEY `ID_KNJIGE` (`ID_KNJIGE`),
  KEY `ID_BIBLIOTEKARA` (`ID_BIBLIOTEKARA`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `izdavanje`
--

INSERT INTO `izdavanje` (`ID_IZDAVANJA`, `DATUM_IZDAVANJA`, `DATUM_VRACANJA`, `ID_KORISNIKA`, `ID_KNJIGE`, `ID_BIBLIOTEKARA`) VALUES
(5, '2024-07-21', '2024-06-13', 1, 4, 2),
(2, '2023-09-12', '2023-10-12', 1, 5, 2),
(3, '2024-07-10', '2024-08-11', 3, 11, 1),
(8, '2024-07-28', '2024-08-28', 5, 11, 1);

-- --------------------------------------------------------

--
-- Table structure for table `knjige`
--

DROP TABLE IF EXISTS `knjige`;
CREATE TABLE IF NOT EXISTS `knjige` (
  `ID_KNJIGE` int NOT NULL AUTO_INCREMENT,
  `NAZIV_KNJIGE` varchar(100) NOT NULL,
  `GODINA_IZDANJA` date NOT NULL,
  `BROJ_DOSTUPNIH_KNJIGA` int NOT NULL,
  `UKUPAN_BROJ_KNJIGA` int NOT NULL,
  `ID_AUTORA` int DEFAULT NULL,
  `ID_ZANRA` int DEFAULT NULL,
  `ID_IZDAVACA` int DEFAULT NULL,
  PRIMARY KEY (`ID_KNJIGE`),
  KEY `ID_AUTORA` (`ID_AUTORA`),
  KEY `ID_ZANRA` (`ID_ZANRA`),
  KEY `ID_IZDAVACA` (`ID_IZDAVACA`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `knjige`
--

INSERT INTO `knjige` (`ID_KNJIGE`, `NAZIV_KNJIGE`, `GODINA_IZDANJA`, `BROJ_DOSTUPNIH_KNJIGA`, `UKUPAN_BROJ_KNJIGA`, `ID_AUTORA`, `ID_ZANRA`, `ID_IZDAVACA`) VALUES
(5, 'Na Drini ćuprija', '2024-07-04', 14, 15, 1, 1, 4),
(4, 'Priče', '2024-07-11', 11, 12, 4, 2, 5),
(11, 'Orlovi rano lete', '2024-07-12', 18, 20, 7, 1, 4);

-- --------------------------------------------------------

--
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
CREATE TABLE IF NOT EXISTS `korisnik` (
  `ID_KORISNIKA` int NOT NULL AUTO_INCREMENT,
  `IME` varchar(100) NOT NULL,
  `PREZIME` varchar(100) NOT NULL,
  `EMAIL` varchar(100) NOT NULL,
  `TELEFON` varchar(25) DEFAULT NULL,
  `ULICA_I_BROJ` varchar(100) DEFAULT NULL,
  `ID_MJESTA` int DEFAULT NULL,
  PRIMARY KEY (`ID_KORISNIKA`),
  KEY `ID_MJESTA` (`ID_MJESTA`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `korisnik`
--

INSERT INTO `korisnik` (`ID_KORISNIKA`, `IME`, `PREZIME`, `EMAIL`, `TELEFON`, `ULICA_I_BROJ`, `ID_MJESTA`) VALUES
(1, 'Petar', 'Petrović', 'petarp@gmail.com', '055/123-456', 'Vidovdanska 15', 1),
(2, 'Marko', 'Marković', 'markom@gmail.com', '053/789-023', 'Beogradska 20', 2),
(3, 'Ana', 'Antić', 'anaantic@gmail.com', '059/444-988', 'Njegoševa 18', 3),
(5, 'Marija', 'Mijatović', 'marijamijatovic@gmail.com', '066/444-332', 'Paje Jovanovića 3333', 10);

-- --------------------------------------------------------

--
-- Table structure for table `mjesto`
--

DROP TABLE IF EXISTS `mjesto`;
CREATE TABLE IF NOT EXISTS `mjesto` (
  `ID_MJESTA` int NOT NULL AUTO_INCREMENT,
  `NAZIV_MJESTA` varchar(100) NOT NULL,
  `POSTANSKI_BROJ` int NOT NULL,
  PRIMARY KEY (`ID_MJESTA`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `mjesto`
--

INSERT INTO `mjesto` (`ID_MJESTA`, `NAZIV_MJESTA`, `POSTANSKI_BROJ`) VALUES
(1, 'Bijeljina', 76300),
(2, 'Doboj', 74000),
(3, 'Trebinje', 89000),
(10, 'Istočno Sarajevo', 71123),
(11, 'Sarajevo', 71123),
(22, 'rrr', 444),
(23, 'mm', 44),
(24, 'Zenuica', 3242),
(25, 'nht', 6666),
(26, 'Tuzla', 333333123),
(27, 'Tunguyija', 241),
(28, 'sss', 24444),
(29, 'gg', 4523);

-- --------------------------------------------------------

--
-- Table structure for table `zanr`
--

DROP TABLE IF EXISTS `zanr`;
CREATE TABLE IF NOT EXISTS `zanr` (
  `ID_ZANRA` int NOT NULL AUTO_INCREMENT,
  `NAZIV_ZANRA` varchar(50) NOT NULL,
  PRIMARY KEY (`ID_ZANRA`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `zanr`
--

INSERT INTO `zanr` (`ID_ZANRA`, `NAZIV_ZANRA`) VALUES
(1, 'Roman'),
(2, 'Triler'),
(15, 'Pripovetka');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
