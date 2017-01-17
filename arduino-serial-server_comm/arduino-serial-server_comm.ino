#include "functions.h"


  
void setup() {
    Serial.begin(250000);
    pinMode(PORT_LED,OUTPUT);
    pinMode(PORT_LED_BRIGHTNESS,OUTPUT);
    pinMode(PORT_SWITCH,INPUT_PULLUP);
    digitalWrite(PORT_LED,HIGH);
    digitalWrite(PORT_LED_BRIGHTNESS,255);
    Serial.println("!start arduino Serial?");
    for(int i = 1;i<=3;i++){
      Serial.print("!");
      Serial.print(i);
      Serial.println("?");
      delay(500);
    
    }

}
void loop() {
  delay(300);
  if(isAwake == false){
    Serial.println("!awake?");
  }
  
  getResistance();
  readSwitch();
  readSerialInput();

}
