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
