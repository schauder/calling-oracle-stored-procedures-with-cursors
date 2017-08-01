CREATE OR REPLACE
PROCEDURE callOne(p_recordset OUT SYS_REFCURSOR) AS
  BEGIN
    OPEN p_recordset FOR
    SELECT 1 as id
    FROM dual;
  END callOne;
