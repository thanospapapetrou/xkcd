package com.github.thanospapapetrou.xkcd.impl.jax.rs

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

import javax.json.JsonObject
import javax.json.JsonReader
import javax.json.JsonReaderFactory
import javax.ws.rs.core.MediaType

import spock.lang.Specification
import spock.lang.Unroll

import com.github.thanospapapetrou.xkcd.domain.Comic

class ComicMessageBodyReaderSpec extends Specification {
	private static final String ALTERNATE = 'Alternate'
	private static final URL BASE_URL = new URL('http://www.example.org/')
	private static final int DAY = 1
	private static final int ID = 1024
	private static final URL IMAGE = new URL('http://www.example.org/image')
	private static final URL LINK = new URL('http://www.example.org/link')
	private static final String MALFORMED_URL = 'foo://www.example.org/'
	private static final int MONTH = 1
	private static final String NEWS = 'News'
	private static final String SAFE_TITLE = 'Safe Title'
	private static final String TITLE = 'Title'
	private static final long TIME = 1048576L
	private static final String TRANSCRIPT = 'Transcript'
	private static final String UNSUPPORTED_CHARSET = 'UNSUPPORTED_CHARSET'
	private static final int YEAR = 1970

	private ComicMessageBodyReader comicMessageBodyReader

	void setup() {
		comicMessageBodyReader = Spy(ComicMessageBodyReader, constructorArgs:[BASE_URL, Mock(JsonReaderFactory), Mock(ThreadLocal)])
	}

	@Unroll('Checking a media type with #charset charset if it is readable')
	void 'Checking a media type with charset if it is readable'() {
		given: 'a media type'
			MediaType mediaType = Mock(MediaType)
		when: 'media type is checked if it is readable'
			boolean result = comicMessageBodyReader.isReadable(null, null, null, mediaType)
		then: 'media type charset is retrieved'
			1 * comicMessageBodyReader.getCharset(mediaType) >> charset
		and: 'the initial call is concluded'
			1 * comicMessageBodyReader.isReadable(null, null, null, mediaType)
		and: 'no other interactions happen'
			0 * _
		and: 'the expected result is returned'
			result == readable
		where:
			charset                || readable
			null                   || false
			StandardCharsets.UTF_8 || true
	}

