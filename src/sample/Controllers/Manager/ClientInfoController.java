package sample.Controllers.Manager;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sample.Controllers.MysqlDB;
import sample.Controllers.PersonController;
import sample.Main;
import sample.Utils;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Vector;

public class ClientInfoController implements PersonController, Initializable {
    public TextField tfLastName;
    public TextField tfFirstName;
    public TextField tfPatronymic;
    public ComboBox cbBirthDay;
    public ComboBox cbBirthMonth;
    public ComboBox cbBirthYear;
    public TextField tfPassSeries;
    public TextField tfPassNum;
    public ComboBox cbBuySellDay;
    public ComboBox cbBuySellMonth;
    public ComboBox cbBuySellYear;
    private int editItemIndex;
    private Vector<TextField> textFields = new Vector<>();
    private Vector<ComboBox> comboBoxes = new Vector<>();

    public ClientInfoController(int index) {
        editItemIndex = index;
    }

    private boolean isEdit() {
        return editItemIndex > 0;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFields.addAll(Arrays.asList(tfLastName, tfFirstName, tfPatronymic, tfPassNum, tfPassSeries));
        comboBoxes.addAll(Arrays.asList(cbBirthDay, cbBirthMonth, cbBirthYear, cbBuySellDay, cbBuySellMonth, cbBuySellYear));

        int initialBirthYear = 1970, initialBuySellYear = 2000, lastBirthYear = 2010, lastBuySellYear = 2020;

        for (int i = 1; i < 32; i++) {
            cbBuySellDay.getItems().add(i);
            cbBirthDay.getItems().add(i);
        }
        cbBuySellDay.getSelectionModel().selectFirst();
        cbBirthDay.getSelectionModel().selectFirst();

        for (int i = 1; i < 13; i++) {
            cbBuySellMonth.getItems().add(i);
            cbBirthMonth.getItems().add(i);
        }
        cbBuySellMonth.getSelectionModel().selectFirst();
        cbBirthMonth.getSelectionModel().selectFirst();

        for (int i = initialBirthYear; i < lastBirthYear; i++) {
            cbBirthYear.getItems().add(i);
        }
        cbBirthYear.getSelectionModel().selectFirst();

        for (int i = initialBuySellYear; i < lastBuySellYear; i++) {
            cbBuySellYear.getItems().add(i);
        }
        cbBuySellYear.getSelectionModel().selectFirst();

        if (isEdit()) {
            ResultSet rs = MysqlDB.execQuery("SELECT * FROM Client WHERE id='" + editItemIndex + "'");
            try {
                if (rs.next()) {

                    tfLastName.setText(rs.getString("last_name"));
                    tfFirstName.setText(rs.getString("first_name"));
                    tfPatronymic.setText(rs.getString("patronymic"));
                    tfPassSeries.setText(rs.getString("passport_series"));
                    tfPassNum.setText(rs.getString("passport_num"));
                    cbBirthDay.getSelectionModel().select(rs.getInt("birth_day")-1);
                    cbBirthMonth.getSelectionModel().select(rs.getInt("birth_month")-1);
                    cbBirthYear.getSelectionModel().select(rs.getInt("birth_year") - initialBirthYear);

                    cbBuySellDay.getSelectionModel().select(rs.getInt("buy_sell_day")-1);
                    cbBuySellMonth.getSelectionModel().select(rs.getInt("buy_sell_month")-1);
                    cbBuySellYear.getSelectionModel().select(rs.getInt("buy_sell_year") - initialBuySellYear);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            cbBuySellDay.getSelectionModel().selectFirst();
            cbBirthDay.getSelectionModel().selectFirst();

            cbBuySellMonth.getSelectionModel().selectFirst();
            cbBirthMonth.getSelectionModel().selectFirst();

            cbBirthYear.getSelectionModel().selectFirst();
            cbBuySellYear.getSelectionModel().selectFirst();

        }


    }

    @Override
    public void back(ActionEvent actionEvent) {
        Main.setRoot("views/StaffWindow.fxml", "Клиенты", new ClientsController());
    }

    @Override
    public void confirm(ActionEvent actionEvent) {
        for (TextField tf : textFields) {
            if (tf.getLength() == 0) {
                Utils.showMessageWindow("Одно либо более полей не заполнены");
                return;
            }
        }

        String query = null;
        if (isEdit()) {
            query = "UPDATE `Client` SET " + "`last_name`='" + tfLastName.getText() + "',`first_name`='" +
                    tfFirstName.getText() + "',`patronymic`='" + tfPatronymic.getText() + "',`birth_day`='" +
                    cbBirthDay.getSelectionModel().getSelectedItem().toString() + "'," +
                    "`birth_month`='" + cbBirthMonth.getSelectionModel().getSelectedItem().toString() + "'," +
                    "`birth_year`='" + cbBirthYear.getSelectionModel().getSelectedItem().toString() + "'," +
                    "`passport_series`='" + tfPassSeries.getText() + "'," +
                    "`passport_num`='" + tfPassNum.getText() + "'," +
                    "`buy_sell_day`='" + cbBuySellDay.getSelectionModel().getSelectedItem().toString() + "'," +
                    "`buy_sell_month`='" + cbBuySellMonth.getSelectionModel().getSelectedItem().toString() + "'," +
                    "`buy_sell_year`='" + cbBuySellYear.getSelectionModel().getSelectedItem().toString() +
                    "' WHERE id='" + editItemIndex + "'";
        } else {
            query = "INSERT INTO `Client`(`last_name`, `first_name`, `patronymic`, `birth_day`, `birth_month`," +
                    "`birth_year`, `passport_series`, `passport_num`, `buy_sell_day`, `buy_sell_month`, `buy_sell_year`)" +
                    " VALUES ('" + tfLastName.getText() + "','" + tfFirstName.getText() + "','" + tfPassNum.getText() + "','" +
                    cbBirthDay.getSelectionModel().getSelectedItem().toString() + "','" + cbBirthMonth.getSelectionModel().getSelectedItem().toString() +
                    "','" + cbBirthYear.getSelectionModel().getSelectedItem().toString() + "','" + tfPassSeries.getText() + "','" + tfPassNum.getText() +
                    "','" + cbBuySellDay.getSelectionModel().getSelectedItem().toString() + "','" + cbBuySellMonth.getSelectionModel().getSelectedItem().toString() +
                    "','" + cbBuySellYear.getSelectionModel().getSelectedItem().toString() + "')";
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
        for (ComboBox cb : comboBoxes) {
            cb.getSelectionModel().selectFirst();
        }
    }
}
