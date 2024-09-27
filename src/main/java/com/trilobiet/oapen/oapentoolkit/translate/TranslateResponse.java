package com.trilobiet.oapen.oapentoolkit.translate;

import java.io.Serializable;
import java.util.List;

public class TranslateResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/*
	{
	    "alternatives": [],
	    "detectedLanguage": {
	        "confidence": 6,
	        "language": "en"
	    },
	    "translatedText": "How do you do?"
	}
	*/
	
	private List<String> translatedText;
	private String error;

	public List<String> getTranslatedText() {
		return translatedText;
	}

	public void setTranslatedText(List<String> translatedText) {
		this.translatedText = translatedText;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
