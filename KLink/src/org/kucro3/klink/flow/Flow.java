package org.kucro3.klink.flow;

import java.util.Collections;

import org.kucro3.collection.CompoundList;
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
			for(Executable operation : flow)
				if(operation != null)
					operation.execute(sys);
		} catch (JumpOut signal) {
		}
	}
	
	public void append(Executable operation)
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
	
	public Executable tail()
	{
		return flow.get(flow.size() - 1);
	}
	
	public Flow copy()
	{
		Flow copy = new Flow();
		for(Executable e : flow)
			copy.flow.add(e);
		return copy;
	}
	
	public FlowSnapshot snapshot()
	{
		return new FlowSnapshot(this, Collections.unmodifiableList(flow));
	}
	
	private CompoundList<Executable> flow = new CompoundList<>();
}