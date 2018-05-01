package soap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.StringReader;
import java.io.StringWriter;

public class SoapUtil {
    public static SOAPMessage createEnvelope(MessageHeader messageHeader, MessageBody messageBody) throws SOAPException, JAXBException {
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        StringWriter messageHeaderXml = new StringWriter();
        JAXBContext.newInstance(MessageHeader.class).createMarshaller().marshal(messageHeader, messageHeaderXml);
        soapMessage.getSOAPHeader().addChildElement(new QName("head", "messageHeader"))
                .setTextContent(String.valueOf(messageHeaderXml));

        StringWriter messageBodyXml = new StringWriter();
        JAXBContext.newInstance(MessageBody.class).createMarshaller().marshal(messageBody, messageBodyXml);
        soapMessage.getSOAPBody().addBodyElement(new QName( "message"))
                .setTextContent(String.valueOf(messageBodyXml));

        return soapMessage;
    }

    public static MessageHeader extractMessageHeader(SOAPMessage soapMessage) throws SOAPException, JAXBException {
        StringReader messageHeaderXml = new StringReader (((SOAPElement)soapMessage.getSOAPHeader()
                .getChildElements(new QName("head", "messageHeader")).next()).getTextContent());
        return (MessageHeader) JAXBContext.newInstance(MessageHeader.class)
                .createUnmarshaller().unmarshal(messageHeaderXml);
    }

    public static MessageBody extractMessage(SOAPMessage soapMessage) throws SOAPException, JAXBException {
        StringReader messageBodyXml = new StringReader (((SOAPElement)soapMessage.getSOAPBody()
                .getChildElements(new QName("message")).next()).getTextContent());
        return (MessageBody) JAXBContext.newInstance(MessageBody.class)
                .createUnmarshaller().unmarshal(messageBodyXml);
    }

    public static void addPathNode(MessageHeader messageHeader, SOAPMessage soapMessage, String nodeFullName) throws SOAPException, JAXBException {
        messageHeader.addVisitedNode(nodeFullName);
        StringWriter messageHeaderXml = new StringWriter();
        JAXBContext.newInstance(MessageHeader.class).createMarshaller().marshal(messageHeader, messageHeaderXml);
        soapMessage.getSOAPHeader().addChildElement(new QName("head", "messageHeader"))
                .setTextContent(String.valueOf(messageHeaderXml));
    }

    public static void addOnlineNodes(MessageHeader messageHeader, MessageBody messageBody, SOAPMessage soapMessage) throws SOAPException, JAXBException {
        messageHeader.setVisitedNodes(null);
        StringWriter messageHeaderXml = new StringWriter();
        JAXBContext.newInstance(MessageHeader.class).createMarshaller().marshal(messageHeader, messageHeaderXml);
        soapMessage.getSOAPHeader().addChildElement(new QName("head", "messageHeader"))
                .setTextContent(String.valueOf(messageHeaderXml));

        StringWriter messageBodyXml = new StringWriter();
        JAXBContext.newInstance(MessageBody.class).createMarshaller().marshal(messageBody, messageBodyXml);
        soapMessage.getSOAPBody().addBodyElement(new QName( "message"))
                .setTextContent(String.valueOf(messageBodyXml));
    }
}
