cp -f ../config/test-configuration-benchmark-jbc.xml ../config/test-configuration-benchmark.xml
cp -f ../config/test-jcr-config-benchmark-jbc.xml ../config/test-jcr-config-benchmark.xml
/usr/lib/java6_x64/jre/bin/java -Xmx12g -Duser.language=en -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.14.2-GA-SNAPSHOT.jar ../config/JCRAPI.xml -last -line
/usr/lib/java6_x64/jre/bin/java -cp ../lib/exo-jcr-benchmark-1.14.2-GA-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
#/usr/lib/java6_x64/jre/bin/java -cp ../lib/exo-jcr-benchmark-1.14.2-GA-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.QueryNodeLoader