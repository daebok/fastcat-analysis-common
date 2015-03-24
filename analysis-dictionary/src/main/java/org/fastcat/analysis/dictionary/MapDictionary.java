package org.fastcat.analysis.dictionary;

import org.fastcat.analysis.dictionary.common.CharSlice;
import org.fastcat.analysis.dictionary.io.DataInput;
import org.fastcat.analysis.dictionary.io.DataInputStream;
import org.fastcat.analysis.dictionary.io.DataOutput;
import org.fastcat.analysis.dictionary.io.DataOutputStream;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/*
 * map 범용 사전. 
 * CharSlice : CharSlice[] pair이다.
 * 만약 value에 Object[]를 사용하길 원한다면 custom dictionary를 사용한다.
 * 
 * 
 * */
public class MapDictionary extends SourceDictionary implements ReloadableDictionary {

	protected Map<CharSlice, CharSlice[]> map;

	public MapDictionary() {
		this(false);
	}

	public MapDictionary(boolean ignoreCase) {
		super(ignoreCase);
		map = new CharVectorHashMap<CharSlice[]>(ignoreCase);
	}

	public MapDictionary(CharVectorHashMap<CharSlice[]> map) {
		super(map.isIgnoreCase());
		this.map = map;
	}

	public MapDictionary(File file, boolean ignoreCase) {
		super(ignoreCase);
		if (!file.exists()) {
			map = new CharVectorHashMap<CharSlice[]>();
			logger.error("사전파일이 존재하지 않습니다. file={}", file.getAbsolutePath());
			return;
		}
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			readFrom(is);
			is.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public MapDictionary(InputStream is, boolean ignoreCase) {
		super(ignoreCase);
		try {
			readFrom(is);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@Override
	public void addEntry(String keyword, Object[] values) {
		if (keyword == null || keyword.length() == 0) {
			return;
		}

		CharSlice[] list = new CharSlice[values.length];
		for (int i = 0; i < values.length; i++) {
			String value = values[i].toString();
			list[i] = new CharSlice(value);
		}
		
		CharSlice cv = new CharSlice(keyword);
		map.put(cv, list);
	}

	public Map<CharSlice, CharSlice[]> getUnmodifiableMap() {
		return Collections.unmodifiableMap(map);
	}

	public Map<CharSlice, CharSlice[]> map() {
		return map;
	}
	
	public void setMap(Map<CharSlice, CharSlice[]> map) {
		this.map = map;
	}
	
	
	public boolean containsKey(CharSlice key){
		return map.containsKey(key);
	}
	
	public CharSlice[] get(CharSlice key){
		return map.get(key);
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {

		DataOutput output = new DataOutputStream(out);
		Iterator<CharSlice> keySet = map.keySet().iterator();
		// write size of map
		output.writeVInt(map.size());
		// write key and value map
		while (keySet.hasNext()) {
			// write key
			CharSlice key = keySet.next();
			output.writeUString(key.array(), key.start(), key.length());
			// write values
			CharSlice[] values = map.get(key);
			output.writeInt(values.length);
			for (CharSlice value : values) {
				output.writeUString(value.array(), value.start(), value.length());
			}
		}

	}

	@Override
	public void readFrom(InputStream in) throws IOException {
		DataInput input = new DataInputStream(in);

		map = new CharVectorHashMap<CharSlice[]>(ignoreCase);

		int size = input.readVInt();
		for (int entryInx = 0; entryInx < size; entryInx++) {
			CharSlice key = new CharSlice(input.readUString());

			int valueLength = input.readVInt();

			CharSlice[] values = new CharSlice[valueLength];

			for (int valueInx = 0; valueInx < valueLength; valueInx++) {
				values[valueInx] = new CharSlice(input.readUString());
			}
			map.put(key, values);
		}

	}

	@Override
	public void addSourceLineEntry(String line) {
		String[] kv = line.split("\t");
		if (kv.length == 1) {
			String value = kv[0].trim();
			addEntry(null, new String[] { value });
		} else if (kv.length == 2) {
			String keyword = kv[0].trim();
			String value = kv[1].trim();
			addEntry(keyword, new String[] { value });
		}
	}

	@Override
	public void reload(Object object) throws IllegalArgumentException {
		if(object != null && object instanceof MapDictionary){
			MapDictionary mapDictionary = (MapDictionary) object;
			this.map = mapDictionary.map();
			
		}else{
			throw new IllegalArgumentException("Reload dictionary argument error. argument = " + object);
		}
	}
}