package com.github.thanospapapetrou.xkcd.api;

import java.util.Objects;

/**
 * Exception thrown by {@link Xkcd} if any errors occur while retrieving comics.
 * 
 * @author thanos
 */
public class XkcdException extends Exception {
	private static final String EMPTY_MESSAGE = "Message must not be empty";
	private static final String NULL_CAUSE = "Cause must not be null";
	private static final String NULL_MESSAGE = "Message must not be null";
	private static final long serialVersionUID = 0L;

	/**
	 * Construct a new xkcd exception.
	 * 
	 * @param message
	 *            the message of this xkcd exception
	 * @param cause
	 *            the cause of this xkcd exception
	 */
	public XkcdException(final String message, final Throwable cause) {
		super(requireValidMessage(message), Objects.requireNonNull(cause, NULL_CAUSE));
	}

	private static String requireValidMessage(final String message) {
		if (Objects.requireNonNull(message, NULL_MESSAGE).isEmpty()) {
			throw new IllegalArgumentException(EMPTY_MESSAGE);
		}
		return message;
	}
}
