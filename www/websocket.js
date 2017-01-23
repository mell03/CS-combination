/**
 * Created by rlarl on 2017-01-19.
 */
var isStart = false;
var inst = "";
if ("WebSocket" in window) {
    var ws = new WebSocket("ws://114.202.97.238:8090");

    ws.onopen = function () {
        ws.send("!START?");
        console.log("start");
    };
    ws.onmessage = function (evt) {
        var received_msg = evt.data;

        for (var i = 0; i < received_msg.length - 1; i++) {
            if (received_msg.charAt(i) == '!')
                isStart = true;
            else if (received_msg.charAt(i) == '?') {
                isStart = false;
                parseServerMsg(inst);
                //  console.log('inst : ' + inst);
                inst = "";
            }
            else if (isStart == true && (('A' <= received_msg.charAt(i) && received_msg.charAt(i) <= 'Z') || ('0' <= received_msg.charAt(i) && received_msg.charAt(i) <= '9')))
                inst += received_msg.charAt(i);
        }
    };
    ws.onclose = function () {
        // websocket is closed.
        //alert("Connection is closed...");
    };
}
else     alert("WebSocket NOT supported by your Browser!");

