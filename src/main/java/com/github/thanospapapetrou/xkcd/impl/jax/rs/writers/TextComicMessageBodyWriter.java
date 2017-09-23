package com.github.thanospapapetrou.xkcd.impl.jax.rs.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Message body writer for converting an xkcd comic to its text representation. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@Produces(MediaType.TEXT_PLAIN)
@Provider
public class TextComicMessageBodyWriter extends AbstractComicMessageBodyWriter {
	private static final String FORMAT = "%1$s\n%2$s\n";
	private static final String NULL_COMIC = "Comic must not be null";
	private static final String NULL_HTTP_HEADERS = "HTTP headers must not be null";
	private static final String NULL_OUTPUT = "Output must not be null";
	private static final MediaType TEXT_PLAIN_CHARSET_UTF_8 = MediaType.TEXT_PLAIN_TYPE.withCharset(StandardCharsets.UTF_8.name());

	@Override
	public void writeTo(final Comic comic, final Class<?> clazz, final Type type, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream output) throws IOException {
		Objects.requireNonNull(comic, NULL_COMIC);
		Objects.requireNonNull(httpHeaders, NULL_HTTP_HEADERS);
		Objects.requireNonNull(output, NULL_OUTPUT);
		httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN_CHARSET_UTF_8);
		createOutputStreamWriter(output).write(String.format(FORMAT, comic.getTitle(), comic.getTranscript()));
	}

	OutputStreamWriter createOutputStreamWriter(final OutputStream output) {
		// this method is package private just for testing
		return new OutputStreamWriter(output, StandardCharsets.UTF_8);
	}
}
