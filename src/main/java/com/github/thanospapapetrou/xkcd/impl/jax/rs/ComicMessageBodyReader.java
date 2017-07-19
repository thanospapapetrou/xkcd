package com.github.thanospapapetrou.xkcd.impl.jax.rs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Message body reader for parsing an xkcd comic from its JSON representation.
 * 
 * @author thanos
 */
@Consumes(MediaType.APPLICATION_JSON)
@Provider
public class ComicMessageBodyReader implements MessageBodyReader<Comic> {
	private static final String ALTERNATE = "alt";
	private static final String DAY = "day";
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT+00:00");
	private static final String ID = "num";
	private static final String IMAGE = "img";
	private static final String LINK = "link";
	private static final String MONTH = "month";
	private static final String NEWS = "news";
	private static final String NULL_BASE_URL = "Base URL must not be null";
	private static final String NULL_INPUT = "Input must not be null";
	private static final String NULL_MEDIA_TYPE = "Media type must not be null";
	private static final String SAFE_TITLE = "safe_title";
	private static final String TITLE = "title";
	private static final String TRANSCRIPT = "transcript";
	private static final String YEAR = "year";

	private final URL baseUrl;

	/**
	 * Construct a new comic message body reader. Instances of this class are thread-safe.
	 * 
	 * @param baseUrl
	 *            the base URL to use for resolving relative link URLs in comic JSON
	 */
	public ComicMessageBodyReader(final URL baseUrl) {
		this.baseUrl = Objects.requireNonNull(baseUrl, NULL_BASE_URL);
	}

	private static Date convertDate(final int year, final int month, final int day) {
		final Calendar calendar = new GregorianCalendar(GMT, Locale.ROOT);
		calendar.set(Calendar.ERA, GregorianCalendar.AD);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.ZONE_OFFSET, 0);
		calendar.set(Calendar.DST_OFFSET, 0);
		return calendar.getTime();
	}

	private static Charset getCharset(final MediaType mediaType) {
		Objects.requireNonNull(mediaType, NULL_MEDIA_TYPE);
		final Map<String, String> parameters = mediaType.getParameters();
		if (parameters.containsKey(MediaType.CHARSET_PARAMETER)) {
			final String charset = parameters.get(MediaType.CHARSET_PARAMETER);
			return Charset.isSupported(charset) ? Charset.forName(charset) : null;
		}
		return StandardCharsets.UTF_8;
	}

	private static JsonObject readJson(final InputStream input, final Charset charset) {
		return Json.createReader(new InputStreamReader(input, charset)).readObject();
	}

	@Override
	public boolean isReadable(final Class<?> clazz, final Type type, final Annotation[] annotations, final MediaType mediaType) {
		return getCharset(mediaType) != null;
	}

	@Override
	public Comic readFrom(final Class<Comic> clazz, final Type type, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream input) throws IOException {
		Objects.requireNonNull(input, NULL_INPUT);
		final JsonObject json = readJson(input, getCharset(mediaType));
		return new Comic(json.getInt(ID), convertDate(Integer.parseInt(json.getString(YEAR)), Integer.parseInt(json.getString(MONTH)) - 1, Integer.parseInt(json.getString(DAY))), json.getString(TITLE), json.getString(SAFE_TITLE), new URL(json.getString(IMAGE)), json.getString(ALTERNATE), json.getString(TRANSCRIPT), json.getString(LINK).isEmpty() ? null : new URL(baseUrl, json.getString(LINK)), json.getString(NEWS));
	}
}
