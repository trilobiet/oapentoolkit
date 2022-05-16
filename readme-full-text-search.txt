
To enable full text-search create a text index:

connect to mongo:
	mongo [server]:[port]
	
> use strapi	
> db.article.createIndex({"title":"text","content":"text","summary":"text","tags":"text","keywords":"text","references":"text","resources":"text","sources":"text"})


