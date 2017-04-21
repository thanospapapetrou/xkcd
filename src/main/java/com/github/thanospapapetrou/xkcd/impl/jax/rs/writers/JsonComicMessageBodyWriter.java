package com.github.thanospapapetrou.xkcd.impl.jax.rs.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Message body writer for converting an xkcd comic to its JSON representation. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@Produces(MediaType.APPLICATION_JSON)
@Provider
public class JsonComicMessageBodyWriter implements MessageBodyWriter<Comic> {
	private static final String ALTERNATE = "alt";
	private static final String DATE = "date";
	private static final String ID = "num";
	private static final String IMAGE = "img";
	private static final String LINK = "link";
	private static final String NEWS = "news";
	private static final String NULL_COMIC = "Comic must not be null";
	private static final String NULL_HTTP_HEADERS = "HTTP headers must not be null";
	private static final String NULL_OUTPUT = "Output must not be null";
	private static final String SAFE_TITLE = "safe_title";
	private static final String TITLE = "title";
	private static final String TRANSCRIPT = "transcript";

	@Override
	public long getSize(final Comic comic, final Class<?> clazz, final Type type, final Annotation[] annotations, final MediaType mediaType) {
		return -1L;
	}

	@Override
	public boolean isWriteable(final Class<?> clazz, final Type type, final Annotation[] annotations, final MediaType mediaType) {
		return true;
	}

	@Override
	public void writeTo(final Comic comic, final Class<?> clazz, final Type type, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream output) throws IOException {
		Objects.requireNonNull(comic, NULL_COMIC);
		Objects.requireNonNull(httpHeaders, NULL_HTTP_HEADERS);
		Objects.requireNonNull(output, NULL_OUTPUT);
		httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_TYPE.withCharset(StandardCharsets.UTF_8.name()));
		JsonObjectBuilder json = Json.createObjectBuilder().add(ID, comic.getId()).add(DATE, comic.getDate().getTime()).add(TITLE, comic.getTitle()).add(SAFE_TITLE, comic.getSafeTitle()).add(IMAGE, comic.getImage().toString());
		json = (comic.getAlternate() == null) ? json.addNull(ALTERNATE) : json.add(ALTERNATE, comic.getAlternate());
		json = (comic.getTranscript() == null) ? json.addNull(TRANSCRIPT) : json.add(TRANSCRIPT, comic.getTranscript());
		json = (comic.getLink() == null) ? json.addNull(LINK) : json.add(LINK, comic.getLink().toString());
		json = (comic.getNews() == null) ? json.addNull(NEWS) : json.add(NEWS, comic.getNews());
		try (final JsonWriter writer = Json.createWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
			writer.writeObject(json.build());
		}
	}
}
