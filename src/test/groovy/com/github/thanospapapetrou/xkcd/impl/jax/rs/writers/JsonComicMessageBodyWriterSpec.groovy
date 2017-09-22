package com.github.thanospapapetrou.xkcd.impl.jax.rs.writers

import java.nio.charset.StandardCharsets

import javax.json.JsonBuilderFactory
import javax.json.JsonObject
import javax.json.JsonObjectBuilder
import javax.json.JsonWriter
import javax.json.JsonWriterFactory
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
	private static final long TIME = 1048576L
	private static final String TITLE = 'Title'
	private static final String TRANSCRIPT = 'Transcript'

	private JsonComicMessageBodyWriter jsonComicMessageBodyWriter

	void setup() {
		jsonComicMessageBodyWriter = new JsonComicMessageBodyWriter(Mock(JsonBuilderFactory), Mock(JsonWriterFactory))
	}

	void 'Writing a comic'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'some HTTP headers'
			MultivaluedMap<String, Object> httpHeaders = Mock(MultivaluedMap)
		and: 'an output stream'
			OutputStream output = Mock(OutputStream)
		and: 'a JSON object builder'
			JsonObjectBuilder jsonObjectBuilder = Mock(JsonObjectBuilder)
		and: 'a date'
			Date date = Mock(Date)
		and: 'a JSON writer'
			JsonWriter jsonWriter = Mock(JsonWriter)
		and: 'a JSON object'
			JsonObject json = Mock(JsonObject)
		when: 'comic is written'
			jsonComicMessageBodyWriter.writeTo(comic, null, null, null, null, httpHeaders, output)
		then: 'HTTP header \'Content-Type\' is set to \'application/json;charset=UTF-8\''
			1 * httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, JsonComicMessageBodyWriter.APPLICATION_JSON_CHARSET_UTF_8)
		and: 'a JSON object builder is created'
			1 * jsonComicMessageBodyWriter.jsonBuilderFactory.createObjectBuilder() >> jsonObjectBuilder
		and: 'comic ID is retrieved'
			1 * comic.id >> ID
		and: 'ID is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.ID, ID) >> jsonObjectBuilder
		and: 'comic date is retrieved'
			1 * comic.date >> date
		and: 'date time is retrieved'
			1 * date.time >> TIME
		and: 'time is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.DATE, TIME) >> jsonObjectBuilder
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'title is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.TITLE, TITLE) >> jsonObjectBuilder
		and: 'comic safe title is retrieved'
			1 * comic.safeTitle >> SAFE_TITLE
		and: 'safe title is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.SAFE_TITLE, SAFE_TITLE) >> jsonObjectBuilder
		and: 'comic image is retrieved'
			1 * comic.image >> IMAGE
		and: 'image is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.IMAGE, IMAGE.toString()) >> jsonObjectBuilder
		and: 'comic alternate is retrieved'
			1 * comic.alternate >> ALTERNATE
		and: 'alternate is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.ALTERNATE, ALTERNATE) >> jsonObjectBuilder
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> TRANSCRIPT
		and: 'transcript is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.TRANSCRIPT, TRANSCRIPT) >> jsonObjectBuilder
		and: 'comic link is retrieved'
			1 * comic.link >> LINK
		and: 'link is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.LINK, LINK.toString()) >> jsonObjectBuilder
		and: 'comic news are retrieved'
			1 * comic.news >> NEWS
		and: 'news are added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.NEWS, NEWS) >> jsonObjectBuilder
		and: 'a JSON writer is created'
			1 * jsonComicMessageBodyWriter.jsonWriterFactory.createWriter(output, StandardCharsets.UTF_8) >> jsonWriter
		and: 'a JSON object is built'
			1 * jsonObjectBuilder.build() >> json
		and: 'JSON object is written to JSON writer'
			1 * jsonWriter.writeObject(json)
		and: 'no other interactions happen'
			0 * _
	}

	void 'Writing a comic with null alternate'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'some HTTP headers'
			MultivaluedMap<String, Object> httpHeaders = Mock(MultivaluedMap)
		and: 'an output stream'
			OutputStream output = Mock(OutputStream)
		and: 'a JSON object builder'
			JsonObjectBuilder jsonObjectBuilder = Mock(JsonObjectBuilder)
		and: 'a date'
			Date date = Mock(Date)
		and: 'a JSON writer'
			JsonWriter jsonWriter = Mock(JsonWriter)
		and: 'a JSON object'
			JsonObject json = Mock(JsonObject)
		when: 'comic is written'
			jsonComicMessageBodyWriter.writeTo(comic, null, null, null, null, httpHeaders, output)
		then: 'HTTP header \'Content-Type\' is set to \'application/json;charset=UTF-8\''
			1 * httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, JsonComicMessageBodyWriter.APPLICATION_JSON_CHARSET_UTF_8)
		and: 'a JSON object builder is created'
			1 * jsonComicMessageBodyWriter.jsonBuilderFactory.createObjectBuilder() >> jsonObjectBuilder
		and: 'comic ID is retrieved'
			1 * comic.id >> ID
		and: 'ID is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.ID, ID) >> jsonObjectBuilder
		and: 'comic date is retrieved'
			1 * comic.date >> date
		and: 'date time is retrieved'
			1 * date.time >> TIME
		and: 'time is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.DATE, TIME) >> jsonObjectBuilder
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'title is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.TITLE, TITLE) >> jsonObjectBuilder
		and: 'comic safe title is retrieved'
			1 * comic.safeTitle >> SAFE_TITLE
		and: 'safe title is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.SAFE_TITLE, SAFE_TITLE) >> jsonObjectBuilder
		and: 'comic image is retrieved'
			1 * comic.image >> IMAGE
		and: 'image is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.IMAGE, IMAGE.toString()) >> jsonObjectBuilder
		and: 'comic alternate is retrieved'
			1 * comic.alternate >> null
		and: 'alternate is added to JSON object builder'
			1 * jsonObjectBuilder.addNull(JsonComicMessageBodyWriter.ALTERNATE) >> jsonObjectBuilder
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> TRANSCRIPT
		and: 'transcript is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.TRANSCRIPT, TRANSCRIPT) >> jsonObjectBuilder
		and: 'comic link is retrieved'
			1 * comic.link >> LINK
		and: 'link is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.LINK, LINK.toString()) >> jsonObjectBuilder
		and: 'comic news are retrieved'
			1 * comic.news >> NEWS
		and: 'news are added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.NEWS, NEWS) >> jsonObjectBuilder
		and: 'a JSON writer is created'
			1 * jsonComicMessageBodyWriter.jsonWriterFactory.createWriter(output, StandardCharsets.UTF_8) >> jsonWriter
		and: 'a JSON object is built'
			1 * jsonObjectBuilder.build() >> json
		and: 'JSON object is written to JSON writer'
			1 * jsonWriter.writeObject(json)
		and: 'no other interactions happen'
			0 * _
	}

	void 'Writing a comic with null transcript'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'some HTTP headers'
			MultivaluedMap<String, Object> httpHeaders = Mock(MultivaluedMap)
		and: 'an output stream'
			OutputStream output = Mock(OutputStream)
		and: 'a JSON object builder'
			JsonObjectBuilder jsonObjectBuilder = Mock(JsonObjectBuilder)
		and: 'a date'
			Date date = Mock(Date)
		and: 'a JSON writer'
			JsonWriter jsonWriter = Mock(JsonWriter)
		and: 'a JSON object'
			JsonObject json = Mock(JsonObject)
		when: 'comic is written'
			jsonComicMessageBodyWriter.writeTo(comic, null, null, null, null, httpHeaders, output)
		then: 'HTTP header \'Content-Type\' is set to \'application/json;charset=UTF-8\''
			1 * httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, JsonComicMessageBodyWriter.APPLICATION_JSON_CHARSET_UTF_8)
		and: 'a JSON object builder is created'
			1 * jsonComicMessageBodyWriter.jsonBuilderFactory.createObjectBuilder() >> jsonObjectBuilder
		and: 'comic ID is retrieved'
			1 * comic.id >> ID
		and: 'ID is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.ID, ID) >> jsonObjectBuilder
		and: 'comic date is retrieved'
			1 * comic.date >> date
		and: 'date time is retrieved'
			1 * date.time >> TIME
		and: 'time is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.DATE, TIME) >> jsonObjectBuilder
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'title is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.TITLE, TITLE) >> jsonObjectBuilder
		and: 'comic safe title is retrieved'
			1 * comic.safeTitle >> SAFE_TITLE
		and: 'safe title is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.SAFE_TITLE, SAFE_TITLE) >> jsonObjectBuilder
		and: 'comic image is retrieved'
			1 * comic.image >> IMAGE
		and: 'image is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.IMAGE, IMAGE.toString()) >> jsonObjectBuilder
		and: 'comic alternate is retrieved'
			1 * comic.alternate >> ALTERNATE
		and: 'alternate is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.ALTERNATE, ALTERNATE) >> jsonObjectBuilder
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> null
		and: 'transcript is added to JSON object builder'
			1 * jsonObjectBuilder.addNull(JsonComicMessageBodyWriter.TRANSCRIPT) >> jsonObjectBuilder
		and: 'comic link is retrieved'
			1 * comic.link >> LINK
		and: 'link is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.LINK, LINK.toString()) >> jsonObjectBuilder
		and: 'comic news are retrieved'
			1 * comic.news >> NEWS
		and: 'news are added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.NEWS, NEWS) >> jsonObjectBuilder
		and: 'a JSON writer is created'
			1 * jsonComicMessageBodyWriter.jsonWriterFactory.createWriter(output, StandardCharsets.UTF_8) >> jsonWriter
		and: 'a JSON object is built'
			1 * jsonObjectBuilder.build() >> json
		and: 'JSON object is written to JSON writer'
			1 * jsonWriter.writeObject(json)
		and: 'no other interactions happen'
			0 * _
	}

	void 'Writing a comic with null link'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'some HTTP headers'
			MultivaluedMap<String, Object> httpHeaders = Mock(MultivaluedMap)
		and: 'an output stream'
			OutputStream output = Mock(OutputStream)
		and: 'a JSON object builder'
			JsonObjectBuilder jsonObjectBuilder = Mock(JsonObjectBuilder)
		and: 'a date'
			Date date = Mock(Date)
		and: 'a JSON writer'
			JsonWriter jsonWriter = Mock(JsonWriter)
		and: 'a JSON object'
			JsonObject json = Mock(JsonObject)
		when: 'comic is written'
			jsonComicMessageBodyWriter.writeTo(comic, null, null, null, null, httpHeaders, output)
		then: 'HTTP header \'Content-Type\' is set to \'application/json;charset=UTF-8\''
			1 * httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, JsonComicMessageBodyWriter.APPLICATION_JSON_CHARSET_UTF_8)
		and: 'a JSON object builder is created'
			1 * jsonComicMessageBodyWriter.jsonBuilderFactory.createObjectBuilder() >> jsonObjectBuilder
		and: 'comic ID is retrieved'
			1 * comic.id >> ID
		and: 'ID is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.ID, ID) >> jsonObjectBuilder
		and: 'comic date is retrieved'
			1 * comic.date >> date
		and: 'date time is retrieved'
			1 * date.time >> TIME
		and: 'time is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.DATE, TIME) >> jsonObjectBuilder
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'title is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.TITLE, TITLE) >> jsonObjectBuilder
		and: 'comic safe title is retrieved'
			1 * comic.safeTitle >> SAFE_TITLE
		and: 'safe title is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.SAFE_TITLE, SAFE_TITLE) >> jsonObjectBuilder
		and: 'comic image is retrieved'
			1 * comic.image >> IMAGE
		and: 'image is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.IMAGE, IMAGE.toString()) >> jsonObjectBuilder
		and: 'comic alternate is retrieved'
			1 * comic.alternate >> ALTERNATE
		and: 'alternate is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.ALTERNATE, ALTERNATE) >> jsonObjectBuilder
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> TRANSCRIPT
		and: 'transcript is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.TRANSCRIPT, TRANSCRIPT) >> jsonObjectBuilder
		and: 'comic link is retrieved'
			1 * comic.link >> null
		and: 'link is added to JSON object builder'
			1 * jsonObjectBuilder.addNull(JsonComicMessageBodyWriter.LINK) >> jsonObjectBuilder
		and: 'comic news are retrieved'
			1 * comic.news >> NEWS
		and: 'news are added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.NEWS, NEWS) >> jsonObjectBuilder
		and: 'a JSON writer is created'
			1 * jsonComicMessageBodyWriter.jsonWriterFactory.createWriter(output, StandardCharsets.UTF_8) >> jsonWriter
		and: 'a JSON object is built'
			1 * jsonObjectBuilder.build() >> json
		and: 'JSON object is written to JSON writer'
			1 * jsonWriter.writeObject(json)
		and: 'no other interactions happen'
			0 * _
	}

	void 'Writing a comic with null news'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'some HTTP headers'
			MultivaluedMap<String, Object> httpHeaders = Mock(MultivaluedMap)
		and: 'an output stream'
			OutputStream output = Mock(OutputStream)
		and: 'a JSON object builder'
			JsonObjectBuilder jsonObjectBuilder = Mock(JsonObjectBuilder)
		and: 'a date'
			Date date = Mock(Date)
		and: 'a JSON writer'
			JsonWriter jsonWriter = Mock(JsonWriter)
		and: 'a JSON object'
			JsonObject json = Mock(JsonObject)
		when: 'comic is written'
			jsonComicMessageBodyWriter.writeTo(comic, null, null, null, null, httpHeaders, output)
		then: 'HTTP header \'Content-Type\' is set to \'application/json;charset=UTF-8\''
			1 * httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, JsonComicMessageBodyWriter.APPLICATION_JSON_CHARSET_UTF_8)
		and: 'a JSON object builder is created'
			1 * jsonComicMessageBodyWriter.jsonBuilderFactory.createObjectBuilder() >> jsonObjectBuilder
		and: 'comic ID is retrieved'
			1 * comic.id >> ID
		and: 'ID is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.ID, ID) >> jsonObjectBuilder
		and: 'comic date is retrieved'
			1 * comic.date >> date
		and: 'date time is retrieved'
			1 * date.time >> TIME
		and: 'time is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.DATE, TIME) >> jsonObjectBuilder
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'title is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.TITLE, TITLE) >> jsonObjectBuilder
		and: 'comic safe title is retrieved'
			1 * comic.safeTitle >> SAFE_TITLE
		and: 'safe title is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.SAFE_TITLE, SAFE_TITLE) >> jsonObjectBuilder
		and: 'comic image is retrieved'
			1 * comic.image >> IMAGE
		and: 'image is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.IMAGE, IMAGE.toString()) >> jsonObjectBuilder
		and: 'comic alternate is retrieved'
			1 * comic.alternate >> ALTERNATE
		and: 'alternate is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.ALTERNATE, ALTERNATE) >> jsonObjectBuilder
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> TRANSCRIPT
		and: 'transcript is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.TRANSCRIPT, TRANSCRIPT) >> jsonObjectBuilder
		and: 'comic link is retrieved'
			1 * comic.link >> LINK
		and: 'link is added to JSON object builder'
			1 * jsonObjectBuilder.add(JsonComicMessageBodyWriter.LINK, LINK.toString()) >> jsonObjectBuilder
		and: 'comic news are retrieved'
			1 * comic.news >> null
		and: 'news are added to JSON object builder'
			1 * jsonObjectBuilder.addNull(JsonComicMessageBodyWriter.NEWS) >> jsonObjectBuilder
		and: 'a JSON writer is created'
			1 * jsonComicMessageBodyWriter.jsonWriterFactory.createWriter(output, StandardCharsets.UTF_8) >> jsonWriter
		and: 'a JSON object is built'
			1 * jsonObjectBuilder.build() >> json
		and: 'JSON object is written to JSON writer'
			1 * jsonWriter.writeObject(json)
		and: 'no other interactions happen'
			0 * _
	}
}
