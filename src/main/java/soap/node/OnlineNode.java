package soap.node;

import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OnlineNode {
    @XmlAttribute(name = "layerNumberAndNodeName")
    private String layerNumberAndNodeName;
    @XmlAttribute(name = "port")
    private Integer port;
    @XmlAttribute(name = "nodeType")
    private String nodeType;
    @XmlAttribute(name = "nextHost")
    private String nextHost;
    @XmlAttribute(name = "nextPort")
    private Integer nextPort;
    @XmlAttribute(name = "layerNumber")
    private String layerNumber;
    @XmlAttribute(name = "nodeName")
    private String nodeName;

    public OnlineNode() {
    }

    public OnlineNode(String layerNumber, String nodeName, Integer port, String nodeType, String nextHost,
                      Integer nextPort) {
        this.layerNumberAndNodeName = layerNumber + "|" + nodeName;
        this.layerNumber = layerNumber;
        this.nodeName = nodeName;
        this.port = port;
        this.nodeType = nodeType;
        this.nextHost = nextHost;
        this.nextPort = nextPort;
    }

    public String getLayerNumberAndNodeName() {
        return layerNumberAndNodeName;
    }

    public void setLayerNumberAndNodeName(String layerNumberAndNodeName) {
        this.layerNumberAndNodeName = layerNumberAndNodeName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNextHost() {
        return nextHost;
    }

    public void setNextHost(String nextHost) {
        this.nextHost = nextHost;
    }

    public Integer getNextPort() {
        return nextPort;
    }

    public void setNextPort(Integer nextPort) {
        this.nextPort = nextPort;
    }

    public String getLayerNumber() {
        return layerNumber;
    }

    public void setLayerNumber(String layerNumber) {
        this.layerNumber = layerNumber;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String toString() {
        return "OnlineNode{" +
                "layerNumberAndNodeName='" + layerNumberAndNodeName + '\'' +
                ", port=" + port +
                ", nodeType='" + nodeType + '\'' +
                ", nextHost='" + nextHost + '\'' +
                ", nextPort=" + nextPort +
                ", layerNumber='" + layerNumber + '\'' +
                ", nodeName='" + nodeName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlineNode that = (OnlineNode) o;
        return Objects.equals(layerNumberAndNodeName, that.layerNumberAndNodeName) &&
                Objects.equals(port, that.port) &&
                Objects.equals(nodeType, that.nodeType) &&
                Objects.equals(nextHost, that.nextHost) &&
                Objects.equals(nextPort, that.nextPort) &&
                Objects.equals(layerNumber, that.layerNumber) &&
                Objects.equals(nodeName, that.nodeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layerNumberAndNodeName, port, nodeType, nextHost, nextPort, layerNumber, nodeName);
    }
}

