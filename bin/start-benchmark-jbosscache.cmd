copy /y ..\config\test-configuration-benchmark-ispn.xml ..\config\test-configuration-benchmark.xml
copy /y ..\config\test-jcr-config-benchmark-ispn.xml ..\config\test-jcr-config-benchmark.xml
java -Xmx800m -Duser.language=en -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.14.0-GA-SNAPSHOT.jar ../config/JCRAPI.xml -last
java -cp ../lib/exo-jcr-benchmark-1.14.0-GA-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
rem java -cp ../lib/exo-jcr-benchmark-1.14.0-GA-SNAPSHOT.jar org.exoplatform.jcr.benchmark.helpers.AddNtFileWithMetadataNoJapex
