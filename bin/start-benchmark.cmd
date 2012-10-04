java -Xmx800m -Duser.language=en -Duser.region=us -Dlog4j.configuration=file:../config/log4j.properties -Dexo.jcr.allow.closed.session.usage=true -jar ../lib/exo-jcr-benchmark-1.15.0-CR2-SNAPSHOT.jar ../config/JCRAPI.xml -last
java -cp ../lib/exo-jcr-benchmark-1.15.0-CR2-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
