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
import java.util.ArrayList;

import com.jernejovc.mkliker.net.Server;

/**
 * Utility class that holds a number of servers.
 * @author matej
 *
 */
public class ServerList {
	private ArrayList<Server> m_servers;
	
	public ServerList() {
		m_servers = new ArrayList<Server>();
	}
	
	/**
	 * Adds a server to the server list.
	 * @param p_server
	 */
	public void add(Server p_server) {
		m_servers.add(p_server);
	}
	
	/**
	 * Returns the server at a specified index.
	 * @param idx
	 * @return
	 */
	public Server get(int idx) {
		return m_servers.get(idx);
	}
	
	/**
	 * Sets the server at a specified index.
	 * @param s Server to be set
	 * @param idx Index
	 */
	public void set(Server s, int idx) {
		m_servers.set(idx, s);
	}
	
	/**
	 * Get the server list.
	 * @return ServerList with default server as the first item.
	 */
	public ArrayList<Server> serverList() {
		ArrayList<Server> toReturn = new ArrayList<Server>();
		for(Server s : m_servers) {
			if(s.getIsDefault()) {
				toReturn.add(s);
				break;
			}
		}
		for(Server s : m_servers) {
			if(!s.getIsDefault()) {
				toReturn.add(s);
			}
		}
		return toReturn;
	}
	
	/**
	 * @return true if the server list is empty, false otherwise
	 */
	public boolean isEmpty() {
		return m_servers.isEmpty();
	}
	
	/**
	 * Returns ServerList converted from SharedPreferences value
	 * @param value SharedPreferences String value
	 * @return
	 */
	public static ServerList fromSharedPrefsValue(String value) {
		ServerList list = new ServerList();
		
		String[] servers = value.split(";");
		for(String server: servers) {
			String[] srvr = server.split(",");
			String name = srvr[0];
			String url = srvr[1];
			boolean def = Integer.valueOf(srvr[2]) == 1;
			String sms = srvr[3];
			Server s = new Server(name, url, sms, def);
			list.add(s);
		}
		return list;
	}

	/**
	 * @return Default server list, currently with only one server, FRI
	 */
	public static ServerList getDefaultServerList() {
		ServerList l = new ServerList();
		l.add(new Server("FRI", "lgm.fri.uni-lj.si", "041239923", true));
		return l;
	}
	
	/**
	 * Converts the server list to a SharedPreferences value
	 * @return SharedPreferences value
	 */
	public String toSharedPrefsValue() {
		StringBuilder builder = new StringBuilder();
		for(Server s : m_servers) {
			builder.append(String.format("%s,%s,%d,%s;", 
					s.getName(), 
					s.getUrl().toString(),
					s.getIsDefault() ? 1 : 0,
					s.getSMSNumber()));
		}
		return builder.toString();
	}

	/**
	 * Checks if the string is a valid SharedPreferences value
	 * @param value Value to be checked
	 * @return true if the value is valid, false otherwise
	 */
	public static boolean isValidSharedPreference(String value) {
		String[] servers = value.split(";");
		if(servers.length == 0)
			return false;
		for(String server: servers) {
			String[] srvr = server.split(",");
			if(!(srvr.length == 3 || srvr.length == 4))
				return false;
		}
		return true;
	}

	/**
	 * Sets the default server in the server list
	 * @param s Server to be set as default
	 */
	public void setDefault(Server s) {
		for(Server server : m_servers) {
			if(server.getName().equalsIgnoreCase(s.getName())) {
				server.setIsDefault(true);
			} else {
				server.setIsDefault(false);
			}
		}
	}

	/**
	 * Removes the server from the ServerList 
	 * @param s
	 */
	public void remove(Server s) {
		m_servers.remove(s);
	}
}
