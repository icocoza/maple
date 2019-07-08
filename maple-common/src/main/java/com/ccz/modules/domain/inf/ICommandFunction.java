package com.ccz.modules.domain.inf;

@FunctionalInterface
public interface ICommandFunction<A, B, C> {
	public B doAction(A a, B b, C c) ;
}
