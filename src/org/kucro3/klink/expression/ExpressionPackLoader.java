package org.kucro3.klink.expression;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Optional;

import org.kucro3.klink.Klink;
import org.kucro3.klink.PackLoader;
import org.kucro3.klink.SequenceUtil;
import org.kucro3.klink.Util;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.syntax.Sequence;

public class ExpressionPackLoader implements PackLoader {
	private ExpressionPackLoader()
	{
	}
	
	public static ExpressionPackLoader getInstance()
	{
		return INSTANCE;
	}
	
	public Optional<Closeable> load(Klink sys, ExpressionLibrary lib, String zipName)
	{
		return load(sys, lib, new File(zipName));
	}
	
	@SuppressWarnings("resource")
	public Optional<Closeable> load(Klink sys, ExpressionLibrary lib, File zip)
	{
		try {
			URLClassLoader cl = null;
			try {
				sys.getMessenger().info("Loading expression pack: " + zip.getName());
				
				cl = new URLClassLoader(new URL[] {zip.toURI().toURL()});
				InputStream is = cl.getResourceAsStream(RESOURCE_CATALOG);
				if(is == null)
					throw CatalogNotFound(zip);
				Sequence catalog = SequenceUtil.readFrom(is);
				while(catalog.hasNext())
				{
					Class<?> clz;
					ExpressionLoader.load(sys, lib, clz = cl.loadClass(catalog.next()));
					sys.getMessenger().info("Loaded expression class: " + clz.getCanonicalName());
				}
				
				sys.getMessenger().info("Loaded expression pack: " + zip.getName());
			} catch (ClassNotFoundException e) {
				throw ClassNotFound(e.getMessage());
			} finally {
//				if(cl != null)
//					cl.close();
			}
			return Optional.of(cl);
		} catch (IOException e) {
			throw Util.IOException(e);
		}
	}
	
	public static ScriptException ClassNotFound(String msg)
	{
		throw new ScriptException("Class not found: " + msg);
	}
	
	public static ScriptException CatalogNotFound(File zip)
	{
		return new ScriptException("Catalog not found in file: " + zip.getName());
	}
	
	public static final String RESOURCE_CATALOG = "catalog";
	
	private static final ExpressionPackLoader INSTANCE = new ExpressionPackLoader();
}