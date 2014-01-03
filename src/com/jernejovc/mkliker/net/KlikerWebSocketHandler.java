package com.jernejovc.mkliker.net;

import android.util.Log;

import com.jernejovc.mkliker.message.MessageFactory;

import de.tavendo.autobahn.WebSocketHandler;

/**
 * WebSocket handler class. Used for receiving messages through WebSocket protocol
 * and relaying those messages to fragments.
 * @author matej
 *
 */
public class KlikerWebSocketHandler extends WebSocketHandler {
	ReceiveMessage m_receiver;
	WaitForConnection m_waitforconnection;
	
	public KlikerWebSocketHandler() {
		super();
	}
	
	/**
	 *  
	 * @param conn Fragment (or a class that implements interface) that 
	 * is waiting for connection to be established through WebSocket protocol.
	 */
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
	
	/**
	 * Sets the receiver Fragment to which all the messages are relayed to.
	 * @param receiver Receiver object
	 */
	public void setReceiver(ReceiveMessage receiver) {
		m_receiver = receiver;
	}
	
	/**
	 * See {@link #KlikerWebSocketHandler(WaitForConnection)}
	 * @param waiter
	 */
	public void setConnectionWaiter(WaitForConnection waiter) {
		m_waitforconnection = waiter;
	}
}
