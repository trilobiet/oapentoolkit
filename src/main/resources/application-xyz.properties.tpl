
# For production call: mvn clean package -Pprod

# -------- generic
spring.profiles.default=dev|prod
spring.profiles.active=@spring.profiles.active@
application.version=@project.version@

spring.cache.jcache.config=classpath:ehcache.xml

url_feed_hypotheses=https://oapen.hypotheses.org/feed

# --------dev or prod

server.port=8080

url_strapi=https://strapi.xyz.com/graphql
google_analytics_id=UA-????
# categories comma separated
blog_categories=

dbcon=jdbc:mysql://strapi.xyz.com:3306/dbname?user=username&password=userpassword

logging.file.name=${user.home}/websites/logs/oabooks-toolkit.log
logging.level.root=INFO
logging.level.com.trilobiet.oapen.oapentoolkit=INFO
logging.logback.rollingpolicy.max-history=40