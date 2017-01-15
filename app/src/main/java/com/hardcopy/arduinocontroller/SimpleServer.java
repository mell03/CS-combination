package com.hardcopy.arduinocontroller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * Created by rlarl on 2016-12-29.
 */

public class SimpleServer extends WebSocketServer {
    ArrayList<WebSocket> clientList = new ArrayList<WebSocket>();
    Context mContext = null;
    public SimpleServer(InetSocketAddress address,Context c) {
        super(address);
        mContext = c;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        //System.out.println("new connection to " + conn.getRemoteSocketAddress());
        Log.i("ws onOpen", conn.getRemoteSocketAddress().toString());
        clientList.add(conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.i("ws onClose", "closed " + conn.getRemoteSocketAddress() + " with exit code " + code  + " additional info: " + reason);
        clientList.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        //System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);
        Log.i("ws onMesage","received message from " + conn.getRemoteSocketAddress() + ": "+ message);
        //ArduinoControllerActivity.getSerialConnection().sendCommand(message);
        ((ArduinoControllerActivity)mContext).getSerialCon().sendCommand(message);

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.e("ws onError","an error occured on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }
}
