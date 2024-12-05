package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.graphqlweb.datamodel.ArticleOutline;
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

		Section section = sectionService.getSectionBySlug(slug)
				.orElseThrow(ResourceNotFoundException::new);

		ModelAndView mv = new ModelAndView();
		
		if (section.getName().trim().equalsIgnoreCase("about")) 
			mv.setViewName("section_expanded");
		else 
			mv.setViewName("section");
		
		List<TopicImp> topics = new ArrayList<>();

		// We must populate a list of TopicImps because Section contains Topics
		// and Topics contain Articles, which summaries we want to show,
		// but these are not Html translated!
		section.getTopics().forEach(topic -> { 
			
			try {
				TopicImp t = topicService.getTopicBySlug(topic.getSlug()).get();

				List<ArticleOutline> articles = new ArrayList<>();
				
				t.getArticles().forEach(article -> {
					try {
						articles.add(articleService.getArticleBySlug(article.getSlug()).get());
					} catch (Exception e) {}
				});
				
				t.setArticles(articles);
				
				topics.add(t);
			} catch (Exception e) {}
			
		});
		
		// topics.remove(1); // test
		
		mv.addObject("section", section);
		mv.addObject("sectiontopics", topics);
		mv.addObject("bodyClass", CmsUtils.getCssClass(section) );
		
		return mv;
	}
	
}
