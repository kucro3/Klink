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
	
	public Sequence(String[] seq, int row, int column)
	{
		this(seq, row, column, null);
	}
	
	public Sequence(String[] seq, int row, int column, String name)
	{
		this(seq, null);
		this.row = row;
		this.column = column;
		this.name = name;
	}
	
	public String[] getSequence()
	{
		return seq;
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
				column = 0;
				row += linemark[1];
				linemarkPtr++;
			}
			else
				;
		}
		column++;
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
		return row + 1;
	}
	
	public int currentColumn()
	{
		return column + 1;
	}
	
	public void incRow()
	{
		row++;
	}
	
	public void decRow()
	{
		row--;
	}
	
	public void incColumn()
	{
		column++;
	}
	
	public void decColumn()
	{
		column--;
	}
	
	public String toString(int shift)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = shift; i < seq.length;)
			sb.append(seq[i++]).append(i < seq.length ? " " : "");
		return sb.toString();
	}
	
	public String leftToString()
	{
		return toString(pointer);
	}
	
	public String toString()
	{
		return toString(0);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
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
	
	private String name;
	
	public static Sequence appendTail(Sequence seq, String tail)
	{
		String[] newArray = new String[seq.seq.length + 1];
		newArray[seq.seq.length] = tail;
		System.arraycopy(seq.seq, 0, newArray, 0, seq.seq.length);
		Sequence newSeq = new Sequence(newArray, seq.linemarks);
		newSeq.linemarkPtr = seq.linemarkPtr;
		newSeq.pointer = seq.pointer;
		newSeq.row = seq.row;
		newSeq.column = seq.column;
		newSeq.name = seq.name;
		return newSeq;
	}
}