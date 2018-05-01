package soap;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageHeader {
    @XmlAttribute(name = "sender")
    private String sender;
    @XmlAttribute(name = "receiverLayerNumber")
    private String receiverLayerNumber;
    @XmlAttribute(name = "receiverNodeName")
    private String receiverNodeName;
    @XmlAttribute(name = "messageType")
    private String messageType;
    @XmlElementWrapper(name = "visitedNodes")
    @XmlElement(name = "visitedNode")
    private List<String> visitedNodes;

    public MessageHeader() {
    }

    public MessageHeader(String senderLayerNumber, String senderNodeName, String receiverLayerNumber, String receiverNodeName, String messageType) {
        this.sender = senderLayerNumber + "|" + senderNodeName;
        this.receiverLayerNumber = receiverLayerNumber;
        this.receiverNodeName = receiverNodeName;
        this.messageType = messageType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiverLayerNumber + "|" + receiverNodeName;
    }

    public String getReceiverLayerNumber() {
        return receiverLayerNumber;
    }

    public void setReceiverLayerNumber(String receiverLayerNumber) {
        this.receiverLayerNumber = receiverLayerNumber;
    }

    public String getReceiverNodeName() {
        return receiverNodeName;
    }

    public void setReceiverNodeName(String receiverNodeName) {
        this.receiverNodeName = receiverNodeName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public List<String> getVisitedNodes() {
        return visitedNodes;
    }

    public void setVisitedNodes(List<String> visitedNodes) {
        this.visitedNodes = visitedNodes;
    }

    public void addVisitedNode(String visitedNode) {
        if (visitedNodes == null)
            visitedNodes = new ArrayList<>();
        visitedNodes.add(visitedNode);
    }

    public Boolean checkIfVisitedNodesContainsNode(String node) {
        if (visitedNodes != null)
            return visitedNodes.stream().anyMatch(visitedNode -> visitedNode.equals(node));
        else return false;
    }

    public Boolean isUnicast() {
        return messageType.equals("unicast");
    }

    public Boolean isLocalBroadcast() {
        return messageType.equals("local_broadcast");
    }

    public Boolean isGlobalBroadcast() {
        return messageType.equals("global_broadcast");
    }

    public boolean isReceiver(String layerNumber, String nodeName) {
        return (this.receiverLayerNumber.equals(layerNumber) && this.receiverNodeName.equals(nodeName)) || this.isGlobalBroadcast()
                || this.isLocalBroadcast() && this.receiverLayerNumber.equals(layerNumber);
    }

    @Override
    public String toString() {
        return "MessageHeader{" +
                "sender='" + sender + '\'' +
                ", receiverLayerNumber='" + receiverLayerNumber + '\'' +
                ", receiverNodeName='" + receiverNodeName + '\'' +
                ", messageType='" + messageType + '\'' +
                ", visitedNodes=" + visitedNodes +
                '}';
    }
}
