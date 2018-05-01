package soap;

public class OnlineNode {
    private String layerNumber;
    private String nodeName;
    private String nodeType;
    private String host;
    private Integer port;

    public OnlineNode() {
    }

    public OnlineNode(String layerNumber, String nodeName, String nodeType, String host, Integer port) {
        this.layerNumber = layerNumber;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.host = host;
        this.port = port;
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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "OnlineNode{" +
                "layerNumber='" + layerNumber + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}

