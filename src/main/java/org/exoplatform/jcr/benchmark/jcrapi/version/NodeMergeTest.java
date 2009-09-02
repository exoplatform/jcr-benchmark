/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.version;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractGetItemTest;
import org.exoplatform.services.jcr.impl.core.JCRPath;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: NodeMergeTest.java 13537 2008-04-22 08:22:36Z vetalok $
 */

public class NodeMergeTest
   extends AbstractGetItemTest
{

   private String workspaceName = "system";

   private Session sysSession;

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      Node vnode = parent.addNode(context.generateUniqueName("versionableNode"));
      vnode.addMixin("mix:versionable");
      parent.save();
      vnode.checkin();// v.1
      vnode.checkout();

      // create corr node in system ws with own content
      sysSession = context.getSession().getRepository().login(context.getCredentials(), workspaceName);

      if (!sysSession.itemExists(vnode.getParent().getPath()))
      {
         // create parent, no support for SNS!!!
         Node ctxNode = sysSession.getRootNode();
         for (String nname : vnode.getParent().getPath().split("/"))
         {
            try
            {
               if (nname.equals("")) nname = JCRPath.THIS_RELPATH;
               ctxNode = ctxNode.getNode(nname);
            }
            catch (PathNotFoundException e)
            {
               ctxNode = ctxNode.addNode(nname);
               sysSession.save();
            }
         }
      }

      sysSession.getWorkspace().clone(context.getSession().getWorkspace().getName(), vnode.getPath(), vnode.getPath(),
               false);
      Node sysVNode = sysSession.getNodeByUUID(vnode.getUUID());

      vnode.addNode("Subnode").setProperty("Property", "property of subnode");
      vnode.save();

      vnode.checkin();// v.2

      vnode.checkout();
      vnode.addNode("Another subnode").setProperty("Property", "property of another subnode");
      vnode.save();
      vnode.checkin();// v.3
      vnode.checkout();

      addNode(vnode);

      // restore to a v.3 from test workspace
      sysVNode.restore("3", true);
      sysVNode.checkout();
      // change content
      sysVNode.getNode("Another subnode").remove();
      sysVNode.addNode("Sys Subnode").setProperty("Sys Property", "property of system subnode");
      sysVNode.save();
      sysVNode.checkin();// v.4
      sysVNode.checkout();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      nextNode().merge(workspaceName, true);// merge with system ws
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);

      try
      {
         sysSession.getRootNode().getNode(rootNodeName).remove();
         sysSession.save();
      }
      catch (RepositoryException e)
      {
         e.printStackTrace();
      }
   }

}
