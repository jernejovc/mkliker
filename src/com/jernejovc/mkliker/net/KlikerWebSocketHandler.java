/* 
 * This file is part of mKliker.
 * 
 * mKliker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * mKliker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with mKliker.  If not, see <http://www.gnu.org/licenses/>.
 */

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
