/*
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.jcr.benchmark.jcrapi.node.read;

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import java.util.Calendar;

import javax.jcr.Node;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class NodeConcurrentReadTest extends JCRTestBase
{

   private Node root;

   private Node node;

   private int iterations = 100;
   
   private Writer writer;

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);

      writer.destroy();
      node.remove();
      context.getSession().save();
   }

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      root = context.getSession().getRootNode().addNode(context.generateUniqueName("testRoot"));
      node = root.addNode(context.generateUniqueName("testConcurentNode"));
      
      writer = new Writer();
      writer.start();

   }

   /**
    * @see org.exoplatform.jcr.benchmark.JCRTestBase#doRun(com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.JCRTestContext)
    */
   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {

      try
      {
         for (int i = 0; i < iterations; i++)
         {
            node.getProperty("name");
            node.getProperty("date");
            node.getProperty("time");
            node.getProperties();
         }

      }
      catch (Exception e)
      {
         LOG.error(e.getMessage(), e);
      }

   }

   class Writer extends Thread
   {

      public void run()
      {
         try
         {
            for (int i = 0; i < iterations; i++)
            {
               node.setProperty("name", this.getName());
               node.setProperty("date", Calendar.getInstance());
               node.setProperty("time", System.currentTimeMillis());
               this.wait(100);
            }
         }
         catch (Exception e)
         {
            LOG.error(e.getMessage(), e);
         }

      }
   }
 
}
