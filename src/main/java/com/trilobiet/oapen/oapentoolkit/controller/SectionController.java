package com.trilobiet.oapen.oapentoolkit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.graphqlweb.datamodel.Section;
import com.trilobiet.graphqlweb.helpers.CmsUtils;

@Controller
public class SectionController extends BaseController {

	@RequestMapping("/{slug}") 
	public ModelAndView showTopic(
			@PathVariable( "slug" ) String slug 
		) throws Exception {

		ModelAndView mv = new ModelAndView("section");
		
		Section section = sectionService.getSectionBySlug(slug)
				.orElseThrow(ResourceNotFoundException::new);
		
		mv.addObject("section", section);
		mv.addObject("bodyClass", CmsUtils.getCssClass(section) );
		
		return mv;
	}
	
}
