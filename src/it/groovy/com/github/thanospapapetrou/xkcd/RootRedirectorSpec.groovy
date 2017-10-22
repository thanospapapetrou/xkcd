package com.github.thanospapapetrou.xkcd

import geb.spock.GebReportingSpec

import spock.lang.Shared

class RootRedirectorSpec extends GebReportingSpec {
  def 'Accessing root path redirects to current comic'() {
    when: 'root path is accessed'
      go 'http://localhost:8080/xkcd/'
    then: 'browser is redirected to current comic'
      driver.currentUrl == 'http://localhost:8080/xkcd/comic/current'
  }
}