	@Unroll('Reading a comic with link "#jsonLink"')
	void 'Reading a comic with link'() {
		given: 'a media type'
			MediaType mediaType = Mock(MediaType)
		and: 'an input stream'
			InputStream input = Mock(InputStream)
		and: 'a charset'
			Charset charset = Mock(Charset)
		and: 'a JSON reader'
			JsonReader jsonReader = Mock(JsonReader)
		and: 'a JSON object'
			JsonObject json = Mock(JsonObject)
		and: 'a calendar'
			Calendar calendar = Mock(Calendar)
		when: 'comic is read'
			Comic result = comicMessageBodyReader.readFrom(null, null, null, mediaType, null, input)
		then: 'media type charset is retrieved'
			1 * comicMessageBodyReader.getCharset(mediaType) >> charset
		and: 'a JSON reader is created'
			1 * comicMessageBodyReader.jsonReaderFactory.createReader(input, charset) >> jsonReader
		and: 'a JSON object is read'
			1 * jsonReader.readObject() >> json
		and: 'a calendar instance is retrieved'
			1 * comicMessageBodyReader.calendar.get() >> calendar
		and: 'calendar era is set'
			1 * calendar.set(Calendar.ERA, GregorianCalendar.AD)
		and: 'JSON object year is retrieved'
			1 * json.getString(ComicMessageBodyReader.YEAR) >> YEAR.toString()
		and: 'calendar year is set'
			1 * calendar.set(Calendar.YEAR, YEAR)
		and: 'JSON object month is retrieved'
			1 * json.getString(ComicMessageBodyReader.MONTH) >> MONTH.toString()
		and: 'calendar month is set'
			1 * calendar.set(Calendar.MONTH, MONTH - 1)
		and: 'JSON object day is retrieved'
			1 * json.getString(ComicMessageBodyReader.DAY) >> DAY.toString()
		and: 'calendar date is set'
			1 * calendar.set(Calendar.DATE, DAY)
		and: 'calendar hour of day is set'
			1 * calendar.set(Calendar.HOUR_OF_DAY, 0)
		and: 'calendar minute is set'
			1 * calendar.set(Calendar.MINUTE, 0)
		and: 'calendar second is set'
			1 * calendar.set(Calendar.SECOND, 0)
		and: 'calendar millisecond is set'
			1 * calendar.set(Calendar.MILLISECOND, 0)
		and: 'JSON object link is retrieved'
			1 * json.getString(ComicMessageBodyReader.LINK) >> jsonLink
		and: 'JSON object ID is retrieved'
			1 * json.getInt(ComicMessageBodyReader.ID) >> ID
		and: 'calendar time is retrieved'
			1 * calendar.timeInMillis >> TIME
		and: 'JSON object title is retrieved'
			1 * json.getString(ComicMessageBodyReader.TITLE) >> TITLE
		and: 'JSON object safe title is retrieved'
			1 * json.getString(ComicMessageBodyReader.SAFE_TITLE) >> SAFE_TITLE
		and: 'JSON object image is retrieved'
			1 * json.getString(ComicMessageBodyReader.IMAGE) >> IMAGE.toString()
		and: 'JSON object alternate is retrieved'
			1 * json.getString(ComicMessageBodyReader.ALTERNATE) >> ALTERNATE
		and: 'JSON object transcript is retrieved'
			1 * json.getString(ComicMessageBodyReader.TRANSCRIPT) >> TRANSCRIPT
		and: 'JSON object news are retrieved'
			1 * json.getString(ComicMessageBodyReader.NEWS) >> NEWS
		and: 'the initial call is concluded'
			1 * comicMessageBodyReader.readFrom(null, null, null, mediaType, null, input)
		and: 'no other interactions happen'
			0 * _
		and: 'the comic read is returned'
			with (result) {
				id == ID
				date == new Date(TIME)
				title == TITLE
				safeTitle == SAFE_TITLE
				image == IMAGE
				alternate == ALTERNATE
				transcript == TRANSCRIPT
				link == resultLink
				news == NEWS
			}
		where:
			jsonLink                                             || resultLink
			''                                                   || null
			LINK.toString()                                      || LINK
			BASE_URL.toURI().relativize(LINK.toURI()).toString() || LINK
	}

