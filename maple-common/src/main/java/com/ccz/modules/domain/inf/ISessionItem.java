package com.ccz.modules.domain.inf;

public interface ISessionItem <T> {
	public String getKey();
	public ISessionItem<T> putSession(T t, String serviceCode);
}