package com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters;

import java.net.URL;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Attribute converter for truncating link to required length. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@Converter
public class LinkConverter extends UrlConverter implements AttributeConverter<URL, String> {
	/**
	 * Construct a new link converter.
	 */
	public LinkConverter() {
		super(Comic.LINK_LENGTH);
	}
}
