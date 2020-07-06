package com.trilobiet.oapen.oapentoolkit.data;

import com.trilobiet.graphqlweb.markdown2html.Md2HtmlArticleConverter;
import com.trilobiet.graphqlweb.markdown2html.StringFunction;

public class TKArticleConverter extends Md2HtmlArticleConverter<TKArticle> {
	
	private final StringFunction function;

	public TKArticleConverter(StringFunction f) {
		super(f);
		function = f;
	}

	@Override
	public void convert(TKArticle article) {
		
		super.convert(article);
		
		if (article.getSources() != null) {
			article.setSources( function.apply(article.getSources())  );
		}	

		if (article.getReferences() != null) {
			article.setReferences( function.apply(article.getReferences())  );
		}	
		
		if (article.getResources() != null) {
			article.setResources( function.apply(article.getResources())  );
		}	
		
		// article.setTitle(article.getTitle().replaceAll("\\[[^]]*\\]", ""));
	}
	
	
}
