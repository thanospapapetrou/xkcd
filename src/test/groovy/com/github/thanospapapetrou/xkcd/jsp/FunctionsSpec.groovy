package com.github.thanospapapetrou.xkcd.jsp

import javax.ws.rs.core.Response

import org.spockframework.compiler.model.Spec

class FunctionsSpec extends Spec {
	void 'Retrieving HTTP reason phrase corresponding to an HTTP status code'() {
		when: 'HTTP reason phrase corresponding to an HTTP status code is retrieved'
			String result = Functions.httpReasonPhrase(statusCode)
		then: 'the expected reason phrase is returned'
			result == reason
		where:
			status << Response.Status.values()
			statusCode = status.statusCode
			reasonPhrase = status.reasonPhrase
	}
}
