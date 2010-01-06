/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.node.read;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id$
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

      initChildNodes(context);

      initChildProperties(context);
      
      root.save();
   }
   
   protected void initChildNodes(JCRTestContext context) throws RepositoryException {
      addChildNodes(context, 10);
   }
   
   protected void initChildProperties(JCRTestContext context) throws RepositoryException {
      addChildProperties(context, 10);
   }
   
   protected void addChildNodes(JCRTestContext context, int amount) throws RepositoryException {
      for (int i = 0; i < amount; i++)
      {
         node.addNode(context.generateUniqueName("node"));
      }
   }
   
   protected void addChildProperties(JCRTestContext context,int amount) throws RepositoryException {
      for (int i = 0; i < amount; i++)
      {
         node.setProperty(context.generateUniqueName("property"), node);
      }
   }
}
