package com.github.thanospapapetrou.xkcd.impl.jax.rs;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.github.thanospapapetrou.xkcd.api.Xkcd;
import com.github.thanospapapetrou.xkcd.api.XkcdException;
import com.github.thanospapapetrou.xkcd.domain.Comic;
import com.github.thanospapapetrou.xkcd.impl.cdi.Configuration;

/**
 * Class implementing an xkcd client using JAX-RS. Instances of this class are not thread-safe.
 * 
 * @author thanos
 */
@RequestScoped
public class XkcdClient implements AutoCloseable, Xkcd {
	private static final String APPLICATION_JSON_CHARSET_UTF_8 = MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name()).toString();
	private static final String COMIC_NOT_FOUND = "Comic %1$d not found at %2$s";
	private static final String COMIC_RETRIEVED_FROM = "Comic %1$d retrieved from %2$s";
	private static final String CURRENT_COMIC_RETRIEVED_FROM = "Current comic (%1$d) retrieved from %2$s";
	private static final String ERROR_RETRIEVING_COMIC = "Error retrieving comic %1$d from %2$s";
	private static final String ERROR_RETRIEVING_CURRENT_COMIC = "Error retrieving current comic from %1$s";
	private static final String GET_COMIC = "/{id}/info.0.json";
	private static final String GET_CURRENT_COMIC = "/info.0.json";
	private static final String ID = "id";
	private static final String NULL_BASE_URL = "Base URL must not be null";

	private final Client client;
	private final WebTarget target;
	private final Logger logger;

	/**
	 * Construct a new xkcd client.
	 * 
	 * @param baseUrl
	 *            the base URL to use for retrieving comics
	 */
	@Inject
	public XkcdClient(@Configuration(Configuration.Key.BASE_URL) final URL baseUrl) {
		client = ClientBuilder.newBuilder().register(new ComicMessageBodyReader(Objects.requireNonNull(baseUrl, NULL_BASE_URL))).build();
		target = client.target(baseUrl.toString());
		logger = Logger.getLogger(XkcdClient.class.getCanonicalName());
	}

	XkcdClient() {
		// this constructor exists just to keep CDI happy
		client = null;
		target = null;
		logger = null;
	}

	@SuppressWarnings("unused")
	private XkcdClient(final Client client, final WebTarget target, final Logger logger) {
		// this constructor exists just for testing
		this.client = client;
		this.target = target;
		this.logger = logger;
	}

	@Override
	@PreDestroy
	public void close() {
		client.close();
	}

	@Override
	public Comic getComic(final int id) throws XkcdException {
		final WebTarget target = this.target.path(GET_COMIC).resolveTemplate(ID, id);
		try {
			final Comic comic = target.request(APPLICATION_JSON_CHARSET_UTF_8).buildGet().invoke(Comic.class);
			logger.fine(String.format(COMIC_RETRIEVED_FROM, id, target.getUri()));
			return comic;
		} catch (final NotFoundException e) {
			logger.fine(String.format(COMIC_NOT_FOUND, id, target.getUri()));
			return null;
		} catch (final WebApplicationException e) {
			throw new XkcdException(String.format(ERROR_RETRIEVING_COMIC, id, target.getUri()), e);
		}
	}

	@Override
	public Comic getCurrentComic() throws XkcdException {
		final WebTarget target = this.target.path(GET_CURRENT_COMIC);
		try {
			final Comic comic = target.request(APPLICATION_JSON_CHARSET_UTF_8).buildGet().invoke(Comic.class);
			logger.fine(String.format(CURRENT_COMIC_RETRIEVED_FROM, comic.getId(), target.getUri()));
			return comic;
		} catch (final WebApplicationException e) {
			throw new XkcdException(String.format(ERROR_RETRIEVING_CURRENT_COMIC, target.getUri()), e);
		}
	}
}
