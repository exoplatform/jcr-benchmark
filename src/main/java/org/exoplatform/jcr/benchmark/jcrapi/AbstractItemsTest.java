/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.jcr.benchmark.jcrapi;

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: AbstractItemsTest.java 13537 2008-04-22 08:22:36Z vetalok $
 */

public abstract class AbstractItemsTest
   extends JCRTestBase
{

   protected Node rootNode = null;

   protected String rootNodeName = "";

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
     
      Session session = context.getSession();
      rootNodeName = context.generateUniqueName("rootNode");
      rootNode = session.getRootNode().addNode(rootNodeName);
      session.save();

      Node parent = null;

      for (int i = 0; i < runIterations; i++)
      {
         if (i % 100 == 0)
         {
            // add 100 props and commit,
            parent = rootNode.addNode(context.generateUniqueName("node"));
            rootNode.save();
         }
         createContent(parent, tc, context);
      }
      session.save();
   }

   protected abstract void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception;

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      rootNode.refresh(false);

      for (NodeIterator nodes = rootNode.getNodes(); nodes.hasNext();)
      {
         nodes.nextNode().remove();
         rootNode.save();
      }

      rootNode.remove();
      context.getSession().save();
   }

}
