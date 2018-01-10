package org.kucro3.util;

public class Reference<T> {
	public Reference()
	{
	}
	
	public Reference(T t)
	{
		set(t);
	}
	
	public T get()
	{
		return reference;
	}
	
	public void set(T reference)
	{
		this.reference = reference;
	}
	
	private T reference;
}