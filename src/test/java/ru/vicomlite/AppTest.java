package ru.vicomlite;
import org.junit.Test;
import ru.vicomlite.vetassist.settings.Config;

import javax.xml.soap.*;
import java.util.Base64;

public class AppTest {



    private  String   soapUrl      = null;
    private  String   soapAction   = null;

    @Test
    public void SoapClientExample()
    {
        setSoapParams();
        callSoapWebService(soapUrl, soapAction);
    }
    private void setSoapParams()
    {

            soapUrl      = "https://api.vetrf.ru/platform/services/2.0/EnterpriseService";

        soapAction = "https://api.vetrf.ru/platform/services/2.0/EnterpriseService";
    }

    private void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        /*"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "xmlns:ws=\"http://api.vetrf.ru/schema/cdm/registry/ws-definitions/v2\"\n" +
                "xmlns:bs=\"http://api.vetrf.ru/schema/cdm/base\"\n" +
                "xmlns:dt=\"http://api.vetrf.ru/schema/cdm/dictionary/v2\">\n" +
                "<soapenv:Header/>\n" +
        "<soapenv:Body>\n" +
            "<ws:getBusinessEntityListRequest>\n" +
                "<dt:businessEntity>\n" +
                    "<dt:inn>"+currentInn+"</dt:inn>\n" +
                "</dt:businessEntity>\n" +
            "</ws:getBusinessEntityListRequest>\n" +
        "</soapenv:Body>\n" +
        "</soapenv:Envelope>";*/
        envelope.addNamespaceDeclaration("ws", "http://api.vetrf.ru/schema/cdm/registry/ws-definitions/v2");
        envelope.addNamespaceDeclaration("bs", "http://api.vetrf.ru/schema/cdm/base");
        envelope.addNamespaceDeclaration("dt", "http://api.vetrf.ru/schema/cdm/dictionary/v2");
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.addChildElement("getBusinessEntityListRequest", "ws").
                addChildElement("businessEntity","dt").
                addChildElement("inn", "dt").
                addTextNode("7705183476");

        /*SOAPElement soapBodyElem;
        SOAPElement soapBodyElem1;
        if (belavia) {
            soapBody.addChildElement(serviceName, namespace);
        } else {
            soapBodyElem =soapBody.addChildElement(serviceName, namespace);
            soapBodyElem1=soapBodyElem.addChildElement("USCity",namespace);
            soapBodyElem1.addTextNode("New York");
        }*/
    }

    private SOAPMessage createSOAPRequest(String soapAction) throws Exception {
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
        //MimeHeaders headers = soapMessage.getMimeHeaders();
        //headers.addHeader("SOAPAction", soapAction);

        //soapMessage.saveChanges();

        // Печать XML текста запроса
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;
    }
    private void callSoapWebService(String destination, String soapService) {
        SOAPConnectionFactory soapFactory  = null;
        SOAPConnection        soapConnect  = null;
        SOAPMessage           soapRequest  = null;
        SOAPMessage           soapResponse = null;
        try {
            // Создание SOAP Connection
            soapFactory = SOAPConnectionFactory.newInstance();
            soapConnect = soapFactory.createConnection();

            // Создание SOAP Message для отправки
            soapRequest  = createSOAPRequest(soapAction);

            soapResponse = soapConnect.call(soapRequest, destination);

            System.out.println("Request SOAP Response:");
            soapResponse.writeTo(System.out);
            System.out.println("\n");

            soapConnect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
    }


}
