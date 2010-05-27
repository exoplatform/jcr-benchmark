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
package org.exoplatform.jcr.benchmark.jcrapi.workspace.write;

import javax.jcr.Node;
import javax.jcr.Session;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: AbstractItemsTest.java 12432 2008-03-27 15:25:10Z pnedonosko $
 */

public abstract class AbstractItemsInDifferentWorkspacesTest
   extends JCRTestBase
{

   protected Node ws1RootNode = null;

   protected Node ws2RootNode = null;
   
   protected Session ws1Session = null;
   
   protected Session ws2Session = null;

   protected static final String WS1 = "collaboration";

   protected static final String WS2 = "system";

   private String testRootNodeName = null;

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
      testRootNodeName = context.generateUniqueName("testRoot");
      // WS1
      ws1Session = context.getSession().getRepository().login(WS1);      
      ws2Session = context.getSession().getRepository().login(WS2);
      ws1RootNode = ws1Session.getRootNode().addNode(testRootNodeName);
      ws2RootNode = ws2Session.getRootNode().addNode(testRootNodeName);      
      ws1Session.save();
      ws2Session.save();
      Node ws1Parent = null;
      Node ws2Parent = null;
      for (int i = 0; i < runIterations; i++)
      {
         if (i % 100 == 0)
         {
            // add 100 props and commit,
            String testParentNodeName = context.generateUniqueName("testParent");
            ws1Parent = ws1RootNode.addNode(testParentNodeName);
            ws2Parent = ws2RootNode.addNode(testParentNodeName);
            ws1RootNode.save();
            ws2RootNode.save();
         }
         ws1CreateContent(ws1Parent, tc, context);
         ws2CreateContent(ws2Parent, tc, context);
      }
      ws1Session.save();
      ws2Session.save();
   }

   protected abstract void ws1CreateContent(Node parent, TestCase tc, JCRTestContext context) throws Exception;

   protected abstract void ws2CreateContent(Node parent, TestCase tc, JCRTestContext context) throws Exception;

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      ws1RootNode.remove();
      ws1RootNode.getSession().save();
      ws2RootNode.remove();
      ws2RootNode.getSession().save();
   }

}
