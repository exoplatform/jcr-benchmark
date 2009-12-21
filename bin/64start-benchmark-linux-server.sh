java -Xmx500m -Duser.language=en -Duser.region=us -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y -jar ../lib/exo-jcr-benchmark-1.12.0-JBC-SNAPSHOT.jar ../config/JCRAPI-webdav.xml -last -line
java -cp ../lib/exo-jcr-benchmark-1.12.0-JBC-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
#/usr/lib/java64/jre/bin/java -cp ../lib/exo-jcr-benchmark-1.12.0-JBC-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.QueryNodeLoader
