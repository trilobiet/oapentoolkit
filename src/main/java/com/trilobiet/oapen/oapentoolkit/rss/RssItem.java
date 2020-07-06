package com.trilobiet.oapen.oapentoolkit.rss;

import java.time.LocalDate;

public interface RssItem {
	
	String getTitle();
	LocalDate getPublicationDate();
	String getAuthor();
	String getDescription();
	String getContents();
	String getLink();

}
