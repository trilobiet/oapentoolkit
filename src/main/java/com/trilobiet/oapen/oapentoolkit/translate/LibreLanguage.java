package com.trilobiet.oapen.oapentoolkit.translate;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LibreLanguage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	private String[] targets;
	
	@Override
	public String toString() {
		return "LibreLanguage [code=" + code + ", name=" + name + ", targets=" + Arrays.toString(targets) + "]";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getTargets() {
		return targets;
	}

	public void setTargets(String[] targets) {
		this.targets = targets;
	}
	
}
