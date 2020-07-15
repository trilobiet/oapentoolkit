package com.trilobiet.oapen.oapentoolkit.rss.hypotheses;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.trilobiet.oapen.oapentoolkit.rss.RssException;
import com.trilobiet.oapen.oapentoolkit.rss.RssItem;
import com.trilobiet.oapen.oapentoolkit.rss.RssService;

public class HypothesesRssService implements RssService {
	
	private final String feedUrl;

	/**
	 * Constructor 
	 * @param feedUrl
	 */
	public HypothesesRssService(String feedUrl) {
		this.feedUrl = feedUrl;
	}
	
	@Override
	public List<RssItem> getItems(int count) throws RssException {
		
		return getItems(count, Collections.emptyList());
	}
	
	@Override
	public List<RssItem> getItems(int count, List<String> categories) throws RssException {
		
		Optional<SyndFeed> feed = getFeed();
		List<RssItem> items = new ArrayList<>();
		
		feed.ifPresent( f -> {
			f.getEntries().stream()
			 .filter(e ->
			 	// show all, or only from listed categories
			  	categories.isEmpty() || e.getCategories().stream().anyMatch(c -> categories.contains(c.getName()))
			 )
			 .limit(count).forEach(e -> items.add(rssItem(e)));
		});

		return items;
	}
	
	@Override
	public Optional<RssItem> getItemByLink(List<RssItem> items, String link) {
		
		Optional<RssItem> oItem = items.parallelStream()
			.filter( itm -> itm.getLink().equals(link) )
			.findAny();
		
		return oItem;
	}

	/**
	 * Adaptor method returning an RssItem from a SyndEntry
	 * 
	 * @param entry
	 * @return
	 */
	private RssItem rssItem(SyndEntry entry) {
		
		RssItemImp.Builder builder = new RssItemImp.Builder( entry.getLink() )
			.setTitle( entry.getTitle())
			.setDescription( entry.getDescription().getValue())
			.setAuthor(entry.getAuthor())
			.setDate(entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
			.setCategory(entry.getCategories().stream().map(e -> e.getName()).collect(Collectors.toList()));
		
		List<SyndContent> lst = entry.getContents();
		lst.stream()
			.filter(content -> content.getType().equalsIgnoreCase("html"))
			.findFirst()
			.ifPresent( content -> builder.setContents(content.getValue()) );
		
		return builder.build();
	}

	/**
	 * Fetch the feed
	 * 
	 * @return
	 * @throws RssException
	 */
	@Cacheable(value="rssCache", key="#root.target.feedUrl")
	private Optional<SyndFeed> getFeed() throws RssException {
		
		SyndFeed feed = null;
		
		try {
			URL url = new URL(feedUrl); 
			SyndFeedInput input = new SyndFeedInput();
			// input.setAllowDoctypes(true);
			// input.setXmlHealerOn(true);
			feed = input.build(new XmlReader(url));
			
		} catch (IllegalArgumentException | FeedException | IOException e) {
			
			throw new RssException( e );
		}
		
		return Optional.ofNullable(feed);
	}
	
	// Test
	public static void main(String[] args) {

		String url = "https://dariahopen.hypotheses.org/feed";
		HypothesesRssService service = new HypothesesRssService(url);
		
		try {
			List<RssItem> items = service.getItems(12);
			items.forEach(System.out::println);
			
		} catch (RssException e) {
			System.out.println(e);
		}
	}

}
