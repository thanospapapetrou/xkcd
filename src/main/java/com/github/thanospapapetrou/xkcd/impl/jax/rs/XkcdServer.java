package com.github.thanospapapetrou.xkcd.impl.jax.rs;

import java.util.Objects;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.thanospapapetrou.xkcd.api.Xkcd;
import com.github.thanospapapetrou.xkcd.api.XkcdException;
import com.github.thanospapapetrou.xkcd.domain.Comic;

/**
 * Class implementing an xkcd server using JAX-RS. Instances of this class are thread safe, provided they are constructed with a thread-safe xkcd.
 * 
 * @author thanos
 */
@Alternative
@Path("/")
public class XkcdServer implements Xkcd {
	private static final String NULL_XKCD = "xkcd must not be null";

	private final Xkcd xkcd;

	/**
	 * Construct a new xkcd server.
	 * 
	 * @param xkcd
	 *            the xkcd to use for retrieving comics
	 */
	@Inject
	public XkcdServer(final Xkcd xkcd) {
		this.xkcd = Objects.requireNonNull(xkcd, NULL_XKCD);
	}

	XkcdServer() {
		// this constructor exists just to keep CDI happy
		xkcd = null;
	}

	@GET
	@Override
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_XHTML_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	public Comic getComic(@PathParam("id") final int id) throws XkcdException {
		final Comic comic = xkcd.getComic(id);
		if (comic == null) {
			throw new NotFoundException();
		}
		return comic;
	}

	@GET
	@Override
	@Path("/current")
	@Produces({MediaType.APPLICATION_XHTML_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	public Comic getCurrentComic() throws XkcdException {
		final Comic comic = xkcd.getCurrentComic();
		if (comic == null) {
			throw new NotFoundException();
		}
		return comic;
	}
}
