package com.github.thanospapapetrou.xkcd.impl.jax.rs

import java.nio.charset.StandardCharsets

import javax.json.Json
import javax.json.JsonWriter
import javax.ws.rs.core.MediaType

import spock.lang.Specification
import spock.lang.Unroll

import com.github.thanospapapetrou.xkcd.domain.Comic

class ComicMessageBodyReaderSpec extends Specification { // TODO add scenarios for private and review
	private static final String ABSOLUTE = 'absolute'
	private static final String ALTERNATE = 'Alternate'
	private static final URL BASE_URL = new URL('http://www.example.org/')
	private static final Date DATE = new Date(0L)
	private static final int ID = 1024
	private static final URL IMAGE = new URL('http://www.example.org/image')
	private static final URL LINK = new URL('http://www.example.org/link')
	private static final String NEWS = 'News'
	private static final String NO = 'no'
	private static final String RELATIVE = 'relative'
	private static final String SAFE_TITLE = 'Safe Title'
	private static final String SUPPORTED = 'supported'
	private static final String TITLE = 'Title'
	private static final String TRANSCRIPT = 'Transcript'
	private static final String UNSUPPORTED = 'unsupported'
	private static final String UNSUPPORTED_CHARSET = 'UnsupportedCharset'
	private static final String YEAR = '1970'
	private static final String MONTH = '1'
	private static final String DAY = '1'

	private ComicMessageBodyReader comicMessageBodyReader

	void setup() {
		comicMessageBodyReader = new ComicMessageBodyReader(BASE_URL)
	}

	@Unroll('Checking a media type with #charset charset if it is readable')
	void 'Checking a media type with charset if it is readable'() {
		when: 'media type is checked if it is readable'
			boolean result = comicMessageBodyReader.isReadable(null, null, null, mediaType)
		then: 'no interactions happen'
			0 * _
		and: 'the expected result is returned'
			result == readable
		where:
			charset     | mediaType                                                                  | readable
			SUPPORTED   | MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name()) | true
			UNSUPPORTED | MediaType.APPLICATION_JSON_TYPE.withCharset(UNSUPPORTED_CHARSET)           | false
			NO          | MediaType.APPLICATION_JSON_TYPE                                            | true
	}

	@Unroll('Reading a comic with #linkDescription link using media type with #charset charset')
	void 'Reading a comic with link using media type with charset'() {
		given: 'an input stream containing a json object'
			ByteArrayOutputStream buffer = new ByteArrayOutputStream()
			JsonWriter jsonWriter = Json.createWriter(new OutputStreamWriter(buffer, ComicMessageBodyReader.getCharset(mediaType)))
			jsonWriter.writeObject(Json.createObjectBuilder().add(ComicMessageBodyReader.ID, ID).add(ComicMessageBodyReader.YEAR, YEAR).add(ComicMessageBodyReader.MONTH, MONTH).add(ComicMessageBodyReader.DAY, DAY).add(ComicMessageBodyReader.TITLE, TITLE).add(ComicMessageBodyReader.SAFE_TITLE, SAFE_TITLE).add(ComicMessageBodyReader.IMAGE, IMAGE.toString()).add(ComicMessageBodyReader.ALTERNATE, ALTERNATE).add(ComicMessageBodyReader.TRANSCRIPT, TRANSCRIPT).add(ComicMessageBodyReader.LINK, jsonLink).add(ComicMessageBodyReader.NEWS, NEWS).build())
			jsonWriter.close()
			InputStream input = new ByteArrayInputStream(buffer.toByteArray())
		when: 'comic is read'
			Comic result = comicMessageBodyReader.readFrom(null, null, null, mediaType, null, input)
		then: 'no interactions happen'
			0 * _
		and: 'the comic read is returned'
			with (result) {
				id == ID
				date == DATE
				title == TITLE
				safeTitle == SAFE_TITLE
				image == IMAGE
				alternate == ALTERNATE
				transcript == TRANSCRIPT
				link == resultLink
				news == NEWS
			}
		where:
			linkDescription | jsonLink                                             | resultLink | charset     | mediaType
			ABSOLUTE      | LINK.toString()                                      | LINK       | SUPPORTED   | MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())
			ABSOLUTE      | LINK.toString()                                      | LINK       | NO          | MediaType.APPLICATION_JSON_TYPE
			RELATIVE      | BASE_URL.toURI().relativize(LINK.toURI()).toString() | LINK       | SUPPORTED   | MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())
			RELATIVE      | BASE_URL.toURI().relativize(LINK.toURI()).toString() | LINK       | NO          | MediaType.APPLICATION_JSON_TYPE
			NO            | ''                                                   | null       | SUPPORTED   | MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())
			NO            | ''                                                   | null       | NO          | MediaType.APPLICATION_JSON_TYPE
	}
}
