package sample;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Controllers.MysqlDB;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;

public class Main extends Application {
    private static Stage mainStage;

    public static void setRoot(String newViewFileName, String newTitle)  {
        Parent root = getParent(newViewFileName);
        updateStage(newTitle, root);
    }

    public static void setRoot(String newViewFileName, String newTitle, Object controller) {
        Parent root = getParent(newViewFileName, controller);
        updateStage(newTitle, root);
    }

    private static void updateStage(String newTitle, Parent root) {
        mainStage.hide();
        mainStage.getScene().setRoot(root);
        mainStage.setTitle(newTitle);
        mainStage.show();
    }


    private static Parent getParent(String newViewFileName) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource(newViewFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private static Parent getParent(String newViewFileName, Object controller)  {
        FXMLLoader loader = new FXMLLoader();
        URL location = Main.class.getResource(newViewFileName);
        loader.setLocation(location);
        loader.setController(controller);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        primaryStage.setScene(new Scene(new VBox(), 600, 500));
        primaryStage.setResizable(false);
        Main.setRoot("views/sample.fxml", "Главное меню");

    }


    public static void main(String[] args) {

        MysqlDB.setConnection("jdbc:mysql://localhost:3306/just?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
        launch(args);
    }
}
