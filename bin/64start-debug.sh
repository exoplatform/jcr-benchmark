java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044  -Xmx1500m -Djava.net.preferIPv4Stack=true -Dlog4j.configuration=file:../config/log4j.properties -Duser.language=en -Duser.region=us -Dexo.jcr.allow.closed.session.usage=true -jar ../lib/exo-jcr-benchmark-1.16.0-Alpha3-SNAPSHOT.jar ../config/JCRAPI.xml -last
