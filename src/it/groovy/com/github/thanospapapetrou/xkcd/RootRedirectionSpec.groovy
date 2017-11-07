package com.github.thanospapapetrou.xkcd

import geb.spock.GebReportingSpec

class RootRedirectionSpec extends GebReportingSpec {
  private static final String CURRENT_COMIC_PATH = 'comic/current'
  private static final String ROOT_PATH = ''

  void 'Accessing root path redirects to current comic'() {
    when: 'root path is accessed'
      go ROOT_PATH
    then: 'browser is redirected to current comic'
      driver.currentUrl == browser.config.baseUrl + CURRENT_COMIC_PATH
  }
}
