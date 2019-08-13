package com.ccz.modules.common.action;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
	
	private static SessionManager s_pThis = null;
	public static synchronized SessionManager getInst() {
		return s_pThis = (s_pThis == null? new SessionManager() : s_pThis);
	}

	Map<String, SessionItem> sessionMap = new ConcurrentHashMap<>();

	public SessionItem put(SessionItem si) {
		SessionItem previousItem = sessionMap.put(si.getKey(), si);
		return previousItem;
	}
	
	public SessionItem get(String key) {
		return sessionMap.get(key);
	}
	
	public boolean del(String key) {
		return sessionMap.remove(key) != null;
	}
	
	public void clear() {
		sessionMap.clear();
	}
	
	public int count() {
		return sessionMap.size();
	}
	
	public Set<String> getAllKey() {
		return sessionMap.keySet();
	}
	
	public Collection<SessionItem> getAllValue() {
		return sessionMap.values();
	}
}
