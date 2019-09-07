package sample.Controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import jdk.jshell.execution.Util;
import sample.Controllers.MysqlDB;
import sample.Main;
import sample.Utils;

import javax.xml.transform.Result;
import java.lang.reflect.Array;
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
    private Vector<TextField> textFields = new Vector<>();

    private int editItemIndex = -1;
    public CarController(int index) {
        editItemIndex = index;
    }

    private boolean isEdit() {
        return editItemIndex > -1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFields.addAll(Arrays.asList(tfMark, tfColor, tfCarcassNum, tfChassisNum, tfModel, tfMileage, tfEngineNum,
                tfReleaseDate, tfSellingPrice, tfReleasePrice, tfStateRegNum));

        if (isEdit()) {
            ResultSet rs = MysqlDB.execQuery("SELECT * FROM Car WHERE id='" + editItemIndex + "'");
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
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Main.setRoot("views/CatalogWindow.fxml", "Каталог автомобилей", new CarCatalogController());
    }

    public void confirm(ActionEvent actionEvent) {
        for (TextField tf : textFields) {
            if (tf.getLength() == 0) {
                Utils.showMessageWindow("Одно либо более полей не заполнены");
                return;
            }
        }
        String query = null;
        if (isEdit()) {
            query = "UPDATE `Car` SET `mark`='" +
                    tfMark.getText() + "',`model`='" + tfModel.getText() + "',`color`='" +
                    tfColor.getText() + "',`mileage`='" + tfMileage.getText() + "',`carcass_num`='" +
                    tfCarcassNum.getText() + "',`engine_num`='" + tfEngineNum.getText() + "',`chassis_num`='" +
                    tfChassisNum.getText() + "',`release_date`='" + tfReleaseDate.getText() + "',`selling_price`='" +
                    tfSellingPrice.getText() + "',`release_price`='" + tfReleasePrice.getText() + "',`state_reg_num`='" + tfStateRegNum.getText() +
                    "' WHERE id='" + editItemIndex + "'";
        } else query = "INSERT INTO `Car`(`mark`, `model`, `color`, `mileage`, `carcass_num`, `engine_num`," +
                " `chassis_num`, `release_date`, `selling_price`, `release_price`, `state_reg_num`)" +
                " VALUES ('" + tfMark.getText() + "','" + tfModel.getText() + "','" + tfColor.getText() +
                "','" + tfMileage.getText() + "','" + tfCarcassNum.getText() + "','" + tfEngineNum.getText() +
                "','" + tfChassisNum.getText() + "','" + tfReleaseDate.getText() + "','" + tfSellingPrice.getText() +
                "','" + tfReleasePrice.getText() + "','" + tfStateRegNum.getText() + "')";

        if (MysqlDB.execUpdate(query) == 0) {
            Utils.showMessageWindow("Некорректные типы данных");
        } else {
            if (!isEdit()) {
                resetInputs();
            }
            Utils.showMessageWindow("Запрос успешно выполнен");
        }
    }

    private void resetInputs() {
        for (TextField tf : textFields) {
            tf.clear();
        }
    }


}
