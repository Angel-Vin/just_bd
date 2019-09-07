package sample.Controllers.Manager;

import javafx.event.ActionEvent;
import sample.Controllers.Admin.StaffWinController;
import sample.Main;

public class ManagerWinController {
    public void back(ActionEvent actionEvent) {
        Main.setRoot("views/AuthenticationWindow.fxml", "Аутентификация");
    }

    public void showBuyingSellingWindow(ActionEvent actionEvent) {
        Main.setRoot("views/Manager/BuyingSellingWindow.fxml", "Покупка/Продажа");
    }

    public void showClients(ActionEvent actionEvent) {
        Main.setRoot("views/StaffWindow.fxml", "Клиенты", new ClientsController());
    }

    public void showExperises(ActionEvent actionEvent) {
        Main.setRoot("views/Manager/ExpertiseWindow.fxml", "Экспертиза");
    }

    public void showCatalog(ActionEvent actionEvent) {
        Main.setRoot("views/Manager/CatalogWindow.fxml", "Каталог");
    }
}
