package ru.vicomlite.vetassist;

import org.w3c.dom.NodeList;
import ru.vicomlite.vetassist.Requests.ResponseValue;
import ru.vicomlite.vetassist.settings.Config;
import ru.vicomlite.vetassist.settings.RequestType;

import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class CallWebService {
    private final RequestType requestType;
    private final String searchValue;

    public CallWebService(RequestType requestType, String value) {
        this.requestType = requestType;
        this.searchValue = value;
    }

    private SOAPMessage createSOAPRequest() throws Exception {
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
        SOAPElement soapElement = soapBody.addChildElement("getBusinessEntityListRequest", "ws").addChildElement("businessEntity","dt");
        if (requestType == RequestType.INN_TO_XC) {
            soapElement.addChildElement("inn", "dt").
                    addTextNode(searchValue);
        }
        if (requestType == RequestType.GUID_TO_XC) {
            soapElement.addChildElement("guid", "bs").
                    addTextNode(searchValue);
        }
    }


    public void callSoapWebService() {
        SOAPMessage soapRequest  = null;
        SOAPMessage soapResponse = null;
        try {
            // Создание SOAP Connection
            SOAPConnectionFactory soapFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnect = soapFactory.createConnection();

            String destination = "https://api.vetrf.ru/platform/services/2.0/EnterpriseService";
            //soapRequest = createSOAPRequest();
            switch (requestType) {
                case INN_TO_XC:
                    destination = "https://api.vetrf.ru/platform/services/2.0/EnterpriseService";
                    soapRequest = createSOAPRequest();
                    break;
                case GUID_TO_XC:
                    destination = "https://api.vetrf.ru/platform/services/2.0/EnterpriseService";
                    soapRequest = createSOAPRequest();
                    break;
                case XC_TO_LOCATION:
                    destination = "https://api.vetrf.ru/platform/services/2.0/EnterpriseService";
                    break;
                case LOCATION_TO_PRODUCTION:
                    destination = "https://api.vetrf.ru/platform/services/2.0/ProductService";
                    break;
            }
            System.out.println("Request:");
            soapRequest.writeTo(System.out);
            System.out.println("\n");

            soapResponse = soapConnect.call(soapRequest, destination);
            SOAPBody soapBody = soapResponse.getSOAPBody();
            soapResponse.writeTo(System.out);
            System.out.println("\n");

            NodeList businessEntity = soapBody.getElementsByTagName("dt:businessEntity");
            NodeList childNodes = businessEntity.item(0).getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                System.out.println(childNodes.item(i).getNodeName() + " " + childNodes.item(i).getTextContent() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printSOAPMessage (SOAPMessage soapResponse)
    {
        TransformerFactory transformerFactory;
        Transformer transformer;
        try {
            // Создание XSLT-процессора
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            // Получение содержимого ответа
            Source content;
            content = soapResponse.getSOAPPart().getContent();
            // Определение выходного потока
            StreamResult result = new StreamResult(System.out);
            transformer.transform(content, result);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ResponseValue> parseResponse(RequestType requestType, SOAPBody soapBody) {
        Iterator itr = soapBody.getChildElements();

                return null;
    }
}
