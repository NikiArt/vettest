package ru.vicomlite.vetassist;

import ru.vicomlite.vetassist.settings.RequestType;

public class Application {

    public static void main(String[] args) {
        CallWebService callXc = new CallWebService(RequestType.INN_TO_XC, "7705183476");
        callXc.callSoapWebService();
    }

}