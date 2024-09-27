package com.trilobiet.oapen.oapentoolkit.translate;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trilobiet.oapen.oapentoolkit.data.TKArticle;

public class LibreTranslateArticleService implements TranslateArticleService {
	
	private final String serverUrl;
	private final ObjectMapper mapper;

	public LibreTranslateArticleService(String serverUrl) {

		this.serverUrl = serverUrl;
		this.mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
	}

	/*@Cacheable(value="bookCache", 
			  key="{ #root.methodName, #isbn?.id, #checkWarehouse }")
			public Book findBook(ISBN isbn, boolean checkWarehouse) 
	*/
	
	@Override
	@Cacheable(value="tkTranslationsCache", 
				key="{#article.id, #sourceLanguage, #targetLanguage }")
	public TKArticle translate(TKArticle article, String sourceLanguage, String targetLanguage) 
		throws TranslateException {
		
		TranslateRequest req = new TranslateRequest();
		req.setSource(sourceLanguage);
		req.setTarget(targetLanguage);
		req.setFormat("html");
		req.addQ(article.getTitle());
		req.addQ(article.getContent());
		
		try {
			
			TKArticle copiedArticle = copyArticle(article);
			
			String requestBody = mapper
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(req);
			
			HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create(serverUrl + "/translate"))
			    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
			    .header("Content-Type", "application/json")
			    .build();
			
			HttpClient httpClient = HttpClient.newHttpClient();
			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
			TranslateResponse tr = mapper.readValue(response.body(), TranslateResponse.class);
			
			if (Objects.nonNull(tr.getError())) throw new TranslateException(tr.getError());
			
			copiedArticle.setTitle(tr.getTranslatedText().get(0));
			copiedArticle.setContent(tr.getTranslatedText().get(1));
			
			return copiedArticle;
			
		} catch (IOException | InterruptedException | TranslateException e) {
			
			throw new TranslateException(e.getMessage());
		}
		
	}

	
	private TKArticle copyArticle(TKArticle article) throws JsonMappingException, JsonProcessingException {
		
		TKArticle deepCopy = mapper.readValue(mapper.writeValueAsString(article), TKArticle.class);
		
		return deepCopy;
	}
	
	
	@Override
	public Map<String, String> getTargetLanguagesFor(String languageCode) throws IOException {
		
		List<LibreLanguage> languages = new ArrayList<>();
		
		CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, LibreLanguage.class);
		
    	URL url = new URL(serverUrl + "/languages");
    	languages.addAll( mapper.readValue(url, javaType) );
	    
	    Map<String, LibreLanguage> langmap = 
	    	languages.stream().collect(Collectors.toMap(LibreLanguage::getCode, lan -> lan));
	    
	    final List<String> targets = new ArrayList<>();
	    
	    if (langmap.containsKey(languageCode))	    
	    	targets.addAll(Arrays.asList(langmap.get(languageCode).getTargets()));
	    
	    Map<String, String> targetLanguages = langmap.entrySet()
	    	.stream()
	    	.filter(e -> targets.contains(e.getValue().getCode()))
	    	// Sort by the language NAME, not the code 
	    	.sorted((e1, e2) -> e1.getValue().getName().compareTo(e2.getValue().getName()))
	    	// Write to a sortable map here, so a LinkedHashMap (3rd argument is for handling duplicates)
	    	.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getName(), (e1, e2) -> e1, LinkedHashMap::new));
	    
		return targetLanguages;
	}

	
	public static void main(String... args) throws TranslateException {
		
		LibreTranslateArticleService s = new LibreTranslateArticleService("http://localhost:5000/");
		
		TKArticle article = new TKArticle();
		article.setId("0");
		article.setTitle("Hoofdstuk 1");
		article.setContent("<h1>Op de hoek van de straat stond een engelse soldaat</h1>");
		
		TKArticle translatedArticle = s.translate(article, "nl", "en");
		
		System.out.println(translatedArticle.getTitle());
		System.out.println(translatedArticle.getContent());
	}
	

}
