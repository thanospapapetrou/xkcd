package com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Attribute converter for truncating transcript to required length. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@Converter
public class TranscriptConverter extends TruncatingStringConverter implements AttributeConverter<String, String> {
	/**
	 * Construct a new transcript converter.
	 */
	public TranscriptConverter() {
		super(Comic.TRANSCRIPT_LENGTH);
	}
}
