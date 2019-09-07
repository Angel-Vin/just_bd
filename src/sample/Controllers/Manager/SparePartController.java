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

public class SparePartController implements Initializable {

    public ListView lvSpareParts;
    public TextField tfName;
    public TextField tfMark;
    public TextField tfModel;
    public TextField tfPrice;
    public TextField tfAmount;
    private Vector<TextField> textFields = new Vector<>();
    private int selectedIndex = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lvSpareParts.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            selectedIndex = lvSpareParts.getSelectionModel().getSelectedIndex();
            ResultSet rs = MysqlDB.execQuery("SELECT * FROM SparePart WHERE id='" + (selectedIndex+1) + "'");
            System.out.println("SELECT * FROM SparePart WHERE id='" + (selectedIndex+1) + "'");
            try {
                if (rs.next()) {
                    tfName.setText(rs.getString("name"));
                    tfMark.setText(rs.getString("car_mark"));
                    tfModel.setText(rs.getString("car_model"));
                    tfPrice.setText(rs.getString("price"));
                    tfAmount.setText(rs.getString("amount"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        ResultSet rs = MysqlDB.execQuery("SELECT name FROM SparePart");
        while (true) {
            try {
                if (!rs.next()) break;
                lvSpareParts.getItems().add(rs.getString(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        textFields.addAll(Arrays.asList(tfName, tfMark, tfModel, tfPrice, tfAmount));

    }

    private void updateList() {
        ResultSet rs = MysqlDB.execQuery("SELECT name FROM SparePart");
        while (true) {
            try {
                if (!rs.next()) break;
                lvSpareParts.getItems().add(rs.getString(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void cancel(ActionEvent actionEvent) {
        Main.setRoot("views/Manager/CatalogWindow.fxml", "Каталог");
    }

    public void cofirm(ActionEvent actionEvent) {
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

        String query = "UPDATE `SparePart` SET " + "`name`='" + tfName.getText() + "',`car_mark`='" +
                tfMark.getText() + "',`car_model`='" + tfModel.getText() + "',`price`='" +
                tfPrice.getText() + "',`amount`='" + tfAmount.getText() + "' WHERE id='" + (selectedIndex+1) + "'";

        if (MysqlDB.execUpdate(query) == 0) {
            Utils.showMessageWindow("Некорректные типы данных");
        } else {
            lvSpareParts.getItems().clear();
            updateList();
            Utils.showMessageWindow("Запрос успешно выполнен");
        }

    }


}
