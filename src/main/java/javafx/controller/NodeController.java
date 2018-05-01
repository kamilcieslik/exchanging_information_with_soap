package javafx.controller;

import app.SoapChat;
import javafx.CustomMessageBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import soap.OnlineNode;

import java.net.URL;
import java.util.ResourceBundle;

public class NodeController implements Initializable {
    private CustomMessageBox customMessageBox;

    @FXML
    private Label labelNodeType, labelHostAndPort, labelLayerNumberAndNodeName, labelForwarding;

    @FXML
    private TableView<OnlineNode> tableViewOnlineNodes;

    @FXML
    private TableColumn<OnlineNode, String> tableColumnLayerNumber, tableColumnNodeName, tableColumnNodeType,
            tableColumnHost;

    @FXML
    private TableColumn<OnlineNode, Integer> tableColumnPort;

    @FXML
    private TextArea textAreaNewMessage, textAreaMessages;


    public void initialize(URL location, ResourceBundle resources) {
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

    }

    private void initTableView(){
        tableColumnLayerNumber.setCellValueFactory(new PropertyValueFactory<>("layerNumber"));
        tableColumnNodeName.setCellValueFactory(new PropertyValueFactory<>("nodeName"));
        tableColumnNodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
        tableColumnHost.setCellValueFactory(new PropertyValueFactory<>("host"));
        tableColumnPort.setCellValueFactory(new PropertyValueFactory<>("port"));

        tableViewOnlineNodes.setItems(SoapChat.onlineNodeObservableList);
    }
}
