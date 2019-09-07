package sample;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Controllers.MysqlDB;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class Utils {
    static Stage getStage(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        return (Stage) source.getScene().getWindow();
    }

    // создание нового окна
    public static Stage createStage(String viewFileName, String title, ActionEvent actionEvent, Object controller) {
        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(viewFileName));
        fxmlLoader.setController(controller);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        stage.setScene(new Scene(root, 400, 500));
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(getStage(actionEvent));
        return stage;
    }

    public static void showMessageWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Message");
        alert.setContentText(message);
        alert.showAndWait();
    }

//    public static void execUpdate(String query, Vector<TextField> textFields) {
//        int result = MysqlDB.execUpdate(query);
//        if (result == 0) {
//            Utils.showMessageWindow("Некорректные типы данных");
//        } else {
//            for (TextField tf : textFields) {
//                tf.clear();
//            }
//            Utils.showMessageWindow("Запрос успешно выполнен");
//        }
//    }

    // распечатать данные в pdf
    public static void makePdfDoc(String data, String filename) {
        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            BaseFont bf = BaseFont.createFont("sample/tahoma.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); //подключаем файл шрифта, который поддерживает кириллицу
            Font font = new Font(bf);
            document.add(new Paragraph(data, font));
            document.close();
            writer.close();
        } catch (DocumentException | IOException e)
        {
            Utils.showMessageWindow(e.getMessage());
        }
    }


}
