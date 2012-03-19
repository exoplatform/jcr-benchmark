java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044  -Xmx1500m -Duser.language=en -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.12.13_GA-SNAPSHOT.jar ../config/JCRAPI.xml -last
#/usr/lib/java64/jre/bin/java -Xmx15000m -Duser.language=en -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.12.13-GA-SNAPSHOT.jar ../config/JCRAPI-usecases.xml -last
#/usr/lib/java64/jre/bin/java -cp ../lib/exo-jcr-benchmark-1.12.13-GA-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
#/usr/lib/java64/jre/bin/java -cp ../lib/exo-jcr-benchmark-1.12.13-GA-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.QueryNodeLoader
