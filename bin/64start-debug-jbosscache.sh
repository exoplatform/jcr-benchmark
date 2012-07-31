cp -f ../config/test-jcr-config-benchmark-jbc.xml ../config/test-jcr-config-benchmark.xml
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044  -Xmx1500m -Duser.language=en -Dexo.jcr.allow.closed.session.usage=true -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.15.0-Beta01-SNAPSHOT.jar ../config/JCRAPI.xml -last
