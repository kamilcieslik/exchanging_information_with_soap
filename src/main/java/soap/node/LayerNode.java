package soap.node;

import javafx.controller.NodeController;

import javax.xml.soap.SOAPMessage;

public class LayerNode extends Node {
    public LayerNode(NodeController nodeController, String layerNumber, String nodeName, Integer port,
                     Integer nextLayerNodePort, String nextLayerNodeHost) {
        super(nodeController, layerNumber, nodeName, port, nextLayerNodePort, nextLayerNodeHost);
    }

    @Override
    protected void transferSoapMessage(SOAPMessage soapMessage) {
        forwardSoapMessage(soapMessage, getNextLayerNodeHost(), getNextLayerNodePort());
    }
}
