#define PORT_LED 6  //on/off led
#define PORT_LED_BRIGHTNESS 7 //brightness led
#define PORT_RESISTANCE 0 //resistance analog input
#define PORT_SWITCH 8 //push button switch 

String instruction = "";// the instruction start!, end?  ex) !ON?
bool isStart = false;      // instruction start mode ex) !O ...  // not readed ? chararcter
bool isLedOn = true;   // on/off led flag
int brightness = 255;   // brightness led's brightness value
int resistance = -1;    // resistance first value is -1 so that first data can be readed
bool isAwake = false; // the application start flag
