package org.kucro3.klink.flow;

import java.util.List;
import java.util.Optional;

import org.kucro3.klink.Executable;

public class FlowSnapshot {
	FlowSnapshot(Flow owner, List<Executable> content)
	{
		this.content = content;
		this.owner = owner;
		this.prev = content.size() - 1;
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
	
	public Optional<Executable> last()
	{
		if(content.isEmpty())
			return Optional.empty();
		return Optional.of(content.get(content.size() - 1));
	}

	public Optional<Executable> prev()
	{
		return get(prev);
	}

	public Optional<Executable> current()
	{
		return get(prev + 1);
	}

	public Optional<Executable> next()
	{
		return get(prev + 2);
	}

	private Optional<Executable> get(int index)
	{
		if(index >= 0 && index < content.size())
			return Optional.of(content.get(index));
		return Optional.of(content.get(index));
	}

	private final int prev;

	private final Flow owner;
	
	private final List<Executable> content;
}