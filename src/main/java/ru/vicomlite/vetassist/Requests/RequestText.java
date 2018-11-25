package ru.vicomlite.vetassist.Requests;

public class RequestText {

    public String getXcByInn(String currentInn) {
        String requestText = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
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
        "</soapenv:Envelope>";
        return requestText;
    }
}
