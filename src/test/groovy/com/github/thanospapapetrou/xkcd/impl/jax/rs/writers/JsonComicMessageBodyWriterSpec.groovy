package com.github.thanospapapetrou.xkcd.impl.jax.rs.writers

import java.nio.charset.StandardCharsets

import javax.json.Json
import javax.json.JsonObject
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MultivaluedMap

import spock.lang.Specification

import com.github.thanospapapetrou.xkcd.domain.Comic

class JsonComicMessageBodyWriterSpec extends Specification {
	private static final String ALTERNATE = 'Alternate'
	private static final int ID = 1024
	private static final URL IMAGE = new URL('http://www.example.org/image')
	private static final URL LINK = new URL('http://www.example.org/link')
	private static final String NEWS = 'News'
	private static final String SAFE_TITLE = 'Safe Title'
	private static final long TIME = 0L
	private static final String TITLE = 'Title'
	private static final String TRANSCRIPT = 'Transcript'

	private JsonComicMessageBodyWriter writer

	void setup() {
		writer = new JsonComicMessageBodyWriter()
	}

	void 'Converting a comic to JSON'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'a date'
			Date date = Mock(Date)
		when: 'converting a comic'
			JsonObject result = JsonComicMessageBodyWriter.convertComic(comic)
		then: 'comic ID is retrieved'
			1 * comic.id >> ID
		and: 'comic date is retrieved'
			1 * comic.date >> date
		and: 'date time is retrieved'
			1 * date.time >> TIME
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'comic safe title is retrieved'
			1 * comic.safeTitle >> SAFE_TITLE
		and: 'comic image is retrieved'
			1 * comic.image >> IMAGE
		and: 'comic alternate is retrieved'
			1 * comic.alternate >> ALTERNATE
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> TRANSCRIPT
		and: 'comic link is retrieved'
			1 * comic.link >> LINK
		and: 'comic news are retrieved'
			1 * comic.news >> NEWS
		and: 'no other interactions happen'
			0 * _
		and: 'the expected JSON object is returned'
			with (result) {
				getInt(JsonComicMessageBodyWriter.ID) == ID
				getJsonNumber(JsonComicMessageBodyWriter.DATE).longValue() == TIME
				getString(JsonComicMessageBodyWriter.TITLE) == TITLE
				getString(JsonComicMessageBodyWriter.SAFE_TITLE) == SAFE_TITLE
				getString(JsonComicMessageBodyWriter.IMAGE) == IMAGE.toString()
				getString(JsonComicMessageBodyWriter.ALTERNATE) == ALTERNATE
				getString(JsonComicMessageBodyWriter.TRANSCRIPT) == TRANSCRIPT
				getString(JsonComicMessageBodyWriter.LINK) == LINK.toString()
				getString(JsonComicMessageBodyWriter.NEWS) == NEWS
			}
	}

	void 'Converting a comic with null alternate to JSON'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'a date'
			Date date = Mock(Date)
		when: 'converting a comic'
			JsonObject result = JsonComicMessageBodyWriter.convertComic(comic)
		then: 'comic ID is retrieved'
			1 * comic.id >> ID
		and: 'comic date is retrieved'
			1 * comic.date >> date
		and: 'date time is retrieved'
			1 * date.time >> TIME
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'comic safe title is retrieved'
			1 * comic.safeTitle >> SAFE_TITLE
		and: 'comic image is retrieved'
			1 * comic.image >> IMAGE
		and: 'comic alternate is retrieved'
			1 * comic.alternate >> null
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> TRANSCRIPT
		and: 'comic link is retrieved'
			1 * comic.link >> LINK
		and: 'comic news are retrieved'
			1 * comic.news >> NEWS
		and: 'no other interactions happen'
			0 * _
		and: 'the expected JSON object is returned'
			with (result) {
				getInt(JsonComicMessageBodyWriter.ID) == ID
				getJsonNumber(JsonComicMessageBodyWriter.DATE).longValue() == TIME
				getString(JsonComicMessageBodyWriter.TITLE) == TITLE
				getString(JsonComicMessageBodyWriter.SAFE_TITLE) == SAFE_TITLE
				getString(JsonComicMessageBodyWriter.IMAGE) == IMAGE.toString()
				isNull(JsonComicMessageBodyWriter.ALTERNATE)
				getString(JsonComicMessageBodyWriter.TRANSCRIPT) == TRANSCRIPT
				getString(JsonComicMessageBodyWriter.LINK) == LINK.toString()
				getString(JsonComicMessageBodyWriter.NEWS) == NEWS
			}
	}

	void 'Converting a comic with null transcript to JSON'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'a date'
			Date date = Mock(Date)
		when: 'converting a comic'
			JsonObject result = JsonComicMessageBodyWriter.convertComic(comic)
		then: 'comic ID is retrieved'
			1 * comic.id >> ID
		and: 'comic date is retrieved'
			1 * comic.date >> date
		and: 'date time is retrieved'
			1 * date.time >> TIME
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'comic safe title is retrieved'
			1 * comic.safeTitle >> SAFE_TITLE
		and: 'comic image is retrieved'
			1 * comic.image >> IMAGE
		and: 'comic alternate is retrieved'
			1 * comic.alternate >> ALTERNATE
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> null
		and: 'comic link is retrieved'
			1 * comic.link >> LINK
		and: 'comic news are retrieved'
			1 * comic.news >> NEWS
		and: 'no other interactions happen'
			0 * _
		and: 'the expected JSON object is returned'
			with (result) {
				getInt(JsonComicMessageBodyWriter.ID) == ID
				getJsonNumber(JsonComicMessageBodyWriter.DATE).longValue() == TIME
				getString(JsonComicMessageBodyWriter.TITLE) == TITLE
				getString(JsonComicMessageBodyWriter.SAFE_TITLE) == SAFE_TITLE
				getString(JsonComicMessageBodyWriter.IMAGE) == IMAGE.toString()
				getString(JsonComicMessageBodyWriter.ALTERNATE) == ALTERNATE
				isNull(JsonComicMessageBodyWriter.TRANSCRIPT)
				getString(JsonComicMessageBodyWriter.LINK) == LINK.toString()
				getString(JsonComicMessageBodyWriter.NEWS) == NEWS
			}
	}

	void 'Converting a comic with null link to JSON'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'a date'
			Date date = Mock(Date)
		when: 'converting a comic'
			JsonObject result = JsonComicMessageBodyWriter.convertComic(comic)
		then: 'comic ID is retrieved'
			1 * comic.id >> ID
		and: 'comic date is retrieved'
			1 * comic.date >> date
		and: 'date time is retrieved'
			1 * date.time >> TIME
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'comic safe title is retrieved'
			1 * comic.safeTitle >> SAFE_TITLE
		and: 'comic image is retrieved'
			1 * comic.image >> IMAGE
		and: 'comic alternate is retrieved'
			1 * comic.alternate >> ALTERNATE
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> TRANSCRIPT
		and: 'comic link is retrieved'
			1 * comic.link >> null
		and: 'comic news are retrieved'
			1 * comic.news >> NEWS
		and: 'no other interactions happen'
			0 * _
		and: 'the expected JSON object is returned'
			with (result) {
				getInt(JsonComicMessageBodyWriter.ID) == ID
				getJsonNumber(JsonComicMessageBodyWriter.DATE).longValue() == TIME
				getString(JsonComicMessageBodyWriter.TITLE) == TITLE
				getString(JsonComicMessageBodyWriter.SAFE_TITLE) == SAFE_TITLE
				getString(JsonComicMessageBodyWriter.IMAGE) == IMAGE.toString()
				getString(JsonComicMessageBodyWriter.ALTERNATE) == ALTERNATE
				getString(JsonComicMessageBodyWriter.TRANSCRIPT) == TRANSCRIPT
				isNull(JsonComicMessageBodyWriter.LINK)
				getString(JsonComicMessageBodyWriter.NEWS) == NEWS
			}
	}

	void 'Converting a comic with null news to JSON'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'a date'
			Date date = Mock(Date)
		when: 'converting a comic'
			JsonObject result = JsonComicMessageBodyWriter.convertComic(comic)
		then: 'comic ID is retrieved'
			1 * comic.id >> ID
		and: 'comic date is retrieved'
			1 * comic.date >> date
		and: 'date time is retrieved'
			1 * date.time >> TIME
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'comic safe title is retrieved'
			1 * comic.safeTitle >> SAFE_TITLE
		and: 'comic image is retrieved'
			1 * comic.image >> IMAGE
		and: 'comic alternate is retrieved'
			1 * comic.alternate >> ALTERNATE
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> TRANSCRIPT
		and: 'comic link is retrieved'
			1 * comic.link >> LINK
		and: 'comic news are retrieved'
			1 * comic.news >> null
		and: 'no other interactions happen'
			0 * _
		and: 'the expected JSON object is returned'
			with (result) {
				getInt(JsonComicMessageBodyWriter.ID) == ID
				getJsonNumber(JsonComicMessageBodyWriter.DATE).longValue() == TIME
				getString(JsonComicMessageBodyWriter.TITLE) == TITLE
				getString(JsonComicMessageBodyWriter.SAFE_TITLE) == SAFE_TITLE
				getString(JsonComicMessageBodyWriter.IMAGE) == IMAGE.toString()
				getString(JsonComicMessageBodyWriter.ALTERNATE) == ALTERNATE
				getString(JsonComicMessageBodyWriter.TRANSCRIPT) == TRANSCRIPT
				getString(JsonComicMessageBodyWriter.LINK) == LINK.toString()
				isNull(JsonComicMessageBodyWriter.NEWS)
			}
	}

	void 'Writing a comic'() {
		given: 'a comic'
			Comic comic = new Comic(ID, new Date(TIME), TITLE, SAFE_TITLE, IMAGE, ALTERNATE, TRANSCRIPT, LINK, NEWS)
		and: 'a map of HTTP headers'
			MultivaluedMap<String, Object> httpHeaders = Mock(MultivaluedMap)
		and: 'an output stream'
			ByteArrayOutputStream output = new ByteArrayOutputStream()
		when: 'comic is written'
			writer.writeTo(comic, null, null, null, null, httpHeaders, output)
		then: 'HTTP header \'Content-Type\' is set to \'application/json;charset=UTF-8\''
			1 * httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, JsonComicMessageBodyWriter.APPLICATION_JSON_CHARSET_UTF_8)
		and: 'no other interactions happen'
			0 * _
		and: 'the expected JSON object is written'
			with (Json.createReader(new InputStreamReader(new ByteArrayInputStream(output.toByteArray()), StandardCharsets.UTF_8)).readObject()) {
				getInt(JsonComicMessageBodyWriter.ID) == ID
				getJsonNumber(JsonComicMessageBodyWriter.DATE).longValue() == TIME
				getString(JsonComicMessageBodyWriter.TITLE) == TITLE
				getString(JsonComicMessageBodyWriter.SAFE_TITLE) == SAFE_TITLE
				getString(JsonComicMessageBodyWriter.IMAGE) == IMAGE.toString()
				getString(JsonComicMessageBodyWriter.ALTERNATE) == ALTERNATE
				getString(JsonComicMessageBodyWriter.TRANSCRIPT) == TRANSCRIPT
				getString(JsonComicMessageBodyWriter.LINK) == LINK.toString()
				getString(JsonComicMessageBodyWriter.NEWS) == NEWS
			}
	}
}
