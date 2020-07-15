package com.trilobiet.oapen.oapentoolkit.rss;

import java.time.LocalDate;
import java.util.List;

public interface RssItem {
	
	String getTitle();
	LocalDate getPublicationDate();
	String getAuthor();
	String getDescription();
	String getContents();
	String getLink();
	List<String> getCategory();

}
