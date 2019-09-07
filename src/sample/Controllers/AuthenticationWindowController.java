package sample.Controllers;

//import com.sun.webkit.network.Util;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import sample.Main;
import sample.Utils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AuthenticationWindowController {

    public TextField tfPass;
    public TextField tfLogin;

    public void back(ActionEvent actionEvent) throws IOException {
        Main.setRoot("views/sample.fxml", "Главное меню");
    }

    public void login(ActionEvent actionEvent) throws IOException {
        if (tfPass.getLength() == 0 || tfLogin.getLength() == 0) {
            Utils.showMessageWindow("Пустой логин или/и пароль");
            return;
        }
        String login = tfLogin.getText(), pass = tfPass.getText();
        //System.out.println("SELECT status FROM User WHERE name='" + login + "' AND password='" + pass + "'");
        ResultSet rs = MysqlDB.execQuery("SELECT status FROM User WHERE name='" + login + "' AND password='" + pass + "'");
        String status = null;
        try {
            if (!rs.next()) {
                Utils.showMessageWindow("Некорректный логин или/и пароль");
                return;
            }
            status = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Objects.requireNonNull(status).compareTo("Admin") == 0) {
            Main.setRoot("views/AdminWindow.fxml", "Подсистема администратора");
        } else if (status.compareTo("Manager") == 0) {
            Main.setRoot("views/Manager/ManagerWindow.fxml", "Подсистема менеджера");
        }
    }
}
