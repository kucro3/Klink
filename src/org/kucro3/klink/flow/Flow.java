package org.kucro3.klink.flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.JumpOut;

public class Flow implements Executable {
	public Flow()
	{
	}
	
	@Override
	public void execute(Klink sys) 
	{
		try {
			for(Optional<Executable> operation : flow)
				operation.ifPresent((e) -> e.execute(sys));
		} catch (JumpOut signal) {
		}
	}
	
	public void append(Optional<Executable> operation)
	{
		flow.add(operation);
	}
	
	public void clear()
	{
		flow.clear();
	}
	
	public int size()
	{
		return flow.size();
	}
	
	public Optional<Executable> tail()
	{
		return flow.get(flow.size() - 1);
	}
	
	public Flow copy()
	{
		Flow copy = new Flow();
		for(Optional<Executable> e : flow)
			copy.flow.add(e);
		return copy;
	}
	
	public FlowSnapshot snapshot()
	{
		return new FlowSnapshot(this, Collections.unmodifiableList(flow));
	}
	
	private ArrayList<Optional<Executable>> flow = new ArrayList<>();
}