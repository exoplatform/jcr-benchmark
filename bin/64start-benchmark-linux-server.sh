$JAVA_HOME/bin/java -Xmx12g -Djava.net.preferIPv4Stack=true -Dlog4j.configuration=file:../config/log4j.properties -Duser.language=en -Dexo.jcr.allow.closed.session.usage=true -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.16.x-SNAPSHOT.jar ../config/JCRAPI.xml -last -line
$JAVA_HOME/bin/java -cp ../lib/exo-jcr-benchmark-1.16.x-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
