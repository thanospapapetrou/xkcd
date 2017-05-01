package com.github.thanospapapetrou.xkcd.impl.jax.rs.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Abstract class defining common implementation for all comic message body writers.
 * 
 * @author thanos
 */
public abstract class AbstractComicMessageBodyWriter implements MessageBodyWriter<Comic> {
	@Override
	public long getSize(final Comic comic, final Class<?> clazz, final Type type, final Annotation[] annotations, final MediaType mediaType) {
		return -1L;
	}

	@Override
	public boolean isWriteable(final Class<?> clazz, final Type type, final Annotation[] annotations, final MediaType mediaType) {
		return true;
	}

	@Override
	public abstract void writeTo(final Comic comic, final Class<?> clazz, final Type type, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream output) throws IOException;
}
