/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.InvalidItemStateException;
import javax.jcr.Node;
import javax.jcr.Session;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 * 
 * @version $Id: AbstractAddItemTest.java 13537 2008-04-22 08:22:36Z vetalok $
 */

public abstract class AbstractAddItemTest
   extends JCRTestBase
{

   private List<Node> parents = new ArrayList<Node>();

   private volatile int iteration = 0;

   protected Node rootNode = null;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      Session session = context.getSession();
      rootNode = session.getRootNode().addNode(context.generateUniqueName("rootNode"));
      session.save();

      int runIterations = tc.getIntParam("japex.runIterations");

      if (tc.hasParam("japex.warmupIterations"))
      {
         int warmUpIterations = tc.getIntParam("japex.warmupIterations");
         if (warmUpIterations>0) 
         {
            runIterations += warmUpIterations;
         }
      }

      Node parent = null;

      for (int i = 0; i < runIterations; i++)
      {
         if (i % 100 == 0)
         {
            // each parent will has no more 100 child nodes
            parent = rootNode.addNode(context.generateUniqueName("parentNode"));
            rootNode.save();
         }
         addParent(parent);
         // create additional content of the parent node
         createContent(parent, tc, context);
      }

   }

   protected abstract void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception;

   protected Node nextParent()
   {
      return parents.get(iteration++);
   }

   protected void addParent(Node parent)
   {
      parents.add(parent);
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      rootNode.refresh(false);

      for (Node parent : parents)
      {
         try
         {
            parent.remove();
         }
         catch (InvalidItemStateException e)
         {
            // was deleted or discarded
         }
         rootNode.save();
      }

      rootNode.remove();
      context.getSession().save();

      parents.clear();
      parents = null;
   }

}
