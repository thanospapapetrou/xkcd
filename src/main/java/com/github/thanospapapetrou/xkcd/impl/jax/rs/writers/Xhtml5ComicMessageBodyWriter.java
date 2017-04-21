package com.github.thanospapapetrou.xkcd.impl.jax.rs.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.github.thanospapapetrou.xkcd.api.Xkcd;
import com.github.thanospapapetrou.xkcd.api.XkcdException;
import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Message body writer for converting an xkcd comic to its XHTML5 representation. Instances of this class are thread-safe.
 * 
 * @author thanos
 */
@Produces(MediaType.APPLICATION_XHTML_XML)
@Provider
public class Xhtml5ComicMessageBodyWriter implements MessageBodyWriter<Comic> {
	private static final String COMIC = "comic";
	private static final String CURRENT = "current";
	private static final String JSP = "/WEB-INF/comic.jspx";
	private static final String NULL_COMIC = "Comic must not be null";
	private static final String NULL_XKCD = "xkcd must not be null";
	private static final String RANDOM = "random";

	private final Xkcd xkcd;
	private final Random random;

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	/**
	 * Construct a new XHTML5 comic message body writer.
	 * 
	 * @param xkcd
	 *            the xkcd to use for retrieving current comic
	 */
	@Inject
	public Xhtml5ComicMessageBodyWriter(final Xkcd xkcd) {
		this.xkcd = Objects.requireNonNull(xkcd, NULL_XKCD);
		random = new Random();
	}

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
		try {
			final StringWriter buffer = new StringWriter();
			request.setAttribute(COMIC, comic);
			final int current = xkcd.getCurrentComic().getId();
			request.setAttribute(CURRENT, current);
			request.setAttribute(RANDOM, random.nextInt(current) + 1);
			request.getRequestDispatcher(JSP).include(request, new HttpServletResponseWrapper(response) {
				@Override
				public PrintWriter getWriter() {
					return new PrintWriter(buffer);
				}
			});
			httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XHTML_XML_TYPE.withCharset(StandardCharsets.UTF_8.name()));
			httpHeaders.putSingle(HttpHeaders.CONTENT_LANGUAGE, Locale.ENGLISH.getLanguage());
			output.write(buffer.toString().getBytes(StandardCharsets.UTF_8));
		} catch (final ServletException | XkcdException e) {
			throw new InternalServerErrorException(e);
		}
	}
}
