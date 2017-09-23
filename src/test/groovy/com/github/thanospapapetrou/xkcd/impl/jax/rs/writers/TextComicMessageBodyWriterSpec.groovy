package com.github.thanospapapetrou.xkcd.impl.jax.rs.writers

import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MultivaluedMap

import spock.lang.Specification

import com.github.thanospapapetrou.xkcd.domain.Comic

class TextComicMessageBodyWriterSpec extends Specification {
	private static final String TITLE = 'Title'
	private static final String TRANSCRIPT = 'Transcript'

	private TextComicMessageBodyWriter textComicMessageBodyWriter

	void setup() {
		textComicMessageBodyWriter = Spy(TextComicMessageBodyWriter)
	}

	void 'Writing a comic'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'some HTTP headers'
			MultivaluedMap<String, Object> httpHeaders = Mock(MultivaluedMap)
		and: 'an output stream'
			OutputStream output = Mock(OutputStream)
		and: 'an output stream writer'
			OutputStreamWriter writer = Mock(OutputStreamWriter)
		when: 'comic is written'
			textComicMessageBodyWriter.writeTo(comic, null, null, null, null, httpHeaders, output)
		then: 'HTTP header \'Content-Type\' is set to \'application/json;charset=UTF-8\''
			1 * httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, TextComicMessageBodyWriter.TEXT_PLAIN_CHARSET_UTF_8)
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> TRANSCRIPT
		and: 'an output stream writer is created'
			1 * textComicMessageBodyWriter.createOutputStreamWriter(output) >> writer
		and: 'comic string representation is written to output stream writer'
			1 * writer.write(String.format(TextComicMessageBodyWriter.FORMAT, TITLE, TRANSCRIPT))
		and: 'the initial call is concluded'
			1 * textComicMessageBodyWriter.writeTo(comic, null, null, null, null, httpHeaders, output)
		and: 'no other interactions happen'
			0 * _
	}

	void 'Error writing a comic'() {
		given: 'a comic'
			Comic comic = Mock(Comic)
		and: 'some HTTP headers'
			MultivaluedMap<String, Object> httpHeaders = Mock(MultivaluedMap)
		and: 'an output stream'
			OutputStream output = Mock(OutputStream)
		and: 'an output stream writer'
			OutputStreamWriter writer = Mock(OutputStreamWriter)
		and: 'an IO exception'
			IOException ioException = Mock(IOException)
		when: 'comic is written'
			textComicMessageBodyWriter.writeTo(comic, null, null, null, null, httpHeaders, output)
		then: 'HTTP header \'Content-Type\' is set to \'application/json;charset=UTF-8\''
			1 * httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, TextComicMessageBodyWriter.TEXT_PLAIN_CHARSET_UTF_8)
		and: 'comic title is retrieved'
			1 * comic.title >> TITLE
		and: 'comic transcript is retrieved'
			1 * comic.transcript >> TRANSCRIPT
		and: 'an output stream writer is created'
			1 * textComicMessageBodyWriter.createOutputStreamWriter(output) >> writer
		and: 'comic string representation is written to output stream writer'
			1 * writer.write(String.format(TextComicMessageBodyWriter.FORMAT, TITLE, TRANSCRIPT)) >> { throw ioException }
		and: 'the initial call is concluded'
			1 * textComicMessageBodyWriter.writeTo(comic, null, null, null, null, httpHeaders, output)
		and: 'no other interactions happen'
			0 * _
		and: 'IO exception is thrown'
			IOException e = thrown(IOException)
			e == ioException
	}
}
