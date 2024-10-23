
To enable full text-search in MySQL create 2 text indexes:

ALTER TABLE 
  articles 
ADD FULLTEXT idx_fulltext(
  title, summary, content, `references`, resources, sources, doi, author, tags, keywords
);

ALTER TABLE 
  articles 
ADD FULLTEXT idx_fulltext_title(
  title
);

ALTER TABLE 
  articles 
ADD FULLTEXT idx_fulltext_visible_fields_only (
  title, content, `references`, resources, sources, doi, author
);
