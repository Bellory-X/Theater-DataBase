SELECT COALESCE(one.s, 0) + COALESCE(two.s, 0)
FROM (SELECT SUM(Места.Цена + Спектакли.Цена) AS s
      FROM Спектакли, Залы, Места
      WHERE Спектакли.Id = Залы.Id_Спектакля
        AND Залы.Id = Места.Id_Зала
        AND Места.Резерв = 'True'
        AND Места.Id_Абонемента IS NULL
        AND Залы.Начало::DATE >= '#'
        AND Залы.Начало::DATE <= '#'
        AND Спектакли.Id = #) AS one,
     (SELECT SUM(Абонементы.Цена / Абонементы.Колличество) AS s
      FROM Спектакли, Залы, Места, Абонементы
      WHERE Спектакли.Id = Залы.Id_Спектакля
        AND Залы.Id = Места.Id_Зала
        AND Места.Id_Абонемента = Абонементы.Id
        AND Залы.Начало::DATE >= '#'
        AND Залы.Начало::DATE <= '#'
        AND Спектакли.Id = #) AS two;