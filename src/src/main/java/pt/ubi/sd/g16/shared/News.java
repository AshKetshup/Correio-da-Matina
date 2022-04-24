package pt.ubi.sd.g16.shared;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class News implements Serializable {
	private final UUID id;
	private String title;
    private char[] content = new char[180];	// Conteudo
	private Topic topic;        		// Topico TODO
	private Publisher publisher;	    	// Publisher TODO
	private Date date; 	   			// Data de criação
	private final static String datePattern = "dd-mm-yyyy hh:mm:ss";

	public News(String title, char[] content, String topicID, Publisher publisher) {
		this.date = new Date();

		this.id = UUID.randomUUID();

		this.title = title;
		setContent(content);

		this.topic = Topic.getTopicFromID(topicID);
		this.publisher = publisher;
	}

	public News(String id, String title, char[] content, String topicID, String publisherID, String date) throws ParseException {
		this.id = UUID.fromString(id);
		this.title = title;
		setContent(content);
		this.topic = Topic.getTopicFromID(topicID);
		this.publisher = Publisher.getPublisherFromID(publisherID);
		setDateFromString(date);
	}

	public UUID getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public Topic getTopic() {
		return topic;
	}

	public char[] getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDateFromString(String dateString) throws ParseException {
		this.date = new SimpleDateFormat(datePattern).parse(dateString);
	}

	public void setContent(char[] newContent) throws ArrayIndexOutOfBoundsException {
		for (int i = 0; i < newContent.length; i++)
			this.content[i] = newContent[i];
	}

	public String getNewsDate() {
		return new SimpleDateFormat(datePattern).format(date);
	}

	@Override
	public String toString() {
		return title + "\t" + topic.getTitle() + "\t" + publisher.getUsername() + "\t" + getNewsDate() + "\n";
	}
}
