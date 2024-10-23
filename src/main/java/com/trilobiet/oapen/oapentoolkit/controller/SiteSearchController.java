package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.oapen.sitesearch.SiteSearchException;
import com.trilobiet.oapen.sitesearch.SiteSearchResult;
import com.trilobiet.oapen.sitesearch.SiteSearchService;


@Controller
public class SiteSearchController extends BaseController {
	
	private final Integer PAGE_SIZE = 10;
	
	@Autowired
	SiteSearchService siteSearchService;

	@RequestMapping({"/sitesearch"})
	public ModelAndView siteSearch( 
			@RequestParam( value="term", defaultValue="" ) String term,
			@RequestParam( value="page", defaultValue="0" ) Integer page
		) {
		
		List<SiteSearchResult> searchResults = Collections.emptyList();
		String searchError = "";
		
		if (!term.isBlank()) {
			try {
				searchResults = siteSearchService.search(term);
			} catch (SiteSearchException e) {
				searchError = e.getMessage();
			}
		}	

		page = Math.max(page, 0);
		int total = searchResults.size();
		int lastPage = (total-1) / PAGE_SIZE;
		int start = page * PAGE_SIZE;
		int nextPage = page < lastPage ? page + 1 : -1 ;
		int prevPage = page > 0 ? page - 1: -1;
		
		List<SiteSearchResult> pagedResults = searchResults.subList(
			Math.min(start, searchResults.size()), 
			Math.min(start + PAGE_SIZE, searchResults.size())
		);
		
		// System.out.println("TOTAL " + total);
		// searchResults.forEach(r -> System.out.println(r.getTitle()));
		
		ModelAndView mv = new ModelAndView("sitesearch");
		mv.addObject("pagedResults", pagedResults);
		mv.addObject("page", page);
		mv.addObject("nextPage", page);
		mv.addObject("pageSize", PAGE_SIZE);
		mv.addObject("nextPage", nextPage);
		mv.addObject("prevPage", prevPage);
		
		if (!searchError.isEmpty()) mv.addObject("searchError", searchError);
		
		return mv;
	}
	
}
