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

package com.jernejovc.mkliker.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class used for accessing and setting mKliker preferences.
 * @author matej
 *
 */
public class KlikerPreferences {
	private Activity m_parent;
	private final String APP_NAME = "com.jernejovc.mkliker";
	private final String SERVER_LIST = "ServerList";
	private final String LASTROOM = "LastRoomName";
	private final String LASTNICK = "LastNickname";
	private final String SMSENABLED = "SMSEnabled";
	private SharedPreferences prefs;  
	
	/**
	 * Consructs a new KlikerPreferences object with parent activity
	 * @param parent
	 */
	public KlikerPreferences(Activity parent) {
		m_parent = parent;
		prefs = m_parent.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * Saves the server list to shared preferences 
	 * @param m_serverlist
	 */
	public void saveServerList(ServerList m_serverlist) {
		prefs
		.edit()
		.putString(SERVER_LIST, m_serverlist.toSharedPrefsValue())
		.commit();
	}
	
	/**
	 * Retrieves the server list saved in shared preferences.
	 * @return
	 */
	public ServerList getServerList() {
		String pref = prefs.getString(SERVER_LIST, "");
		if(pref == "" || !ServerList.isValidSharedPreference(pref)) {
			return ServerList.getDefaultServerList();
		} else {
			return ServerList.fromSharedPrefsValue(pref);
		}
	}
	
	/**
	 * @return Last room name that was successfully joined
	 */
	public String getLastRoomName() {
		return prefs.getString(LASTROOM, "");
	}
	
	/**
	 * Sets the last room name that was succesfully joined
	 * @param room
	 */
	public void setLastRoomName(String room) {
		prefs
		.edit()
		.putString(LASTROOM, room)
		.commit();
	}
	
	/**
	 * @return Last nickname that was used in a successful connection
	 */
	public String getLastNickname() {
		return prefs.getString(LASTNICK, "");
	}
	
	/**
	 * Sets the last nickname that was used in a successful connection
	 * @param nick
	 */
	public void setLastNickName(String nick) {
		prefs
		.edit()
		.putString(LASTNICK, nick)
		.commit();
	}
	
	/**
	 * @return Whether the SMS participation is enabled or not
	 */
	public boolean isSMSEnabled() {
		return prefs.getBoolean(SMSENABLED, false);
	}
	
	/**
	 * Sets whether the SMS participation is enabled or not
	 * @param enabled
	 */
	public void setSMSEnabled(boolean enabled) {
		prefs
		.edit()
		.putBoolean(SMSENABLED, enabled)
		.commit();
	}
}
