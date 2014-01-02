package com.jernejovc.mkliker.net;

import android.util.Log;

import com.jernejovc.mkliker.message.MessageFactory;

import de.tavendo.autobahn.WebSocketHandler;

public class KlikerWebSocketHandler extends WebSocketHandler {
	ReceiveMessage m_receiver;
	WaitForConnection m_waitforconnection;
	
	public KlikerWebSocketHandler() {
		super();
	}
	
	public KlikerWebSocketHandler(WaitForConnection conn) {
		super();
		m_waitforconnection = conn;  
	}
	
	@Override
	public void onClose(int code, String reason) {
		// TODO Auto-generated method stub
		super.onClose(code, reason);
		Log.e("mKliker", "KlikerWebSocketHandler: onClose. Error code: " + code + " reason: " + reason);
		m_waitforconnection.connectionEstablished();
	}

	@Override
	public void onOpen() {
		// TODO Auto-generated method stub
		super.onOpen();
		Log.e("mKliker", "KlikerWebSocketHandler: onOpen.");
		m_waitforconnection.connectionEstablished();
	}

	@Override
	public void onTextMessage(String payload) {
		// TODO Auto-generated method stub
		//super.onTextMessage(payload);
		Log.d("mKliker", "Received: " + payload);
		m_receiver.receiveMessage(MessageFactory.fromWSString(payload));
	}
	
	public void setReceiver(ReceiveMessage receiver) {
		m_receiver = receiver;
	}
	
	public void setConnectionWaiter(WaitForConnection waiter) {
		m_waitforconnection = waiter;
	}
}
