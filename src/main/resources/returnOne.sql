CREATE OR REPLACE FUNCTION returnOne
  RETURN SYS_REFCURSOR
AS
  my_cursor SYS_REFCURSOR;
BEGIN
  OPEN my_cursor FOR SELECT * FROM dual;
  RETURN my_cursor;
END returnOne;