package com.github.thanospapapetrou.xkcd.domain;

import java.net.URL;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.AlternateConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.ImageConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.LinkConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.NewsConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.SafeTitleConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.TitleConverter;
import com.github.thanospapapetrou.xkcd.impl.cache.jpa.converters.TranscriptConverter;

/**
 * Class representing an xkcd comic.
 * 
 * @author thanos
 */
@Entity
@Table(schema = "APP", name = "COMICS")
public class Comic {
	/**
	 * Alternate maximum length.
	 */
	public static final int ALTERNATE_LENGTH = 1024;

	/**
	 * Image maximum length.
	 */
	public static final int IMAGE_LENGTH = 128;

	/**
	 * Link maximum length.
	 */
	public static final int LINK_LENGTH = 128;

	/**
	 * News maximum length.
	 */
	public static final int NEWS_LENGTH = 512;

	/**
	 * Safe title maximum length.
	 */
	public static final int SAFE_TITLE_LENGTH = 64;

	/**
	 * Title maximum length.
	 */
	public static final int TITLE_LENGTH = 64;

	/**
	 * Transcript maximum length.
	 */
	public static final int TRANSCRIPT_LENGTH = 16384;

	private static final String NULL_DATE = "Date must not be null";
	private static final String NULL_IMAGE = "Image must not be null";
	private static final String NULL_SAFE_TITLE = "Safe title must not be null";
	private static final String NULL_TITLE = "Title must not be null";

	@Column(name = "ID", nullable = false, updatable = false)
	@Id
	private final int id;

	@Column(name = "DATE", nullable = false, updatable = false)
	private final Date date;

	@Column(name = "TITLE", length = TITLE_LENGTH, nullable = false, updatable = false)
	@Convert(converter = TitleConverter.class)
	private final String title;

	@Column(name = "SAFE_TITLE", length = SAFE_TITLE_LENGTH, nullable = false, updatable = false)
	@Convert(converter = SafeTitleConverter.class)
	private final String safeTitle;

	@Column(name = "IMAGE", length = IMAGE_LENGTH, nullable = false, updatable = false)
	@Convert(converter = ImageConverter.class)
	private final URL image;

	@Column(name = "ALTERNATE", length = ALTERNATE_LENGTH, updatable = false)
	@Convert(converter = AlternateConverter.class)
	private final String alternate;

	@Column(name = "TRANSCRIPT", length = TRANSCRIPT_LENGTH, updatable = false)
	@Convert(converter = TranscriptConverter.class)
	private final String transcript;

	@Column(name = "LINK", length = LINK_LENGTH, updatable = false)
	@Convert(converter = LinkConverter.class)
	private final URL link;

	@Column(name = "NEWS", length = NEWS_LENGTH, updatable = false)
	@Convert(converter = NewsConverter.class)
	private final String news;

	/**
	 * Construct a new xkcd comic.
	 * 
	 * @param id
	 *            the ID of this xkcd comic
	 * @param date
	 *            the publication date of this xkcd comic
	 * @param title
	 *            the title of this xkcd comic
	 * @param safeTitle
	 *            the safe title of this xkcd comic
	 * @param image
	 *            the URL of the image of this xkcd comic
	 * @param alternate
	 *            the alternate title of this xkcd comic or <code>null</code> to leave it unspecified
	 * @param transcript
	 *            the transcript of this xkcd comic or <code>null</code> to leave it unspecified
	 * @param link
	 *            the URL of the link of this xkcd comic or <code>null</code> to leave it unspecified
	 * @param news
	 *            the news of this xkcd comic or <code>null</code> to leave it unspecified
	 */
	public Comic(final int id, final Date date, final String title, final String safeTitle, final URL image, final String alternate, final String transcript, final URL link, final String news) {
		this.id = id;
		this.date = Objects.requireNonNull(date, NULL_DATE);
		this.title = Objects.requireNonNull(title, NULL_TITLE);
		this.safeTitle = Objects.requireNonNull(safeTitle, NULL_SAFE_TITLE);
		this.image = Objects.requireNonNull(image, NULL_IMAGE);
		this.alternate = ((alternate != null) && alternate.isEmpty()) ? null : alternate;
		this.transcript = ((transcript != null) && transcript.isEmpty()) ? null : transcript;
		this.link = link;
		this.news = ((news != null) && news.isEmpty()) ? null : news;
	}

	Comic() {
		// this constructor exists just to keep JPA happy
		id = 0;
		date = null;
		title = null;
		safeTitle = null;
		image = null;
		alternate = null;
		transcript = null;
		link = null;
		news = null;
	}

	/**
	 * Get the ID.
	 * 
	 * @return the ID of this xkcd comic
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the publication date.
	 * 
	 * @return the publication date of this xkcd comic
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Get the title.
	 * 
	 * @return the title of this xkcd comic
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get the safe title.
	 * 
	 * @return the safe title of this xkcd comic
	 */
	public String getSafeTitle() {
		return safeTitle;
	}

	/**
	 * Get the URL of the image.
	 * 
	 * @return the URL of the image of this xkcd comic
	 */
	public URL getImage() {
		return image;
	}

	/**
	 * Get the alternate title.
	 * 
	 * @return the alternate title of this xkcd comic or <code>null</code> if unspecified
	 */
	public String getAlternate() {
		return alternate;
	}

	/**
	 * Get the transcript.
	 * 
	 * @return the transcript of this xkcd comic or <code>null</code> if unspecified
	 */
	public String getTranscript() {
		return transcript;
	}

	/**
	 * Get the URL of the link.
	 * 
	 * @return the URL of the link of this xkcd comic or <code>null</code> if unspecified
	 */
	public URL getLink() {
		return link;
	}

	/**
	 * Get the news.
	 * 
	 * @return the news of this xkcd comic or <code>null</code> if unspecified
	 */
	public String getNews() {
		return news;
	}

	@Override
	public boolean equals(final Object object) {
		return (object instanceof Comic) && (id == ((Comic) object).id);
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return Integer.toString(id);
	}
}
