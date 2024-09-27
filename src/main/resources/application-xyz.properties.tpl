
# For production call: mvn clean package -Pprod
spring.profiles.default=dev|prod
spring.profiles.active=@spring.profiles.active@
application.version=@project.version@

spring.cache.jcache.config=classpath:ehcache.xml

url_feed_hypotheses=https://oapen.hypotheses.org/feed

server.port=8081

url_strapi=https://strapi.xyz.com/graphql
google_analytics_id=UA-????
# categories comma separated
blog_categories=

url_mysql_search=jdbc:mysql://localhost:3306/strapi36_oatoolkit?user=sitesearch&password=Whateveryouwant
url_translate_service=http://localhost:5000

logging.file.name=${user.home}/websites/logs/oabooks-toolkit.log
logging.level.root=INFO
logging.level.com.trilobiet.oapen.oapentoolkit=INFO
logging.logback.rollingpolicy.max-history=40

