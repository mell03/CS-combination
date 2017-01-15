package com.hardcopy.arduinocontroller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;


public class ArduinoControllerActivity extends Activity implements View.OnClickListener {

	private Context mContext = null;
	private ActivityHandler mHandler = null;
	
	private SerialListener mListener = null;
	private SerialConnector mSerialConn = null;
	
	private TextView mTextLog = null;
	private TextView mTextInfo = null;
	private Button mButton1;
	private Button mButton2;
	private Button mButton3;
	private Button mButton4;

	private WebServer server;
	private static final String TAG = "MYSERVER";
	private static final int PORT = 8080;
	private static final int PORT_FOR_WEBSOCKET = 8090;
	String ipAddress;
	SimpleServer wsServer;

	private EditText textBoxForMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		// Layouts
		setContentView(R.layout.activity_arduino_controller);

		// System
		mContext = getApplicationContext();
		
		mTextLog = (TextView) findViewById(R.id.text_serial);
		mTextLog.setMovementMethod(new ScrollingMovementMethod());
		mTextInfo = (TextView) findViewById(R.id.text_info);
		mTextInfo.setMovementMethod(new ScrollingMovementMethod());
		mButton1 = (Button) findViewById(R.id.button_send1);
		mButton1.setOnClickListener(this);
		mButton2 = (Button) findViewById(R.id.button_send2);
		mButton2.setOnClickListener(this);
		mButton3 = (Button) findViewById(R.id.button_send3);
		mButton3.setOnClickListener(this);
		mButton4 = (Button) findViewById(R.id.button_send4);
		mButton4.setOnClickListener(this);

		textBoxForMsg =(EditText) findViewById(R.id.edit_msg_to_send);

		
		// Initialize
		mListener = new SerialListener();
		mHandler = new ActivityHandler();
		
		// Initialize Serial connector and starts Serial monitoring thread.
		mSerialConn = new SerialConnector(mContext, mListener, mHandler);
		mSerialConn.initialize();

		ipAddress = getLocalIpAddress();




		TextView text = (TextView) findViewById(R.id.ipaddr);


		if (ipAddress != null) {
			text.setText("Please Access:" + "http://" + ipAddress + ":" + PORT);
		} else {
			text.setText("Wi-Fi Network Not Available");
		}
		new Thread() {
			public void run() {
				try {
					wsServer = new SimpleServer(new InetSocketAddress(ipAddress, PORT_FOR_WEBSOCKET),ArduinoControllerActivity.this);
					wsServer.run();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		server = new WebServer();
		try {
			server.start();
		} catch(IOException ioe) {
			Log.w("Httpd", "The server could not start.");
		}
		Log.w("Httpd", "Web server initialized.");


	}
	public SerialConnector getSerialCon() {
		return mSerialConn;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		mSerialConn.finalize();
		if (server != null)
			server.stop();
	}
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						String ipAddr = inetAddress.getHostAddress();
						return ipAddr;
					}
				}
			}
		} catch (SocketException ex) {
			Log.d(TAG, ex.toString());
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_send1:
			mSerialConn.sendCommand("Button1");
			break;
		case R.id.button_send2:
			mSerialConn.sendCommand("Button2");
			break;
		case R.id.button_send3:
			mSerialConn.sendCommand("Button3");
			break;
		case R.id.button_send4:
			mSerialConn.sendCommand("Button4");
			break;
		default:
			break;
		}
	}

	
	
	public class SerialListener {
		public void onReceive(int msg, int arg0, int arg1, String arg2, Object arg3) {
			switch(msg) {
			case Constants.MSG_DEVICD_INFO:
				mTextLog.append(arg2);
				break;
			case Constants.MSG_DEVICE_COUNT:
				mTextLog.append(Integer.toString(arg0) + " device(s) found \n");
				break;
			case Constants.MSG_READ_DATA_COUNT:
				mTextLog.append(Integer.toString(arg0) + " buffer received \n");
				break;
			case Constants.MSG_READ_DATA:
				if(arg3 != null) {
					mTextInfo.setText((String)arg3);
					mTextLog.append((String)arg3);
					mTextLog.append("\n");

				}
				break;
			case Constants.MSG_SERIAL_ERROR:
				mTextLog.append(arg2);
				break;
			case Constants.MSG_FATAL_ERROR_FINISH_APP:
				finish();
				break;
			}
		}
	}
	
	public class ActivityHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case Constants.MSG_DEVICD_INFO:
				mTextLog.append((String)msg.obj);
				break;
			case Constants.MSG_DEVICE_COUNT:
				mTextLog.append(Integer.toString(msg.arg1) + " device(s) found \n");
				break;
			case Constants.MSG_READ_DATA_COUNT:
				mTextLog.append(((String)msg.obj) + "\n");
				Log.i("asdf",(String)msg.obj + "\n");
				for(int i = 0 ;i<wsServer.clientList.size();i++) {
					wsServer.clientList.get(i).send((String)msg.obj);
				}


				break;
			case Constants.MSG_READ_DATA:
				if(msg.obj != null) {
					mTextInfo.setText((String)msg.obj);
					mTextLog.append((String)msg.obj);
					mTextLog.append("\n");

				}
				break;
			case Constants.MSG_SERIAL_ERROR:
				mTextLog.append((String)msg.obj);
				break;
			}
		}
	}
	private class WebServer extends NanoHTTPD {

		public WebServer()
		{
			super(PORT);
		}

		@Override
		public Response serve(String uri, Method method,
							  Map<String, String> header,
							  Map<String, String> parameters,
							  Map<String, String> files) {
			NanoHTTPD.Response tempResponse = null;
			String answer = "";
			String ext = Environment.getExternalStorageState();
			if(ext.equals(Environment.MEDIA_MOUNTED)) {

				Log.i("root " , Environment.getExternalStorageDirectory().getAbsolutePath());
			} else {

				Log.i("root " , Environment.getExternalStorageDirectory().getAbsolutePath() + " not mounted");
			}
			if(uri.equals("/")) {
				try {
					// Open file from SD Card
					File root = Environment.getExternalStorageDirectory();
					FileReader index = new FileReader(root.getAbsolutePath() +
							"/www/chatClient.html");
					BufferedReader reader = new BufferedReader(index);
					String line = "";
					while ((line = reader.readLine()) != null) {
						answer += line + '\n';
					}
					tempResponse = new NanoHTTPD.Response(answer);
					reader.close();

				} catch(IOException ioe) {
					Log.w("Httpd", ioe.toString());
				}
			}else {
				try {
					// Open file from SD Card
					File root = Environment.getExternalStorageDirectory();

					FileReader index = new FileReader(root.getAbsolutePath() +
							"/www" + uri);
					BufferedReader reader = new BufferedReader(index);
					String line = "";
					while ((line = reader.readLine()) != null) {
						answer += line +'\n';
					}
					tempResponse = new NanoHTTPD.Response(answer);
					if(uri.substring(uri.length()-3, uri.length()).equals("css")) {
						tempResponse.setMimeType("text/css");
					}

					reader.close();

				} catch(IOException ioe) {
					Log.w("Httpd", ioe.toString());
				}

			}


			return tempResponse;
			//return new NanoHTTPD.Response(answer);
		}
	}
	public void onClickSendBtn(View v) {
		String msg = textBoxForMsg.getText().toString();
		for(int i = 0;i<wsServer.clientList.size();i++) {
			wsServer.clientList.get(i).send("server Message : " + msg);
		}
	}

	
}
