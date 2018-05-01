package soap;

import soap.node.OnlineNode;

import javax.xml.bind.annotation.*;
import java.util.Set;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageBody {
    @XmlAttribute(name = "message")
    private String message;
    @XmlElementWrapper(name = "onlineNodes")
    @XmlElement(name = "onlineNode")
    private Set<OnlineNode> onlineNodeSet;

    public MessageBody() {
    }

    public MessageBody(String message) {
        this.message = message;
    }

    public MessageBody(Set<OnlineNode> onlineNodeSet) {
        this.onlineNodeSet = onlineNodeSet;
    }

    public MessageBody(String message, Set<OnlineNode> onlineNodeSet) {
        this.message = message;
        this.onlineNodeSet = onlineNodeSet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<OnlineNode> getOnlineNodeSet() {
        return onlineNodeSet;
    }

    public void setOnlineNodeSet(Set<OnlineNode> onlineNodeSet) {
        this.onlineNodeSet = onlineNodeSet;
    }
}
