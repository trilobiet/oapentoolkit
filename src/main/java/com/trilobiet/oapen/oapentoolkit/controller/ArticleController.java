package com.trilobiet.oapen.oapentoolkit.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.graphqlweb.datamodel.ArticleOutline;
import com.trilobiet.graphqlweb.helpers.CmsUtils;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.topic.TopicImp;
import com.trilobiet.oapen.oapentoolkit.data.TKArticle;
import com.trilobiet.oapen.oapentoolkit.translate.TranslateArticleService;

@Controller
public class ArticleController extends BaseController {
	
	final private String MSGTRANSLATESUCCESS = 
		"The text below is an automatically generated translation";
	final private String MSGTRANSLATEFAILURE = 
		"Could not retrieve a translation!";
	
	@Autowired
	protected TranslateArticleService translateArticleService;

	@GetMapping({
		"{sectionslug}/{topicslug}/article/{slug}",
		"{sectionslug}/article/{slug}",
		"/article/{slug}"
	})
	public ModelAndView showArticle(
			@PathVariable( "slug" ) String slug,
			@PathVariable(required=false, name="topicslug" ) String topicslug,
			@PathVariable(required=false, name="sectionslug" ) String sectionslug,
			@RequestParam(name="language",required=false ) String language
		) throws Exception {
		
		ModelAndView mv = new ModelAndView("article");
		
		TKArticle article = articleService.getArticleBySlug(slug)
				.orElseThrow( () -> new ResourceNotFoundException() );
		
		mv.addObject("bodyClass", CmsUtils.getCssClass(article));
		
		// Translate or not -- TRANSLATIONS CURRENTLY DISABLED
		mv.addObject("article", article);
		/*
		if (language == null) 
			mv.addObject("article", article);
		else try { 
			mv.addObject("article", translateArticleService.translate(article, "en", language));
			mv.addObject("msg_translate_success", MSGTRANSLATESUCCESS);
		}
		catch (TranslateException e) {
			System.out.println(e);
			mv.addObject("article", article);
			mv.addObject("msg_translate_failure", MSGTRANSLATEFAILURE);
		}
		*/
		
		Set<ArticleOutline> linked = articleService.getLinked(article);
		mv.addObject("linked", linked);
		
		if(topicslug != null) {
			Optional<TopicImp> otopic = topicService.getTopicBySlug(topicslug);
			//mv.addObject("topic",otopic.orElse(new TopicImp()));
			mv.addObject("topic",otopic.orElse(null));
		}

		if(sectionslug != null) {
			Optional<SectionImp> osection = sectionService.getSectionBySlug(sectionslug);
			//mv.addObject("section", osection.orElse(new SectionImp()));
			mv.addObject("section", osection.orElse(null));
		}
		
		// for prevnext links
		mv.addObject("sectionprefix", sectionPrefix( sectionslug ));
		
		return mv;
	}
	
	@ResponseBody
	@GetMapping({"/articlelanguages"})
	public Map<String, String> getLanguages() {
		
		Map<String, String> langmap = new LinkedHashMap<>();
		
		try {
			langmap.putAll(translateArticleService.getTargetLanguagesFor("en"));
		} catch (IOException e) {
			log.warn("Language map is empty! Check whether your connection string is correct and the language service is running.");
		}
			
		return langmap;
	}		
	
	
}
