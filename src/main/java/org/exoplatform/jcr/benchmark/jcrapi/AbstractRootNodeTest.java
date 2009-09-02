/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi;

import javax.jcr.Item;
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

public abstract class AbstractRootNodeTest
   extends JCRTestBase
{

   protected Node node;

   protected Item item;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      node = context.getSession().getRootNode().addNode(context.generateUniqueName("testNode"));
      item = node;
      context.getSession().save();
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);

      node.remove();
      context.getSession().save();
   }

}
