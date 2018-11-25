package ru.vicomlite.vetassist.settings;

import lombok.Getter;

@Getter
public class Config {
    private static Config instance;
    private final String loginXC = "korussng-170925";
    private final String passworXC = "To8qP5Qj7";
    private final String loginUser = "";
    private final String serviceUrlAdress = "https://api.vetrf.ru/platform/services/2.0/EnterpriseService";
    private final String serviceUrlProduct = "https://api.vetrf.ru/platform/services/2.0/ProductService";

    public Config() {    }

    public static synchronized Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
