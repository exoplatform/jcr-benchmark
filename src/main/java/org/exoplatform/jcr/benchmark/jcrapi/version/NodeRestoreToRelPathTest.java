/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.version;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.version.Version;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractGetItemTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: RestoreTest.java 12320 2008-03-24 16:32:41Z pnedonosko $
 */

public class NodeRestoreToRelPathTest
   extends AbstractGetItemTest
{

   private List<Version> versions = new ArrayList<Version>();

   private List<String> names = new ArrayList<String>();

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      Node vnode = parent.addNode(context.generateUniqueName("versionableNode"));
      vnode.addMixin("mix:versionable");
      parent.save();

      vnode.checkin();// v.1
      vnode.checkout();
      vnode.setProperty("Property", "property of subnode");
      vnode.save();

      versions.add(vnode.checkin());// v.2

      vnode.checkout();
      vnode.setProperty("Property", "property of subnode, v.3");
      vnode.save();
      vnode.checkin();// v.3
      vnode.checkout();

      addNode(vnode);

      // gen a relPath for a restore
      Node rnode = parent.addNode(context.generateUniqueName("restoredNode"));
      parent.save();

      names.add("../" + rnode.getName() + "/" + vnode.getName() + "_restored");
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      final int iter = getCurrentIteration();
      final String relPath = names.get(iter);
      final Node n = nextNode();
      // log.info(n.getPath() + " " + n.getUUID() + " vh:" + n.getVersionHistory().getPath() + " v:" +
      // versions.get(iter).getPath() + " " +
      // versions.get(iter).getContainingHistory().getVersionableUUID());
      // restore v.2 to ../restoredNode-xxx/versionableNode-xxx_restored
      n.restore(versions.get(iter), relPath, true);
   }

}
