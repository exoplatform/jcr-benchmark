cp -f ../config/test-configuration-benchmark-ispn.xml ../config/test-configuration-benchmark.xml
cp -f ../config/test-jcr-config-benchmark-ispn.xml ../config/test-jcr-config-benchmark.xml
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044  -Xmx1500m -Duser.language=en -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.14.4-GA-SNAPSHOT.jar ../config/JCRAPI.xml -last
#/usr/lib/java64/jre/bin/java -Xmx15000m -Duser.language=en -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.14.4-GA-SNAPSHOT.jar ../config/JCRAPI-usecases.xml -last
#/usr/lib/java64/jre/bin/java -cp ../lib/exo-jcr-benchmark-1.14.4-GA-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
#/usr/lib/java64/jre/bin/java -cp ../lib/exo-jcr-benchmark-1.14.4-GA-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.QueryNodeLoader
