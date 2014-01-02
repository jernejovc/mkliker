package com.jernejovc.mkliker.message;

import java.util.Locale;


public class MessageFactory {
	
	public static Message makeLoginStudentMessage(String room, String pass) {
		String payload = String.format(Locale.getDefault(),
				"%s%s%s", 
				room,
				Message.SEPARATOR,
				pass);
		return new Message(MessageType.LOGINSTUDENT, payload);
	}
	
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
	
	public static Message makePingStudentMessage(String room, int userID) {
		String payload = String.format(Locale.getDefault(), 
				"%s%s%d",
				room,
				Message.SEPARATOR,
				userID);
		
		return new Message(MessageType.PINGSTUDENT, payload);
	}

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
