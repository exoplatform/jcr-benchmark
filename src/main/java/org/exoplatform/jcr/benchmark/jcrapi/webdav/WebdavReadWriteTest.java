/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.exoplatform.common.http.client.CookieModule;
import org.exoplatform.common.http.client.HTTPResponse;
import org.exoplatform.common.http.client.ModuleException;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 2010
 *
 * @author <a href="mailto:alex.reshetnyak@exoplatform.com.ua">Alex Reshetnyak</a> 
 * @version $Id$
 */
public class WebdavReadWriteTest
{
   protected JCRWebdavConnectionEx item;

   protected String rootNodeName;

   private List<String> nodesPath = new ArrayList<String>();

   private class NodesWrited
      extends Thread
   {
      private final String nodePath;

      private int count;

      private final int writeDelay;

      private JCRWebdavConnectionEx connection;

      public NodesWrited(String threadName, String nodePath, int writeDelay, JCRWebdavConnectionEx conn)
      {
         super(threadName);
         this.nodePath = nodePath;
         this.count = 100;
         this.writeDelay = writeDelay;
         this.connection = conn;
      }

      /**
       * {@inheritDoc}
       */
      public void run()
      {
         while (true)
         {
            String subNodePath = null;

            if (count == 100)
            {
               String subFolder = this.getName() + "_" + System.currentTimeMillis();
               subNodePath = nodePath + "/" + subFolder;

               try
               {
                  connection.addDir(subNodePath);
                  count = 0;
               }
               catch (Exception e)
               {
                  System.out.println(this.getName() + " : Can not add node with path : " + subNodePath);
                  e.printStackTrace();
                  continue;
               }
            }

            String path = subNodePath + "/" + "node" + count;
            HTTPResponse response = null;

            try
            {
               response = connection.addNode(path, ("__the_data_in_nt+file__" + count).getBytes());
               
               if (response.getStatusCode() != 200)
                  System.out.println("Can not get (response code " + response.getStatusCode() + " ) node with path : "
                           + nodePath);
            }
            catch (Exception e)
            {
               System.out.println(this.getName() + " : Can not add node with path : " + path);
               e.printStackTrace();
               continue;
            }

            if (writeDelay > 0)
            {
               try
               {
                  Thread.sleep(writeDelay);
               }
               catch (InterruptedException e)
               {
                  e.printStackTrace();
               }
            }
         }
      }
   }

   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doPrepare(TestCase tc, WebdavTestContext context) throws Exception
   {
      int nodesPoolSizeToRead = tc.getIntParam("nodesPoolSize");

      int writeThreadsCount = tc.getIntParam("writeThreads");

      int delayWrite = tc.getIntParam("delayWriteInSeconds");

      item = new JCRWebdavConnectionEx(context);

      rootNodeName = context.generateUniqueName("rootNode");
      item.addDir(rootNodeName);

      // prepare to read
      int foldersCount = nodesPoolSizeToRead % 100;
      for (int i = 0; i < foldersCount; i++)
      {
         String parentNodeName = rootNodeName + "/" + context.generateUniqueName("node");
         item.addDir(parentNodeName);

         int subFoldersCount = nodesPoolSizeToRead / foldersCount;

         for (int j = 0; j < subFoldersCount; j++)
         {
            String subFolder = parentNodeName + "/" + context.generateUniqueName("subNode");
            item.addNode(subFolder, ("__the_data_in_nt+file__" + i + "_" + j).getBytes());
            nodesPath.add(subFolder);
         }
      }

      // repare to write

   }

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
      return nodesPath.get((int) (Math.random() * Integer.MAX_VALUE) % nodesPath.size());
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
   public void doRun(final TestCase tc, WebdavTestContext context) throws Exception
   {
      String nodePath = nextNodePath();
      HTTPResponse response = item.getNode(nodePath);

      if (response.getStatusCode() != 200)
         System.out.println("Can not get (response code " + response.getStatusCode() + " ) node with path : "
                  + nodePath);
   }

}
