CREATE OR REPLACE
PROCEDURE callOne (p_recordset OUT SYS_REFCURSOR) AS
BEGIN
  OPEN p_recordset FOR
    SELECT *
    FROM   dual;
END callOne;