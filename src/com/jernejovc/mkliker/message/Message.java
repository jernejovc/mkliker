package com.jernejovc.mkliker.message;

import java.util.Locale;

/**
 * Class that holds information about the message received from
 * WebSocket connection.
 * @author matej
 *
 */
public class Message {
	public static final String SEPARATOR = "ß";
	private MessageType m_messageType;
	private String m_message;
	
	/**
	 * @param mt Type of the message
	 * @param msg Payload the message carries
	 */
	public Message(MessageType mt, String msg) {
		m_messageType = mt;
		m_message = msg;
	}
	
	/**
	 * Returns the Message in the WebSocket form that mKliker server 
	 * should understand.
	 * @return
	 */
	public String toWSForm() {
		return String.format(Locale.getDefault(), 
				"%s%s%s", 
				MessageTypeUtil.messageTypeToString(m_messageType),
				Message.SEPARATOR,
				m_message);
				
	}
	
	/**
	 * @return MessageType of the Message
	 */
	public MessageType messageType() {
		return m_messageType;
	}
	
	/**
	 * @return Payload of the message
	 */
	public String message() {
		return m_message;
	}
}
