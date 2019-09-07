package sample.Controllers.Manager;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import sample.Controllers.MysqlDB;
import sample.Main;
import sample.Utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Vector;


class ReportInfo {
    StringBuilder item_info;
    float item_price;
    String paymentWay;
    String reportDirection;
    int amount;
    String clientFullName;
    int clientId;

    ReportInfo (StringBuilder _item_info, float _item_price, String _paymentWay, String _reportDirection, int _amount, String _clientFullName, int _clientId) {
        item_info = _item_info;
        item_price = _item_price;
        paymentWay = _paymentWay;
        reportDirection = _reportDirection;
        amount = _amount;
        clientFullName = _clientFullName;
        clientId = _clientId;
    }

    @Override
    public String toString() {
        StringBuilder report = new StringBuilder();
        report.append(item_info)
                .append("\nВыбор оплаты: \t").append(paymentWay)
                .append("\nДоговор: \t").append(reportDirection)
                .append("\nКоличество договоров: \t").append(amount)
                .append("\nФИО клиента: \t").append(clientFullName);
        return report.toString();
    }
}

public class BuyingSellingController implements Initializable {

    public ComboBox cbItems;
    public ComboBox cbClients;
    public ComboBox cbAmount;
    public RadioButton rbCashPay;
    public RadioButton rbCardPay;
    public RadioButton rbSell;
    public RadioButton rbBuy;
    private ToggleGroup tgPayment, tgBuySell;
    private int carsAmount = 0, sparePartsAmount = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tgPayment = new ToggleGroup();
        tgBuySell = new ToggleGroup();
        rbCashPay.setToggleGroup(tgPayment);
        rbCardPay.setToggleGroup(tgPayment);
        rbSell.setToggleGroup(tgBuySell);
        rbBuy.setToggleGroup(tgBuySell);
        rbCashPay.setSelected(true);
        rbSell.setSelected(true);

