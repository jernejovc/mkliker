package com.jernejovc.mkliker.message;

import java.util.Locale;


public class Message {
	public static final String SEPARATOR = "ß";
	private MessageType m_messageType;
	private String m_message;
	
	public Message(MessageType mt, String msg) {
		m_messageType = mt;
		m_message = msg;
	}
	
	public String toWSForm() {
		return String.format(Locale.getDefault(), 
				"%s%s%s", 
				MessageTypeUtil.messageTypeToString(m_messageType),
				Message.SEPARATOR,
				m_message);
				
	}
	
	public MessageType messageType() {
		return m_messageType;
	}
	
	public String message() {
		return m_message;
	}
}
