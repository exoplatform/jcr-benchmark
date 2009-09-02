/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.item.write;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public abstract class AbstractGetNoSavedItemsTest
   extends JCRTestBase
{

   private List<Node> parents = new ArrayList<Node>();

   private List<Node> childs = new ArrayList<Node>();

   private Node testRoot = null;

   private volatile int iteration = 0;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      int runIterations = tc.getIntParam("japex.runIterations");
  
      if (tc.hasParam("japex.warmupIterations"))
      {
         int warmUpIterations = tc.getIntParam("japex.warmupIterations");
         if (warmUpIterations>0) 
         {
            runIterations += warmUpIterations;
         }
      }
      // 1. Create root node for this test using current session
      testRoot = context.getSession().getRootNode().addNode(context.generateUniqueName("testRoot"));
      context.getSession().save();
      // 2. Create saved parents
      for (int i = 0; i < runIterations; i++)
      {
         Node parent = testRoot.addNode(context.generateUniqueName("parent"));
         context.getSession().save();
         parents.add(parent);
      }
      // 3. Create unsaved childs
      for (int i = 0; i < runIterations; i++)
      {
         Node parent = (parents.get(i));
         Node child = parent.addNode(context.generateUniqueName("child"));
         child.setProperty(context.generateUniqueName("property"), "value");
         childs.add(child);
      }
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      testRoot.remove();
      context.getSession().save();
   }

   protected Node nextParent()
   {
      return parents.get(iteration++);
   }
}
