package com.trilobiet.oapen.oapentoolkit.data;

import java.util.List;

import com.trilobiet.graphqlweb.implementations.aexpgraphql2.article.GenericArticleList;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;
import io.aexp.nodes.graphql.annotations.GraphQLArguments;
import io.aexp.nodes.graphql.annotations.GraphQLProperty;

/**
 * 
 * @author acdhirr
 *
 */
public class TKArticleList implements GenericArticleList<TKArticle> {
	
	@GraphQLArguments({
		@GraphQLArgument(
			name = "sort",
			type = "String",
			optional = true, value = "index:asc,slug:asc"
		),
		@GraphQLArgument(
			name = "where",
			optional = false, value = "publish:true"
		)
	})
	
	// @GraphQLProperty(name="toolkitarticles")
	@GraphQLProperty(name="articles")
	private List<TKArticle> articles;

	public List<TKArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<TKArticle> articles) {
		this.articles = articles;
	}
}
