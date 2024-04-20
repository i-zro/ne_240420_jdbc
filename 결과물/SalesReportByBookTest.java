import java.sql.*;

public class SalesReportByBookTest {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        // SalesReportByBook 프로시저 호출 구문
        String sql = "{call SalesReportByBook()}";

        try {
            // Oracle JDBC Driver 로드
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // 데이터베이스 연결
            try (Connection con = DriverManager.getConnection(url, "c##madang", "madang");
                 // CallableStatement 생성
                 CallableStatement cstmt = con.prepareCall(sql)) {

                // 서버 출력을 활성화
                try (Statement stmt = con.createStatement()) {
                    stmt.execute("BEGIN DBMS_OUTPUT.ENABLE(); END;");
                }

                // 프로시저 실행
                cstmt.execute();

                // DBMS_OUTPUT에서 결과 가져오기
                try (CallableStatement cstmt2 = con.prepareCall("{call dbms_output.get_line(?,?)}")) {
                    cstmt2.registerOutParameter(1, Types.VARCHAR);
                    cstmt2.registerOutParameter(2, Types.INTEGER);

                    String line;
                    int status;
                    do {
                        cstmt2.execute();
                        line = cstmt2.getString(1);
                        status = cstmt2.getInt(2);
                        if (line != null) {
                            System.out.println(line);
                        }
                    } while (status == 0);
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database connection failure: " + e.getMessage());
        }
    }
}
