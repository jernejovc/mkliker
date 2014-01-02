package com.jernejovc.mkliker.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class KlikerPreferences {
	private Activity m_parent;
	private final String APP_NAME = "com.jernejovc.mkliker";
	private final String SERVER_LIST = "ServerList";
	private final String LASTROOM = "LastRoomName";
	private final String LASTNICK = "LastNickname";
	private final String SMSENABLED = "SMSEnabled";
	private SharedPreferences prefs;  
	
	
	public KlikerPreferences(Activity parent) {
		m_parent = parent;
		prefs = m_parent.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
	}
	
	public void saveServerList(ServerList m_serverlist) {
		prefs
		.edit()
		.putString(SERVER_LIST, m_serverlist.toSharedPrefsValue())
		.commit();
	}

	public ServerList getServerList() {
		String pref = prefs.getString(SERVER_LIST, "");
		if(pref == "" || !ServerList.isValidSharedPreference(pref)) {
			return ServerList.getDefaultServerList();
		} else {
			return ServerList.fromSharedPrefsValue(pref);
		}
	}
	
	public String getLastRoomName() {
		return prefs.getString(LASTROOM, "");
	}
	
	public void setLastRoomName(String room) {
		prefs
		.edit()
		.putString(LASTROOM, room)
		.commit();
	}
	
	public String getLastNickname() {
		return prefs.getString(LASTNICK, "");
	}
	
	public void setLastNickName(String nick) {
		prefs
		.edit()
		.putString(LASTNICK, nick)
		.commit();
	}
	
	public boolean isSMSEnabled() {
		return prefs.getBoolean(SMSENABLED, false);
	}
	
	public void setSMSEnabled(boolean enabled) {
		prefs
		.edit()
		.putBoolean(SMSENABLED, enabled)
		.commit();
	}
}
