package com.trilobiet.oapen.oapentoolkit.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RootController {

	/** 
	 * Make it look like favicon is in the root
	 * 
	 * @return
	 */
	@RequestMapping("/favicon.ico")
	public ModelAndView showFavicon() {
		return new ModelAndView("forward:/static-assets/images/favicon.ico");
	}
	
	@CacheEvict(value={"tkSectionsCache","tkTopicsCache","tkArticlesCache","tkFilesCache","tkKeywordsCache","tkRssCache"}, allEntries=true)
	@RequestMapping("/clearCache") 
	public ModelAndView clearCache(Model model) {
		return new ModelAndView("redirect:/home");
	}
	
}
