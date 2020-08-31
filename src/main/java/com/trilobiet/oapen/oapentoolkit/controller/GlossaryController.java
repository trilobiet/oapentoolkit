package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.graphqlweb.datamodel.ArticleOutline;
import com.trilobiet.graphqlweb.datamodel.Topic;
import com.trilobiet.oapen.oapentoolkit.data.TopicTocGenerator;

@Controller
public class GlossaryController extends BaseController {
	
	@Autowired
	protected TopicTocGenerator tocGenerator;	

	@RequestMapping({"/glossary","/glossary/{topicslug}"}) // no sub pages for glossary (topic redirect to section)
	public ModelAndView showGlossary() throws Exception {

		ModelAndView mv = new ModelAndView("glossary/list");
		
		Topic topic = topicService.getTopicBySlug("glossary")
			.orElseThrow(() -> new ResourceNotFoundException("Topic 'glossary' not found"));
		
		Map<Character, Collection<ArticleOutline>> glossary = tocGenerator.alphabetizedToc(topic);
		
		mv.addObject("glossary", glossary );
		mv.addObject("section",getBreadcrumbSection("Glossary","glossary"));
	
		return mv;
	}

}
