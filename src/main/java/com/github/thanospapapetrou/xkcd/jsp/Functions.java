package com.github.thanospapapetrou.xkcd.jsp;

import javax.ws.rs.core.Response;

/**
 * JSP functions.
 * 
 * @author thanos
 */
public class Functions {
	private Functions() {
	}

	/**
	 * Get the HTTP reason phrase corresponding to an HTTP status code.
	 * 
	 * @param statusCode
	 *            an HTTP status code
	 * @return the corresponding HTTP reason phrase
	 */
	public static String httpReasonPhrase(final int statusCode) {
		return Response.Status.fromStatusCode(statusCode).getReasonPhrase();
	}
}
