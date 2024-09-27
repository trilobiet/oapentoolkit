package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.graphqlweb.datamodel.ArticleOutline;
import com.trilobiet.graphqlweb.datamodel.Topic;
import com.trilobiet.graphqlweb.helpers.CmsUtils;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionImp;
import com.trilobiet.oapen.oapentoolkit.data.TKArticle;

@Controller
public class TopicController extends BaseController {

	@RequestMapping({
		"{sectionslug}/{slug}",
		"/topic/{slug}"
	})
	public ModelAndView showTopic(
			@PathVariable( "slug" ) String slug,
			@PathVariable(required=false, name="sectionslug" ) String sectionslug	
		) throws Exception {
		
		ModelAndView mv;
		
		Topic topic = topicService.getTopicBySlug(slug)
				.orElseThrow( () -> new ResourceNotFoundException() );
		
		List<TKArticle> articles = articleService.getArticlesByTopic(topic);
		
		if (topic.getType().equals("redirect")) {
			
			String redirectUrl = CmsUtils.getParamValue(topic, "redirect");
			return new ModelAndView("redirect:"+redirectUrl);
		}	
		else {

			mv = selectView(topic);
			
			// When only one article is shown, get list of related articles based on tags
			if (!articles.isEmpty() 
					&& (
						topic.getArticleDisplay() == null	
					||	topic.getArticleDisplay().equals("Show_lead_article_and_list_titles")
					||	topic.getArticleDisplay().equals("Show_first_article")
				) ) {
				
				Set<ArticleOutline> linked = articleService.getLinked(articles.get(0));
				mv.addObject("linked", linked);
				mv.addObject("firstarticle",articles.get(0));
				mv.addObject("article",articles.get(0));
			}			
		}
		
		if(sectionslug != null) {
			Optional<SectionImp> osection = sectionService.getSectionBySlug(sectionslug);
			mv.addObject("section",osection.orElse(new SectionImp()));
		}		
		
		mv.addObject("topic", topic);
		// articles may be an empty collection if we only need summaries (which are in the topic tree)
		mv.addObject("articles", articles);
		mv.addObject("bodyClass", CmsUtils.getCssClass(topic) );
		
		// for prevnext links
		mv.addObject("sectionprefix", sectionPrefix( sectionslug ));
		
		return mv;
	}

	
	private ModelAndView selectView(Topic topic) {
		
		/*
		if(topic.getArticleDisplay() != null) {
			
			switch( topic.getArticleDisplay() ) {
			
				case "Show_lead_article_and_list_titles":
					return new ModelAndView("topic_first_and_list_titles");
				case "Show_first_article":
					return new ModelAndView("article");
				case "Show_list_of_articles":
					return new ModelAndView("topic_list_articles");
				case "Show_list_of_titles":
					return new ModelAndView("topic_list_titles");
				case "Show_list_of_summaries":					
					return new ModelAndView("topic_list_summaries");
			}
		}
		
		// None set, choose default 
		return new ModelAndView("topic_list_titles");
		*/
		
		// Functionality disabled, always show summaries
		return new ModelAndView("topic_list_summaries");
		
	}
	
	
	@RequestMapping(
		value={
			"/listtopic/{slug}",
			"{sectionslug}/listtopic/{slug}"
		}
		, params={ "rows" }
	)
	public ModelAndView listArticles(
			@PathVariable( "slug" ) String slug,
			@PathVariable( required=false, name="sectionslug" ) String sectionslug,
			@RequestParam( value="rows", required=false, defaultValue="100" ) int rows
		) throws Exception {
		
		ModelAndView mv = new ModelAndView("include/listarticles.html");

		Topic topic = topicService.getTopicBySlug(slug)
				.orElseThrow( () -> new ResourceNotFoundException() );
		
		List<TKArticle> allArticles = articleService.getArticlesByTopic(topic);
		List<TKArticle> articles = allArticles.subList(0, Math.min(rows, allArticles.size()));
		
		if(sectionslug != null) {
			Optional<SectionImp> osection = sectionService.getSectionBySlug(sectionslug);
			mv.addObject("section",osection.orElse(new SectionImp())); // null section
		}

		mv.addObject("topic", topic);
		mv.addObject("articles", articles);
		mv.addObject("rows", rows);
		mv.addObject("hasMore", allArticles.size()>rows);
		
		return mv;
	}
	
	
}
