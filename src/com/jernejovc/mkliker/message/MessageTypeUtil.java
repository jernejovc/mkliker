package com.jernejovc.mkliker.message;

import java.util.HashMap;

public class MessageTypeUtil {
	public static String messageTypeToString(MessageType type) {
		return getHashMapMTS().get(type);
	}
	
	private static HashMap<MessageType, String> getHashMapMTS() {
		HashMap<MessageType, String> map = new HashMap<MessageType, String>();
		map.put(MessageType.ACKNOWGLEDGE, "acknowledge");
		map.put(MessageType.BLOCK, "block");
		map.put(MessageType.ENROLLED, "Enrolled");
		map.put(MessageType.LOGINSTUDENT, "LoginStudent");
		map.put(MessageType.MESSAGE, "message");
		map.put(MessageType.NOSUCHROOM, "NoSuchRoom");
		map.put(MessageType.PINGSTUDENT, "PingStudent");
		map.put(MessageType.ROOMCLOSED, "roomClosed");
		map.put(MessageType.ROOMSTATUS, "roomStatus");
		map.put(MessageType.STUDENTANSWER, "StudentAnswer");
		map.put(MessageType.STARTQUESTION, "startQuestion");
		map.put(MessageType.STOPQUESTION, "stopQuestion");
		return map;
	}
	
	public static MessageType stringToMessageType(String str) {
		return getHashMapSTM().get(str);
	}
	
	private static HashMap<String, MessageType> getHashMapSTM() {
		HashMap<String, MessageType> map = new HashMap<String, MessageType>();
		for(MessageType m : getHashMapMTS().keySet()) {
			map.put(getHashMapMTS().get(m), m);
		}
		
		return map;
	}
}
