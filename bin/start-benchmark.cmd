java -Xmx800m -Duser.language=en -Duser.region=us -Dlog4j.configuration=file:../config/log4j.properties -Dexo.jcr.allow.closed.session.usage=true -jar ../lib/exo-jcr-benchmark-1.16.0-Alpha3-SNAPSHOT.jar ../config/JCRAPI.xml -last
java -cp ../lib/exo-jcr-benchmark-1.16.0-Alpha3-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
