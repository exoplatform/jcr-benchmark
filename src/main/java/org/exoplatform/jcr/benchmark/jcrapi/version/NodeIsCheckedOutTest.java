/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.version;

import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractGetItemTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: IsCheckedOutTest.java 12320 2008-03-24 16:32:41Z pnedonosko $
 */

public class NodeIsCheckedOutTest
   extends AbstractGetItemTest
{

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      Node vnode = parent.addNode(context.generateUniqueName("versionableNode"));
      vnode.addMixin("mix:versionable");
      context.getSession().save();
      vnode.checkin();
      vnode.checkout();
      vnode.addNode("Subnode").setProperty("Property", "property of subnode");
      vnode.save();

      addNode(vnode);
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      nextNode().isCheckedOut();
   }

}