        ResultSet rs = MysqlDB.execQuery("SELECT mark, model, selling_price FROM Car");
        try {
            while (rs.next()) {
                cbItems.getItems().add(rs.getString("mark") + " " + rs.getString("model"));
                carsAmount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        rs = MysqlDB.execQuery("SELECT name, price FROM SparePart");
        try {
            while (rs.next()) {
                cbItems.getItems().add(rs.getString("name"));
                sparePartsAmount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cbItems.getSelectionModel().selectFirst();

        rs = MysqlDB.execQuery("SELECT last_name FROM Client");
        try {
            while (rs.next()) {
                cbClients.getItems().add(rs.getString("last_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cbClients.getSelectionModel().selectFirst();
    }

    public void back(ActionEvent actionEvent) {
        Main.setRoot("views/Manager/ManagerWindow.fxml", "Подсистема менеджера");
    }

    private boolean checkInputs () {
        return cbItems.getItems().size() != 0 && cbClients.getItems().size() != 0;
    }
    public void addTransaction(ActionEvent actionEvent) {

        if (!checkInputs()) {
            Utils.showMessageWindow("Недостаточно данных для заказа");
            return;
        }

        String query = null;
        ReportInfo reportInfo = getReportInfo();

        if (tgBuySell.getSelectedToggle() == rbSell) {
            removeSelectedItem();
        }



        ResultSet rs = MysqlDB.execQuery("SELECT MAX(id) FROM Contract");
        int contractId = -1;
        try {
            if (rs.next()) {
                contractId = rs.getInt(1)+1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        query = "INSERT INTO `Contract`(`item_name`, `payment_way`, `transaction_direction`, `client_id`, `amount`) VALUES ('" +
                reportInfo.item_info +
                "','" +
                reportInfo.paymentWay +
                "','" +
                reportInfo.reportDirection +
                "','" +
                reportInfo.clientId +
                "','" +
                reportInfo.amount + "')";

        if (MysqlDB.execUpdate(query) == 0) {
            Utils.showMessageWindow("Запрос не был выполнен");
        }
        query = "INSERT INTO `Payment_transaction`(`contract_num`, `price`) VALUES ('" +
                contractId +
                "','" +
                reportInfo.item_price +
                "')";
        if (MysqlDB.execUpdate(query) == 0) {
            Utils.showMessageWindow("Запрос не был выполнен");
        } else {
            Utils.showMessageWindow("Запрос успешно выполнен");

        }

    }

    private void removeSelectedItem() {
        int selectedItemIndex = cbItems.getSelectionModel().getSelectedIndex();
        String query = null;
        if (selectedItemIndex > carsAmount - 1) {
            query = "DELETE FROM `SparePart` WHERE id='" + (selectedItemIndex + 1) + "';";
            MysqlDB.updateAI("SparePart");
        } else {
            query = "DELETE FROM `Car` WHERE id='" + (selectedItemIndex + 1) + "';";
            MysqlDB.updateAI("Car");
        }

        ResultSet rs = MysqlDB.execQuery(query);
        if (MysqlDB.execUpdate(query) == 0) {
            Utils.showMessageWindow("Удаление товара невозможно");
        } else {
            cbItems.getItems().remove(selectedItemIndex);
            cbItems.getSelectionModel().selectFirst();
        }
    }

    private ReportInfo getReportInfo () {
        StringBuilder item_info = new StringBuilder();
        float item_price = 0.0f;
        int selectedItemIndex = cbItems.getSelectionModel().getSelectedIndex();
        if (selectedItemIndex > carsAmount-1) {
            ResultSet rs = MysqlDB.execQuery("SELECT * FROM SparePart WHERE id='" + (selectedItemIndex+1) + "'");
            try {
                if (rs.next()) {
                    item_info.append("Запчасть\n").append("Название: \t").append(rs.getString("name"))
                            .append("\nМарка автомобиля: \t").append(rs.getString("car_mark"))
                            .append("\nМодель автомобиля: \t").append(rs.getString("car_model"))
                            .append("\nСтоимость запчасти: \t").append(rs.getString("price"))
                            .append("\nКоличество на складе: \t").append(rs.getString("amount"));
                    item_price = rs.getFloat("price");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            ResultSet rs = MysqlDB.execQuery("SELECT * FROM Car WHERE id='" + (selectedItemIndex+1) + "'");
            try {
                if (rs.next()) {
                    item_info.append("\tАвтомобиль\n").append("Марка: \t").append(rs.getString("mark"))
                            .append("\nМодель: \t").append(rs.getString("model"))
                            .append("\nЦвет: \t").append(rs.getString("color"))
                            .append("\nПробег: \t").append(rs.getString("mileage"))
                            .append("\nНомер кузова: \t").append(rs.getString("carcass_num"))
                            .append("\nНомер двигателя: \t").append(rs.getString("engine_num"))
                            .append("\nНомер шасси: \t").append(rs.getString("chassis_num"))
                            .append("\nДата выпуска: \t").append(rs.getString("release_date"))
                            .append("\nСтоимость продажи: \t").append(rs.getString("selling_price"))
                            .append("\nСтоимость при выпуске: \t").append(rs.getString("release_price"))
                            .append("\nНомер гос. регистрации: \t").append(rs.getString("state_reg_num"));
                    item_price = rs.getFloat("selling_price");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String selectedPayWay, selectedTransDirection;
        if (tgPayment.getSelectedToggle() == rbCashPay) selectedPayWay = "наличные";
        else selectedPayWay = "карта";
        if (tgBuySell.getSelectedToggle() == rbSell) selectedTransDirection = "продажа";
        else selectedTransDirection = "покупка";

        int amount = cbClients.getSelectionModel().getSelectedIndex()+1;
        int clientId = cbAmount.getSelectionModel().getSelectedIndex()+1;
        String clientFullName = null;
        ResultSet rs = MysqlDB.execQuery("SELECT last_name, first_name, patronymic FROM Client WHERE id='" + clientId + "'");
        try {
            if (rs.next()) {
                clientFullName = rs.getString("last_name") + " " + rs.getString("first_name") + " " + rs.getString("patronymic");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new ReportInfo(item_info, item_price, selectedPayWay, selectedTransDirection, amount, clientFullName, clientId);
    }


    public void printReportInPdf(ActionEvent actionEvent) {
        Utils.makePdfDoc(getReportInfo().toString(), "report.pdf");
        Utils.showMessageWindow("report.pdf успешно создан");
    }



    public void addAndPrint(ActionEvent actionEvent) {
        addTransaction(null);
        printReportInPdf(null);
    }

    public void reset(ActionEvent actionEvent) {
        cbItems.getSelectionModel().selectFirst();
        cbClients.getSelectionModel().selectFirst();
        cbAmount.getSelectionModel().selectFirst();
        rbCashPay.setSelected(true);
        rbSell.setSelected(true);
    }
}
