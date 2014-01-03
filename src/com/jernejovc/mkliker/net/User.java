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
