package org.fastcat.analysis.dictionary;


import org.fastcat.analysis.dictionary.common.CharSlice;

import java.util.HashMap;
import java.util.Map;

public class CharVectorHashMap<V> extends HashMap<CharSlice, V> {

	private boolean isIgnoreCase;

	public CharVectorHashMap() {
		super();
	}

	public CharVectorHashMap(boolean isIgnoreCase) {
		this(2, isIgnoreCase);
	}
	public CharVectorHashMap(int initialCapacity, boolean isIgnoreCase) {
		super(initialCapacity);
		this.isIgnoreCase = isIgnoreCase;
	}
	public CharVectorHashMap(Map<CharSlice, V> m, boolean isIgnoreCase) {
		super(m);
		this.isIgnoreCase = isIgnoreCase;
	}
	public boolean isIgnoreCase() {
		return isIgnoreCase;
	}

	@Override
	public V get(Object key) {

		CharSlice charKey = (CharSlice) key;
		if (isIgnoreCase) {
			if (!charKey.isIgnoreCase()) {
				charKey.setIgnoreCase();
				V v = super.get(charKey);
				charKey.unsetIgnoreCase();
				return v;
			}
		}

		return super.get(charKey);
	}

	@Override
	public boolean containsKey(Object key) {
		return this.get(key) != null;
	}

	@Override
	public V put(CharSlice charKey, V value) {
		if (isIgnoreCase) {
			charKey.setIgnoreCase();
		}
		return super.put(charKey, value);
	}

}
