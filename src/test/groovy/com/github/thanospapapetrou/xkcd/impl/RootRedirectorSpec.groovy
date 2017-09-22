package com.github.thanospapapetrou.xkcd.impl

import javax.servlet.http.HttpServletResponse

import spock.lang.Specification

class RootRedirectorSpec extends Specification {
	private RootRedirector rootRedirector

	void setup() {
		rootRedirector = new RootRedirector()
	}

	void 'Accessing root path redirects to current comic'() {
		given: 'an HTTP servlet response'
			HttpServletResponse response = Mock(HttpServletResponse)
		when: 'an HTTP GET request is performed on web application root path'
			rootRedirector.doGet(null, response)
		then: 'a redirection to current comic is sent as response'
			1 * response.sendRedirect(RootRedirector.CURRENT_COMIC)
		and: 'no other interactions happen'
			0 * _
	}

	void 'Error redirecting to current comic while accessing root path'() {
		given: 'an HTTP servlet response'
			HttpServletResponse response = Mock(HttpServletResponse)
		and: 'an IO exception'
			IOException ioException = Mock(IOException)
		when: 'an HTTP GET request is performed on web application root path'
			rootRedirector.doGet(null, response)
		then: 'redirection to current comic fails'
			1 * response.sendRedirect(RootRedirector.CURRENT_COMIC) >> { throw ioException }
		and: 'no other interactions happen'
			0 * _
		and: 'IO exception is thrown'
			IOException e = thrown(IOException)
			e == ioException
	}
}
