package javafx.controller;

import app.SoapChat;
import javafx.CustomMessageBox;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import soap.MessageBody;
import soap.node.LayerNode;
import soap.node.Node;
import soap.node.OnlineNode;
import soap.node.RouterNode;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

public class NodeController implements Initializable {
    private CustomMessageBox customMessageBox;
    private SimpleDateFormat simpleDateFormat;
    private Node node;

    @FXML
    private Label labelNodeType, labelPort, labelLayerNumberAndNodeName, labelForwarding;

    @FXML
    private TableView<OnlineNode> tableViewOnlineNodes;

    @FXML
    private TableColumn<OnlineNode, String> tableColumnLayerNumberAndNodeName, tableColumnNodeType, tableColumnNextHost;

    @FXML
    private TableColumn<OnlineNode, Integer> tableColumnPort, tableColumnNextPort;

    @FXML
    private TextArea textAreaNewMessage, textAreaMessages;

    public void setOnlineNodes(Set<OnlineNode> onlineNodes) {
        SoapChat.onlineNodeObservableList = FXCollections.observableSet(onlineNodes);
        refreshTableView();
    }

    public Set<OnlineNode> getOnlineNodes() {
        return SoapChat.onlineNodeObservableList;
    }

    public void setNode(LayerNode layerNode) {
        node = layerNode;

        labelNodeType.setText("L");
        labelPort.setText(String.valueOf(node.getPort()));
        labelLayerNumberAndNodeName.setText(node.getNodeFullName());
        labelForwarding.setText(node.getNextLayerNodeHost() + ":" + node.getNextLayerNodePort());

        node.startListening();

        SoapChat.onlineNodeObservableList.add(new OnlineNode(layerNode.getLayerNumber(), layerNode.getNodeName(),
                node.getPort(), "L", node.getNextLayerNodeHost(), node.getNextLayerNodePort()));
        refreshTableView();

        try {
            node.sendSoapMessage("", "", "global_broadcast",
                    new MessageBody(getOnlineNodes()));
        } catch (SOAPException | JAXBException ignored) {
        }
    }

    public void setNode(RouterNode routerNode) {
        this.node = routerNode;

        labelNodeType.setText("R - " + routerNode.getNextRouterNodeHost() + ":" + routerNode.getNextRouterNodePort());
        labelPort.setText(String.valueOf(node.getPort()));
        labelLayerNumberAndNodeName.setText(node.getNodeFullName());
        labelForwarding.setText(node.getNextLayerNodeHost() + ":" + node.getNextLayerNodePort());

        node.startListening();

        SoapChat.onlineNodeObservableList.add(new OnlineNode(routerNode.getLayerNumber(), routerNode.getNodeName(),
                node.getPort(), "R - " + routerNode.getNextRouterNodeHost() + ":"
                + routerNode.getNextRouterNodePort(), node.getNextLayerNodeHost(), node.getNextLayerNodePort()));
        refreshTableView();

        try {
            node.sendSoapMessage("", "", "global_broadcast",
                    new MessageBody(getOnlineNodes()));
        } catch (SOAPException | JAXBException ignored) {
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        simpleDateFormat = new SimpleDateFormat("[HH:mm:ss]");
        customMessageBox = new CustomMessageBox("image/icon.png");
        initTableView();
    }

    @FXML
    void buttonSendToAllNodes_onAction() {
        try {
            node.sendSoapMessage("", "", "global_broadcast",
                    new MessageBody(textAreaNewMessage.getText()));
            textAreaNewMessage.clear();
        } catch (SOAPException | JAXBException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja wysłania wiadomości nie powiodła się.",
                    "Powód: " + e.getMessage() + ".").showAndWait();
        }
    }

    @FXML
    void buttonSendToGroupOfSelectedNode_onAction() {
        OnlineNode selectedNode = tableViewOnlineNodes.getSelectionModel().getSelectedItem();
        if (selectedNode != null) {
            try {
                node.sendSoapMessage(selectedNode.getLayerNumber(), "", "local_broadcast",
                        new MessageBody(textAreaNewMessage.getText()));
                textAreaNewMessage.clear();
            } catch (SOAPException | JAXBException e) {
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                        "Operacja wysłania wiadomości nie powiodła się.",
                        "Powód: " + e.getMessage() + ".").showAndWait();
            }
        } else
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja wysłania wiadomości warstwy konkretnego węzła nie powiedzie się.",
                    "Powód: nie zaznaczono węzła.").showAndWait();
    }

    @FXML
    void buttonSendToSelectedNode_onAction() {
        OnlineNode selectedNode = tableViewOnlineNodes.getSelectionModel().getSelectedItem();
        if (selectedNode != null) {
            try {
                System.out.println("SIEMA: " + selectedNode.getLayerNumber());
                node.sendSoapMessage(selectedNode.getLayerNumber(), selectedNode.getNodeName(), "unicast",
                        new MessageBody(textAreaNewMessage.getText()));
                textAreaNewMessage.clear();
            } catch (SOAPException | JAXBException e) {
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                        "Operacja wysłania wiadomości nie powiodła się.",
                        "Powód: " + e.getMessage() + ".").showAndWait();
            }
        } else
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja wysłania wiadomości do konkretnego węzła nie powiedzie się.",
                    "Powód: nie zaznaczono węzła.").showAndWait();
    }

    private void initTableView() {
        tableColumnLayerNumberAndNodeName.setCellValueFactory(new PropertyValueFactory<>("layerNumberAndNodeName"));
        tableColumnPort.setCellValueFactory(new PropertyValueFactory<>("port"));
        tableColumnNodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
        tableColumnNextHost.setCellValueFactory(new PropertyValueFactory<>("nextHost"));
        tableColumnNextPort.setCellValueFactory(new PropertyValueFactory<>("nextPort"));

        tableViewOnlineNodes
                .setItems(FXCollections.observableArrayList(new ArrayList<>(SoapChat.onlineNodeObservableList)));
    }

    private void refreshTableView() {
        tableViewOnlineNodes
                .setItems(FXCollections.observableArrayList(new ArrayList<>(SoapChat.onlineNodeObservableList)));
    }

    public void showReceivedMessage(String sender, String message) {
        String receivedMessage = simpleDateFormat.format(new Date()) + " " + sender + ": " + message;
        textAreaMessages.appendText(receivedMessage + "\n");
    }

    public void showWarning(String exceptionMessage) {
        customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                "Operacja wymiany informacji nie powiodła się.",
                "Powód: " + exceptionMessage + ".").showAndWait();
    }
}
