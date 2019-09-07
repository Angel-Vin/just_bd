package sample.Controllers.Admin;

import javafx.event.ActionEvent;
import sample.Main;

public interface StaffController {
    public void back(ActionEvent actionEvent);

    public void addWorkerInfo(ActionEvent actionEvent);
    public void editWorkerInfo(ActionEvent actionEvent);

    public void removeWorker(ActionEvent actionEvent);
}
