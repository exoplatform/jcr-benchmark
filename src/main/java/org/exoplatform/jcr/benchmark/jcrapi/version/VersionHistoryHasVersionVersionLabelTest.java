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
 * @version $Id: VersionHistoryHasVersionVersionLabelTest.java 13537 2008-04-22 08:22:36Z vetalok $
 */

public class VersionHistoryHasVersionVersionLabelTest
   extends AbstractGetVersionTest
{

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      vnode.checkout();
      vnode.addNode("Subnode").setProperty("Property", "property of subnode");
      vnode.save();

      version = vnode.checkin(); // v.2
      vnode.checkout();

      vhistory.addVersionLabel("2", "v.2", false);
      vhistory.addVersionLabel("2", "ver.2", false);
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      vhistory.hasVersionLabel(version, "ver.2");
   }

}
