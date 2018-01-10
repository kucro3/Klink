package org.kucro3.klink;

import java.io.Closeable;
import java.io.File;
import java.util.Optional;

import org.kucro3.klink.expression.ExpressionLibrary;

public interface PackLoader {
	public Optional<Closeable> load(Klink sys, ExpressionLibrary lib, File file);
	
	public Optional<Closeable> load(Klink sys, ExpressionLibrary lib, String file);
}