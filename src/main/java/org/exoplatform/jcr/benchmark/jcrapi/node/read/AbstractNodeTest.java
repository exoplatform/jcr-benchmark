/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.node.read;

import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: AbstractNodeTest.java 12446 2008-03-28 08:28:14Z pnedonosko $
 */

public abstract class AbstractNodeTest
   extends JCRTestBase
{

   protected Node root;

   protected String nodeName;

   protected String propertyName;

   protected Node node;

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);

      node.remove();
      context.getSession().save();
   }

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      root = context.getSession().getRootNode().addNode(context.generateUniqueName("testRoot"));

      node = root.addNode(nodeName = context.generateUniqueName("testNode"));
      node.addMixin("mix:referenceable");
      node.setProperty(propertyName = context.generateUniqueName("property"), "content");
      context.getSession().save();

      for (int i = 0; i < 10; i++)
      {
         node.addNode(context.generateUniqueName("node"));
      }

      for (int i = 0; i < 10; i++)
      {
         node.setProperty(context.generateUniqueName("property"), node);
      }

      root.save();
   }

}
