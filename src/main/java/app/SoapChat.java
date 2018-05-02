package app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.controller.NodeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import soap.node.LayerNode;
import soap.node.OnlineNode;
import soap.node.RouterNode;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoapChat extends Application {
    public static ObservableSet<OnlineNode> onlineNodeObservableList = FXCollections.observableSet();
    private RouterNode routerNode;
    private LayerNode layerNode;

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getClassLoader().getResource("fxml/node.fxml"));
            loader.load();
            Parent root = loader.getRoot();
            NodeController nodeController = loader.getController();
            getInputParameters(nodeController);
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

    public void stop() {
        try {
            if (layerNode != null) {
                layerNode.stopListening();
            } else if (routerNode != null)
                routerNode.stopListening();
        } catch (IOException e) {
            System.exit(0);
        }

        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void getInputParameters(NodeController nodeController) {
        Map<String, String> parameters = getParameters().getNamed();

        String layerNumber = String.valueOf(parameters.get("layerNumber"));
        String nodeName = String.valueOf(parameters.get("nodeName"));
        Integer port = Integer.parseInt(parameters.get("port"));
        String nodeType = String.valueOf(parameters.get("nodeType"));
        Integer nextLayerNodePort = Integer.valueOf(parameters.get("nextLayerNodePort"));
        String nextLayerNodeHost = parameters.getOrDefault("nextLayerNodeHost", "localhost");

        switch (nodeType) {
            case "L":
                layerNode = new LayerNode(nodeController, layerNumber, nodeName, port, nextLayerNodePort, nextLayerNodeHost);
                nodeController.setNode(layerNode);
                break;
            case "R":
                Integer nextRouterNodePort = Integer.valueOf(parameters.get("nextRouterNodePort"));
                String nextRouterNodeHost = parameters.getOrDefault("nextRouterNodeHost", "localhost");

                routerNode = new RouterNode(nodeController, layerNumber, nodeName, port, nextLayerNodePort, nextLayerNodeHost,
                        nextRouterNodePort, nextRouterNodeHost);
                nodeController.setNode(routerNode);
                break;
            default:
                System.exit(1);
                System.out.println("Niepoprawny argument " + nodeType + " dla parametru określającego typ węzła.");
                break;
        }
    }
}
