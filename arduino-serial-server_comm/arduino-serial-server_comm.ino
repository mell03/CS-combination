#include "functions.h"


  
void setup() {
    Serial.begin(250000);
    pinMode(PORT_LED,OUTPUT);
    pinMode(PORT_LED_BRIGHTNESS,OUTPUT);
    pinMode(PORT_SWITCH,INPUT_PULLUP);
    digitalWrite(PORT_LED,HIGH);
    digitalWrite(PORT_LED_BRIGHTNESS,255);
    Serial.println("!start arduino Serial?");
}
void loop() {
  delay(300);
  if(isAwake == false){// for due board wake the application up // if(the application do not respond, it is in starting process
    Serial.println("!awake?");
  }
  
  getResistance(); // if new resistance is different from last resistance, send new resistance
  readSwitch();      //check push button switch is pushed
  readSerialInput(); // check which serial input is any command

}
