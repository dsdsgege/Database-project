-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Gép: 127.0.0.1
-- Létrehozás ideje: 2024. Nov 20. 23:23
-- Kiszolgáló verziója: 10.4.32-MariaDB
-- PHP verzió: 8.0.30

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
(5, 'Honvéd FC', 'Kispest', '1989-12-25', 'user05');

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
('asd', 'user09', 'asd'),
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
-- Tábla szerkezet ehhez a táblához `jatszik`
--

CREATE TABLE `jatszik` (
  `nev` varchar(50) NOT NULL,
  `szuletesi_datum` date NOT NULL,
  `csapat_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `jatszik`
--

INSERT INTO `jatszik` (`nev`, `szuletesi_datum`, `csapat_id`) VALUES
('Kiss Judit', '1987-09-10', 5),
('Kovács Péter', '1990-03-25', 1),
('Nagy Éva', '1988-11-14', 2),
('Szabó László', '1992-02-17', 4),
('Tóth Ádám', '1995-07-08', 3);

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
  `nyertes_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- A tábla adatainak kiíratása `merkozes`
--

INSERT INTO `merkozes` (`merkozes_id`, `eredmeny`, `datum`, `helyszin`, `felhasznalonev`, `nyertes_id`) VALUES
(1, '2-1', '2024-01-15', 'Budapest', 'user01', 0),
(2, '3-3', '2024-02-10', 'Debrecen', 'user02', 0),
(3, '1-0', '2024-03-12', 'Paks', 'user03', 0),
(4, '4-2', '2024-04-18', 'Kispest', 'user04', 0),
(5, '0-0', '2024-05-22', 'Győr', 'user05', 0);

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
(2, 2),
(3, 3),
(4, 4),
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
('Tóth Ádám', '1995-07-08', 'középpályás', 'user03', 5, 3);

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
('Román', 'user02', 3);

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
-- A tábla indexei `jatszik`
--
ALTER TABLE `jatszik`
  ADD PRIMARY KEY (`nev`,`szuletesi_datum`,`csapat_id`);

--
-- A tábla indexei `merkozes`
--
ALTER TABLE `merkozes`
  ADD PRIMARY KEY (`merkozes_id`),
  ADD KEY `felhasznalonev` (`felhasznalonev`);

--
-- A tábla indexei `merkozik`
--
ALTER TABLE `merkozik`
  ADD PRIMARY KEY (`csapat_id`,`merkozes_id`);

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
  ADD CONSTRAINT `merkozes_ibfk_1` FOREIGN KEY (`felhasznalonev`) REFERENCES `felhasznalo` (`felhasznalonev`);

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
