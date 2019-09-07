package sample.Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlDB {

    private static Connection con;
    private static Statement st;
    public static void setConnection(String url, String user, String password) {
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static ResultSet execQuery(String query) {
        ResultSet rs = null;
        try {
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static int execUpdate(String query) {
        int result = 0;
        try {
            result = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void updateAI(String tableName) {
        execUpdate("SET  @num := 0;");
        execUpdate("UPDATE " + tableName + " SET id = @num := (@num+1);");
        execUpdate("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1;");
    }


}
