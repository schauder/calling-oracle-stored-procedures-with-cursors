CREATE OR REPLACE
PROCEDURE callTwo(rs1 OUT SYS_REFCURSOR, rs2 OUT SYS_REFCURSOR) AS
  BEGIN
    OPEN rs1 FOR
    SELECT *
    FROM dual;

    OPEN rs2 FOR
    SELECT
      1,
      2,
      "Hello"
    FROM dual
    UNION ALL
    SELECT
      3,
      4,
      "Hola"
    FROM dual;


  END callTwo;