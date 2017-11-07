package com.github.thanospapapetrou.xkcd

import geb.spock.GebReportingSpec

class CurrentComicSpec extends GebReportingSpec {
  private static final String CURRENT = 'current'

  void 'Retrieving current comic'() {
    when: 'viewing current comic'
      ComicPage page = to ComicPage, CURRENT
    then: 'title is not null'
      page.comicTitle
  }
}
