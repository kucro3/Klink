package org.kucro3.klink.flow;

import java.util.List;

import org.kucro3.klink.Executable;

public class FlowSnapshot {
	FlowSnapshot(Flow owner, List<Executable> content)
	{
		this.content = content;
		this.owner = owner;
	}
	
	public Flow getOwner()
	{
		return owner;
	}
	
	public List<Executable> getFlow()
	{
		return content;
	}
	
	public int count()
	{
		return content.size();
	}
	
	public Executable last()
	{
		return content.get(content.size() - 1);
	}
	
	private final Flow owner;
	
	private final List<Executable> content;
}