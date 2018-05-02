package soap.node;

import javafx.controller.NodeController;
import soap.MessageHeader;
import soap.Soap;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

public class RouterNode extends Node {
    private Integer nextRouterNodePort;
    private String nextRouterNodeHost;

    public RouterNode(NodeController nodeController, String layerNumber, String nodeName, Integer port,
                      Integer nextLayerNodePort, String nextLayerNodeHost, Integer nextRouterNodePort,
                      String nextRouterNodeHost) {
        super(nodeController, layerNumber, nodeName, port, nextLayerNodePort, nextLayerNodeHost);
        this.nextRouterNodePort = nextRouterNodePort;
        this.nextRouterNodeHost = nextRouterNodeHost;
    }

    public Integer getNextRouterNodePort() {
        return nextRouterNodePort;
    }

    public String getNextRouterNodeHost() {
        return nextRouterNodeHost;
    }

    @Override
    protected void transferSoapMessage(SOAPMessage soapMessage) {
        try {
            MessageHeader messageHeader = Soap.getMessageHeader(soapMessage);
            if (messageHeader.isLocalBroadcast() && messageHeader.getReceiverLayerNumber().equals(getLayerNumber())) {
                forwardSoapMessage(soapMessage, getNextLayerNodeHost(), getNextLayerNodePort());
            } else if (messageHeader.isLocalBroadcast()) {
                forwardSoapMessage(soapMessage, nextRouterNodeHost, nextRouterNodePort);
            } else if (messageHeader.isUnicast() && messageHeader.getReceiverLayerNumber().equals(getLayerNumber())) {
                forwardSoapMessage(soapMessage, getNextLayerNodeHost(), getNextLayerNodePort());
            } else if (messageHeader.isUnicast() && !messageHeader.getReceiverLayerNumber().equals(getLayerNumber())) {
                forwardSoapMessage(soapMessage, nextRouterNodeHost, nextRouterNodePort);
            } else {
                forwardSoapMessage(soapMessage, getNextLayerNodeHost(), getNextLayerNodePort());
                forwardSoapMessage(soapMessage, nextRouterNodeHost, nextRouterNodePort);
            }
        } catch (SOAPException | JAXBException e) {
            getNodeController().showWarning(e.getMessage());
        }
    }
}
