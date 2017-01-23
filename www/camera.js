/**
 * Created by rlarl on 2017-01-18.
 */

var PTZ_STOP = 1;
var TILT_UP = 2;
var TILT_UP_STOP = 1;
var TILT_DOWN = 0;
var TILT_DOWN_STOP = 3;
var PAN_LEFT = 6;
var PAN_LEFT_STOP = 5;
var PAN_RIGHT = 4;
var PAN_RIGHT_STOP = 7;
var PTZ_LEFT_UP = 93;
var PTZ_RIGHT_UP = 92;
var PTZ_LEFT_DOWN = 91;
var PTZ_RIGHT_DOWN = 90;
var PTZ_CENTER = 25;
var PTZ_VPATROL = 26;
var PTZ_VPATROL_STOP = 27;
var PTZ_HPATROL = 28;
var PTZ_HPATROL_STOP = 29;
var PTZ_PELCO_D_HPATROL = 20;
var PTZ_PELCO_D_HPATROL_STOP = 21;

var IO_ON = 94;
var IO_OFF = 95;
function decoder_control_onestep(command) {
    action_zone.location = 'http://114.202.97.238:10000/decoder_control.cgi?onestep=1&command=' + command;
}

function decoder_control(command) {
    action_zone.location = 'http://114.202.97.238:10000/decoder_control.cgi?command=' + command;
}
function leftclicked() {
    (flip & 0x02) ? decoder_control_onestep(PAN_RIGHT) : decoder_control_onestep(PAN_LEFT);
}
function rightclicked() {
    (flip & 0x02) ? decoder_control_onestep(PAN_LEFT) : decoder_control_onestep(PAN_RIGHT);
}
function downclicked() {
    (flip & 0x01) ? decoder_control_onestep(TILT_UP) : decoder_control_onestep(TILT_DOWN);
}
function upclicked() {
    (flip & 0x01) ? decoder_control_onestep(TILT_DOWN) : decoder_control_onestep(TILT_UP);
}