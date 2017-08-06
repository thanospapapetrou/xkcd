package com.github.thanospapapetrou.xkcd.jsp

import javax.ws.rs.core.Response

import spock.lang.Specification

class FunctionsSpec extends Specification {
	void 'Retrieving HTTP reason phrase corresponding to an HTTP status code'() {
		when: 'HTTP reason phrase corresponding to an HTTP status code is retrieved'
			String result = Functions.httpReasonPhrase(statusCode)
		then: 'the expected reason phrase is returned'
			result == reasonPhrase
		where:
			status << Response.Status.values()
			statusCode = status.statusCode
			reasonPhrase = status.reasonPhrase
	}
}
