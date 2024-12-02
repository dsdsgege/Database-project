-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Gép: 127.0.0.1
-- Létrehozás ideje: 2024. Nov 30. 12:12
-- Kiszolgáló verziója: 10.4.32-MariaDB
-- PHP verzió: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Adatbázis: `teamsports`
--

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `csapat`
--

CREATE TABLE `csapat` (
  `csapat_id` int(11) NOT NULL,
  `nev` varchar(50) NOT NULL,
  `varos` varchar(50) DEFAULT NULL,
  `alapitas_eve` date NOT NULL,
  `felhasznalonev` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `csapat`
--

INSERT INTO `csapat` (`csapat_id`, `nev`, `varos`, `alapitas_eve`, `felhasznalonev`) VALUES
(1, 'Fradi SE', 'Budapest', '1990-01-01', 'user01'),
(2, 'Újpest FC', 'Budapest', '1992-06-15', 'user02'),
(3, 'Debrecen VSC', 'Debrecen', '1985-08-20', 'user03'),
(4, 'Paks SC', 'Paks', '2000-03-10', 'user04'),
(5, 'Honvéd FC', 'Kispest', '1989-12-25', 'user05'),
(6, 'Kistelek FC', 'Kistelek', '2005-01-01', 'barni01');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `felhasznalo`
--

CREATE TABLE `felhasznalo` (
  `felhasznalonev` varchar(50) NOT NULL,
  `nev` varchar(50) NOT NULL,
  `jelszo` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `felhasznalo`
--

INSERT INTO `felhasznalo` (`felhasznalonev`, `nev`, `jelszo`) VALUES
('', 'barni', ''),
('asd', 'user09', 'asd'),
('asd1', 'asd', 'asd'),
('barni01', 'Szabó Barnabás', 'asd'),
('sara01', 'Czakó Sára', 'asd'),
('sara02', 'Czakó Sára', 'asd'),
('user01', 'Kovács Péter', 'jelszo123!'),
('user02', 'Nagy Éva', 'titok123#'),
('user03', 'Tóth Ádám', 'securePASS1'),
('user04', 'Szabó László', 'mypass2023'),
('user05', 'Kiss Judit', 'password12'),
('user09', 'asdf', 'asd');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `merkozes`
--

CREATE TABLE `merkozes` (
  `merkozes_id` int(11) NOT NULL,
  `eredmeny` varchar(50) NOT NULL,
  `datum` date NOT NULL,
  `helyszin` varchar(50) NOT NULL,
  `felhasznalonev` varchar(50) NOT NULL,
  `nyertes_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `merkozes`
--

INSERT INTO `merkozes` (`merkozes_id`, `eredmeny`, `datum`, `helyszin`, `felhasznalonev`, `nyertes_id`) VALUES
(1, '2-1', '2024-01-15', 'Budapest', 'user01', 1),
(2, '3-3', '2024-02-10', 'Debrecen', 'user02', 3),
(3, '1-0', '2024-03-12', 'Paks', 'user03', 3),
(4, '4-2', '2024-04-18', 'Kispest', 'user04', 4),
(5, '0-0', '2024-05-22', 'Győr', 'user05', 1);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `merkozik`
--

CREATE TABLE `merkozik` (
  `csapat_id` int(11) NOT NULL,
  `merkozes_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `merkozik`
--

INSERT INTO `merkozik` (`csapat_id`, `merkozes_id`) VALUES
(1, 1),
(1, 5),
(2, 1),
(2, 2),
(3, 2),
(3, 3),
(4, 3),
(4, 4),
(5, 4),
(5, 5);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `tag`
--

CREATE TABLE `tag` (
  `nev` varchar(50) NOT NULL,
  `szuletesi_datum` date NOT NULL,
  `poszt` varchar(50) DEFAULT NULL,
  `felhasznalonev` varchar(50) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `csapat_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `tag`
--

INSERT INTO `tag` (`nev`, `szuletesi_datum`, `poszt`, `felhasznalonev`, `tag_id`, `csapat_id`) VALUES
('Kiss Judit', '1987-09-10', 'védő', 'user05', 1, 5),
('Kovács Péter', '1990-03-25', 'kapus', 'user01', 2, 1),
('Nagy Éva', '1988-11-14', 'védő', 'user02', 3, 2),
('Szabó László', '1992-02-17', 'csatár', 'user04', 4, 4),
('Tóth Ádám', '1995-07-08', 'középpályás', 'user03', 5, 3),
('Balogh István', '1992-04-11', 'védő', 'barni01', 6, 5),
('Molnár Tamás', '1993-03-15', 'kapus', 'barni01', 7, 5),
('Szabó Márton', '1988-12-19', 'csatár', 'barni01', 8, 5),
('Kiss Attila', '1990-05-20', 'középpályás', 'barni01', 9, 5),
('Tóth Dávid', '1994-07-11', 'védő', 'barni01', 10, 4),
('Varga Péter', '1989-02-05', 'kapus', 'barni01', 11, 4),
('Nagy Gábor', '1991-08-13', 'középpályás', 'barni01', 12, 4),
('Fekete Ádám', '1995-09-07', 'csatár', 'barni01', 13, 4),
('Papp László', '1993-11-22', 'védő', 'sara01', 14, 4),
('Horváth András', '1990-10-30', 'kapus', 'sara01', 15, 3),
('Kovács Zoltán', '1992-06-18', 'csatár', 'sara01', 16, 3),
('Takács Tamás', '1996-03-12', 'középpályás', 'sara01', 17, 3),
('Simon Bence', '1991-01-20', 'védő', 'sara01', 18, 3),
('Juhász Viktor', '1994-09-10', 'kapus', 'sara01', 19, 2),
('Sipos Levente', '1987-07-24', 'középpályás', 'sara01', 20, 2),
('Hegedűs Róbert', '1989-11-03', 'védő', 'sara01', 21, 2),
('Pál Krisztián', '1995-05-15', 'csatár', 'sara02', 22, 2),
('Somogyi Balázs', '1993-04-28', 'középpályás', 'sara02', 23, 1),
('Bognár Norbert', '1990-12-09', 'védő', 'sara02', 24, 1),
('Oláh Richárd', '1992-02-17', 'kapus', 'sara02', 25, 1);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `tag_allampolgarsag`
--

CREATE TABLE `tag_allampolgarsag` (
  `allampolgarsag` varchar(50) NOT NULL,
  `felhasznalonev` varchar(50) NOT NULL,
  `tag_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `tag_allampolgarsag`
--

INSERT INTO `tag_allampolgarsag` (`allampolgarsag`, `felhasznalonev`, `tag_id`) VALUES
('Horvát', 'user05', 1),
('Magyar', 'user01', 2),
('Magyar', 'user02', 3),
('Szlovák', 'user04', 4),
('Román', 'user03', 5),
('Román', 'user02', 3),
('magyar', 'barni01', 8),
('szlovák', 'barni01', 9),
('magyar', 'barni01', 10),
('román', 'barni01', 11),
('szlovák', 'barni01', 12),
('magyar', 'barni01', 13),
('szerb', 'sara01', 14),
('ukrán', 'sara01', 15),
('magyar', 'sara01', 16),
('horvát', 'sara01', 17),
('szlovén', 'sara01', 18),
('magyar', 'sara01', 19),
('román', 'sara01', 20),
('magyar', 'sara01', 21),
('szerb', 'sara01', 22),
('magyar', 'sara02', 23),
('szlovén', 'sara02', 24),
('horvát', 'sara02', 25),
('magyar', 'sara01', 20),
('román', 'barni01', 8),
('magyar', 'sara01', 15);

--
-- Indexek a kiírt táblákhoz
--

--
-- A tábla indexei `csapat`
--
ALTER TABLE `csapat`
  ADD PRIMARY KEY (`csapat_id`),
  ADD KEY `felhasznalonev` (`felhasznalonev`);

--
-- A tábla indexei `felhasznalo`
--
ALTER TABLE `felhasznalo`
  ADD PRIMARY KEY (`felhasznalonev`);

--
-- A tábla indexei `merkozes`
--
ALTER TABLE `merkozes`
  ADD PRIMARY KEY (`merkozes_id`),
  ADD KEY `felhasznalonev` (`felhasznalonev`),
  ADD KEY `fk_nyertes_id` (`nyertes_id`);

--
-- A tábla indexei `merkozik`
--
ALTER TABLE `merkozik`
  ADD PRIMARY KEY (`csapat_id`,`merkozes_id`),
  ADD KEY `fk_merkozes_id` (`merkozes_id`);

--
-- A tábla indexei `tag`
--
ALTER TABLE `tag`
  ADD PRIMARY KEY (`tag_id`),
  ADD KEY `felhasznalonev` (`felhasznalonev`),
  ADD KEY `fk` (`csapat_id`);

--
-- A tábla indexei `tag_allampolgarsag`
--
ALTER TABLE `tag_allampolgarsag`
  ADD KEY `felhasznalonev` (`felhasznalonev`),
  ADD KEY `pk` (`tag_id`);

--
-- Megkötések a kiírt táblákhoz
--

--
-- Megkötések a táblához `csapat`
--
ALTER TABLE `csapat`
  ADD CONSTRAINT `csapat_ibfk_1` FOREIGN KEY (`felhasznalonev`) REFERENCES `felhasznalo` (`felhasznalonev`);

--
-- Megkötések a táblához `merkozes`
--
ALTER TABLE `merkozes`
  ADD CONSTRAINT `fk_nyertes_id` FOREIGN KEY (`nyertes_id`) REFERENCES `csapat` (`csapat_id`),
  ADD CONSTRAINT `merkozes_ibfk_1` FOREIGN KEY (`felhasznalonev`) REFERENCES `felhasznalo` (`felhasznalonev`);

--
-- Megkötések a táblához `merkozik`
--
ALTER TABLE `merkozik`
  ADD CONSTRAINT `fk_csapat_id` FOREIGN KEY (`csapat_id`) REFERENCES `csapat` (`csapat_id`),
  ADD CONSTRAINT `fk_merkozes_id` FOREIGN KEY (`merkozes_id`) REFERENCES `merkozes` (`merkozes_id`);

--
-- Megkötések a táblához `tag`
--
ALTER TABLE `tag`
  ADD CONSTRAINT `fk` FOREIGN KEY (`csapat_id`) REFERENCES `csapat` (`csapat_id`),
  ADD CONSTRAINT `tag_ibfk_1` FOREIGN KEY (`felhasznalonev`) REFERENCES `felhasznalo` (`felhasznalonev`);

--
-- Megkötések a táblához `tag_allampolgarsag`
--
ALTER TABLE `tag_allampolgarsag`
  ADD CONSTRAINT `pk` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`),
  ADD CONSTRAINT `tag_allampolgarsag_ibfk_1` FOREIGN KEY (`felhasznalonev`) REFERENCES `felhasznalo` (`felhasznalonev`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
