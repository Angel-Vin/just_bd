package sample.Controllers.Manager;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import sample.Main;

public class CatalogController {

    public ComboBox cbOptions;

    public void back(ActionEvent actionEvent) {
        Main.setRoot("views/Manager/ManagerWindow.fxml", "Подсистема менеджера");
    }

    public void showItem(ActionEvent actionEvent) {
        if (cbOptions.getSelectionModel().getSelectedItem().toString().compareTo("Автомобиль") == 0) {
            Main.setRoot("views/Manager/CarWindow.fxml", "Автомобиль");
        } else {
            Main.setRoot("views/Manager/SparePartWindow.fxml", "Запчасть");
        }
    }

}
