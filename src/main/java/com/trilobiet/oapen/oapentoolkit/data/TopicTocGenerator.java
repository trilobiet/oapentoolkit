package com.trilobiet.oapen.oapentoolkit.data;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;

import com.trilobiet.graphqlweb.datamodel.ArticleOutline;
import com.trilobiet.graphqlweb.datamodel.Topic;
import com.trilobiet.graphqlweb.datamodel.comparator.ArticleTitleComparator;

public class TopicTocGenerator {
	
	// not static, so we can cache
	@Cacheable(value="tkGlossaryCache", key="#root.methodName")
	public Map<Character, Collection<ArticleOutline>> alphabetizedToc(Topic topic) {
		
		Stream<ArticleOutline> v2 = topic.getArticles().stream();
		
		Map<Character, Collection<ArticleOutline>> glossary = v2.collect(Collectors.groupingBy(
			article -> article.getTitle().toUpperCase().charAt(0), 
			TreeMap::new, 
			Collectors.toCollection(() -> new TreeSet<ArticleOutline>(new ArticleTitleComparator()))
		));
		
		return glossary;
	}

}
