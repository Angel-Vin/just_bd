package sample.Controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
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
    public TextField tfName;
    public TextField tfMark;
    public TextField tfModel;
    public TextField tfPrice;
    public TextField tfAmount;
    private int editItemIndex = -1;
    private Vector<TextField> textFields = new Vector<>();
    
    public SparePartController(int index) {
        editItemIndex = index;
    }

    private boolean isEdit() {
        return editItemIndex > -1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFields.addAll(Arrays.asList(tfName, tfMark, tfModel, tfPrice, tfAmount));

        if (isEdit()) {
            ResultSet rs = MysqlDB.execQuery("SELECT * FROM SparePart WHERE id='" + editItemIndex + "'");
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
        }

    }
    
    
    
    public void cancel(ActionEvent actionEvent) {
        Main.setRoot("views/CatalogWindow.fxml", "Каталог запчастей", new SparePartCatalogController());
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
            query = "UPDATE `SparePart` SET " + "`name`='" + tfName.getText() + "',`car_mark`='" +
                    tfMark.getText() + "',`car_model`='" + tfModel.getText() + "',`price`='" +
                    tfPrice.getText() + "',`amount`='" + tfAmount.getText() + "' WHERE id='" + editItemIndex + "'";
        } else {
            query = "INSERT INTO `SparePart`(`name`, `car_mark`, `car_model`, `price`, `amount`)" +
                    " VALUES ('" + tfName.getText() + "','" + tfMark.getText() + "','" + tfModel.getText() + "','" + tfPrice.getText() +
                    "','" + tfAmount.getText() + "')";
        }

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
