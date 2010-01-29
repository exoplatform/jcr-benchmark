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

import com.sun.japex.TestCase;

import org.exoplatform.common.http.client.HTTPResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 2010
 *
 * @author <a href="mailto:alex.reshetnyak@exoplatform.com.ua">Alex Reshetnyak</a> 
 * @version $Id$
 */
public class WebdavReadWriteTest extends AbstractWebdavTest
{
   protected int iterator = 0;

   protected String rootNodeName;

   private static List<String> nodesPath = new ArrayList<String>();

   private List<NodesWriter> nodesWriters = new ArrayList<NodesWriter>();

   private static volatile boolean writersStarted = false;

   private class NodesWriter extends Thread
   {
      protected boolean isRun = true;

      private final String nodePath;

      private int count;

      private final int writeDelay;

      //private JCRWebdavConnectionEx connection;

      private final WebdavTestContext context;

      public NodesWriter(String threadName, String nodePath, int writeDelay, WebdavTestContext context)
      {
         super(threadName);
         this.nodePath = nodePath;
         this.context = context;
         this.count = 100;
         this.writeDelay = writeDelay;

      }

      /**
       * {@inheritDoc}
       */
      public void run()
      {
         String subNodePath = null;

         int folderCount = 0;

         while (isRun)
         {
            JCRWebdavConnectionEx connection = new JCRWebdavConnectionEx(context);
            try
            {
               long time = System.currentTimeMillis();
               if (count == 100)
               {
                  String subFolder = "folder" + folderCount++;
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
               String path = subNodePath + "/" + "node" + count++;
               HTTPResponse response = null;
               try
               {
                  response = connection.addNode(path, ("__the_data_in_nt+file__" + count).getBytes());

                  if (response.getStatusCode() != 201)
                  {
                     System.out.println(this.getName() + " : Can not add (response code " + response.getStatusCode()
                        + new String(response.getData()) + " ) node with path : " + nodePath);
                  }

                  //               System.out.println(this.getName() + " : Add node : " + path);
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
            finally
            {
               connection.stop();
            }
         }
      }

      public void isRun(boolean b)
      {
         isRun = b;
      }
   }

   /**
    * {@inheritDoc}
    */
   public void doPrepare(TestCase tc, WebdavTestContext context) throws Exception
   {
      if (!tc.hasParam("nodesPoolSize"))
      {
         throw new RuntimeException("<nodesPoolSize> parameter required");
      }
      int nodesPoolSizeToRead = tc.getIntParam("nodesPoolSize");

      if (!tc.hasParam("writeThreads"))
      {
         throw new RuntimeException("<writeThreads> parameter required");
      }
      int writeThreadsCount = tc.getIntParam("writeThreads");

      if (!tc.hasParam("delayWrite"))
      {
         throw new RuntimeException("<delayWrite> parameter required");
      }
      int delayWrite = tc.getIntParam("delayWrite");

      JCRWebdavConnectionEx conn = new JCRWebdavConnectionEx(context);

      try
      {
         if (!writersStarted)
         {
            writersStarted = true;

            rootNodeName = context.generateUniqueName("rootNode");
            conn.addDir(rootNodeName);

            // prepare to read
            int foldersCount = nodesPoolSizeToRead / 100;
            for (int i = 0; i < foldersCount; i++)
            {
               String parentNodeName = rootNodeName + "/" + context.generateUniqueName("node");
               conn.addDir(parentNodeName);

               int subFoldersCount = nodesPoolSizeToRead / foldersCount;

               for (int j = 0; j < subFoldersCount; j++)
               {
                  String subFolder = parentNodeName + "/" + context.generateUniqueName("subNode");
                  conn.addNode(subFolder, ("__the_data_in_nt+file__" + i + "_" + j).getBytes());
                  nodesPath.add(subFolder);
               }
            }

            // prepare to threads writers 
            for (int j = 0; j < writeThreadsCount; j++)
            {
               String parentNodeName = rootNodeName + "/" + context.generateUniqueName("node_writers");
               conn.addDir(parentNodeName);

               NodesWriter writer = new NodesWriter("ThreadWriter_" + j, parentNodeName, delayWrite, context);
               writer.start();
               nodesWriters.add(writer);
            }
         }
      }
      finally
      {
         conn.stop();
      }

   }

   /**
    * {@inheritDoc}
    */
   protected void addNode(String nodePath)
   {
      nodesPath.add(nodePath);
   }

   /**
    * {@inheritDoc}
    */
   protected String nextNodePath()
   {
      if (iterator > nodesPath.size() - 1)
      {
         iterator = 0;
      }

      return nodesPath.get(iterator++);
   }

   /**
    * {@inheritDoc}
    */
   public void doFinish(TestCase tc, WebdavTestContext context) throws Exception
   {
      for (NodesWriter writer : nodesWriters)
      {
         writer.isRun(false);
      }

      Thread.sleep(10000);

      //conn.removeNode(rootNodeName);
      // conn.stop();
   }

   /**
    * {@inheritDoc}
    */
   public void doRun(final TestCase tc, WebdavTestContext context) throws Exception
   {
      String nodePath = nextNodePath();
      JCRWebdavConnectionEx conn = new JCRWebdavConnectionEx(context);
      try
      {
         HTTPResponse response = conn.getNode(nodePath);
         if (response.getStatusCode() != 200)
         {
            System.out.println("Can not get (response code " + response.getStatusCode()
               + new String(response.getData()) + " ) node with path : " + nodePath);
         }

      }
      finally
      {
         conn.stop();
      }
   }

   @Override
   protected void createContent(String parentNodeName, TestCase tc, WebdavTestContext context) throws Exception
   {
   }

}
