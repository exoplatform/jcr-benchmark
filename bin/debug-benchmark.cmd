@SET JPDA_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,address=18000,server=y,suspend=y
@java -Xmx800m -Duser.language=en -Dlog4j.configuration=file:../config/log4j.properties -Duser.region=us -Dexo.jcr.allow.closed.session.usage=true %JPDA_OPTS% -jar ../lib/exo-jcr-benchmark-1.15.0-CR1-SNAPSHOT.jar ../config/JCRAPI.xml -last
@java -cp ../lib/exo-jcr-benchmark-1.15.0-CR1-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
