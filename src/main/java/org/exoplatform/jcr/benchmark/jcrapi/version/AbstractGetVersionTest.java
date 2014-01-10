/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.version;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: AbstractGetVersionTest.java 13537 2008-04-22 08:22:36Z vetalok $
 */

public abstract class AbstractGetVersionTest
   extends JCRTestBase
{

   protected Node root;

   protected Node vnode;

   protected VersionHistory vhistory;

   protected Version version;

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);

      try
      {
         context.getSession().getNodeByUUID(vhistory.getVersionableUUID()).checkout();
      }
      catch (RepositoryException e)//NOSONAR
      {
         // skip it
      }

      vnode.remove();
      context.getSession().save();
   }

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      root = context.getSession().getRootNode();

      vnode = root.addNode(context.generateUniqueName("versionableNode"));
      vnode.addMixin("mix:versionable");
      root.save();

      version = vnode.checkin(); // v.1
      vnode.checkout();

      vhistory = vnode.getVersionHistory();
      vhistory.addVersionLabel("1", "v.1", false);
   }

}
