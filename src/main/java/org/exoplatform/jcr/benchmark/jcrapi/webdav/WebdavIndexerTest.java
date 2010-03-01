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

import org.exoplatform.common.http.client.HTTPResponse;
import org.exoplatform.common.http.client.HttpOutputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class WebdavIndexerTest extends AbstractWebdavTest
{

   //   private TestResource[] testTesources;

   private ArrayList<TestResource> testTesources = new ArrayList<TestResource>();

   private class TestResource
   {
      private String contentType;

      private InputStream fileStream;

      public TestResource(String resourcePath, String contentType)
      {

         try
         {
            FileInputStream fs = new FileInputStream(resourcePath);
            this.fileStream = fs;
            this.contentType = contentType;
         }
         catch (FileNotFoundException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   /**
    * @see org.exoplatform.jcr.benchmark.jcrapi.webdav.AbstractWebdavTest#doPrepare(com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.jcrapi.webdav.WebdavTestContext)
    */
   @Override
   public void doPrepare(TestCase tc, WebdavTestContext context) throws Exception
   {
      JCRWebdavConnectionEx item = new JCRWebdavConnectionEx(context);
      try
      {

         rootNodeName = context.generateUniqueName("rootNode");
         item.addDir(rootNodeName);

         testTesources.add(new TestResource("../resources/index/test_index.doc", "application/msword"));
         testTesources.add(new TestResource("../resources/index/test_index.htm", "text/html"));
         testTesources.add(new TestResource("../resources/index/test_index.xml", "text/xml"));
         testTesources.add(new TestResource("../resources/index/test_index.ppt", "application/vnd.ms-powerpoint"));
         testTesources.add(new TestResource("../resources/index/test_index.txt", "text/plain"));
         testTesources.add(new TestResource("../resources/index/test_index.xls", "application/vnd.ms-excel"));

      }
      finally
      {
         item.stop();
      }

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
   public void doRun(TestCase tc, WebdavTestContext context) throws Exception
   {
      item = new JCRWebdavConnectionEx(context);

      try
      {
         int i = new Random().nextInt(testTesources.size());
         InputStream inStream = testTesources.get(i).fileStream;
         String contentType = testTesources.get(i).contentType;

         String nodeName = rootNodeName + "/" + context.generateUniqueName("node");

         HttpOutputStream outStream = new HttpOutputStream();

         HTTPResponse response = item.addNode(nodeName, outStream, contentType);

         writeToOutputStream(inStream, outStream);

         outStream.close();
         System.out.println(response.getStatusCode());

      }
      finally
      {
         item.stop();
      }

   }

   @Override
   public void doFinish(TestCase tc, WebdavTestContext context) throws Exception
   {
      item.stop();
      super.doFinish(tc, context);
   }

   private void writeToOutputStream(InputStream inStream, HttpOutputStream outStream) throws IOException
   {
      int b;
      while ((b = inStream.read()) != -1)
      {
         outStream.write(b);
      }
   }

}
