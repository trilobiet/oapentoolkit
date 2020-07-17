package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.trilobiet.oapen.oapentoolkit.rss.RssItem;

@Controller
public class BlogController extends BaseController {

	@RequestMapping({"/blog"})
	public ModelAndView showBlog(
			@RequestParam( value="link", required=false ) String link
		) throws Exception {

		ModelAndView mv = new ModelAndView("blog");
		
		String cats = environment.getProperty("blog_categories");
		// NB: cats.split would produce a single empty list item on an empty string, hence the test!
		List<String> categories = cats.equals("") ? Collections.emptyList() : Arrays.asList(cats.split(","));
		List<RssItem> blogPosts = rssService.getItems(10,categories);
		
		/* display post by link or first post */
		RssItem featuredPost = null;
		if (link != null) {
			featuredPost = rssService.getItemByLink(blogPosts, link).orElse(featuredPost);
		}	
		else if ( !blogPosts.isEmpty() ){
			featuredPost = blogPosts.get(0);
		}

		blogPosts.remove(featuredPost);
		
		mv.addObject("blogPosts", blogPosts);
		mv.addObject("featuredPost", featuredPost);	
		
		return mv;
	}
	
}
