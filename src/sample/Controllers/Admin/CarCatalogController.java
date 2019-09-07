package sample.Controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import sample.Controllers.MysqlDB;
import sample.Main;
import sample.Utils;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CarCatalogController implements CatalogController, Initializable {

    public ListView lwItems;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResultSet rs = MysqlDB.execQuery("SELECT mark, model FROM Car");
        while (true) {
            try {
                if (!rs.next()) break;
                lwItems.getItems().add(rs.getString(1) + " " + rs.getString(2));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void back(ActionEvent actionEvent) {
        Main.setRoot("views/AdminWindow.fxml", "Подсистема администратора");
    }

    @Override
    public void add() {
        Main.setRoot("views/Admin/CarWindow.fxml", "Добавление автомобиля", new CarController(-1));
    }

    @Override
    public void remove() {

        int selectedIndex = getSelectedIndex();
        if (selectedIndex == -1) return;

        String query = "DELETE FROM `Car` WHERE id='" + (selectedIndex+1) + "';";

        if (MysqlDB.execUpdate(query) == 0) {
            Utils.showMessageWindow("Удаление невозможно");
        } else {
            lwItems.getItems().remove(selectedIndex);
            MysqlDB.updateAI("Car");
            Utils.showMessageWindow("Автомобиль был удален");
        }
    }

    @Override
    public void edit() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex == -1) return;
        Main.setRoot("views/Admin/CarWindow.fxml", "Редактирование автомобиля", new CarController(selectedIndex+1));
    }

    private int getSelectedIndex() {
        int selectedIndex = lwItems.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) Utils.showMessageWindow("Запись не была выбрана");
        return selectedIndex;
    }



}
