SELECT COUNT(*)
FROM Спектакли, Места, Залы
WHERE Спектакли.Id = Залы.Id_Спектакля
  AND Залы.Id = Места.Id_Зала
  AND Залы.Начало IN (SELECT MIN(Залы.Начало) AS Начало
                      FROM Залы
                      GROUP BY Залы.Id_Спектакля)
  AND Места.Резерв = 'True'
  AND Залы.Начало::DATE >= '#'
  AND Залы.Начало::DATE <= '#'