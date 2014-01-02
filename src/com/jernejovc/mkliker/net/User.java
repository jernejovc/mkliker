package com.jernejovc.mkliker.net;

public class User {
	private int m_userID;
	private String m_nickname;
	private String m_room;
	public User(int userID, String nickname, String room) {
		m_userID = userID;
		m_nickname = nickname;
		m_room = room;
	}
	public int getuserID() {
		return m_userID;
	}
	
	public String getNickname() {
		return m_nickname;
	}
	
	public String getRoom() {
		return m_room;
	}
}
