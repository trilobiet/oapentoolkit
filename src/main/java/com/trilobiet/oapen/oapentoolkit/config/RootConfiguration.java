package com.trilobiet.oapen.oapentoolkit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.trilobiet.graphqlweb.implementations.aexpgraphql2.article.GenericArticleDao;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.file.FileImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.GenericSectionDao;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionList;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.html.HtmlArticleService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.html.HtmlFileService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.html.HtmlSectionService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.html.HtmlSnippetService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.html.HtmlTopicService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.snippet.SnippetImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.topic.TopicImp;
import com.trilobiet.graphqlweb.markdown2html.Flexmark2HtmlFunction;
import com.trilobiet.graphqlweb.markdown2html.Md2HtmlConverter;
import com.trilobiet.graphqlweb.markdown2html.Md2HtmlSectionConverter;
import com.trilobiet.graphqlweb.markdown2html.Md2HtmlSnippetConverter;
import com.trilobiet.graphqlweb.markdown2html.Md2HtmlTopicConverter;
import com.trilobiet.graphqlweb.markdown2html.StringFunction;
import com.trilobiet.oapen.oapentoolkit.data.TopicTocGenerator;
import com.trilobiet.oapen.oapentoolkit.data.KeywordService;
import com.trilobiet.oapen.oapentoolkit.data.TKArticle;
import com.trilobiet.oapen.oapentoolkit.data.TKArticleConverter;
import com.trilobiet.oapen.oapentoolkit.data.TKArticleList;
import com.trilobiet.oapen.oapentoolkit.rss.RssService;
import com.trilobiet.oapen.oapentoolkit.rss.hypotheses.HypothesesRssService;

@Configuration
@ComponentScan (
	basePackages = {"com.trilobiet.oapen.oapentoolkit"},
	excludeFilters = {
			@Filter( type=FilterType.ANNOTATION, value=EnableWebMvc.class ) 
	}
)
public class RootConfiguration {
	
	@Autowired
	public Environment env;	
	
	@Bean
	public StringFunction markdownflavour() {
		return new Flexmark2HtmlFunction();
	}
	
	@Bean 
	public Md2HtmlConverter<SectionImp> sectionMdConverter() {
		return new Md2HtmlSectionConverter<SectionImp>( markdownflavour() );
	}

	@Bean(name = "sectionService")
	public HtmlSectionService<SectionImp> sectionService() {
		return new HtmlSectionService<>( env.getProperty("url_strapi"), sectionMdConverter());
	}
	
	@Bean 
	public Md2HtmlConverter<TopicImp> topicMdConverter() {
		return new Md2HtmlTopicConverter<TopicImp>( markdownflavour() );
	}

	@Bean(name = "topicService")
	public HtmlTopicService<TopicImp> topicService() {
		return new HtmlTopicService<>( env.getProperty("url_strapi"), topicMdConverter());
	}
	
	@Bean 
	public Md2HtmlConverter<TKArticle> articleMdConverter() {
		return new TKArticleConverter( markdownflavour() );
	}
	
	@Bean
	public GenericArticleDao<TKArticle> articleDao() {
		return new GenericArticleDao<>(env.getProperty("url_strapi"), TKArticle.class, TKArticleList.class);
	}
	
	@Bean(name = "articleService") 
	public HtmlArticleService<TKArticle> articleService() {
		return new HtmlArticleService<TKArticle>( articleDao(), articleMdConverter() );
	}


	@Bean 
	public Md2HtmlConverter<SnippetImp> snippetMdConverter() {
		return new Md2HtmlSnippetConverter<SnippetImp>( markdownflavour() );
	}

	@Bean 
	public HtmlSnippetService<SnippetImp> snippetService() {
		return new HtmlSnippetService<>( env.getProperty("url_strapi"), snippetMdConverter() );
	}
	
	@Bean 
	public HtmlFileService<FileImp> fileService() {
		return new HtmlFileService<>( env.getProperty("url_strapi") );
	}
	
	
	@Bean 
	public RssService rssService() {
		return new HypothesesRssService(env.getProperty("url_feed_hypotheses"));
	}
	
	@Bean 
	public KeywordService keywordService() {
		return new KeywordService(sectionDao(), articleDao());
	}
	@Bean
	public GenericSectionDao<SectionImp> sectionDao() {
		return new GenericSectionDao<>(env.getProperty("url_strapi"), SectionImp.class, SectionList.class);
	}

	@Bean 
	public TopicTocGenerator topicTocGenerator() {
		return new TopicTocGenerator();
	}
	
	
}

