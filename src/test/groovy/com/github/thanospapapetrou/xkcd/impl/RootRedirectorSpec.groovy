package com.github.thanospapapetrou.xkcd.impl

import javax.servlet.http.HttpServletResponse

import spock.lang.Specification

class RootRedirectorSpec extends Specification {
	def 'Accessing root path redirects to current comic'() {
	given: 'an HTTP servlet response'
		HttpServletResponse response = Mock(HttpServletResponse)
	when: 'an HTTP GET request is performed on root path of the web application'
		new RootRedirector().doGet(null, response)
	then: 'a redirection to current comic is sent as response'
		1 * response.sendRedirect(RootRedirector.CURRENT_COMIC)
	}
}
