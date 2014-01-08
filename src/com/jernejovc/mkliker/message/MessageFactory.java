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
 * Utility class for creating messages from String values.
 * @author matej
 *
 */
public class MessageFactory {
	
	/**
	 * Creates a LoginStudent message
	 * @param room Room to which the student is logging in
	 * @param pass Password of the room
	 * @return String containing the message
	 */
	public static Message makeLoginStudentMessage(String room, String pass) {
		String payload = String.format(Locale.getDefault(),
				"%s%s%s", 
				room,
				Message.SEPARATOR,
				pass);
		return new Message(MessageType.LOGINSTUDENT, payload);
	}
	
	/**
	 * Creates a StudentAnswer message
	 * @param room Room to which the answer is being sent to
	 * @param userID User's ID
	 * @param answer Answer to be sent
	 * @return String containing the message
	 */
	public static Message makeStudentAnswerMessage(String room, int userID, String answer) {
		String payload = String.format(Locale.getDefault(),
				"%s%s%d%s%s", 
				room,
				Message.SEPARATOR,
				userID, 
				Message.SEPARATOR,
				answer);
		
		return new Message(MessageType.STUDENTANSWER, payload);
	}
	
	/**
	 * Creates a message to the lecturer type Message
	 * @param room Room to which the message is being sent to
	 * @param userID User's ID
	 * @param nickname Nickname of the user
	 * @param message Message to be send
	 * @return String containing the message
	 */
	public static Message makeMessageMessage(String room, int userID, String nickname, String message) {
		String payload = String.format(Locale.getDefault(), 
				"%s%s%s%s%s%s%d", 
				room,
				Message.SEPARATOR,
				nickname,
				Message.SEPARATOR,
				message, 
				Message.SEPARATOR,
				userID);
		
		return new Message(MessageType.MESSAGE, payload);
	}
	
	/**
	 * Make a PingStudent message 
	 * @param room Room to which Ping will be sent
	 * @param userID User's ID
	 * @return
	 */
	public static Message makePingStudentMessage(String room, int userID) {
		String payload = String.format(Locale.getDefault(), 
				"%s%s%d",
				room,
				Message.SEPARATOR,
				userID);
		
		return new Message(MessageType.PINGSTUDENT, payload);
	}

	/**
	 * Converts the message received from mKliker server to {@link Message} object
	 * @param payload Payload received via WebSocket connection
	 * @return
	 */
	public static Message fromWSString(String payload) {
		String[] payloads = payload.split(Message.SEPARATOR);
		MessageType messageType = MessageTypeUtil.stringToMessageType(payloads[0]);
		StringBuilder payload_builder = new StringBuilder();
		for(int i = 1; i < payloads.length; ++i) {
			payload_builder.append(payloads[i]);
			if(i != payloads.length-1)
				payload_builder.append(Message.SEPARATOR);
		}
		return new Message(messageType, payload_builder.toString());
	}
}
