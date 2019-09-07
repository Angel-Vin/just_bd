package sample.Controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.Controllers.MysqlDB;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DealingsWindowController implements Initializable {

    private int clientIndex;
    public ListView lvDeals;
    public Button btnClose;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResultSet rs = MysqlDB.execQuery("SELECT * FROM Contract WHERE client_id='" + clientIndex + "'");
        try {
            int dealNum = 1;
            while (rs.next()) {
                StringBuilder dealInfo = new StringBuilder("\t\tСделка №" + dealNum).append(rs.getString("item_name"))
                        .append("\n\nСпособ оплаты:\t").append(rs.getString("payment_way"))
                        .append("\n\nВид сделки:\t").append(rs.getString("transaction_direction"))
                        .append("\n\nКоличество договоров:\t").append(rs.getString("amount"));
                lvDeals.getItems().add(dealInfo.toString());
                dealNum++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DealingsWindowController(int index) {
        clientIndex = index;
    }


    public void close(ActionEvent actionEvent) {
        ((Stage) lvDeals.getScene().getWindow()).close();
    }


}
