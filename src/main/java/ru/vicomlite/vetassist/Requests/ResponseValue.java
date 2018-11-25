package ru.vicomlite.vetassist.Requests;

import lombok.Getter;

@Getter
public class ResponseValue {
    private final String type;
    private final String value;

    public ResponseValue(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
