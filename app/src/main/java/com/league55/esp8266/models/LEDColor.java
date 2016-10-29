package com.league55.esp8266.models;

public enum LEDColor {
    // color code is a kind of convention to simplify code on controller and avoid working with strings
    BLUE(1), RED(2), GREEN(3);

    private int colorCode;

    LEDColor(int colorCode){
        this.colorCode = colorCode;
    }

    public int getColorCode() {
        return colorCode;
    }
}