	void 'Error reading a comic with malformed image URL'() {
		given: 'a media type'
			MediaType mediaType = Mock(MediaType)
		and: 'an input stream'
			InputStream input = Mock(InputStream)
		and: 'a charset'
			Charset charset = Mock(Charset)
		and: 'a JSON reader'
			JsonReader jsonReader = Mock(JsonReader)
		and: 'a JSON object'
			JsonObject json = Mock(JsonObject)
		and: 'a calendar'
			Calendar calendar = Mock(Calendar)
		when: 'comic is read'
			comicMessageBodyReader.readFrom(null, null, null, mediaType, null, input)
		then: 'media type charset is retrieved'
			1 * comicMessageBodyReader.getCharset(mediaType) >> charset
		and: 'a JSON reader is created'
			1 * comicMessageBodyReader.jsonReaderFactory.createReader(input, charset) >> jsonReader
		and: 'a JSON object is read'
			1 * jsonReader.readObject() >> json
		and: 'a calendar instance is retrieved'
			1 * comicMessageBodyReader.calendar.get() >> calendar
		and: 'calendar era is set'
			1 * calendar.set(Calendar.ERA, GregorianCalendar.AD)
		and: 'JSON object year is retrieved'
			1 * json.getString(ComicMessageBodyReader.YEAR) >> YEAR.toString()
		and: 'calendar year is set'
			1 * calendar.set(Calendar.YEAR, YEAR)
		and: 'JSON object month is retrieved'
			1 * json.getString(ComicMessageBodyReader.MONTH) >> MONTH.toString()
		and: 'calendar month is set'
			1 * calendar.set(Calendar.MONTH, MONTH - 1)
		and: 'JSON object day is retrieved'
			1 * json.getString(ComicMessageBodyReader.DAY) >> DAY.toString()
		and: 'calendar date is set'
			1 * calendar.set(Calendar.DATE, DAY)
		and: 'calendar hour of day is set'
			1 * calendar.set(Calendar.HOUR_OF_DAY, 0)
		and: 'calendar minute is set'
			1 * calendar.set(Calendar.MINUTE, 0)
		and: 'calendar second is set'
			1 * calendar.set(Calendar.SECOND, 0)
		and: 'calendar millisecond is set'
			1 * calendar.set(Calendar.MILLISECOND, 0)
		and: 'JSON object link is retrieved'
			1 * json.getString(ComicMessageBodyReader.LINK) >> LINK.toString()
		and: 'JSON object ID is retrieved'
			1 * json.getInt(ComicMessageBodyReader.ID) >> ID
		and: 'calendar time is retrieved'
			1 * calendar.timeInMillis >> TIME
		and: 'JSON object title is retrieved'
			1 * json.getString(ComicMessageBodyReader.TITLE) >> TITLE
		and: 'JSON object safe title is retrieved'
			1 * json.getString(ComicMessageBodyReader.SAFE_TITLE) >> SAFE_TITLE
		and: 'JSON object image is retrieved'
			1 * json.getString(ComicMessageBodyReader.IMAGE) >> MALFORMED_URL
		and: 'the initial call is concluded'
			1 * comicMessageBodyReader.readFrom(null, null, null, mediaType, null, input)
		and: 'no other interactions happen'
			0 * _
		and: 'a malformed URL exception is thrown'
			thrown(MalformedURLException)
	}

	void 'Error reading a comic with a malformed link URL'() {
		given: 'a media type'
			MediaType mediaType = Mock(MediaType)
		and: 'an input stream'
			InputStream input = Mock(InputStream)
		and: 'a charset'
			Charset charset = Mock(Charset)
		and: 'a JSON reader'
			JsonReader jsonReader = Mock(JsonReader)
		and: 'a JSON object'
			JsonObject json = Mock(JsonObject)
		and: 'a calendar'
			Calendar calendar = Mock(Calendar)
		when: 'comic is read'
			comicMessageBodyReader.readFrom(null, null, null, mediaType, null, input)
		then: 'media type charset is retrieved'
			1 * comicMessageBodyReader.getCharset(mediaType) >> charset
		and: 'a JSON reader is created'
			1 * comicMessageBodyReader.jsonReaderFactory.createReader(input, charset) >> jsonReader
		and: 'a JSON object is read'
			1 * jsonReader.readObject() >> json
		and: 'a calendar instance is retrieved'
			1 * comicMessageBodyReader.calendar.get() >> calendar
		and: 'calendar era is set'
			1 * calendar.set(Calendar.ERA, GregorianCalendar.AD)
		and: 'JSON object year is retrieved'
			1 * json.getString(ComicMessageBodyReader.YEAR) >> YEAR.toString()
		and: 'calendar year is set'
			1 * calendar.set(Calendar.YEAR, YEAR)
		and: 'JSON object month is retrieved'
			1 * json.getString(ComicMessageBodyReader.MONTH) >> MONTH.toString()
		and: 'calendar month is set'
			1 * calendar.set(Calendar.MONTH, MONTH - 1)
		and: 'JSON object day is retrieved'
			1 * json.getString(ComicMessageBodyReader.DAY) >> DAY.toString()
		and: 'calendar date is set'
			1 * calendar.set(Calendar.DATE, DAY)
		and: 'calendar hour of day is set'
			1 * calendar.set(Calendar.HOUR_OF_DAY, 0)
		and: 'calendar minute is set'
			1 * calendar.set(Calendar.MINUTE, 0)
		and: 'calendar second is set'
			1 * calendar.set(Calendar.SECOND, 0)
		and: 'calendar millisecond is set'
			1 * calendar.set(Calendar.MILLISECOND, 0)
		and: 'JSON object link is retrieved'
			1 * json.getString(ComicMessageBodyReader.LINK) >> MALFORMED_URL
		and: 'JSON object ID is retrieved'
			1 * json.getInt(ComicMessageBodyReader.ID) >> ID
		and: 'calendar time is retrieved'
			1 * calendar.timeInMillis >> TIME
		and: 'JSON object title is retrieved'
			1 * json.getString(ComicMessageBodyReader.TITLE) >> TITLE
		and: 'JSON object safe title is retrieved'
			1 * json.getString(ComicMessageBodyReader.SAFE_TITLE) >> SAFE_TITLE
		and: 'JSON object image is retrieved'
			1 * json.getString(ComicMessageBodyReader.IMAGE) >> IMAGE.toString()
		and: 'JSON object alternate is retrieved'
			1 * json.getString(ComicMessageBodyReader.ALTERNATE) >> ALTERNATE
		and: 'JSON object transcript is retrieved'
			1 * json.getString(ComicMessageBodyReader.TRANSCRIPT) >> TRANSCRIPT
		and: 'the initial call is concluded'
			1 * comicMessageBodyReader.readFrom(null, null, null, mediaType, null, input)
		and: 'no other interactions happen'
			0 * _
		and: 'a malformed URL exception is thrown'
			thrown(MalformedURLException)
		}

