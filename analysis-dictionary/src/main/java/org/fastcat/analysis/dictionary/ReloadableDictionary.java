package org.fastcat.analysis.dictionary;

public interface ReloadableDictionary {
	public void reload(Object object) throws IllegalArgumentException;
}
