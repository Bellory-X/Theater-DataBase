-- залы по времени
CREATE OR REPLACE FUNCTION insert_from_halls() RETURNS TRIGGER AS $Theaters$
	BEGIN
		IF 0 < (SELECT COUNT(*)
				FROM Залы
				WHERE Залы.Название = NEW.Название
				AND (NEW.Начало, NEW.Конец) OVERLAPS (Залы.Начало, Залы.Конец)
			   )
		THEN
			RAISE EXCEPTION 'An entry in a table interects with another';
		END IF;
		RETURN NEW;
	END;
$Theaters$ LANGUAGE plpgsql;

CREATE TRIGGER view_insert_from_halls
	BEFORE INSERT 
	ON Залы
    FOR EACH ROW EXECUTE PROCEDURE insert_from_halls();

-- актеры в спектакли без повторений
CREATE OR REPLACE FUNCTION insert_from_actors_roles() RETURNS TRIGGER AS $Theaters$
	BEGIN
		IF EXISTS (SELECT 1
				   FROM Роли
				   WHERE Роли.Id = NEW.Id_Роли
				   AND Роли.Id_Спектакля IN (SELECT Роли.Id_Спектакля
											 FROM Роли, Роли_Актеры
											 WHERE Роли_Актеры.Id_Роли = Роли.Id
											 AND Роли_Актеры.Id_Служащего = NEW.Id_Служащего))
		THEN
			RAISE EXCEPTION 'Actor is already in the play';
		END IF;
		RETURN NEW;
	END;
$Theaters$ LANGUAGE plpgsql;

CREATE TRIGGER view_insert_from_actors_roles
	BEFORE INSERT 
	ON Роли_Актеры
    FOR EACH ROW EXECUTE PROCEDURE insert_from_actors_roles();

