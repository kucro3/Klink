package org.kucro3.klink.expression;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

import org.kucro3.klink.Klink;
import org.kucro3.klink.SequenceUtil;
import org.kucro3.klink.Util;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.syntax.Sequence;

public class ExpressionPackLoader {
	public static void load(Klink sys, ExpressionLibrary lib, String zipName)
	{
		load(sys, lib, new File(zipName));
	}
	
	public static void load(Klink sys, ExpressionLibrary lib, File zip)
	{
		try {
			URLClassLoader cl = null;
			try {
				cl = new URLClassLoader(new URL[] {zip.toURI().toURL()});
				InputStream is = cl.getResourceAsStream(RESOURCE_CATALOG);
				if(is == null)
					throw CatalogNotFound(zip);
				Sequence catalog = SequenceUtil.readFrom(is);
				while(catalog.hasNext())
					ExpressionLoader.load(sys, lib, cl.loadClass(catalog.next()));
			} catch (ClassNotFoundException e) {
				throw new ScriptException("Class not found: " + e.getMessage());
			} finally {
				if(cl != null)
					cl.close();
			}
		} catch (IOException e) {
			throw Util.IOException(e);
		}
	}
	
	public static ScriptException CatalogNotFound(File zip)
	{
		return new ScriptException("Catalog not found in file: " + zip.getName());
	}
	
	public static final String RESOURCE_CATALOG = "catalog";
}