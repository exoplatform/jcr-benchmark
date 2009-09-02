/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.version;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: VersionHistoryHasVersionLabelTest.java 13537 2008-04-22 08:22:36Z vetalok $
 */

public class VersionHistoryHasVersionLabelTest
   extends AbstractGetVersionTest
{

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      vhistory.hasVersionLabel("v.1");
   }

}
