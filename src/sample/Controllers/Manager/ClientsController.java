package sample.Controllers.Manager;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import sample.Controllers.Admin.StaffController;
import sample.Controllers.MysqlDB;
import sample.Main;
import sample.Utils;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientsController implements StaffController, Initializable {

    public ListView lwStaff;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResultSet rs = MysqlDB.execQuery("SELECT last_name FROM Client");
        while (true) {
            try {
                if (!rs.next()) break;
                lwStaff.getItems().add(rs.getString(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void back(ActionEvent actionEvent) {
        Main.setRoot("views/Manager/ManagerWindow.fxml", "Подсистема менеджера");
    }


    @Override
    public void addWorkerInfo(ActionEvent actionEvent) {
        Main.setRoot("views/Manager/ClientWindow.fxml", "Клиент", new ClientInfoController(-1));
    }

    @Override
    public void editWorkerInfo(ActionEvent actionEvent) {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex == -1) return;
        Main.setRoot("views/Manager/ClientWindow.fxml", "Клиент", new ClientInfoController(selectedIndex+1));
    }

    @Override
    public void removeWorker(ActionEvent actionEvent) {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex == -1) return;
        String query = "DELETE FROM `Client` WHERE id='" + (selectedIndex+1) + "';";

        System.out.println(query);
        if (MysqlDB.execUpdate(query) == 0) {
            Utils.showMessageWindow("Удаление невозможно");
        } else {
            lwStaff.getItems().remove(selectedIndex);
            MysqlDB.updateAI("Client");
            Utils.showMessageWindow("Клиент был удален");
        }
    }

    private int getSelectedIndex() {
        int selectedIndex = lwStaff.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) Utils.showMessageWindow("Запись не была выбрана");
        return selectedIndex;
    }
}
