SELECT
CONCAT(YEAR(BH.at),"-" ,MONTH(BH.at)) AS "id",
CONCAT(YEAR(BH.at),"-" ,MONTH(BH.at)) AS "date",
SUM(BH.duration) AS "duration"
FROM build_history BH
WHERE {{condition}}
GROUP BY CONCAT(YEAR(BH.at),"-" ,MONTH(BH.at)), YEAR(BH.at), MONTH(BH.at)
ORDER BY YEAR(BH.at), MONTH(BH.at) ASC