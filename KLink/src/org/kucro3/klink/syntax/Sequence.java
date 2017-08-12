package org.kucro3.klink.syntax;

import java.util.Iterator;

import org.kucro3.klink.exception.ScriptException;

public class Sequence implements Iterable<String>, Iterator<String> {
	public Sequence(String[] seq)
	{
		this(seq, null);
	}
	
	public Sequence(String[] seq, int[][] linemarks)
	{
		this.seq = seq;
		this.linemarks = linemarks;
	}
	
	@Override
	public Iterator<String> iterator()
	{
		return this;
	}

	@Override
	public boolean hasNext() 
	{
		return pointer < seq.length;
	}
	
	public String getNext()
	{
		if(!hasNext())
			throw EOF();
		return seq[pointer];
	}

	@Override
	public String next() 
	{
		if(!hasNext())
			throw EOF();
		
		if(linemarks == null || linemarks.length == 0 || !(linemarkPtr < linemarks.length))
			;
		else
		{
			int[] linemark = linemarks[linemarkPtr];
			if(pointer == linemark[0])
			{
				row = 0;
				column += linemark[1];
			}
			else
				;
		}
		row++;
		return seq[pointer++];
	}
	
	public void reset()
	{
		pointer = 0;
		column = 0;
		row = 0;
	}
	
	public int current()
	{
		return pointer - 1;
	}
	
	public int currentRow()
	{
		return row - 1;
	}
	
	public int currentColumn()
	{
		return column;
	}
	
	public static ScriptException EOF()
	{
		return new ScriptException("Unexpected End of file");
	}
	
	private final String[] seq;
	
	private int linemarkPtr;
	
	private final int[][] linemarks;
	
	private int pointer;
	
	private int row = 0;
	
	private int column = 0;
}