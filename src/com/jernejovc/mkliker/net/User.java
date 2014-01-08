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

package com.jernejovc.mkliker.net;

/**
 * Class that holds the user information:
 * <ul>
 * 	<li>Nickname</li>
 *  <li>Room that the user is connected to</li>
 *  <li>User ID returned by the server</li>
 * </ul> 
 * @author matej
 */
public class User {
	private int m_userID;
	private String m_nickname;
	private String m_room;
	
	/**
	 * Default constructor that sets all the parameters 
	 * @param userID User ID
	 * @param nickname Nickname
	 * @param room Room to which the user is connected
	 */
	public User(int userID, String nickname, String room) {
		m_userID = userID;
		m_nickname = nickname;
		m_room = room;
	}
	
	/**
	 * @return User ID of the user
	 */
	public int getuserID() {
		return m_userID;
	}
	
	/**
	 * @return Nickname of the user 
	 */
	public String getNickname() {
		return m_nickname;
	}
	
	/**
	 * @return Room name to which user is connected to
	 */
	public String getRoom() {
		return m_room;
	}
}
