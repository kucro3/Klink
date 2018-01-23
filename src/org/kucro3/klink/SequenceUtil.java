package org.kucro3.klink;

import java.io.*;
import java.util.ArrayList;

import org.kucro3.klink.syntax.*;

public class SequenceUtil {
	public static Sequence readFrom(String filename)
	{
		return readFrom(new File(filename));
	}
	
	public static Sequence readFrom(InputStream is)
	{
		return readFrom(new BufferedReader(new InputStreamReader(is)));
	}
	
	public static Sequence readFrom(File file)
	{
		BufferedReader closeable = null;
		try {
			try {
				return readFrom(closeable = new BufferedReader(new FileReader(file)));
			} finally {
				if(closeable != null)
					closeable.close();
			}
		} catch (IOException e) {
			throw Util.IOException(e);
		}
	}
	
	public static Sequence readFrom(BufferedReader reader)
	{
		try {
			ArrayList<String> strs = new ArrayList<>();
			ArrayList<int[]> linemarks = new ArrayList<>();
			String line;
			
			int[] currentLinemark = null;
			
			while((line = reader.readLine()) != null)
			{
				line = line.trim();
				if(line.length() != 0)
				{
					if(currentLinemark != null)
						linemarks.add(currentLinemark);
					currentLinemark = null;
					
					String[] seq = line.split(" ");
					for(String s : seq)
						strs.add(s);
				}
				
				if(currentLinemark == null)
					(currentLinemark = new int[2])[0] = strs.size();
				currentLinemark[1]++;
			}
			
			reader.close();
			
			return new Sequence(strs.toArray(new String[0]), (int[][]) linemarks.toArray(new int[0][]));
		} catch (IOException e) {
			throw Util.IOException(e);
		}
	}

	public static Sequence create(String str, String separator)
	{
		return new Sequence(str.split(separator));
	}
}