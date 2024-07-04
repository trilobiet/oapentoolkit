package com.trilobiet.oapen.oapentoolkit.data;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.trilobiet.graphqlweb.dao.DaoException;
import com.trilobiet.graphqlweb.datamodel.ArticleOutline;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.article.GenericArticleDao;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.GenericSectionDao;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionList;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.topic.GenericTopicDao;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.topic.TopicImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.topic.TopicList;

public class ToolkitDataTest {
	
	private final String host = "https://strapi-oatoolkit.trilobiet.eu/graphql";  

    @Test // Integration test
	public void keywordsTest() throws IOException, DaoException {
    	
		GenericSectionDao<SectionImp> sDao = new GenericSectionDao<>(host, SectionImp.class, SectionList.class);
		GenericArticleDao<TKArticle> aDao = new GenericArticleDao<>(host, TKArticle.class, TKArticleList.class);
		
		KeywordService ks = new KeywordService(sDao, aDao);
		
		Map<Character, Set<String>> keywords = ks.getKeywords();

		assertNotNull(keywords);
		assertTrue(!keywords.keySet().isEmpty());
		assertTrue(!keywords.values().isEmpty());
		
		System.out.println("Keywords: " + keywords);

    }

    @Test // Integration test
	public void glossaryTest() throws IOException, DaoException {
    	
		GenericTopicDao<TopicImp> tDao = new GenericTopicDao<>(host, TopicImp.class, TopicList.class);
		TopicImp topic = tDao.getBySlug("glossary").orElseThrow();
		
		TopicTocGenerator gen = new TopicTocGenerator();
		
		Map<Character, Collection<ArticleOutline>> toc = gen.alphabetizedToc(topic);
		
		assertNotNull(toc);
		assertTrue(!toc.keySet().isEmpty());
		assertTrue(!toc.values().isEmpty());
		
		System.out.println("Toc: " + toc);

    }
    
}
