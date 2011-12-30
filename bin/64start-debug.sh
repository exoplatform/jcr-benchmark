cp -f ../config/test-configuration-benchmark-ispn.xml ../config/test-configuration-benchmark.xml
cp -f ../config/test-jcr-config-benchmark-ispn.xml ../config/test-jcr-config-benchmark.xml
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044  -Xmx1500m -Duser.language=en -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.15.0-Beta01-SNAPSHOT.jar ../config/JCRAPI.xml -last
