package com.league55.esp8266.models;

import java.io.Serializable;

public class LED implements Serializable {
    private LEDColor color;
    private String operation;

    LED(String color) {
        this.color = LEDColor.valueOf(color);
    }

    public LEDColor getColor() {
        return color;
    }

    public void setColor(LEDColor color) {
        this.color = color;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
