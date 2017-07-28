CREATE OR REPLACE
PROCEDURE get_from_dual (p_recordset OUT SYS_REFCURSOR) AS
BEGIN
  OPEN p_recordset FOR
    SELECT *
    FROM   dual;
END get_from_dual;