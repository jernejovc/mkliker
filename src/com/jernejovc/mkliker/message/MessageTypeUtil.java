package com.jernejovc.mkliker.message;

import java.util.HashMap;

/**
 * Utility class for converting MessageType to String
 * and vice-versa
 * @author matej
 *
 */
public class MessageTypeUtil {
	/**
	 * Converts MessageType enum to String
	 * @param type MessageType to be converted
	 * @return String corresponding to that Message type or null if it doesn't exist
	 */
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
	
	/**
	 * Converts String to MessageType enum value
	 * @param type String to be converted
	 * @return MessageType corresponding to that String or null if it doesn't exist
	 */
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
