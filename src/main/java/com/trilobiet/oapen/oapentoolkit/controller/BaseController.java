package com.trilobiet.oapen.oapentoolkit.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import com.trilobiet.graphqlweb.implementations.aexpgraphql2.file.FileImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.ArticleService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.FileService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.SectionService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.SnippetService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.TopicService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.snippet.SnippetImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.topic.TopicImp;
import com.trilobiet.oapen.oapentoolkit.data.TKArticle;

@Controller
public class BaseController {

	protected final Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	protected SectionService<SectionImp> sectionService;

	@Autowired
	protected TopicService<TopicImp> topicService;

	@Autowired
	protected ArticleService<TKArticle> articleService;
	
	@Autowired
	protected SnippetService<SnippetImp> snippetService;

	@Autowired
	protected FileService<FileImp> fileService;

	@Autowired
	protected Environment environment;	
    
	
	/**
	 * Construct a section placeholder for the breadcrumb path
	 * 
	 * @param name
	 * @param slug
	 * @return
	 */
	protected SectionImp getBreadcrumbSection(String name, String slug) {
		
		SectionImp section = new SectionImp(); 
		section.setName(name);
		section.setSlug(slug);
		return section;
	}
	
	/**
	 * Use to prepend section to prevnext links
	 * 
	 * @param sectionslug
	 * @return
	 */
	protected String sectionPrefix(String sectionslug) {
		
		StringBuilder sb = new StringBuilder();
		
		if (sectionslug != null) {
			sb.append("/").append(sectionslug);
		}
		
		return sb.append("/article/").toString();
	}

}
