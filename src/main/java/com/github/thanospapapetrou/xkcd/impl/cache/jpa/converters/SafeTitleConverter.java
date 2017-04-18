package com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Attribute converter for truncating safe title to required length. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@Converter
public class SafeTitleConverter extends TruncatingStringConverter implements AttributeConverter<String, String> {
	/**
	 * Construct a new safe title converter.
	 */
	public SafeTitleConverter() {
		super(Comic.SAFE_TITLE_LENGTH);
	}
}
