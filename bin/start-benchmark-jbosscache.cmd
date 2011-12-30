copy /y ..\config\test-configuration-benchmark-jbc.xml ..\config\test-configuration-benchmark.xml
copy /y ..\config\test-jcr-config-benchmark-jbc.xml ..\config\test-jcr-config-benchmark.xml
java -Xmx800m -Duser.language=en -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.15.0-Beta01-SNAPSHOT.jar ../config/JCRAPI.xml -last
java -cp ../lib/exo-jcr-benchmark-1.15.0-Beta01-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
