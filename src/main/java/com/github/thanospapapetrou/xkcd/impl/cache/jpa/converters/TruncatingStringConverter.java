package com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters;

import java.util.logging.Logger;

import javax.persistence.AttributeConverter;

/**
 * Attribute converter for truncating strings to specific length. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
public class TruncatingStringConverter implements AttributeConverter<String, String> {
	private static final Logger LOGGER = Logger.getLogger(TruncatingStringConverter.class.getCanonicalName());
	private static final String NEGATIVE_LENGTH = "Length must not be negative";
	private static final String TRUNCATING_STRING = "Truncating string %1$s from %2$d to %3$d characters long";

	private final int length;

	/**
	 * Construct a new truncating string converter.
	 * 
	 * @param length
	 *            the length to truncate strings to
	 */
	public TruncatingStringConverter(final int length) {
		if (length < 0) {
			throw new IllegalArgumentException(NEGATIVE_LENGTH);
		}
		this.length = length;
	}

	@Override
	public String convertToDatabaseColumn(final String string) {
		if (string == null) {
			return null;
		}
		if (string.length() > length) {
			LOGGER.warning(String.format(TRUNCATING_STRING, string, string.length(), length));
			return string.substring(0, length);
		}
		return string;

	}

	@Override
	public String convertToEntityAttribute(final String string) {
		return string;
	}
}
