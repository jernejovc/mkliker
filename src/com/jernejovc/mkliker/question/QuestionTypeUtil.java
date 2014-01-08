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

package com.jernejovc.mkliker.question;

import java.util.HashMap;

/**
 * Utility class used for converting QuestionType to String and
 * vice-versa 
 * @author matej
 *
 */
public class QuestionTypeUtil {
	
	/**
	 * Converts QuestionType to String
	 * @param type
	 * @return
	 */
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
	
	/**
	 * Converts String number to QuestionType 
	 * @param type 
	 * @return
	 */
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
