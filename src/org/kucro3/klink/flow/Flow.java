package org.kucro3.klink.flow;

import java.util.*;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Executable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.JumpOut;

@SuppressWarnings("unchecked")
public class Flow implements Executable {
	public Flow()
	{
	}
	
	@Override
	public void execute(Klink sys, Environment env)
	{
		try {
			for(Executable operation : flow)
			    operation.execute(sys, env);
		} catch (JumpOut signal) {
		}
	}

	public void append(Executable operation)
	{
	    if(operation != null)
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
        copy.flow.addAll(flow);
		return copy;
	}

    public Map<Object, Object> getAttributes()
    {
        return attributes;
    }

    public <T> T putAttirbute(Object key, T value)
    {
        return (T) attributes.put(key, value);
    }

    public <T> T getAttribute(Object key)
    {
        return (T) attributes.get(key);
    }

    public void clearAttributes()
    {
        attributes.clear();
    }

    public FlowSnapshot snapshot()
	{
		return new FlowSnapshot(this, Collections.unmodifiableList(flow));
	}
	
	private ArrayList<Executable> flow = new ArrayList<>();

	private final Map<Object, Object> attributes = new HashMap<>();
}