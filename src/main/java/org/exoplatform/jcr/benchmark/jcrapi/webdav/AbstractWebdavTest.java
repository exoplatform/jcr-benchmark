/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.jcrapi.webdav;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

import com.sun.japex.TestCase;

import org.exoplatform.common.http.client.CookieModule;
import org.exoplatform.jcr.benchmark.JCRTestContext;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public abstract class AbstractWebdavTest
{

   protected JCRWebdavConnection item;
   
   protected String rootNodeName;
   
   private List<String> nodesPath = new ArrayList<String>();
   
   private volatile int iteration = 0;

   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doPrepare(TestCase tc, WebdavTestContext context) throws Exception
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
      
      CookieModule.setCookiePolicyHandler(null);
      item = new JCRWebdavConnection(context);
      
      
      rootNodeName = context.generateUniqueName("rootNode");
      item.addNode(rootNodeName, "".getBytes());
      
      for (int i = 0; i < runIterations; i++)
      {
         String parentNodeName = rootNodeName + "/" + context.generateUniqueName("node");
         item.addNode(parentNodeName, "".getBytes());

         createContent(parentNodeName, tc, context);
      }
   }
   
   /**
    * Create content to test.
    * 
    * @param parentNodeName
    * @param tc
    * @param context
    * @throws Exception
    */
   protected abstract void createContent(String parentNodeName, TestCase tc, WebdavTestContext context) throws Exception;
   
   /**
    * @param nodePath
    */
   protected void addNode(String nodePath)
   {
      nodesPath.add(nodePath);
   }
   
   /**
    * @return
    */
   protected String nextNodePath()
   {
      return nodesPath.get(iteration++);
   }
   
   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doFinish(TestCase tc, WebdavTestContext context) throws Exception
   {
      item.removeNode(rootNodeName);
      item.stop();
   }
   
   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public abstract void doRun(final TestCase tc, WebdavTestContext context) throws Exception;
}
