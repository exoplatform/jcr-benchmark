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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
   private static List<String> nodesPath = new ArrayList<String>();

   private static volatile boolean writersStarted = false;

   private static volatile boolean writersRun = true;

   private static PrintWriter reportFile;

   private static boolean gatherReport;

   private static byte[] writeData;

   private static volatile int reportsCounter = 1;

   protected int iterator = 0;

   private static List<NodesWriter> nodesWriters = new ArrayList<NodesWriter>();

   private StringBuilder report = new StringBuilder();

   private class NodesWriter extends Thread
   {
      private final String nodePath;

      private int count;

      private final int writeDelay;

      private final WebdavTestContext context;

      private StringBuilder report = new StringBuilder();

      private CountDownLatch countDownLatch;

      public NodesWriter(String threadName, String nodePath, int writeDelay, WebdavTestContext context,
         CountDownLatch countDownLatch)
      {
         super(threadName);
         this.nodePath = nodePath;
         this.context = context;
         this.count = 100;
         this.writeDelay = writeDelay;
         this.countDownLatch = countDownLatch;
      }

      /**
       * {@inheritDoc}
       */
      public void run()
      {
         try
         {
            // wait till latch is counted down by creating 
            countDownLatch.await();
         }
         catch (InterruptedException e1)
         {
         }
         String subNodePath = null;
         int folderCount = 0;

         while (writersRun)
         {
            long start = System.currentTimeMillis();
            long dataSize = 0;
            String path = "";
            JCRWebdavConnectionEx connection = new JCRWebdavConnectionEx(context);
            try
            {
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

               path = subNodePath + "/" + "node" + count++;
               HTTPResponse response = null;
               try
               {
                  response = connection.addNode(path, writeData);

                  if (response.getStatusCode() != 201)
                  {
                     System.out.println(this.getName() + " : Can not add (response code " + response.getStatusCode()
                        + new String(response.getData()) + " ) node with path : " + nodePath);
                  }
                  else
                  {
                     dataSize += writeData.length;
                  }
               }
               catch (Exception e)
               {
                  System.out.println(this.getName() + " : Can not add node with path : " + path);
                  e.printStackTrace();
                  continue;
               }
            }
            finally
            {
               connection.stop();
            }

            // report
            if (gatherReport)
            {
               report.append(System.currentTimeMillis());
               report.append(":");
               report.append(this.getName());
               report.append(":Add:");
               report.append(path);
               report.append(":");
               report.append(dataSize);
               report.append(":");
               report.append((System.currentTimeMillis() - start));
               report.append('\r');
               report.append('\n');
            }

            // delay if configured
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

      public String getReport()
      {
         return report.toString();
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

      if (tc.hasParam("gatherReport"))
      {
         gatherReport = tc.getBooleanParam("gatherReport");
      }
      else
      {
         gatherReport = false;
      }

      if (!writersStarted)
      {
         JCRWebdavConnectionEx conn = new JCRWebdavConnectionEx(context);
         try
         {
            long time = System.currentTimeMillis();
            writersStarted = true;

            String rootName = context.generateUniqueName("rootNode");
            conn.addDir(rootName);

            // prepare data for a read
            byte[] readData = readData(WebdavReadWriteTest.class.getResourceAsStream("/test-data/1k-web-page.html"));

            // prepare to read
            int foldersCount = nodesPoolSizeToRead / 100;
            foldersCount = foldersCount > 0 ? foldersCount : 1;
            for (int i = 0; i < foldersCount; i++)
            {
               String parentNodeName = rootName + "/" + context.generateUniqueName("node");
               conn.addDir(parentNodeName);

               int subFoldersCount = nodesPoolSizeToRead / foldersCount;

               for (int j = 0; j < subFoldersCount; j++)
               {
                  String subFolder = parentNodeName + "/" + context.generateUniqueName("subNode");
                  conn.addNode(subFolder, readData);
                  nodesPath.add(subFolder);
               }
            }

            // prepare data for write
            //writeData = readData(WebdavReadWriteTest.class.getResourceAsStream("/test-data/average-web-page.html"));
            // use same data
            writeData = readData;
            // Make all threads start writing at the same time
            CountDownLatch countDownLatch = new CountDownLatch(1);
            // prepare to threads writers 
            for (int j = 0; j < writeThreadsCount; j++)
            {
               String parentNodeName = rootName + "/" + context.generateUniqueName("node_writers");
               conn.addDir(parentNodeName);

               NodesWriter writer =
                  new NodesWriter("ThreadWriter_" + j, parentNodeName, delayWrite, context, countDownLatch);
               writer.start();
               nodesWriters.add(writer);
            }
            // Notify writers to start their job 
            countDownLatch.countDown();
            System.out.println(Thread.currentThread() + " : Prepare done in " + (System.currentTimeMillis() - time));
         }
         finally
         {
            conn.stop();
         }
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
      // only one thread will stop and report writers
      synchronized (nodesWriters)
      {
         if (gatherReport && reportFile == null)
         {
            File reportsDir = new File("report");
            reportsDir.mkdirs();
            reportFile = new PrintWriter(new File(reportsDir, "report.txt"));
         }
         // stop cycles in threads
         writersRun = false;
         // wait for everyone to finish
         for (NodesWriter writer : nodesWriters)
         {
            writer.join();
            if (gatherReport)
            {
               reportFile.append(writer.getReport());
            }
         }
         nodesWriters.clear();

         if (gatherReport)
         {
            reportFile.append(report.toString());
            reportFile.flush();
         }
      }

   }

   /**
    * {@inheritDoc}
    */
   public void doRun(final TestCase tc, WebdavTestContext context) throws Exception
   {
      String nodePath = nextNodePath();
      long start = System.currentTimeMillis();
      JCRWebdavConnectionEx conn = new JCRWebdavConnectionEx(context);
      long packetSize = -1;
      try
      {
         // request
         HTTPResponse response = conn.getNode(nodePath);
         if (response.getStatusCode() != 200)
         {
            System.out.println("Can not get (response code " + response.getStatusCode()
               + new String(response.getData()) + " ) node with path : " + nodePath);
         }
         else
         {
            // read all data
            byte[] data = response.getData();
            if (data != null)
            {
               packetSize = data.length;
            }
         }
      }
      finally
      {
         conn.stop();
      }

      // report
      if (gatherReport)
      {
         report.append(System.currentTimeMillis());
         report.append(":");
         report.append(Thread.currentThread());
         report.append(":Read:");
         report.append(nodePath);
         report.append(":");
         report.append(packetSize);
         report.append(":");
         report.append((System.currentTimeMillis() - start));
         report.append('\r');
         report.append('\n');
      }
      //System.out.println(Thread.currentThread() + ":Read:" + nodePath + ":" + packetSize + ":" + (System.currentTimeMillis() - start));
   }

   @Override
   protected void createContent(String parentNodeName, TestCase tc, WebdavTestContext context) throws Exception
   {
   }

   private byte[] readData(InputStream dataIn) throws IOException
   {
      ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
      byte[] buff = new byte[2048];
      int res = -1;
      try
      {
         while ((res = dataIn.read(buff)) >= 0)
         {
            dataOut.write(buff, 0, res);
         }
      }
      finally
      {
         dataIn.close();
         dataOut.close();
      }

      return dataOut.toByteArray();
   }
}
