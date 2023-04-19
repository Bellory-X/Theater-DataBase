CREATE TABLE Театры
(
	Название CHARACTER VARYING(30) PRIMARY KEY
)

CREATE TABLE Абонементы
(
	Id SERIAL PRIMARY KEY,
	Колличество INTEGER NOT NULL CHECK(Цена > 0),
	Цена INTEGER NOT NULL CHECK(Цена > 0)
)

CREATE TABLE Залы
(
	Id SERIAL PRIMARY KEY,
	Начало TIMESTAMP NOT NULL,
	Конец TIMESTAMP NOT NULL,
	Название CHARACTER VARYING(30) NOT NULL,
	Id_Спектакля INTEGER NOT NULL REFERENCES Спектакли(Id) ON DELETE CASCADE,
	UNIQUE(Название, Id_Спектакля)
)

CREATE TABLE Места
(
	Id SERIAL PRIMARY KEY,
	Номер INTEGER NOT NULL,
	Id_Зала INTEGER NOT NULL REFERENCES Залы(Id) ON DELETE CASCADE,
	Цена INTEGER CHECK(Цена >= 0) DEFAULT 0,
	Резерв BOOLEAN DEFAULT 'False',
	Id_Абонемента INTEGER REFERENCES Абонементы(Id) ON DELETE SET NULL,
	UNIQUE(Номер, Id_Зала)
)

CREATE TABLE Служащие
(
	Id SERIAL PRIMARY KEY,
	ФИО CHARACTER VARYING(30) NOT NULL,
	Опыт INTEGER DEFAULT 0 CHECK(Опыт >= 0),
	Пол CHARACTER(30),
	Дата_рождения DATE,
	Колличество_Детей INTEGER DEFAULT 0 CHECK(Колличество_Детей >= 0),
	Зарплата INTEGER DEFAULT 0 CHECK(Зарплата >= 0),
	Является_Работником BOOLEAN NOT NULL,
	Театр CHARACTER VARYING(30) REFERENCES Театры(Название) ON DELETE SET NULL
)

CREATE TABLE Категории_Актеров
(
	Категория CHARACTER VARYING(30) PRIMARY KEY
)

CREATE TABLE Категории_Постановщиков
(
	Категория CHARACTER VARYING(30) PRIMARY KEY
)

CREATE TABLE Категории_Музыкантов
(
	Категория CHARACTER VARYING(30) PRIMARY KEY
)

CREATE TABLE Категории_Работников
(
	Категория CHARACTER VARYING(30) PRIMARY KEY
)

CREATE TABLE Актеры
(
	Id_Служащего INTEGER PRIMARY KEY REFERENCES Служащие(Id) ON DELETE CASCADE,
	Категория CHARACTER VARYING(30) REFERENCES Категории_Актеров(Категория) ON UPDATE CASCADE ON DELETE SET NULL
)

CREATE TABLE Постановщики
(
	Id_Служащего INTEGER PRIMARY KEY REFERENCES Служащие(Id) ON DELETE CASCADE,
	Категория CHARACTER VARYING(30) REFERENCES Категории_Постановщиков(Категория) ON UPDATE CASCADE ON DELETE SET NULL
)

CREATE TABLE Музыканты
(
	Id_Служащего INTEGER PRIMARY KEY REFERENCES Служащие(Id) ON DELETE CASCADE,
	Категория CHARACTER VARYING(30) REFERENCES Категории_Музыкантов(Категория) ON UPDATE CASCADE ON DELETE SET NULL
)

CREATE TABLE Работники
(
	Id_Служащего INTEGER PRIMARY KEY REFERENCES Служащие(Id) ON DELETE CASCADE,
	Категория CHARACTER VARYING(30) REFERENCES Категории_Работников(Категория) ON UPDATE CASCADE ON DELETE SET NULL
)

CREATE TABLE Характеристики_Актеров
(
	Характеристика CHARACTER VARYING(30) PRIMARY KEY
)

CREATE TABLE Характеристики_Музыкантов
(
	Характеристика CHARACTER VARYING(30) PRIMARY KEY
)

CREATE TABLE Характеристики_Постановщиков
(
	Характеристика CHARACTER VARYING(30) PRIMARY KEY
)

CREATE TABLE Характеристики_Работников
(
	Характеристика CHARACTER VARYING(30) PRIMARY KEY
)

CREATE TABLE Актеры_И_Их_Характерискики
(
	Id_Служащего INTEGER NOT NULL REFERENCES Актеры(Id_Служащего) ON DELETE CASCADE,
	Характеристика CHARACTER VARYING(30) NOT NULL REFERENCES Характеристики_Актеров(Характеристика) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(Id_Служащего, Характеристика)
)

CREATE TABLE Постановщики_И_Их_Характерискики
(
	Id_Служащего INTEGER NOT NULL REFERENCES Постановщики(Id_Служащего) ON DELETE CASCADE,
	Характеристика CHARACTER VARYING(30) NOT NULL REFERENCES Характеристики_Постановщиков(Характеристика) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(Id_Служащего, Характеристика)
)

