import java.sql.*;

public class tTotalSpentByCustomerTes {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        // 고객 ID를 파라미터로 설정하고 프로시저를 호출하는 SQL 구문
        String sql = "{call TotalSpentByCustomer(?)}"; // 프로시저 이름과 파라미터

        try {
            // Oracle JDBC Driver 로드
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // 데이터베이스 연결
            try (Connection con = DriverManager.getConnection(url, "c##madang", "madang");
                 // CallableStatement 생성
                 CallableStatement cstmt = con.prepareCall(sql)) {

                // 고객 ID 설정 (예: 1번 고객)
                cstmt.setInt(1, 2); // 이 부분을 다른 고객 ID로 변경하여 사용 가능

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
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
