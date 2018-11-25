package ru.vicomlite.vetassist.Requests;

import ru.vicomlite.vetassist.settings.Config;
import ru.vicomlite.vetassist.settings.RequestType;

import javax.xml.soap.*;
import java.util.Base64;


public class RequsetXc {
    private final String searchValue;
    private final RequestType requestType;

    public RequsetXc(String searchValue, RequestType requestType) {
        this.searchValue = searchValue;
        this.requestType = requestType;
    }

    public SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        // Определение авторизации сервиса
        String loginPassword = Config.getInstance().getLoginXC() + ":" + Config.getInstance().getPassworXC();
        byte[]  bytes = loginPassword.getBytes();
        String auth = new String(Base64.getMimeEncoder().encode(bytes));
        headers.addHeader("Authorization", "Basic " + auth);
        soapMessage.saveChanges();

        return soapMessage;
    }

    private void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ws", "http://api.vetrf.ru/schema/cdm/registry/ws-definitions/v2");
        envelope.addNamespaceDeclaration("bs", "http://api.vetrf.ru/schema/cdm/base");
        envelope.addNamespaceDeclaration("dt", "http://api.vetrf.ru/schema/cdm/dictionary/v2");
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.addChildElement("getBusinessEntityListRequest", "ws").
                addChildElement("businessEntity","dt").
                addChildElement("inn", "dt").
                addTextNode("7705183476");
        /*SOAPBody soapBody = envelope.getBody();
        SOAPElement soapElement = soapBody.addChildElement("getBusinessEntityListRequest", "ws").addChildElement("businessEntity","dt");
        if (requestType == RequestType.INN_TO_XC) {
            soapElement.addChildElement("inn", "dt").
                    addTextNode(searchValue);
        }
        if (requestType == RequestType.GUID_TO_XC) {
            soapElement.addChildElement("guid", "bs").
                    addTextNode(searchValue);
        }*/
    }

}
