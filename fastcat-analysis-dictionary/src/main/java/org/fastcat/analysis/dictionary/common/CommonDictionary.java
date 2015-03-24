package org.fastcat.analysis.dictionary.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CommonDictionary<E> {
	protected static final Logger logger = LoggerFactory.getLogger(CommonDictionary.class);
	private Date createTime;
	
	private Dictionary<E> systemDictionary;
	
	private Map<String, Object> dictionaryMap;
	
	public CommonDictionary(Dictionary<E> systemDictionary){
		this.systemDictionary = systemDictionary;
		dictionaryMap = new HashMap<String, Object>();
		createTime = new Date();
	}
	
	//systemDictionary를 재설정한다. dictionaryMap은 따로 외부에서 해주어야함.
	public void reset(CommonDictionary<E> dictionary){
		this.systemDictionary = dictionary.systemDictionary;
		this.createTime = dictionary.createTime;
	}
	public List<E> find(CharSlice token) {
		return systemDictionary.find(token);
	}
	public int size(){
		return systemDictionary.size();
	}
	
	public Object getDictionary(String dictionaryId){
		return dictionaryMap.get(dictionaryId);
	}
	
	public Map<String, Object> getDictionaryMap(){
		return dictionaryMap;
	}
	public Object addDictionary(String dictionaryId, Object dictionary){
		logger.debug("addDictionary {} : {}", dictionaryId, dictionary);
		return dictionaryMap.put(dictionaryId, dictionary);
	}

	public void appendAdditionalNounEntry(Set<CharSlice> keySet, String tokenType) {
		systemDictionary.appendAdditionalNounEntry(keySet, tokenType);
	}
	
	@Override
	public String toString(){
		return getClass().getSimpleName() + "] createTime=" + createTime + ", entry = " + (systemDictionary != null ? systemDictionary.size() : 0) + ", dictionaries = "+dictionaryMap.size(); 
	}
	
}
