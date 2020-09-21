package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.graphqlweb.dao.DaoException;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionImp;
import com.trilobiet.oapen.oapentoolkit.data.KeywordService;
import com.trilobiet.oapen.oapentoolkit.data.TKArticle;

@Controller
public class KeywordsController extends BaseController {
	
	@Autowired
	protected KeywordService keywordService;	

	@RequestMapping("/keywords")
	public ModelAndView showKeywords() throws DaoException {
		
		ModelAndView mv = new ModelAndView("keywords/list");
		mv.addObject("keywords", keywordService.getKeywords());
		mv.addObject("section",getBreadcrumbSection("Keywords","keywords"));
		
		Optional<SectionImp> osection = sectionService.getSectionBySlug("keywords");
		mv.addObject("section",osection.orElse(new SectionImp()));

		return mv;
	}


	@RequestMapping(value="/keywords",params={"keyword"})
	public ModelAndView searchResults(
			@RequestParam( value="keyword", required=true ) String keyword
		) throws DaoException {
		
		/* 
		 * TODO escape keywords parentheses and special characters
		 *  
		 * ( -> \\(
		 * \ -> \\
		 * " -> \"
		 * 
		 * to facilitate searching in graphql:
		 *
		 * e.g.:
		 * 		keyword = "Archiving (digital archiving)"
		 * 		in graphql:	
		 * 		keywords_contains: "Archiving \\(digital archiving\\)"
		 */
		
		ModelAndView mv = new ModelAndView("keywords/searchresults");
		
		List<TKArticle> tkarticles = articleService.getByFieldContainsValue("keywords", keyword);
		
		mv.addObject("keyword", keyword);
		mv.addObject("tkarticles", tkarticles);
		mv.addObject("section",getBreadcrumbSection("Keywords","keywords"));

		return mv;
	}
	
}
