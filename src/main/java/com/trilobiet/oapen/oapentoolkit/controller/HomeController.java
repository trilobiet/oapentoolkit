package com.trilobiet.oapen.oapentoolkit.controller;

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
			Optional<SnippetImp> introText = snippetService.getSnippet("home-intro");
			if (introText.isPresent() ) mv.addObject("home_intro",introText.get().getCode());
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
			Optional<SectionImp> toolkitSection = sectionService.getSectionBySlug("toolkit");
			if( toolkitSection.isPresent() ) {
				List<Topic> tktopics = toolkitSection.get().getTopics();
				mv.addObject("toolkittopics",tktopics);
			}
			else {
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
			// Get latest blog post(s)
			List<RssItem> rssItems = rssService.getItems(2);
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
	
}
