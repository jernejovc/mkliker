package com.jernejovc.mkliker.net;

import com.jernejovc.mkliker.message.Message;

import android.util.Log;
import android.widget.Toast;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;

/**
 * Class that holds the information about a certain server and
 * is able to connect to that server.
 * @author matej
 *
 */
public class Server {
	private final WebSocketConnection m_connection;
	private KlikerWebSocketHandler m_handler;
	private String m_name;
	private String m_url;
	private String m_sms;
	private boolean m_isDefault;

	/**
	 * 
	 * @param p_name Name of the server
	 * @param p_url URL of the server, plain name (e.g. 'example.given.com')
	 * @param p_sms SMS number of the server, if available
	 * @param p_isDefault Whether the server is set as default
	 */
	public Server(String p_name, String p_url, String p_sms, boolean p_isDefault) {
		m_name = p_name;
		m_url = p_url;
		m_sms = p_sms;
		m_isDefault = p_isDefault;
		m_connection = new WebSocketConnection();
	}

	/**
	 * Connects to the server using WebSocket connection
	 * @return whether the connection has been successful or not
	 */
	public boolean connect() {
		try {
			m_connection.connect(getWSURI(), m_handler);
			return true;
		} catch (WebSocketException e) {
			Toast.makeText(null, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	/**
	 * 
	 * @return The raw WebSocket connection of this server
	 */
	public WebSocketConnection getConnection() {
		return m_connection;
	}

	/**
	 * @return Whether the server is marked as default
	 */
	public boolean getIsDefault() {
		return m_isDefault;
	}

	/**
	 * 
	 * @return Name of the server
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * @return SMS number of the server
	 */
	public String getSMSNumber() {
		return m_sms;
	}

	/**
	 * @return URL of the server
	 */
	public String getUrl() {
		return m_url;
	}

	/**
	 * @return WebSocket form of the URL
	 */
	public String getWSURI() {
		return "ws://" + m_url + ":443";
	}

	/**
	 * Sends a message to the websocket connection
	 * @param msg Message to be sent
	 */
	public void send(Message msg) {
		if(m_connection.isConnected()) {
			Log.d("mKliker", "Sent message: " + msg.toWSForm());
			m_connection.sendTextMessage(msg.toWSForm());
		}
	}

	/**
	 * Sets the websocket handler.
	 * @param handler
	 */
	public void setHandler(KlikerWebSocketHandler handler) {
		m_handler = handler;
	}

	/**
	 * Marks that the server is default
	 * @param p_isDefault
	 */
	public void setIsDefault(boolean p_isDefault) {
		m_isDefault = p_isDefault;
	}

	/**
	 * Sets the name of the server
	 * @param p_name Name of the server
	 */
	public void setName(String p_name) {
		m_name = p_name;
	}

	/**
	 * Sets the object to which messages will be relayed to
	 * @see {@link ReceiveMessage}
	 */
	public void setReceiver(ReceiveMessage receiver) {
		m_handler.setReceiver(receiver);
	}

	/**
	 * Sets the SMS number of the server
	 * @param sms
	 */
	public void setSMSNumber(String sms) {
		// TODO: Validation?
		m_sms = sms;
	}

	/**
	 * Sets the URL of the server 
	 * @param p_url
	 */
	public void setUrl(String p_url) {
		m_url = p_url;
	}
}
