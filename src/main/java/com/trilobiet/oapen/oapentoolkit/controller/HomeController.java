package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.graphqlweb.datamodel.Snippet;
import com.trilobiet.graphqlweb.datamodel.Topic;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.snippet.SnippetImp;
import com.trilobiet.oapen.oapentoolkit.data.TKArticle;
import com.trilobiet.oapen.oapentoolkit.rss.RssItem;

@Controller
public class HomeController extends BaseController {

	@RequestMapping("/home")
	public ModelAndView showHome(HttpServletRequest request, HttpServletResponse resp) {
		
		ModelAndView mv = new ModelAndView("home"); 

		mv.addObject("ip",request.getRemoteAddr());
		
		// Catch all errors here: home page must always be rendered, erroneous sections can be empty
		
		try {
			Optional<SnippetImp> heroText = snippetService.getSnippet("home-hero-text");
			if (heroText.isPresent() ) mv.addObject("heroText",heroText.get().getCode());
			Optional<SnippetImp> introText = snippetService.getSnippet("home-intro");
			if (introText.isPresent() ) mv.addObject("introText",introText.get().getCode());
			Optional<SnippetImp> lifecycleCaption = snippetService.getSnippet("home-lifecycle-caption");
			if (lifecycleCaption.isPresent() ) mv.addObject("lifecycleCaption",lifecycleCaption.get().getCode());
			// Optional<SnippetImp> toolkitInfoBox = snippetService.getSnippet("home-toolkit-infobox");
			// if (lifecycleCaption.isPresent() ) mv.addObject("toolkitInfoBox",toolkitInfoBox.get().getCode());
		} catch (Exception e) {
			log.error(e);
		}
		
		try {
			List<TKArticle> showcases = articleService.getByFieldContainsValue("params", "showcase=true");
			mv.addObject("showcases",showcases);
		} catch (Exception e) {
			log.error(e);
		}
		
		try {
			Optional<SectionImp> tkSection = sectionService.getSectionBySlug("lifecycle");
			if( tkSection.isPresent() ) {
				List<Topic> tktopics = tkSection.get().getTopics();
				mv.addObject("toolkitsection",tkSection.get());
				mv.addObject("toolkittopics",tktopics);
			}
			else {
				mv.addObject("toolkitsection",null);
				mv.addObject("toolkittopics",null);
			}
		} catch (Exception e) {
			log.error(e);
		}
		
		try {
			List<TKArticle> spotlights = articleService.getByFieldValue("spotlight", "true");
			mv.addObject("spotlights",spotlights);
		} catch (Exception e) {
			log.error(e);
		}
		
		try {
			// Get latest blog post(s) of selected categories
			String cats = environment.getProperty("blog_categories");
			// NB: cats.split would produce a single empty list item on an empty string, hence the test!
			List<String> categories = cats.equals("") ? Collections.emptyList() : Arrays.asList(cats.split(","));
			List<RssItem> rssItems = rssService.getItems(2,categories);
			mv.addObject("rssItems",rssItems);
		} catch (Exception e) {
			log.error(e);
		}
		
		try {
			Optional<SnippetImp> otwitter = snippetService.getSnippet("twitter-timeline");
			Snippet ttl = otwitter.orElseGet(()->new SnippetImp());
			mv.addObject("twittertimeline",ttl);
		} catch (Exception e) {
			log.error(e);
		}
		
		return mv;
	}
	
	
	@RequestMapping("/sitemap") 
	public ModelAndView showSitemap() throws Exception {
		
		ModelAndView mv = new ModelAndView("sitemap"); 
		List<SectionImp> sections = sectionService.getSections();
		mv.addObject("sections", sections);

		return mv;
	}	
	
}
