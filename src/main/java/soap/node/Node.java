package soap.node;

import javafx.controller.NodeController;
import soap.MessageBody;
import soap.MessageHeader;
import soap.Soap;

import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Node {
    private NodeController nodeController;
    private String layerNumber;
    private String nodeName;
    private Integer port;
    private Integer nextLayerNodePort;
    private String nextLayerNodeHost;

    private volatile boolean listening = true;
    private ServerSocket serverSocket;

    Node(NodeController nodeController, String layerNumber, String nodeName, Integer port, Integer nextLayerNodePort,
         String nextLayerNodeHost) {
        this.nodeController = nodeController;
        this.layerNumber = layerNumber;
        this.nodeName = nodeName;
        this.port = port;
        this.nextLayerNodePort = nextLayerNodePort;
        this.nextLayerNodeHost = nextLayerNodeHost;
    }

    NodeController getNodeController() {
        return nodeController;
    }

    public String getLayerNumber() {
        return layerNumber;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getNodeFullName() {
        return layerNumber + "|" + nodeName;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getNextLayerNodePort() {
        return nextLayerNodePort;
    }

    public String getNextLayerNodeHost() {
        return nextLayerNodeHost;
    }


    public void startListening() {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        Thread thread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(this.port);
                while (listening) {
                    Socket clientSocket = serverSocket.accept();
                    executorService.submit(() -> {
                        try {
                            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null,
                                    clientSocket.getInputStream());
                            onSoapMessageReceived(soapMessage);
                            clientSocket.close();
                        } catch (IOException | SOAPException ignored) {
                        }
                    });
                }
            } catch (IOException ignored) {
            } finally {
                executorService.shutdown();
            }
        });

        thread.start();
    }

    public void stopListening() throws IOException {
        serverSocket.close();
        this.listening = false;
    }

    public void sendSoapMessage(String receiverLayerNumber, String receiverNodeName, String messageType,
                                MessageBody messageBody) throws SOAPException, JAXBException {
        SOAPMessage soapMessage = Soap.createSoapMessage(new MessageHeader(layerNumber, nodeName, receiverLayerNumber,
                receiverNodeName, messageType), messageBody);

        transferSoapMessage(soapMessage);
    }

    private void sendSoapMessage(SOAPMessage soapMessage) {
        transferSoapMessage(soapMessage);
    }

    void forwardSoapMessage(SOAPMessage soapMessage, String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            soapMessage.writeTo(socket.getOutputStream());
            socket.getOutputStream().flush();
            socket.close();
        } catch (Exception ignored) {
        }
    }

    private void onSoapMessageReceived(SOAPMessage soapMessage) {
        try {
            MessageHeader messageHeader = Soap.getMessageHeader(soapMessage);

            if (messageHeader.checkIfVisitedNodesContainsNode(getNodeFullName()))
                return;

            if (messageHeader.isReceiver(getLayerNumber(), getNodeName())) {
                MessageBody messageBody = Soap.getMessageBody(soapMessage);

                if (messageBody.getOnlineNodeSet() == null) {
                    getNodeController().showReceivedMessage(messageHeader.getSender(), messageBody.getMessage());
                } else {
                    Set<OnlineNode> onlineNodeSet = nodeController.getOnlineNodes();
                    onlineNodeSet.addAll(messageBody.getOnlineNodeSet());

                    if (onlineNodeSet.size() > messageBody.getOnlineNodeSet().size()) {
                        messageBody.setOnlineNodeSet(onlineNodeSet);
                        soapMessage = Soap.createSoapMessage(messageHeader, messageBody);
                        messageHeader.setVisitedNodes(null);
                    } else {
                        messageHeader.addVisitedNode(getNodeFullName());
                        messageBody = Soap.getMessageBody(soapMessage);
                        soapMessage = Soap.createSoapMessage(messageHeader, messageBody);
                    }
                    nodeController.setOnlineNodes(messageBody.getOnlineNodeSet());
                    sendSoapMessage(soapMessage);
                }
            }

            if (messageHeader.isGlobalBroadcast() || messageHeader.isLocalBroadcast()
                    || !messageHeader.isReceiver(getLayerNumber(), getNodeName()))
                if (!messageHeader.checkIfVisitedNodesContainsNode(getNodeFullName())) {
                    messageHeader.addVisitedNode(getNodeFullName());
                    MessageBody messageBody = Soap.getMessageBody(soapMessage);
                    soapMessage = Soap.createSoapMessage(messageHeader, messageBody);
                    sendSoapMessage(soapMessage);
                }
        } catch (SOAPException | JAXBException e) {
            getNodeController().showWarning(e.getMessage());
        }
    }

    abstract protected void transferSoapMessage(SOAPMessage soapMessage);
}
