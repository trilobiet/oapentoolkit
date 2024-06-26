package com.trilobiet.oapen.oapentoolkit.data;

import com.trilobiet.graphqlweb.implementations.aexpgraphql2.article.ArticleImp;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;
import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import io.aexp.nodes.graphql.annotations.GraphQLProperty;

@GraphQLProperty(
		name = "article", 
		arguments = {
			@GraphQLArgument(name = "id")
		}
	)
public class TKArticle extends ArticleImp {

	@GraphQLIgnore
	private static final long serialVersionUID = 1L;

	private String author, doi, references, sources, resources, keywords;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getReferences() {
		return references;
	}

	public void setReferences(String references) {
		this.references = references;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	// source acknowledgement
	public String getSources() {
		return sources;
	}

	public void setSources(String sources) {
		this.sources = sources;
	}

	public String getKeywords() {
		return keywords != null? keywords : "";
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	@Override
	public String toString() {
		return "TKArticle [toString()=" + super.toString() + "]";
	}

}
