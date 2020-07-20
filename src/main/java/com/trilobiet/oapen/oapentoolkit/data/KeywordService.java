package com.trilobiet.oapen.oapentoolkit.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;

import com.trilobiet.graphqlweb.dao.ArticleDao;
import com.trilobiet.graphqlweb.dao.DaoException;
import com.trilobiet.graphqlweb.dao.SectionDao;
import com.trilobiet.graphqlweb.datamodel.Topic;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionImp;

public class KeywordService {

	private final SectionDao<SectionImp> sectiondao;
	private final ArticleDao<TKArticle> tkarticledao;

	public KeywordService(SectionDao<SectionImp> sectiondao, ArticleDao<TKArticle> articledao) {

		this.sectiondao = sectiondao;
		this.tkarticledao = articledao;
	}
	
	@Cacheable(value="tkKeywordsCache", key="#root.methodName")
	public Map<Character, Set<String>> getKeywords() throws DaoException {
		
		System.out.println("kw");
		
		// Optional<SectionImp> section = sectiondao.getBySlug("toolkit");
		Map<Character, Set<String>> keywords = new TreeMap<>();
		
		keywords = sectiondao.list().stream()
		
		.flatMap( section -> section.getTopics().stream() )
		
		// get a stream of articles for all these topics together
		.flatMap( topic -> listArticles(topic).stream() )
		
		// get stream of keywords for these articles (getKeywords must never return null)
		.flatMap( article -> 
		
			Stream.of(article.getKeywords().trim().split("\\s*,\\s*"))
				.filter(w -> !w.isEmpty()) // remove empty values
		)
		
		// collect in a map, mapping first letters to ordered sets of keywords  
		.collect(Collectors.groupingBy(
			kw -> kw.toString().toUpperCase().charAt(0), 
			TreeMap::new, 
			Collectors.toCollection(() -> new TreeSet<String>(String.CASE_INSENSITIVE_ORDER))
		));
		
		return keywords;
	}
	
	
	// Just a utility method to hide the exception handling...
	private List<TKArticle> listArticles(Topic topic) {
		
		List<TKArticle> articles = new ArrayList<>();
		try {
			articles = tkarticledao.list(topic,"index");
		} catch (DaoException e) {/* Bad luck now we get an empty list */}
		
		return articles;
	}
	
}


