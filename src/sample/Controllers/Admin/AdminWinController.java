package sample.Controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.net.URL;

public class AdminWinController {
    public void back(ActionEvent actionEvent) {
        Main.setRoot("views/AuthenticationWindow.fxml", "Аутентификация");
    }

    public void showStaff(ActionEvent actionEvent) {
        Main.setRoot("views/StaffWindow.fxml", "Сотрудники", new StaffWinController());
    }

    public void showSparePartCatalog(ActionEvent actionEvent) {
        Main.setRoot("views/CatalogWindow.fxml", "Каталог запчастей", new SparePartCatalogController());
    }

    public void showCarCatalog(ActionEvent actionEvent) throws IOException {
        Main.setRoot("views/CatalogWindow.fxml", "Каталог автомобилей", new CarCatalogController());
    }

    public void showDocumentation(ActionEvent actionEvent) {
        Main.setRoot("views/Admin/DocumentationWindow.fxml", "Документация");
    }
}
