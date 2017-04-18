package com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters;

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.AttributeConverter;

/**
 * Attribute converter for converting URLs to strings and vice versa, truncating them to specific length. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
public class UrlConverter implements AttributeConverter<URL, String> {
	private static final String ERROR_CONVERTING_STRING_TO_URL = "Error converting string %1$s to URL";

	private final TruncatingStringConverter truncatingStringConverter;

	/**
	 * Construct a new URL converter.
	 * 
	 * @param length
	 *            the length to truncate URLs to
	 */
	public UrlConverter(final int length) {
		truncatingStringConverter = new TruncatingStringConverter(length);
	}

	@Override
	public String convertToDatabaseColumn(final URL url) {
		return (url == null) ? null : truncatingStringConverter.convertToDatabaseColumn(url.toString());
	}

	@Override
	public URL convertToEntityAttribute(final String url) {
		try {
			return (url == null) ? null : new URL(url);
		} catch (final MalformedURLException e) {
			throw new IllegalStateException(String.format(ERROR_CONVERTING_STRING_TO_URL, url), e);
		}
	}
}
