/*
 * Copyright (C) 2010 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.jcrapi.webdav;

import com.sun.japex.TestCase;

import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.common.http.client.HTTPResponse;
import org.exoplatform.common.http.client.ModuleException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class WebdavIndexerTest extends AbstractWebdavTest
{

   private ArrayList<TestResource> testResources = new ArrayList<TestResource>();

   private class TestResource
   {
      private String contentType;

      private byte[] resourceBuffer;

      public TestResource(String resourcePath, String contentType) throws IOException
      {
         this.contentType = contentType;
         FileInputStream inStream = new FileInputStream(resourcePath);
         try
         {
            resourceBuffer = new byte[inStream.available()];

            int i = 0;
            int b = 0;
            while ((b = inStream.read()) != -1)
            {
               resourceBuffer[i] = (byte)b;
               i++;
            }
         }
         catch (Exception exc)
         {
            exc.printStackTrace();
         }
         finally
         {
            inStream.close();
         }

      }
   }

   /**
    * @throws ModuleException 
    * @throws IOException 
    * @see org.exoplatform.jcr.benchmark.jcrapi.webdav.AbstractWebdavTest#doPrepare(com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.jcrapi.webdav.WebdavTestContext)
    */
   @Override
   public void doPrepare(TestCase tc, WebdavTestContext context) throws IOException, ModuleException
   {
      JCRWebdavConnectionEx item = new JCRWebdavConnectionEx(context);
      rootNodeName = context.generateUniqueName("rootNode");
      item.addDir(rootNodeName);

      testResources.add(new TestResource("../resources/index/test_index.doc", "application/msword"));
      testResources.add(new TestResource("../resources/index/test_index.htm", "text/html"));
      testResources.add(new TestResource("../resources/index/test_index.xml", "text/xml"));
      testResources.add(new TestResource("../resources/index/test_index.ppt", "application/vnd.ms-powerpoint"));
      testResources.add(new TestResource("../resources/index/test_index.txt", "text/plain"));
      testResources.add(new TestResource("../resources/index/test_index.xls", "application/vnd.ms-excel"));
      // testTesources.add(new TestResource("../resources/index/test_index.pdf", "application/pdf"));

   }

   /**
    * @see org.exoplatform.jcr.benchmark.jcrapi.webdav.AbstractWebdavTest#createContent(java.lang.String, com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.jcrapi.webdav.WebdavTestContext)
    */
   @Override
   protected void createContent(String parentNodeName, TestCase tc, WebdavTestContext context) throws Exception
   {
      // TODO Auto-generated method stub

   }

   /**
    * @see org.exoplatform.jcr.benchmark.jcrapi.webdav.AbstractWebdavTest#doRun(com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.jcrapi.webdav.WebdavTestContext)
    */
   @Override
   public void doRun(TestCase tc, WebdavTestContext context)
   {
      item = new JCRWebdavConnectionEx(context);
      Random rand = new Random();

      try
      {
         int i = rand.nextInt(testResources.size());
         TestResource res = testResources.get(i);

         String nodeName = rootNodeName + "/" + context.generateUniqueName("node");
         HTTPResponse response = item.addNode(nodeName, res.resourceBuffer, res.contentType);

         if (response.getStatusCode() != HTTPStatus.CREATED)
         {
            System.out.println("Server returned Status " + response.getStatusCode() + " : "
               + new String(response.getData()));
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      finally
      {
         item.stop();
      }

   }

   public void doFinish(TestCase tc, WebdavTestContext context) throws Exception
   {
      //super.doFinish(tc, context);
   }
}
