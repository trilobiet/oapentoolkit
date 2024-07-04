
To enable full text-search in MySQL create a text index:

ALTER TABLE 
  articles 
ADD FULLTEXT idx_fulltext(
  title, summary, content, 'references', resources, sources, doi, author, tags, keywords
);