	void 'Retrieving charset of media type with supported charset'() {
		given: 'a media type'
			MediaType mediaType = Mock(MediaType)
		and: 'a parameter map'
			Map<String, String> parameters = Mock(Map)
		when: 'media type charset is retrieved'
			Charset result = comicMessageBodyReader.getCharset(mediaType)
		then: 'media type parameters are retrieved'
			1 * mediaType.parameters >> parameters
		and: 'parameters are checked if they contain \'charset\' parameter'
			1 * parameters.containsKey(MediaType.CHARSET_PARAMETER) >> true
		and: '\'charset\' parameter is retrieved'
			1 * parameters.get(MediaType.CHARSET_PARAMETER) >> StandardCharsets.UTF_8.name()
		and: 'the initial call is concluded'
			1 * comicMessageBodyReader.getCharset(mediaType)
		and: 'no other interactions happen'
			0 * _
		and: 'the expected charset is returned'
			result == StandardCharsets.UTF_8
	}

	void 'Retrieving charset of media type with unsupported charset'() {
		given: 'a media type'
			MediaType mediaType = Mock(MediaType)
		and: 'a parameter map'
			Map<String, String> parameters = Mock(Map)
		when: 'media type charset is retrieved'
			Charset result = comicMessageBodyReader.getCharset(mediaType)
		then: 'media type parameters are retrieved'
			1 * mediaType.parameters >> parameters
		and: 'parameters are checked if they contain \'charset\' parameter'
			1 * parameters.containsKey(MediaType.CHARSET_PARAMETER) >> true
		and: '\'charset\' parameter is retrieved'
			1 * parameters.get(MediaType.CHARSET_PARAMETER) >> UNSUPPORTED_CHARSET
		and: 'the initial call is concluded'
			1 * comicMessageBodyReader.getCharset(mediaType)
		and: 'no other interactions happen'
			0 * _
		and: 'null is returned'
			result == null
	}

	void 'Retrieving charset of media type with no charset'() {
		given: 'a media type'
			MediaType mediaType = Mock(MediaType)
		and: 'a parameter map'
			Map<String, String> parameters = Mock(Map)
		when: 'media type charset is retrieved'
			Charset result = comicMessageBodyReader.getCharset(mediaType)
		then: 'media type parameters are retrieved'
			1 * mediaType.parameters >> parameters
		and: 'parameters are checked if they contain \'charset\' parameter'
			1 * parameters.containsKey(MediaType.CHARSET_PARAMETER) >> false
		and: 'the initial call is concluded'
			1 * comicMessageBodyReader.getCharset(mediaType)
		and: 'no other interactions happen'
			0 * _
		and: 'ISO-8859-1 is returned'
			result == StandardCharsets.ISO_8859_1
	}
}
