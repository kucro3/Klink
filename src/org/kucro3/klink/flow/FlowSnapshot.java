package org.kucro3.klink.flow;

import java.util.List;
import java.util.Optional;

import org.kucro3.klink.Executable;

public class FlowSnapshot {
	FlowSnapshot(Flow owner, List<Optional<Executable>> content)
	{
		this.content = content;
		this.owner = owner;
	}
	
	public Flow getOwner()
	{
		return owner;
	}
	
	public List<Optional<Executable>> getFlow()
	{
		return content;
	}
	
	public int count()
	{
		return content.size();
	}
	
	public Optional<Executable> last()
	{
		return content.get(content.size() - 1);
	}
	
	private final Flow owner;
	
	private final List<Optional<Executable>> content;
}