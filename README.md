# Oapen Toolkit

Oapen Toolkit website uses:

- Spring MVC 4 https://spring.io
- Thymeleaf https://www.thymeleaf.org/
- ~~Rome tools https://github.com/rometools/rome~~
- Flexmark https://github.com/vsch/flexmark-java

And the following home brewn repositories:

- GraphQL Java client https://github.com/trilobiet/graphqlweb
- Strapi CMS API configuration for a simple website https://github.com/trilobiet/strapi-simple-website

Toolkit uses TKArticle that inherits ArticleImp and adds a few more fields.

## Version 2 2024

- New logo / layout / identity;
- RSS support has been discontinued; 
- Added service to translate TKArticles using LibreTranslate (https://libretranslate.com/)   
  Using a subscription sets a 2000 character limit on translations, which in the context of this project
  is a no-go, so a local LibreTranslate installation is mandatory.   
  See `dev/libre-translate` for instructions on how to run a LibreTranslate server through Docker Compose;
- A Python script is provided in `dev/pdfprinter` that takes a list of urls and outputs the corresponding
  web pages in a single pdf document. It can be used to wrap the entire site in a single downloadable document.
  