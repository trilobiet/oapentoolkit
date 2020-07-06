package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.List;

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

		return mv;
	}


	@RequestMapping(value="/keywords",params={"keyword"})
	public ModelAndView searchResults(
			@RequestParam( value="keyword", required=true ) String keyword
		) throws DaoException {
		
		List<TKArticle> tkarticles = articleService.getByFieldContainsValue("keywords", keyword);
		
		ModelAndView mv = new ModelAndView("keywords/searchresults");
		mv.addObject("keyword", keyword);
		mv.addObject("tkarticles", tkarticles);
		mv.addObject("section",getBreadcrumbSection("Keywords","keywords"));

		return mv;
	}
	
	/**
	 * Construct a section placeholder for the breadcrumb path
	 * 
	 * @param name
	 * @param slug
	 * @return
	 */
	private SectionImp getBreadcrumbSection(String name, String slug) {
		
		SectionImp section = new SectionImp(); 
		section.setName(name);
		section.setSlug(slug);
		return section;
		
	}
	
}
