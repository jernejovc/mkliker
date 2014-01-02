package com.jernejovc.mkliker.question;

import java.util.HashMap;

public class QuestionTypeUtil {
	public static Integer questionTypeToString(QuestionType type) {
		return getHashMapQTS().get(type);
	}
	
	private static HashMap<QuestionType, Integer> getHashMapQTS() {
		HashMap<QuestionType, Integer> map = new HashMap<QuestionType, Integer>();
		map.put(QuestionType.YESNO, 1);
		map.put(QuestionType.ABCDESINGLE, 2);
		map.put(QuestionType.ABCDEMULTI, 4);
		map.put(QuestionType.SHORTANSWER, 5);
		return map;
	}
	
	public static QuestionType stringToQuestionType(String type) {
		return getHashMapSTQ().get(Integer.valueOf(type));
	}
	
	private static HashMap<Integer, QuestionType> getHashMapSTQ() {
		HashMap<Integer, QuestionType> map = new HashMap<Integer, QuestionType>();
		for(QuestionType q : getHashMapQTS().keySet()) {
			map.put(getHashMapQTS().get(q), q);
		}
		return map;
	}
}
