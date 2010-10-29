java -Xmx800m -Duser.language=en -Duser.region=us -jar ../lib/exo-jcr-benchmark-1.12.5.jar ../config/JCRAPI.xml -last
java -cp ../lib/exo-jcr-benchmark-1.12.5.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
rem java -cp ../lib/exo-jcr-benchmark-1.12.5.jar org.exoplatform.jcr.benchmark.helpers.AddNtFileWithMetadataNoJapex
