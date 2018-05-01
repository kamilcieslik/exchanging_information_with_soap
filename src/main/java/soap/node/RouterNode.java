package soap.node;

import javafx.controller.NodeController;
import soap.MessageHeader;
import soap.SoapUtil;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;

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
    protected void onSoapMessageReadyToSend(SOAPMessage soapMessage) {
        try {
            MessageHeader messageHeader = SoapUtil.extractMessageHeader(soapMessage);

            if (messageHeader.isLocalBroadcast() && messageHeader.getReceiverLayerNumber().equals(getLayerNumber())) {
                System.out.println("1");
                forwardTo(soapMessage, getNextLayerNodeHost(), getNextLayerNodePort());
            } else if (messageHeader.isLocalBroadcast()) {
                System.out.println("2");
                forwardTo(soapMessage, nextRouterNodeHost, nextRouterNodePort);
            } else if (messageHeader.isUnicast() && messageHeader.getReceiverLayerNumber().equals(getLayerNumber())) {
                System.out.println("3");
                forwardTo(soapMessage, getNextLayerNodeHost(), getNextLayerNodePort());
            } else if (messageHeader.isUnicast() && !messageHeader.getReceiverLayerNumber().equals(getLayerNumber())) {
                System.out.println("4");
                forwardTo(soapMessage, nextRouterNodeHost, nextRouterNodePort);
            } else {
                System.out.println("5");
                forwardTo(soapMessage, getNextLayerNodeHost(), getNextLayerNodePort());
              //  forwardTo(soapMessage, nextRouterNodeHost, nextRouterNodePort);
            }
        } catch (SOAPException | IOException | JAXBException e) {
            e.printStackTrace();
            getNodeController().showWarning(e.getMessage());
        }
    }
}
