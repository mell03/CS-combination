#define PORT_LED 6
#define PORT_LED_BRIGHTNESS 7
#define PORT_RESISTANCE 0
#define PORT_SWITCH 8
char input = 0;
char temp[130];
String instruction = "";
bool isStart = false;
bool isLedOn = true;
int brightness = 255;
int resistance = -1;
bool isSwichHigh = false;
bool isAwake = false;
