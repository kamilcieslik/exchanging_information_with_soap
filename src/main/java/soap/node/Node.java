package soap.node;

import javafx.controller.NodeController;
import soap.MessageHeader;
import soap.SoapUtil;

import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Node {
    private NodeController nodeController;
    private String layerNumber;
    private String nodeName;
    private Integer port;
    private Integer nextLayerNodePort;
    private String nextLayerNodeHost;

    private volatile boolean threadRunning = true;
    private ServerSocket serverSocket;

    public Node(NodeController nodeController, String layerNumber, String nodeName, Integer port, Integer nextLayerNodePort, String nextLayerNodeHost) {
        this.nodeController = nodeController;
        this.layerNumber = layerNumber;
        this.nodeName = nodeName;
        this.port = port;
        this.nextLayerNodePort = nextLayerNodePort;
        this.nextLayerNodeHost = nextLayerNodeHost;
    }

    public NodeController getNodeController() {
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
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

        int nodePort = this.port;

        Thread thread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(nodePort);
                while (threadRunning) {
                    Socket clientSocket = serverSocket.accept();
                    clientProcessingPool.submit(() -> {
                        try {
                            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null,
                                    clientSocket.getInputStream());
                            this.onSoapMessageReceived(soapMessage);
                            clientSocket.close();
                        } catch (IOException | SOAPException ignored) {
                        }

                    });
                }
            } catch (IOException ignored) {
            } finally {
                clientProcessingPool.shutdown();
            }
        });

        thread.start();
    }

    public void stopListening() throws IOException {
        serverSocket.close();
        this.threadRunning = false;
    }

    public void sendMessage(String receiverLayerNumber, String receiverNodeName, String messageType, String message) throws SOAPException, JAXBException {
        SOAPMessage soapMessage = SoapUtil.createEnvelope(new MessageHeader(layerNumber, nodeName, receiverLayerNumber, receiverNodeName, messageType), message);

        onSoapMessageReadyToSend(soapMessage);
    }

    public void sendMessage(SOAPMessage soapMessage) throws SOAPException, UnknownHostException, IOException {
        onSoapMessageReadyToSend(soapMessage);
    }

    protected void forwardTo(SOAPMessage soapMessage, String host, int port) throws SOAPException, IOException {
        Socket socket = new Socket(host, port);
        soapMessage.writeTo(socket.getOutputStream());
        socket.getOutputStream().flush();
        socket.close();
    }

    public void onSoapMessageReceived(SOAPMessage soapMessage) {
        try {
            MessageHeader messageHeader = SoapUtil.extractMessageHeader(soapMessage);

            if (messageHeader.checkIfVisitedNodesContainsNode(getNodeFullName()))
                return;

            if (messageHeader.isReceiver(getLayerNumber(), getNodeName()))
                getNodeController().showReceivedMessage(messageHeader.getSender(), SoapUtil.extractMessage(soapMessage));

            if (messageHeader.isGlobalBroadcast() || messageHeader.isLocalBroadcast() || !messageHeader.isReceiver(getLayerNumber(), getNodeName()))
                if (messageHeader.checkIfVisitedNodesContainsNode(getNodeFullName())) {
                    SoapUtil.addPathNode(messageHeader, soapMessage, getNodeFullName());
                    sendMessage(soapMessage);
                }
        } catch (SOAPException | IOException | JAXBException e) {
            e.printStackTrace();
            getNodeController().showWarning(e.getMessage());
        }
    }

    abstract protected void onSoapMessageReadyToSend(SOAPMessage soapMessage);
}
