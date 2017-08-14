package org.kucro3.klink;

public interface Ref {
	Object get(Environment env);
	
	void set(Environment env, Object obj);
	
	void force(Environment env, Object obj);
	
	boolean isVar();
	
	boolean isReg();
}