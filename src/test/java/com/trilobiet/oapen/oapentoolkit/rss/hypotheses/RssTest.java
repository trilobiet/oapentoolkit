package com.trilobiet.oapen.oapentoolkit.rss.hypotheses;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trilobiet.oapen.oapentoolkit.rss.RssException;
import com.trilobiet.oapen.oapentoolkit.rss.RssItem;

public class RssTest {
	
	private final String host = "https://oapen.hypotheses.org/feed";
	private HypothesesRssService rssService;
	
	@BeforeEach
	public void setHost() {
		rssService = new HypothesesRssService(host);
	}
	
    @Test // Integration test
	public void count_1_without_category_test_returns_1() throws RssException {
    	
    	List<RssItem> items = rssService.getItems(1);
		assertNotNull(items);
		assertTrue(items.size()==1);
		System.out.println("count_1_without_category_test_returns_1: " + items);
    }

    @Test // Integration test
	public void count_1_with_empty_category_returns_1() throws RssException {
    	
		List<String> categories = Collections.emptyList();
		List<RssItem> items = rssService.getItems(1, categories);
		
		assertNotNull(items);
		assertTrue(items.size()==1);
		System.out.println("count_1_with_empty_category_returns_1: " + items);
    }

    @Test // Integration test
	public void count_1_with_impossible_category_returns_none() throws RssException {
    	
		List<String> categories = Arrays.asList("THIS_IS_A_RIDICULOUS_CATEGORY_THAT_SHOULD_YIELD_NO_MATCHES");
		List<RssItem> items = rssService.getItems(1, categories);
		
		assertNotNull(items);
		assertTrue(items.isEmpty());
		System.out.println("count_1_with_impossible_category_returns_none: " + items);
    }

    @Test // Integration test
	public void count_1_with_normal_and_impossible_category_returns_1() throws RssException {
    	
    	// Categories are case sensitive!
		List<String> categories = Arrays.asList("THIS_IS_A_RIDICULOUS_CATEGORY_THAT_SHOULD_YIELD_NO_MATCHES","open access");
		List<RssItem> items = rssService.getItems(1, categories);
		
		assertNotNull(items);
		assertTrue(items.size()==1);
		System.out.println("count_1_with_normal_and_impossible_category_returns_1: " + items);
    }
    
}
