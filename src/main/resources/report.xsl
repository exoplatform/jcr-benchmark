<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:rep="http://www.sun.com/japex/testSuiteReport"
    xmlns:suite="http://www.sun.com/japex/testSuite"    
    xmlns:extrep="http://www.sun.com/japex/extendedTestSuiteReport"
    exclude-result-prefixes="xi" version='1.0' >

    <xsl:output method="xml" indent="yes"/>
     
    <xsl:template match="rep:testSuiteReport">
        <html xmlns="http://www.w3.org/1999/xhtml">
            <link href="report.css" type="text/css" rel="stylesheet"/>

            <body>
                <table border="0" cellpadding="2">
                    <tr>
                        <td valign="middle" width="90"><p>
                            <a href="https://japex.dev.java.net">
                                <img src="small_japex.gif" align="middle" border="0"/>
                            </a>
                        </p></td>
                        <td valign="middle"><h1>Japex Report: <xsl:value-of select="@name"/></h1></td>  
                    </tr>
                </table>
                
                <!-- Generate description in its own section -->
                <xsl:if test="suite:description">
                    <h2>Description</h2>
                    <div class="description">
                        <xsl:copy-of select="suite:description/*"/>
                    </div>
                </xsl:if>
   
                <h2>Global Parameters</h2>
                <ul>
                    <!-- japex.configFile -->
                    <li><a href="test-configuration-benchmark.xml">test-configuration-benchmark.xml</a></li>
                    <li><a href="test-jcr-config-benchmark.xml">test-jcr-config-benchmark.xml</a></li>
                    <li><xsl:text>configFile: </xsl:text>
                    <a href="{rep:configFile}"><xsl:value-of select="rep:configFile"/></a>
                    </li>
                    <!-- Use not(@name) to omit sibling drivers -->
                    <xsl:for-each select="*[not(@name) and local-name() != 'configFile' and namespace-uri() = 'http://www.sun.com/japex/testSuiteReport']">
                        <li><xsl:value-of select="name()"/>
                        <xsl:text>: </xsl:text>
                        <xsl:value-of select="."/></li>
                    </xsl:for-each>
                </ul>

								<h2>Results</h2>

                <!-- Generate detailed result per driver -->
                <!--<xsl:for-each select="rep:driver">-->
                    <xsl:call-template name="resultsPerDriver"/>
                <!--</xsl:for-each>-->

                <xsl:choose>
                    <xsl:when test="rep:plotDrivers = 'true'">
                        <h2>Results Per Driver</h2>
                    </xsl:when>
                    <xsl:otherwise>
                        <h2>Results Per Test</h2>
                    </xsl:otherwise>
                </xsl:choose>      
                <br/>
                <xsl:for-each select="/*/extrep:testCaseChart">
                    <center><img src="{.}"/></center>
                    <br/><br/>
                </xsl:for-each>
      
                <br/>
                <small>
                    <hr/>
                    <font size="-2">
                        Generated using <a href="https://japex.dev.java.net">Japex</a> version 
                        <xsl:value-of select="rep:version"/>
                    </font>
                </small>
            </body>   
        </html>
    </xsl:template>

    <xsl:template name="resultsPerDriver">
    <xsl:for-each select="rep:driver[1]">
        <h2>Driver: <xsl:value-of select="@name"/></h2>
        <h3>(the difference between drivers is number of threads only)</h3>
        <!-- Generate description before list of params -->
        <xsl:if test="suite:description">
            <div class="description">
                <xsl:copy-of select="suite:description/*"/>
            </div>
        </xsl:if>        
    
        <!-- List classPath, driverClass and any user-define parameter here -->
        <p>
            <ul>
                <xsl:for-each select="rep:driverClass | rep:classPath | *[not(@name) and namespace-uri(.)='']">
                    <li><xsl:value-of select="name()"/>
                    <xsl:text>: </xsl:text>
                    <xsl:value-of select="."/></li>
                </xsl:for-each>
            </ul>
        </p>
     </xsl:for-each> 
     <table width="80%" border="1">
     <xsl:for-each select="rep:driver[1]">
            <thead>
                <tr><th><b>driver</b></th>
                    <th><b>nOfThreads</b></th>
                    <th><b>testCase</b></th>
                    <xsl:for-each select="rep:testCase[1]/*[namespace-uri(.)!='' and name()='resultValue']">
                        <th><b><xsl:value-of select="name()"/></b></th>
                    </xsl:for-each>
                    <xsl:for-each select="rep:testCase[1]/*[namespace-uri(.)!='' and name()!='resultValue']">
                        <th><b><xsl:value-of select="name()"/></b></th>
                    </xsl:for-each>
                    <xsl:for-each select="rep:testCase[1]/*[namespace-uri(.)='']">
                        <th><b><xsl:value-of select="name()"/></b></th>
                    </xsl:for-each>
                    
                </tr>
            </thead>     
     </xsl:for-each>
     <xsl:for-each select="rep:driver">
        <!--
        - Use an HTML table to list all test case params, regardless of whether they
        - are user-defined or not. List all Japex params first, though.
        -->
        
		   <xsl:variable name="outerCurrent" select="current()" />
            <tbody>
                <xsl:for-each select="rep:testCase">
                <tr>
                  <td><b><xsl:value-of select="$outerCurrent/@name"/></b></td>                        
                  <td>
                    <xsl:for-each select="$outerCurrent/rep:numberOfThreads">
                      <xsl:value-of select="."/>
                    </xsl:for-each>
                  </td>          
                   <td align="right">
                        <xsl:value-of select="@name"/></td>     
                        <xsl:for-each select="*[namespace-uri(.)!='' and name()='resultValue']">
                            <td align="right"><nobr>                          
                                <xsl:variable name="value" select="."/>
                                <xsl:choose>
                                    <xsl:when test="$value = 'NaN'">
                                        <font color="red"><xsl:value-of select="$value"/></font>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$value"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </nobr></td>
                        </xsl:for-each>
                        <xsl:for-each select="*[namespace-uri(.)!='' and name()!='resultValue']">
                            <td align="right"><nobr>                          
                                <xsl:variable name="value" select="."/>
                                <xsl:choose>
                                    <xsl:when test="$value = 'NaN'">
                                        <font color="red"><xsl:value-of select="$value"/></font>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$value"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </nobr></td>
                        </xsl:for-each>
                        <xsl:for-each select="*[namespace-uri(.)='']">
                            <td align="left"><nobr><xsl:value-of select="."/></nobr></td>
                        </xsl:for-each>                    
                </tr>
                </xsl:for-each>
            </tbody>
    </xsl:for-each>    
    </table>    
    <br/>
    </xsl:template>

    <xsl:template match="text()"/>

</xsl:stylesheet>
