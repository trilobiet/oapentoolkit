package com.trilobiet.oapen.oapentoolkit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.trilobiet.graphqlweb.dao.DaoException;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.file.FileImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.section.SectionImp;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.FileService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.SectionService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.service.SnippetService;
import com.trilobiet.graphqlweb.implementations.aexpgraphql2.snippet.SnippetImp;
import com.trilobiet.oapen.oapentoolkit.helpers.OapenMenuParser;

/**
 * 
 * @author acdhirr
 */
@ControllerAdvice
public class GlobalAdvice {
	
	protected final Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	protected SectionService<SectionImp> sectionService;
	
	@Autowired
	protected SnippetService<SnippetImp> snippetService;
	
	@Autowired
	protected FileService<FileImp> fileService;

	@Autowired
	public Environment environment;	

	@ModelAttribute(name="settings")
	public Map<String, String> getSettings() {
		
		Map<String, String> settings = new HashMap<>();
		settings.put("dspace_site", environment.getProperty("url_dspace_site"));
		settings.put("dspace_api", environment.getProperty("url_dspace_api"));
		settings.put("google_analytics_id", environment.getProperty("google_analytics_id"));
		return settings;
	}
	
	// Add navigation to all model-views
    @ModelAttribute(name="navigation")
    public void addAttributes(Model model) throws Exception {

		List<SectionImp> sections = sectionService.getSections();
		
		OapenMenuParser<SectionImp> menuparser = new OapenMenuParser<>(sections);
		model.addAttribute("headerSections", menuparser.getSectionsForHeader());
		model.addAttribute("menuLeftSections", menuparser.getSectionsForMainLeft());
		model.addAttribute("menuRightSections", menuparser.getSectionsForMainRight());
		model.addAttribute("footerSections", menuparser.getSectionsForFooter());
		
		if (sections.isEmpty()) System.out.println("Warning: headerSections is empty! Not read from cache?");
    }	

	// Add navigation to all model-views
    @ModelAttribute(name="sectiongroupnames")
    public void addSectionGroupNames(Model model) {
    	
    	// OA Books Toolkit,Researchers Toolkit,Publishing Policies and Funding
    	
    	String[] sectionGroupNames = {};
    	String[] defaults = "Undefined Group 0,Undefined Group 1,Undefined Group 2,Undefined Group 3, Undefined Group 4".split(",");
    	
    	try {
			Optional<SnippetImp> os = snippetService.getSnippet("section-group-names");
			if (os.isPresent()) {
				sectionGroupNames = os.get().getCode().split(",");
			}
		} catch (Exception e) {	} 
    	
    	// Add the defaults to make sure we always have a sufficiently long array
    	sectionGroupNames = ArrayUtils.addAll(sectionGroupNames, defaults) ;
    	
		model.addAttribute("sectiongroupnames", sectionGroupNames);
    }	
    
    
	
    @ModelAttribute(name="miscsettings") 
	public void addMiscSettings(Model model) {
	
	    // Add downloadable site file name to all model-views (display in footer)	
		try {
			Optional<FileImp> oFile;
			oFile = fileService.getFirstWithName("oabooks-toolkit.pdf");
			if (oFile.isPresent()) model.addAttribute("sitepdf", oFile.get().getUrl());
		} catch (DaoException e) {
			log.warn(e.getMessage());
		}
	}	
    
    
}

