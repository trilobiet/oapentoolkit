package com.trilobiet.oapen.oapentoolkit.rss;

import java.util.List;
import java.util.Optional;

public interface RssService {
	
	/**
	 * Get [count] items from Rss feed
	 * @param count
	 * @return
	 * @throws RssException
	 */
	List<RssItem> getItems(int count) throws RssException;
	
	/**
	 * Get [count] items belonging to one of [categories] from Rss feed
	 * @param count
	 * @param categories
	 * @return
	 * @throws RssException
	 */
	List<RssItem> getItems(int count, List<String> categories) throws RssException;

	/**
	 * Get item from Rss feed by link value
	 * @param items
	 * @param link
	 * @return
	 */
	Optional<RssItem> getItemByLink(List<RssItem> items, String link);

}
