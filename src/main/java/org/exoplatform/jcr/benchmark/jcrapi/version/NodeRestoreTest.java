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

public class NodeRestoreTest
   extends AbstractGetItemTest
{

   private List<Version> versions = new ArrayList<Version>();

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      Node vnode = parent.addNode(context.generateUniqueName("versionableNode"));
      vnode.addMixin("mix:versionable");
      parent.save();
      vnode.checkin();// v.1
      vnode.checkout();
      vnode.addNode("Subnode").setProperty("Property", "property of subnode");
      vnode.save();

      versions.add(vnode.checkin());// v.2

      vnode.checkout();
      vnode.addNode("Another subnode").setProperty("Property", "property of another subnode");
      vnode.save();
      vnode.checkin();// v.3
      vnode.checkout();

      addNode(vnode);
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      final Version v = versions.get(getCurrentIteration());
      nextNode().restore(v, true);// restore v.2
   }

}
