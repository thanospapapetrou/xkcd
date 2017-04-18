package com.github.thanospapapetrou.xkcd.impl.jax_rs;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.github.thanospapapetrou.xkcd.impl.jax_rs.writers.JsonComicMessageBodyWriter;
import com.github.thanospapapetrou.xkcd.impl.jax_rs.writers.TextComicMessageBodyWriter;
import com.github.thanospapapetrou.xkcd.impl.jax_rs.writers.Xhtml5ComicMessageBodyWriter;

/**
 * JAX-RS application exposing an xkcd server. This class is thread-safe.
 * 
 * @author thanos
 */
@ApplicationPath("/comic")
public class XkcdApplication extends Application {
	private static final Set<Class<?>> CLASSES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(XkcdServer.class, Xhtml5ComicMessageBodyWriter.class, JsonComicMessageBodyWriter.class, TextComicMessageBodyWriter.class)));

	@Override
	public Set<Class<?>> getClasses() {
		return CLASSES;
	}
}
