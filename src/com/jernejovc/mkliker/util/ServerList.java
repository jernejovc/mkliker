package com.jernejovc.mkliker.util;
import java.util.ArrayList;

import com.jernejovc.mkliker.net.Server;



public class ServerList {
	private ArrayList<Server> m_servers;
	
	public ServerList() {
		m_servers = new ArrayList<Server>();
	}
	
	public void add(Server p_server) {
		m_servers.add(p_server);
	}
	
	public Server get(int idx) {
		return m_servers.get(idx);
	}
	
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
	
	public boolean isEmpty() {
		return m_servers.isEmpty();
	}
	
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

	public static ServerList getDefaultServerList() {
		ServerList l = new ServerList();
		l.add(new Server("FRI", "lgm.fri.uni-lj.si", "041239923", true));
		return l;
	}
	
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

	public static boolean isValidSharedPreference(String value) {
		String[] servers = value.split(";");
		if(servers.length == 0)
			return false;
		for(String server: servers) {
			String[] srvr = server.split(",");
			if(srvr.length != 3 || srvr.length != 4)
				return false;
		}
		return true;
	}

	public void setDefault(Server s) {
		for(Server server : m_servers) {
			if(server.getName().equalsIgnoreCase(s.getName())) {
				server.setIsDefault(true);
			} else {
				server.setIsDefault(false);
			}
		}
	}

	public void remove(Server s) {
		m_servers.remove(s);
	}
}