CREATE TABLE Музыканты_И_Их_Характерискики
(
	Id_Служащего INTEGER NOT NULL REFERENCES Музыканты(Id_Служащего) ON DELETE CASCADE,
	Характеристика CHARACTER VARYING(30) NOT NULL REFERENCES Характеристики_Музыкантов(Характеристика) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(Id_Служащего, Характеристика)
)

CREATE TABLE Работники_И_Их_Характерискики
(
	Id_Служащего INTEGER NOT NULL REFERENCES Музыканты(Id_Служащего) ON DELETE CASCADE,
	Характеристика CHARACTER VARYING(30) NOT NULL REFERENCES Характеристики_Работников(Характеристика) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(Id_Служащего, Характеристика)
)

CREATE TABLE Звания
(
	Id SERIAL PRIMARY KEY,
	Название CHARACTER VARYING(30) NOT NULL,
	Конкурс CHARACTER VARYING(30) NOT NULL,
	Дата DATE NOT NULL
)

CREATE TABLE Актеры_И_Их_Звания
(
	Id_Служащего INTEGER NOT NULL REFERENCES Актеры(Id_Служащего) ON DELETE CASCADE,
	Id_Звания INTEGER NOT NULL REFERENCES Звания(Id) ON DELETE CASCADE,
	PRIMARY KEY(Id_Служащего, Id_Звания)
)

CREATE TABLE Авторы
(
	Id SERIAL PRIMARY KEY,
	ФИО CHARACTER VARYING(30) NOT NULL,
	Дата_Рождения DATE,
	Страна CHARACTER VARYING(30)
)

CREATE TABLE Жанры
(
	Название CHARACTER VARYING(30) PRIMARY KEY
)

CREATE TABLE Пьесы
(
	Id SERIAL PRIMARY KEY,
	Название CHARACTER VARYING(30) NOT NULL,
	Дата DATE,
	Рейтинг INTEGER NOT NULL CHECK(Рейтинг >= 0),
	Жанр CHARACTER VARYING(30) NOT NULL REFERENCES Жанры(Название) ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE Пьесы_И_Их_Авторы
(
	Id_Автора INTEGER NOT NULL REFERENCES Авторы(Id) ON DELETE CASCADE,
	Id_Пьесы INTEGER NOT NULL REFERENCES Пьесы(Id) ON DELETE CASCADE,
	PRIMARY KEY(Id_Автора, Id_Пьесы)
)

CREATE TABLE Репертуары
(
	Id SERIAL PRIMARY KEY,
	Номер INTEGER NOT NULL,
	Театр CHARACTER VARYING(30) REFERENCES Театры(Название) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE(Номер, Театр)
)

CREATE TABLE Спектакли
(
	Id SERIAL PRIMARY KEY,
	Id_Пьесы INTEGER REFERENCES Пьесы(Id) ON DELETE SET NULL,
	Id_Репертуара INTEGER REFERENCES Репертуары(Id) ON DELETE SET NULL,
	Цена INTEGER NOT NULL,
	От_Театра CHARACTER VARYING(30) REFERENCES Театры(Название) ON UPDATE CASCADE ON DELETE SET NULL
)

CREATE TABLE Спектакли_И_Их_Постановщики
(
	Id_Спектакля INTEGER NOT NULL REFERENCES Спектакли(Id) ON DELETE CASCADE,
	Id_Служащего INTEGER NOT NULL REFERENCES Постановщики(Id_Служащего) ON DELETE CASCADE,
	PRIMARY KEY(Id_Спектакля, Id_Служащего)
)

CREATE TABLE Роли
(
	Id SERIAL PRIMARY KEY,
	Название CHARACTER VARYING(30) NOT NULL,
	Главная BOOLEAN NOT NULL,
	Дублер BOOLEAN NOT NULL,
	Id_Спектакля INTEGER NOT NULL REFERENCES Спектакли(Id) ON DELETE CASCADE
)

CREATE TABLE Роли_Актеры
(
	Id_Роли INTEGER NOT NULL REFERENCES Роли(Id) ON DELETE CASCADE,
	Id_Служащего INTEGER NOT NULL REFERENCES Актеры(Id_Служащего) ON DELETE CASCADE,
	PRIMARY KEY(Id_Роли, Id_Служащего)
)

CREATE TABLE Роли_И_Их_Характерискики
(
	Id_Роли INTEGER NOT NULL REFERENCES Роли(Id) ON DELETE CASCADE,
	Характеристика CHARACTER VARYING(30) NOT NULL REFERENCES Характеристики_Актеров(Характеристика) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(Id_Роли, Характеристика)
)

CREATE TABLE Спектакли_И_Их_Музыканты
(
	Id_Служащего INTEGER NOT NULL REFERENCES Музыканты(Id_Служащего) ON DELETE CASCADE,
	Id_Спектакля INTEGER NOT NULL REFERENCES Спектакли(Id) ON DELETE CASCADE,
	PRIMARY KEY(Id_Служащего, Id_Спектакля)
)


