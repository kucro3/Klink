package org.kucro3.klink.syntax;

import org.kucro3.collection.CompoundList;
import org.kucro3.klink.Klink;

public class Flow implements Executable {
	public Flow()
	{
	}
	
	@Override
	public void execute(Klink sys) 
	{
		for(Executable operation : flow)
			operation.execute(sys);
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
	
	private CompoundList<Executable> flow = new CompoundList<>();
}