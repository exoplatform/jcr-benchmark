java -Xmx12g -Djava.net.preferIPv4Stack=true -Dlog4j.configuration=file:../config/log4j.properties -Duser.language=en -Dexo.jcr.allow.closed.session.usage=true -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.15.8-GA-SNAPSHOT.jar ../config/JCRAPI.xml -last -line
java -cp ../lib/exo-jcr-benchmark-1.15.8-GA-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
