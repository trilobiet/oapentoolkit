package com.trilobiet.oapen.oapentoolkit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
import com.trilobiet.oapen.oapentoolkit.data.TKArticle;
import com.trilobiet.oapen.oapentoolkit.data.TKArticleConverter;
import com.trilobiet.oapen.oapentoolkit.data.TKArticleList;
import com.trilobiet.oapen.oapentoolkit.data.TopicTocGenerator;
import com.trilobiet.oapen.oapentoolkit.translate.LibreTranslateArticleService;
import com.trilobiet.oapen.oapentoolkit.translate.TranslateArticleService;
import com.trilobiet.oapen.sitesearch.SiteSearchService;
import com.trilobiet.oapen.sitesearch.mysql.MySQLSiteSearchService;

@Configuration
@ComponentScan (
	basePackages = {"com.trilobiet.oapen.oapentoolkit"}
	//, excludeFilters = {
	//		@Filter( type=FilterType.ANNOTATION, value=EnableWebMvc.class ) 
	//}
)
public class RootConfiguration {
	
	@Value("${url_strapi}")
	private String urlStrapi = "";	

	@Value("${url_feed_hypotheses}")
	private String urlHypotheses = "";
	
	@Value("${url_mysql_search}")
	private String urlMysqlSearch = "";
	
	@Value("${url_translate_service}")
	private String urlTranslateService = "";
	
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
		
		// System.out.println("URL Strapi: " + urlStrapi);
		return new HtmlSectionService<>(urlStrapi, sectionMdConverter());
	}
	
	@Bean 
	public Md2HtmlConverter<TopicImp> topicMdConverter() {
		return new Md2HtmlTopicConverter<TopicImp>( markdownflavour() );
	}

	@Bean(name = "topicService")
	public HtmlTopicService<TopicImp> topicService() {
		return new HtmlTopicService<>(urlStrapi, topicMdConverter());
	}
	
	@Bean 
	public Md2HtmlConverter<TKArticle> articleMdConverter() {
		return new TKArticleConverter( markdownflavour() );
	}
	
	@Bean
	public GenericArticleDao<TKArticle> articleDao() {
		return new GenericArticleDao<>(urlStrapi, TKArticle.class, TKArticleList.class);
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
		return new HtmlSnippetService<>(urlStrapi, snippetMdConverter() );
	}
	
	@Bean 
	public HtmlFileService<FileImp> fileService() {
		return new HtmlFileService<>(urlStrapi );
	}
	
	@Bean
	public GenericSectionDao<SectionImp> sectionDao() {
		return new GenericSectionDao<>(urlStrapi, SectionImp.class, SectionList.class);
	}

	@Bean 
	public TopicTocGenerator topicTocGenerator() {
		return new TopicTocGenerator();
	}
	
	@Bean 
	public SiteSearchService siteSearchService() {
		
		// Use only fields from table 'article' for which a full-text index has been created 
		String searchfields = "title, summary, content, `references`, resources, sources, doi, author, tags, keywords";
		return new MySQLSiteSearchService(urlMysqlSearch, searchfields);
	}
	
	@Bean 
	public TranslateArticleService translateArticleService() {
		
		return new LibreTranslateArticleService(urlTranslateService);
	}
	
}

