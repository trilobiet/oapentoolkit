package com.trilobiet.oapen.oapentoolkit.rss;

public class RssException extends Exception {

	private static final long serialVersionUID = 1L;

	public RssException(Exception root) {
		super(root);
	}

	public RssException(String message) {
		super(message);
	}

	public RssException() {
		super();
	}
	
}
