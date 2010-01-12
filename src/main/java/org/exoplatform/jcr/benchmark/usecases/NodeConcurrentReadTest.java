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
package org.exoplatform.jcr.benchmark.usecases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 */
public class NodeConcurrentReadTest extends JCRTestBase
{

   private static String testRootPath = null;

   private Repository repo;

   private Credentials credentials;

   private String workspace;

   private static volatile int nodes = 0;

   private static Map<Integer, String> nodeNames = new ConcurrentHashMap<Integer, String>();

   private static volatile boolean writersActive = true;

   private static List<Writer> writersList = new ArrayList<Writer>();

   private static volatile long counter = 0;

   private static volatile long threads = 0;
   
   private JCRTestContext context = null;

   /**
    * {@inheritDoc}
    */
   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      this.repo = context.getSession().getRepository();
      this.workspace = context.getSession().getWorkspace().getName();
      this.credentials = context.getCredentials();
      this.context = context;

      synchronized (writersList)
      {
         if (writersList.size() == 0)
         {
            System.out.println();
            System.out.println("========= prepare test =========");
             
            // initial nodes
            
            Node testRoot = context.getSession().getRootNode().addNode("testRoot");
            context.getSession().save();
            testRootPath = testRoot.getPath();
            
            if (tc.hasParam("exo.numberOfNodes"))
            {
               nodes = tc.getIntParam("exo.numberOfNodes");
            }
            else
            {
               nodes = 100;
            }
            System.out.println("exo.numberOfNodes " + nodes);

            for (int i = 0; i < nodes; i++)
            {
               String parentName = context.generateUniqueName("parent");
               Node parent = testRoot.addNode(parentName);
               parent.setProperty("testProp", "testVal");
               context.getSession().save();
               nodeNames.put(i, parentName);
            }
      
            // writers
            int writers;
            if (tc.hasParam("exo.numberOfWriters"))
            {
               writers = tc.getIntParam("exo.numberOfWriters");
            }
            else
            {
               writers = 1;
            }

            long pause;
            if (tc.hasParam("exo.writerPause"))
            {
               pause = tc.getIntParam("exo.writerPause");
            }
            else
            {
               pause = 50;
            }
            
            System.out.println("exo.numberOfWriters " + writers);
            System.out.println("exo.writerPause " + pause);
            
            for (int i = 0; i < writers; i++)
            {
               Writer w = new Writer(pause);
               writersList.add(w);
               w.start();
            }
            
            System.out.println("===================================");
         }
      }

      threads++;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      Session session = context.getSession();

      //System.out.println((counter++) + ": " + Thread.currentThread() + ", session " + session);

      // read
      Node readNode = ((Node)session.getItem(testRootPath)).getNode(nodeNames.get(new Random().nextInt(nodes)));
      readNode.getProperty("testProp").getValue().getString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      writersActive = false;
      synchronized (writersList)
      {
         if (writersList.size() > 0)
         {
            String msg = "Ran readers " + threads + " writers " + writersList.size();
            System.out.print("Finishing... ");
            System.out.print("stopping writers... ");
            for (Writer w : writersList)
            {
               w.join(1000);
            }
            writersList.clear();
            
            System.out.println("done.");
            System.out.println(msg);
         }
      }
      
      super.doFinish(tc, context);
   }

   private class Writer extends Thread
   {

      private final Session session;
      
      private final long pause;

      Writer(long pause) throws LoginException, RepositoryException
      {
         this.session = repo.login(credentials, workspace);
         this.pause = pause;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void run()
      {
         super.run();
         try
         {
            Node root = ((Node)session.getItem(testRootPath));

            while (writersActive)
            {
               String name = context.generateUniqueName(this.getName());
               Node writeNode = root.addNode(name);
               writeNode.setProperty("testProp", this.getName());
               session.save();

               nodeNames.put(nodeNames.size(), name);

               if (pause > 0)
               {
                  Thread.sleep(this.pause);
               }

               Thread.yield();

               nodes++;
            }
         }
         catch (Exception e)
         {
            log.error("Writer stopped: " + e.getMessage(), e);
         }
         finally
         {
            session.logout();
         }
      }
   }
}
