package app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import soap.OnlineNode;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoapChat extends Application {
    public static ObservableList<OnlineNode> onlineNodeObservableList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getClassLoader().getResource("fxml/node.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            primaryStage.setTitle("Soap Chat");
            primaryStage.getIcons().add(new Image("/image/icon.png"));
            primaryStage.setMinWidth(610);
            primaryStage.setMinHeight(390);
            primaryStage.setScene(new Scene(root, 620, 400));
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException ioEcx) {
            Logger.getLogger(SoapChat.class.getName()).log(Level.SEVERE, null, ioEcx);
        }
    }

    public void stop()  {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
