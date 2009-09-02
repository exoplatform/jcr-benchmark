java -Xmx800m -Duser.language=en -Duser.region=us -jar ../lib/exo.jcr.benchmark-trunk.jar ../config/JCRAPI.xml -last
java -cp ../lib/exo.jcr.benchmark-trunk.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper
rem java -cp ../lib/exo.jcr.benchmark-trunk.jar org.exoplatform.jcr.benchmark.helpers.AddNtFileWithMetadataNoJapex