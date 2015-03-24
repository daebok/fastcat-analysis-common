package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

import java.util.List;

/**
 */
public interface SynonymAttribute extends Attribute {

	public void setSynonyms(List synonym);
	
	public List getSynonyms();
	
}
