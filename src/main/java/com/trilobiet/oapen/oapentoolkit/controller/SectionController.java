package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.graphqlweb.datamodel.Section;
import com.trilobiet.graphqlweb.helpers.CmsUtils;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.topic.TopicImp;
import com.trilobiet.graphqlweb.markdown2html.Md2HtmlConverter;

@Controller
public class SectionController extends BaseController {
	
	@Autowired
	protected Md2HtmlConverter<TopicImp> topicHtmlConverter;

	@RequestMapping("/{slug}") 
	public ModelAndView showTopic(
			@PathVariable( "slug" ) String slug 
		) throws Exception {

		ModelAndView mv = new ModelAndView();

		switch (slug) {
			case "faqs":
			case "lifecycle": 
				mv.setViewName("section_expanded"); break;
			default: 
				mv.setViewName("section"); 
		}
		
		Section section = sectionService.getSectionBySlug(slug)
				.orElseThrow(ResourceNotFoundException::new);
		
		List<TopicImp> topics = new ArrayList<>();

		// We must populate a list of TopicImps because Section contains Topics
		// but these are not Html translated
		section.getTopics().forEach(topic -> { 
			try {
				topics.add(topicService.getTopicBySlug(topic.getSlug()).get());
			} catch (Exception e) {}
		});
		
		mv.addObject("section", section);
		mv.addObject("sectiontopics", topics);
		mv.addObject("bodyClass", CmsUtils.getCssClass(section) );
		
		return mv;
	}
	
}
