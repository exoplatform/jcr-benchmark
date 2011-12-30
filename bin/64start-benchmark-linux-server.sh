cp -f ../config/test-configuration-benchmark-ispn.xml ../config/test-configuration-benchmark.xml
cp -f ../config/test-jcr-config-benchmark-ispn.xml ../config/test-jcr-config-benchmark.xml
/usr/lib/java6_x64/jre/bin/java -Xmx12g -Duser.language=en -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.15.0-Beta01-SNAPSHOT.jar ../config/JCRAPI.xml -last -line
/usr/lib/java6_x64/jre/bin/java -cp ../lib/exo-jcr-benchmark-1.15.0-Beta01-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
