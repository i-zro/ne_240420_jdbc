CREATE OR REPLACE PROCEDURE SalesReportByBook
AS
BEGIN
  FOR r IN (SELECT b.bookname, COUNT(o.book_id) AS total_sales, SUM(o.amount_paid) AS total_revenue
            FROM BOOK b
            JOIN ORDERS o ON b.bookid = o.book_id
            GROUP BY b.bookname)
  LOOP
    DBMS_OUTPUT.PUT_LINE('도서명: ' || r.bookname || ', 판매량: ' || r.total_sales || ', 총 수익: ' || r.total_revenue);
  END LOOP;
END SalesReportByBook;