SELECT cp.Начало
FROM Спектакли, (SELECT MIN(Залы.Начало) AS Начало, Залы.Id_Спектакля
                 FROM Залы
                 GROUP BY Залы.Id_Спектакля) AS cp
WHERE Спектакли.Id = cp.Id_Спектакля
  AND Спектакли.Id = #