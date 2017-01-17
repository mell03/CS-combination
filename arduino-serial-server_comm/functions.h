#include "vars.h"
void ledOn() {
	isLedOn = true;
	Serial.println("!ON?");
	digitalWrite(PORT_LED, HIGH);
}
void ledOff() {
	isLedOn = false;
	Serial.println("!OFF?");
	digitalWrite(PORT_LED, LOW);
}

void startInstruction(String inst){

  if(inst=="AWAKE"){
    isAwake=true;
    return;
  }

  //ON led on
  //OFF led off
  //bri 0~255 밝기조절
  if(inst == "ON" || inst=="on"){
    ledOn();
  }
  else if(inst == "OFF"||inst =="off"){
    ledOff();
  }
  else if(inst =="REVERSELED"){
    if(isLedOn == true){
        ledOff();
      }
      else{
        ledOn();
      }
  }
  else if(inst.substring(0,3) =="BRI" || inst.substring(0,3) =="bri"){
    char tempBrightnessCharArray[4];
    inst.substring(3,6).toCharArray(tempBrightnessCharArray,4);
    
    int tempBrightness = atoi(tempBrightnessCharArray);

    if(tempBrightness>=0 && tempBrightness<=255){
      brightness = tempBrightness; 
    }
    else if(tempBrightness<0){
      brightness=0;
    }
    else if(tempBrightness>255){
      brightness = 255;
      
    }
    analogWrite(PORT_LED_BRIGHTNESS,brightness);
    Serial.print("!BRI");
    Serial.print(brightness);
    Serial.println('?');
    return;
  
  }
  return;
}
void readSerialInput() {
	for (; Serial.peek() != -1;) {

		char inputChar = (char)Serial.read();
	

		if (inputChar == '!' && isStart == false) {//input char is ! and not start state then start
												   
			isStart = true;
		}
		else if (inputChar == '!' && isStart == true) { //input char is '!' and start, maybe '?' is missing
                                                                                  //So, run commands and start new instruction 
													   
			startInstruction(instruction);
			instruction = "";
		}
		else if (inputChar == '?' && isStart == true) {//input char is '?' and start state, 
                                                                                  //run commands and set the start state to false
			isStart = false;
			startInstruction(instruction);
			instruction = "";
		}
		else if (inputChar == '?' && isStart == false) {// input char is '?' and not start state
                                                                                  //start char '!' might miss So, run commands			
			startInstruction(instruction);
			instruction = "";
		}
		else if (!isStart) {
							
							//Serial.println("start");
			isStart = true;
			instruction += inputChar;
		}
		else if (isStart) {
			instruction += inputChar;
			//Serial.println(instruction);
		}

	}
}
void readSwitch(){

  if(digitalRead(PORT_SWITCH) == LOW){
    if(isLedOn == true){
      ledOff();
    }
    else{
      ledOn();
    }
  }
}
void getResistance(){
  int tempResistance = analogRead(PORT_RESISTANCE);
  if(Abs(tempResistance -  resistance) > 3){
    resistance = tempResistance;
    Serial.print("!RESIST");
    Serial.print(resistance);
    Serial.println("?");
  }
}
