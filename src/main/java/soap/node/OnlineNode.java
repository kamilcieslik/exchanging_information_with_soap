package soap.node;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

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

    public OnlineNode() {
    }

    public OnlineNode(String layerNumberAndNodeName, Integer port, String nodeType, String nextHost, Integer nextPort) {
        this.layerNumberAndNodeName = layerNumberAndNodeName;
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

    @Override
    public String toString() {
        return "OnlineNode{" +
                "layerNumberAndNodeName='" + layerNumberAndNodeName + '\'' +
                ", port=" + port +
                ", nodeType='" + nodeType + '\'' +
                ", nextHost='" + nextHost + '\'' +
                ", nextPort=" + nextPort +
                '}';
    }
}

