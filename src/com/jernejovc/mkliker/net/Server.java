package com.jernejovc.mkliker.net;

import com.jernejovc.mkliker.message.Message;

import android.util.Log;
import android.widget.Toast;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;

public class Server {
	private final WebSocketConnection m_connection;
	private KlikerWebSocketHandler m_handler;
	private String m_name;
	private String m_url;
	private String m_sms;
	private boolean m_isDefault;
	
	public Server(String p_name, String p_url, String p_sms ,boolean p_isDefault) {
		m_name = p_name;
		m_url = p_url;
		m_sms = p_sms;
		m_isDefault = p_isDefault;
		m_connection = new WebSocketConnection();
	}
	
	public boolean connect() {
		try {
			m_connection.connect(getWSURI(), m_handler);
			return true;
		} catch (WebSocketException e) {
			Toast.makeText(null, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	
	public WebSocketConnection getConnection() {
		return m_connection;
	}
	
	public String getName() {
		return m_name;
	}

	public void setName(String p_name) {
		m_name = p_name;
	}

	public void setUrl(String p_url) {
		m_url = p_url;
	}
	
	public void setIsDefault(boolean p_isDefault) {
		m_isDefault = p_isDefault;
	}

	public String getUrl() {
		return m_url;
	}

	public boolean getIsDefault() {
		return m_isDefault;
	}
	
	public String getWSURI() {
		return "ws://" + m_url + ":443";
	}
	
	public void send(Message msg) {
		if(m_connection.isConnected()) {
			Log.d("mKliker", "Sent message: " + msg.toWSForm());
			m_connection.sendTextMessage(msg.toWSForm());
		}
	}
	
	public void setReceiver(ReceiveMessage receiver) {
		m_handler.setReceiver(receiver);
	}
	
	public void setHandler(KlikerWebSocketHandler handler) {
		m_handler = handler;
	}
	
	public String getSMSNumber() {
		return m_sms;
	}
	
	public void setSMSNumber(String sms) {
		// TODO: Validation?
		m_sms = sms;
	}
}
