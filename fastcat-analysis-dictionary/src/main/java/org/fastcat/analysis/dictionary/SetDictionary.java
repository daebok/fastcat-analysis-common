package org.fastcat.analysis.dictionary;

import org.fastcat.analysis.dictionary.common.CharSlice;
import org.fastcat.analysis.dictionary.io.DataInput;
import org.fastcat.analysis.dictionary.io.DataInputStream;
import org.fastcat.analysis.dictionary.io.DataOutput;
import org.fastcat.analysis.dictionary.io.DataOutputStream;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class SetDictionary extends SourceDictionary {
	
	private Set<CharSlice> set;

	public SetDictionary() {
		this(false);
	}
	
	public SetDictionary(boolean ignoreCase) {
		super(ignoreCase);
		set = new CharVectorHashSet(ignoreCase);
	}

	public SetDictionary(CharVectorHashSet set, boolean ignoreCase) {
		super(ignoreCase);
		this.set = set;
	}

	public SetDictionary(File file, boolean ignoreCase) {
		super(ignoreCase);
		if(!file.exists()){
			set = new CharVectorHashSet(ignoreCase);
			logger.error("사전파일이 존재하지 않습니다. file={}", file.getAbsolutePath());
			return;
		}
		InputStream is;
		try {
			is = new FileInputStream(file);
			readFrom(is);
			is.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public SetDictionary(InputStream is, boolean ignoreCase){
		super(ignoreCase);
		try {
			readFrom(is);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	@Override
	public void addEntry(String keyword, Object[] value) {
		keyword = keyword.trim();
		if (keyword.length() > 0) {
			CharSlice cv = new CharSlice(keyword);
			set.add(cv);
		}
	}

	public Set<CharSlice> getUnmodifiableSet() {
		return Collections.unmodifiableSet(set);
	}
	
	public Set<CharSlice> set() {
		return set;
	}
	
	public void setSet(Set<CharSlice> set) {
		this.set = set;
	}
	
	public boolean contains(CharSlice key){
		return set.contains(key);
	}
	
	@Override
	public void writeTo(OutputStream out) throws IOException {


		DataOutput output = new DataOutputStream(out);
		Iterator<CharSlice> valueIter = set.iterator();
		//write size of set
		
		output.writeInt(set.size());
		//write values
		for(;valueIter.hasNext();) {
			CharSlice value = valueIter.next();
			output.writeUString(value.array(), value.start(), value.length());
		}
	}

	@Override
	public void readFrom(InputStream in) throws IOException {
		
		DataInput input = new DataInputStream(in);
		set = new CharVectorHashSet(ignoreCase);
		int size = input.readInt();
		
		for(int entryInx=0;entryInx < size; entryInx++) {
			char[] chars = input.readUString();
			set.add(new CharSlice(input.readString()));
		}
	}

	@Override
	public void addSourceLineEntry(String line) {
		addEntry(line, null);
	}

	@Override
	public void reload(Object object) throws IllegalArgumentException {
		if(object != null && object instanceof SetDictionary){
			SetDictionary setDictionary = (SetDictionary) object;
			this.set = setDictionary.set();
			
		}else{
			throw new IllegalArgumentException("Reload dictionary argument error. argument = " + object);
		}
	}


}
