package com.github.thanospapapetrou.xkcd

import geb.Page

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

class ComicPage extends Page {
  private static final Pattern URL_PATTERN = Pattern.compile('/comic/(\\d+)')
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat('yyyy-MM-dd')

  static url = 'comic'
  static content = {
    id {
      Matcher matcher = URL_PATTERN.matcher(driver.currentUrl)
      matcher.matches() ? matcher.group(1) : null
    }
    date { DATE_FORMAT.parse($('time').getAttribute('datetime')) }
    comicTitle { $('h1').getAttribute('title') }
    safeTitle { $('h1').text() }
    image { $('figure img').getAttribute('src') }
    alternate { $('figcaption').text() }
    transcript { $('figure img').getAttribute('alt') }
    link { }
    news { }
  }
}
