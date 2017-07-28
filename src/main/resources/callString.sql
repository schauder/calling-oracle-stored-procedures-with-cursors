CREATE OR REPLACE
PROCEDURE callString(text IN OUT VARCHAR2) AS
  BEGIN
    text := text || 'hello';
  END callString;
