/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.node.write;

import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractAddItemTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class NodeSetNodePropertyTest
   extends AbstractAddItemTest
{

   private Node parent2 = null;

   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      if (!(parent.isSame(parent2)))
      {
         parent.addMixin("mix:referenceable");
         context.getSession().save();
         parent2 = parent;
      }
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      Node node = nextParent();
      node.setProperty(context.generateUniqueName("property"), node);
   }

}
