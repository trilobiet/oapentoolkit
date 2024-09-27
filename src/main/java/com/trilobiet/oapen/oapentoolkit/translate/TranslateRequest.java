package com.trilobiet.oapen.oapentoolkit.translate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TranslateRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/*
	q: "Hallo",
	source: "auto",
	target: "en",
	format: "text",
	alternatives: 3,
	api_key: ""
	*/	
	
	private List<String> qs = new ArrayList<>();
	private String source, target, format;
	private String apiKey;
	private int alternatives;
	
	public List<String> getQ() {
		return qs;
	}
	
	public void addQ(String q) {
		this.qs.add(q == null?"":q);
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getTarget() {
		return target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public int getAlternatives() {
		return alternatives;
	}
	
	public void setAlternatives(int alternatives) {
		this.alternatives = alternatives;
	}
	
}
