package org.fastcat.analysis.dictionary.common;

import java.util.List;
import java.util.Set;

public abstract class Dictionary<E> {
	
	public abstract List<E> find(CharSlice token);
	
	public abstract int size();

	public abstract void appendAdditionalNounEntry(Set<CharSlice> set, String tokenType);
}
