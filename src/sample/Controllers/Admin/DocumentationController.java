package sample.Controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import sample.Controllers.MysqlDB;
import sample.Main;
import sample.Utils;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;



public class DocumentationController implements Initializable {
    public RadioButton rbMonth;
    public RadioButton rbQuarter;
    public RadioButton rbYear;
    public ComboBox cbPaymentsClients;
    public ComboBox cbDealsClients;
    private ToggleGroup tgReportTerm = new ToggleGroup();

    public void back(ActionEvent actionEvent) {
        Main.setRoot("views/AdminWindow.fxml", "Подсистема администратора");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rbMonth.setToggleGroup(tgReportTerm);
        rbQuarter.setToggleGroup(tgReportTerm);
        rbYear.setToggleGroup(tgReportTerm);
        rbMonth.setSelected(true);

        ResultSet rs = MysqlDB.execQuery("SELECT last_name FROM Client");
        while (true) {
            try {
                if (!rs.next()) break;
                cbDealsClients.getItems().add(rs.getString(1));
                cbPaymentsClients.getItems().add(rs.getString(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cbDealsClients.getSelectionModel().selectFirst();
            cbPaymentsClients.getSelectionModel().selectFirst();
        }

    }

    private String generateReportQuery(int monthsAgo, String direction) {
        return "SELECT SUM(price) FROM Payment_transaction " +
                "JOIN Contract ON Payment_transaction.contract_num = Contract.id " +
                "WHERE transaction_date > getDateAgo('" +
                monthsAgo +
                "') AND transaction_direction = '" +
                direction +
                "'";
    }

    private int getMonthsAgo() {
        int monthsAmountAgo;
        if (tgReportTerm.getSelectedToggle() == rbMonth) monthsAmountAgo = 1;
        else if (tgReportTerm.getSelectedToggle() == rbQuarter) monthsAmountAgo = 4;
        else monthsAmountAgo = 12;
        return monthsAmountAgo;
    }

    public void showReport(ActionEvent actionEvent) {
        int monthsAmountAgo = getMonthsAgo();
        float sellSum = getSellSum(monthsAmountAgo), buySum = getBuySum(monthsAmountAgo);

        System.out.println(sellSum + " " + buySum);
        Utils.showMessageWindow(generateReport(sellSum, buySum));

    }

    private String generateReport(float sellSum, float buySum) {
        return "Сумма продаж за указанный срок - " + sellSum +
                "\nСумма покупок за указанный срок - " + buySum;
    }
    private float getBuySum(int monthsAmountAgo) {
        float buySum = 0.0f;

        ResultSet rs = MysqlDB.execQuery(generateReportQuery(monthsAmountAgo, "покупка"));
        try {
            if (rs.next()) {
                buySum = rs.getFloat(1);
            }
        } catch (SQLException e) {
            Utils.showMessageWindow(e.getMessage());
        }
        return buySum;
    }

    private float getSellSum(int monthsAmountAgo) {
        float sellSum = 0.0f;
        ResultSet rs = MysqlDB.execQuery(generateReportQuery(monthsAmountAgo, "продажа"));
        try {
            if (rs.next()) {
                sellSum = rs.getFloat(1);
            }
        } catch (SQLException e) {
            Utils.showMessageWindow(e.getMessage());
        }
        return sellSum;
    }

    public void printReport(ActionEvent actionEvent) {
        int monthsAgo = getMonthsAgo();
        String filename = null;
        switch (monthsAgo) {
            case 1: filename = "month_report.pdf"; break;
            case 4: filename = "quarter_report.pdf"; break;
            case 12: filename = "year_report.pdf"; break;
        }
        Utils.makePdfDoc(generateReport(getSellSum(monthsAgo), getBuySum(monthsAgo)), filename);
        Utils.showMessageWindow(filename + " был успешно создан");
    }

    public void searchClientDeals(ActionEvent actionEvent) {
        int selectedClient = cbDealsClients.getSelectionModel().getSelectedIndex();

        Stage stage = Utils.createStage("views/Admin/DealingsWindow.fxml", "Сведения о сделках", actionEvent, new DealingsWindowController(selectedClient+1));
        stage.showAndWait();
    }

    public void showNotification(ActionEvent actionEvent) {
        Utils.showMessageWindow("Данная функция доступна лишь в платной версии");
    }
}
