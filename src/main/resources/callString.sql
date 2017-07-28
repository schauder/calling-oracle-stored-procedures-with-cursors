CREATE OR REPLACE
PROCEDURE callString(pIn IN VARCHAR2, pOut OUT VARCHAR2) AS
  BEGIN
    pOut := pIn || ' h a l l o';
  END callString;
