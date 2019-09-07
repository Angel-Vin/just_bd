package sample.Controllers.Manager;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import sample.Controllers.MysqlDB;
import sample.Main;
import sample.Utils;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ExpertiseConroller implements Initializable {

    public ComboBox experts_names;
    public ComboBox cars_ids;

    public void back(ActionEvent actionEvent) {
        Main.setRoot("views/Manager/ManagerWindow.fxml", "Подсистема менеджера");
    }

    public void confirm(ActionEvent actionEvent) {
        if (!checkInputs()) {
            Utils.showMessageWindow("Недостаточно данных для экспертизы");
            return;
        }
        Utils.showMessageWindow("Эксперт был успешно назначен");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResultSet rs = MysqlDB.execQuery("SELECT last_name FROM Expert");
        try {
            while (rs.next()) {
                experts_names.getItems().add(rs.getString("last_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        experts_names.getSelectionModel().selectFirst();
        rs = MysqlDB.execQuery("SELECT id FROM Car");
        try {
            while (rs.next()) {
                cars_ids.getItems().add(rs.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cars_ids.getSelectionModel().selectFirst();
    }

    private boolean checkInputs () {
        return cars_ids.getItems().size() != 0 && experts_names.getItems().size() != 0;
    }
}
