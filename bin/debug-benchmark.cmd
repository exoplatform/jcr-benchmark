@SET JPDA_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,address=18000,server=y,suspend=y
@java -Xmx800m -Duser.language=en -Duser.region=us %JPDA_OPTS% -jar ../lib/exo.jcr.benchmark-trunk.jar ../config/JCRAPI.xml -last
@java -cp ../lib/exo.jcr.benchmark-trunk.jar org.exoplatform.jcr.benchmark.helpers.SimpleReportHelper

@rem java -cp ../lib/exo.jcr.benchmark-trunk.jar org.exoplatform.jcr.benchmark.helpers.AddNtFileWithMetadataNoJapex