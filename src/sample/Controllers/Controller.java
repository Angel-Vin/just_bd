package sample.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import sample.Main;
import sample.Utils;


import java.io.IOException;

public class Controller {


    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void authenticate(ActionEvent actionEvent) throws IOException {
        Main.setRoot("views/AuthenticationWindow.fxml", "Аутентификация");
    }

    public void payToContinue(ActionEvent actionEvent) {
        Utils.showMessageWindow("Доступно в платной версии");
    }


}
