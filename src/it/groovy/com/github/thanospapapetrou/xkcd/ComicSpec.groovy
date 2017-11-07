package com.github.thanospapapetrou.xkcd

import geb.spock.GebReportingSpec

class ComicSpec extends GebReportingSpec {
  private static final int ID = 1024
  private static final String TITLE = 'Error Code'

  void 'Retrieving specific comic'() {
    when: 'viewing comic'
      ComicPage page = to ComicPage, ID
    then: 'title is correct'
      page.comicTitle == TITLE
  }
}
