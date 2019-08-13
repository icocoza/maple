package com.ccz.modules.domain.inf;

public interface ISessionItem <T> {
	String getKey();
	ISessionItem<T> putSession(String scode, T rec);
}