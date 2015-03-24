package org.fastcat.analysis.dictionary;


import org.fastcat.analysis.dictionary.common.CharSlice;

import java.util.HashSet;

public class CharVectorHashSet extends HashSet<CharSlice> {

	private boolean isIgnoreCase;

	public CharVectorHashSet() {
		this(false);
	}

	public CharVectorHashSet(boolean isIgnoreCase) {
		super();
		this.isIgnoreCase = isIgnoreCase;
	}

	public boolean isIgnoreCase() {
		return isIgnoreCase;
	}

	@Override
	public boolean add(CharSlice e) {
		if (isIgnoreCase) {
			e.setIgnoreCase();
		}
		return super.add(e);
	}

	@Override
	public boolean contains(Object key) {
		CharSlice charKey = (CharSlice) key;
		if (isIgnoreCase) {
			if (!charKey.isIgnoreCase()) {
				charKey.setIgnoreCase();
				boolean v = super.contains(charKey);
				charKey.unsetIgnoreCase();
				return v;
			}
		}
		
		return super.contains(charKey);
	}

}
