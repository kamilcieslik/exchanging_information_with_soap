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
    public static SOAPMessage createEnvelope(MessageHeader messageHeader, String message) throws SOAPException, JAXBException {
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        StringWriter messageHeaderXml = new StringWriter();
        JAXBContext.newInstance(MessageHeader.class).createMarshaller().marshal(messageHeader, messageHeaderXml);
        soapMessage.getSOAPHeader().addChildElement(new QName("head", "messageHeader"))
                .setTextContent(String.valueOf(messageHeaderXml));

        soapMessage.getSOAPBody().addBodyElement(new QName("message")).setTextContent(message);

        return soapMessage;
    }

    public static MessageHeader extractMessageHeader(SOAPMessage soapMessage) throws SOAPException, JAXBException {
        StringReader messageHeaderXml = new StringReader (((SOAPElement)soapMessage.getSOAPHeader()
                .getChildElements(new QName("head", "messageHeader")).next()).getTextContent());
        return (MessageHeader) JAXBContext.newInstance(MessageHeader.class)
                .createUnmarshaller().unmarshal(messageHeaderXml);
    }

    public static String extractMessage(SOAPMessage soapMessage) throws SOAPException {
        return ((SOAPElement) soapMessage.getSOAPBody().getChildElements(new QName("message")).next()).getTextContent();
    }

    public static void addPathNode(MessageHeader messageHeader, SOAPMessage soapMessage, String nodeFullName) throws SOAPException, JAXBException {
        messageHeader.addVisitedNode(nodeFullName);
        StringWriter messageHeaderXml = new StringWriter();
        JAXBContext.newInstance(MessageHeader.class).createMarshaller().marshal(messageHeader, messageHeaderXml);
        soapMessage.getSOAPHeader().addChildElement(new QName("head", "messageHeader"))
                .setTextContent(String.valueOf(messageHeaderXml));
    }
}
