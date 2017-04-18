package com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters;

import java.net.URL;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Attribute converter for truncating image to required length. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@Converter
public class ImageConverter extends UrlConverter implements AttributeConverter<URL, String> {
	/**
	 * Construct a new image converter.
	 */
	public ImageConverter() {
		super(Comic.IMAGE_LENGTH);
	}
}
