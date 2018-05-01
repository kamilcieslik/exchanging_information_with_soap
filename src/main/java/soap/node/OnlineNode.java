package soap.node;

public class OnlineNode {
   private String layerNumberAndNodeName;
   private Integer port;
   private String nodeType;
   private String nextHost;
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

