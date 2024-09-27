package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.snippet.SnippetImp;
import com.trilobiet.oapen.oapentoolkit.data.TKArticle;

@Controller
public class HomeController extends BaseController {

	@GetMapping({"/","/home"})
	public ModelAndView showHome(HttpServletRequest request, HttpServletResponse resp) {
		
		ModelAndView mv = new ModelAndView("home"); 

		mv.addObject("ip",request.getRemoteAddr());
		
		// Catch all errors here: home page must always be rendered, erroneous sections can be empty
		
		try {
			Optional<SnippetImp> introText = snippetService.getSnippet("home-intro");
			if (introText.isPresent() ) mv.addObject("introText",introText.get().getCode());
			Optional<SnippetImp> lifecycleCaption = snippetService.getSnippet("home-lifecycle-caption");
			if (lifecycleCaption.isPresent() ) mv.addObject("lifecycleCaption",lifecycleCaption.get().getCode());
			// Optional<SnippetImp> toolkitInfoBox = snippetService.getSnippet("home-toolkit-infobox");
			// if (lifecycleCaption.isPresent() ) mv.addObject("toolkitInfoBox",toolkitInfoBox.get().getCode());
		} catch (Exception e) {
			log.error(e);
		}
		
		// A map of all sections grouped by groupNumber
		Map<Integer, List<SectionImp>> sections = new HashMap<>();
		
		try {
			
			sections = sectionService.getSections().stream()
				.filter(sec -> sec.getGroupNumber() > 0) // group 0 is reserved for special menu items
				.filter(sec -> sec.isPublish())	
				.collect(Collectors.groupingBy(sec -> sec.getGroupNumber())
			);

		} catch (Exception e) {
			log.error(e);
		}	

		mv.addObject("sectiongroups", sections);
		
		
		/*
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
		*/
		
		try {
			List<TKArticle> spotlights = articleService.getByFieldValue("spotlight", "true");
			mv.addObject("spotlights",spotlights);
		} catch (Exception e) {
			log.error(e);
		}

		return mv;
	}
	
	
	@GetMapping("/sitemap") 
	public ModelAndView showSitemap() throws Exception {
		
		ModelAndView mv = new ModelAndView("sitemap"); 
		List<SectionImp> sections = sectionService.getSections();
		mv.addObject("sections", sections);

		return mv;
	}	
	
}
