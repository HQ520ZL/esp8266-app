#include <ESP8266WiFi.h>
#include <WiFiClient.h> 
#include <ESP8266WebServer.h>

  const char* ssid = "esp8266";

  const int RED = 15;
  const int GREEN = 12;
  const int BLUE = 13;

  int brightnessBlue;
  int brightnessGreen;
  int brightnessRed;

  ESP8266WebServer server(80);

void handleRoot() {
  server.send(200, "text/html", "<h1>You are connected</h1>");
    Serial.println("Handling Root request");

}



boolean handleLED() {
  Serial.println("Handling LED request");
  String message;
for (uint8_t i = 0; i < server.args(); i++) {
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
   Serial.println(message);
  //handles POST request with LED_COLOR and OPERATION in body
  //1 -> blue; 2-> red; 3 -> green;
  const int LED_COLOR = server.arg("LED_COLOR").toInt();;
  const String OPERATION = server.arg("OPERATION");
  if(server.args() == 0) {
     Serial.print(" Can't find argument OPERATOR or LED_COLOR");
    return false;
  }
    
  if(OPERATION == "UP") {
     switch(LED_COLOR){
      case 1: increaseBrightness(&brightnessBlue);
      case 2: increaseBrightness(&brightnessRed);
      case 3: increaseBrightness(&brightnessGreen);                
  }
  } else if(OPERATION == "DOWN") {
    switch(LED_COLOR){
      case 1: decreaseBrightness(&brightnessBlue);
      case 2: decreaseBrightness(&brightnessRed);
      case 3: decreaseBrightness(&brightnessGreen);
     }
  }
    analogWrite(BLUE, brightnessBlue);
    analogWrite(RED, brightnessRed); 
    analogWrite(GREEN, brightnessGreen); 
    server.send(200, "text/plain", String("LED is now "));
    return true;
}

void setup() {
  Serial.begin(115200);
  Serial.println();

//settion up pins
  pinMode(RED, OUTPUT);
  pinMode(GREEN, OUTPUT);
  pinMode(BLUE, OUTPUT);

  brightnessBlue = 0;
  brightnessGreen = 0;
  brightnessRed = 0;

//setting up WI-FI access point
  Serial.print("Setting soft-AP ... ");
  Serial.println(WiFi.softAP(ssid) ? "Ready": "Failed");
  Serial.println("Soft-AP IP address = %d\n");
  Serial.print(WiFi.softAPIP());

// handlers for request
  server.on("/LED", handleLED);
  server.on("/", handleRoot);
  server.begin();
  Serial.println("HTTP server started");
}

void loop() {
server.handleClient();
}

int increaseBrightness(int* brightness){
  if(*brightness < 1023) {
    *brightness +=100;
  }
   Serial.println("New brightness = ");
    Serial.print(*brightness);
  return *brightness;
}

int decreaseBrightness(int* brightness){
  if(*brightness > 0) {
    *brightness -=100;
  }
    Serial.println("New brightness = ");
    Serial.print(*brightness);
  return *brightness;
}