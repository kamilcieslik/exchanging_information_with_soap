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

    public void setOnlineNodes(Set<OnlineNode> onlineNodes){
        SoapChat.onlineNodeObservableList = FXCollections.observableSet(onlineNodes);
        refreshTableView();
    }

    public Set<OnlineNode> getOnlineNodes(){
        return SoapChat.onlineNodeObservableList;
    }

    public void setNode(LayerNode layerNode) {
        node = layerNode;

        labelNodeType.setText("L");
        labelPort.setText(String.valueOf(node.getPort()));
        labelLayerNumberAndNodeName.setText(node.getNodeFullName());
        labelForwarding.setText(node.getNextLayerNodeHost() + ":" + node.getNextLayerNodePort());

        node.startListening();

        SoapChat.onlineNodeObservableList.add(new OnlineNode(layerNode.getNodeFullName(), node.getPort(), "L", node.getNextLayerNodeHost(), node.getNextLayerNodePort()));
        try {
            node.sendMessage("1", "R", "global_broadcast", new MessageBody(getOnlineNodes()));
        } catch (SOAPException | JAXBException e) {
            e.printStackTrace();
        }
    }

    public void setNode(RouterNode routerNode) {
        this.node = routerNode;

        labelNodeType.setText("R - " + routerNode.getNextRouterNodeHost() + ":" + routerNode.getNextRouterNodePort());
        labelPort.setText(String.valueOf(node.getPort()));
        labelLayerNumberAndNodeName.setText(node.getNodeFullName());
        labelForwarding.setText(node.getNextLayerNodeHost() + ":" + node.getNextLayerNodePort());

        node.startListening();

       // SoapChat.onlineNodeObservableList.add(new OnlineNode(routerNode.getNodeFullName(), node.getPort(), "R - " + routerNode.getNextRouterNodeHost() + ":" + routerNode.getNextRouterNodePort(), node.getNextLayerNodeHost(), node.getNextLayerNodePort()));
     //   try {
       //     node.sendMessage("1", "A", "unicast", new MessageBody(getOnlineNodes()));
      //  } catch (SOAPException | JAXBException e) {
       //     e.printStackTrace();
      //  }
    }

    public void initialize(URL location, ResourceBundle resources) {
        simpleDateFormat = new SimpleDateFormat("[HH:mm:ss]");
        customMessageBox = new CustomMessageBox("image/icon.png");
        initTableView();
    }

    @FXML
    void buttonSendToAllNodes_onAction() {

    }

    @FXML
    void buttonSendToGroupOfSelectedNode_onAction() {

    }

    @FXML
    void buttonSendToSelectedNode_onAction() {
        try {
            node.sendMessage("1", "A", "unicast", new MessageBody(textAreaNewMessage.getText()));
            textAreaNewMessage.clear();
        } catch (SOAPException | JAXBException e) {
            e.printStackTrace();
        }
    }

    private void initTableView() {
        tableColumnLayerNumberAndNodeName.setCellValueFactory(new PropertyValueFactory<>("layerNumberAndNodeName"));
        tableColumnPort.setCellValueFactory(new PropertyValueFactory<>("port"));
        tableColumnNodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
        tableColumnNextHost.setCellValueFactory(new PropertyValueFactory<>("nextHost"));
        tableColumnNextPort.setCellValueFactory(new PropertyValueFactory<>("nextPort"));

        tableViewOnlineNodes.setItems(FXCollections.observableArrayList(new ArrayList<>(SoapChat.onlineNodeObservableList)));
    }

    private void refreshTableView(){
        tableViewOnlineNodes.setItems(FXCollections.observableArrayList(new ArrayList<>(SoapChat.onlineNodeObservableList)));
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
