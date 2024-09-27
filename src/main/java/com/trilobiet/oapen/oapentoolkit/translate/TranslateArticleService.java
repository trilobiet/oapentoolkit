package com.trilobiet.oapen.oapentoolkit.translate;

import java.io.IOException;
import java.util.Map;

import com.trilobiet.oapen.oapentoolkit.data.TKArticle;

public interface TranslateArticleService {
	
	TKArticle translate(TKArticle article, String sourceLanguage, String targetLanguage)
		throws TranslateException;
	
	Map<String, String> getTargetLanguagesFor(String languageCode)
		throws IOException; 
}
