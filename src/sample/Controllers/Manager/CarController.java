package sample.Controllers.Manager;

import javafx.beans.value.ChangeListener;
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
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Vector;

public class CarController implements Initializable {

    public TextField tfMark;
    public TextField tfColor;
    public TextField tfCarcassNum;
    public TextField tfChassisNum;
    public TextField tfModel;
    public TextField tfMileage;
    public TextField tfEngineNum;
    public TextField tfReleaseDate;
    public TextField tfSellingPrice;
    public TextField tfReleasePrice;
    public TextField tfStateRegNum;
    public ListView lvCars;
    private Vector<TextField> textFields = new Vector<>();
    private int selectedIndex = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lvCars.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            selectedIndex = lvCars.getSelectionModel().getSelectedIndex();
            ResultSet rs = MysqlDB.execQuery("SELECT * FROM Car WHERE id='" + (selectedIndex+1) + "'");
            try {
                if (rs.next()) {
                    tfMark.setText(rs.getString("mark"));
                    tfColor.setText(rs.getString("color"));
                    tfCarcassNum.setText(rs.getString("carcass_num"));
                    tfChassisNum.setText(rs.getString("chassis_num"));
                    tfModel.setText(rs.getString("model"));
                    tfMileage.setText(rs.getString("mileage"));
                    tfEngineNum.setText(rs.getString("engine_num"));
                    tfReleaseDate.setText(rs.getString("release_date"));
                    tfSellingPrice.setText(rs.getString("selling_price"));
                    tfReleasePrice.setText(rs.getString("release_price"));
                    tfStateRegNum.setText(rs.getString("state_reg_num"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        ResultSet rs = MysqlDB.execQuery("SELECT mark, model FROM Car");
        while (true) {
            try {
                if (!rs.next()) break;
                lvCars.getItems().add(rs.getString(1) + " " + rs.getString(2));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        textFields.addAll(Arrays.asList(tfMark, tfColor, tfCarcassNum, tfChassisNum, tfModel, tfMileage, tfEngineNum,
                tfReleaseDate, tfSellingPrice, tfReleasePrice, tfStateRegNum));
    }

    public void cancel(ActionEvent actionEvent) {
        Main.setRoot("views/Manager/CatalogWindow.fxml", "Каталог");
    }

    public void confirm(ActionEvent actionEvent) {
        if (selectedIndex == -1) {
            Utils.showMessageWindow("Запись не выбрана\nРедактирование невозможно");
            return;
        }
        for (TextField tf : textFields) {
            if (tf.getLength() == 0) {
                Utils.showMessageWindow("Одно либо более полей не заполнены");
                return;
            }
        }

        String query = "UPDATE `Car` SET `mark`='" +
                tfMark.getText() + "',`model`='" + tfModel.getText() + "',`color`='" +
                tfColor.getText() + "',`mileage`='" + tfMileage.getText() + "',`carcass_num`='" +
                tfCarcassNum.getText() + "',`engine_num`='" + tfEngineNum.getText() + "',`chassis_num`='" +
                tfChassisNum.getText() + "',`release_date`='" + tfReleaseDate.getText() + "',`selling_price`='" +
                tfSellingPrice.getText() + "',`release_price`='" + tfReleasePrice.getText() + "',`state_reg_num`='" + tfStateRegNum.getText() +
                "' WHERE id='" + (selectedIndex+1) + "'";

        if (MysqlDB.execUpdate(query) == 0) {
            Utils.showMessageWindow("Некорректные типы данных");
        } else {
            lvCars.getItems().clear();
            updateList();
            Utils.showMessageWindow("Запрос успешно выполнен");
        }
    }

    private void updateList() {
        ResultSet rs = MysqlDB.execQuery("SELECT mark, model FROM Car");
        while (true) {
            try {
                if (!rs.next()) break;
                lvCars.getItems().add(rs.getString(1) + " " + rs.getString(2));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}
