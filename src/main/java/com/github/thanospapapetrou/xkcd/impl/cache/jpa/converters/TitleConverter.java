package com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Attribute converter for truncating title to required length. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@Converter
public class TitleConverter extends TruncatingStringConverter implements AttributeConverter<String, String> {
	/**
	 * Construct a new title converter.
	 */
	public TitleConverter() {
		super(Comic.TITLE_LENGTH);
	}
}
