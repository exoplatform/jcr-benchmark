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
import java.util.ArrayList;
import java.util.Random;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class WebdavIndexerTest extends AbstractWebdavTest
{

   private ArrayList<TestResource> testTesources = new ArrayList<TestResource>();

   private class TestResource
   {
      private String contentType;
      
      private byte[] byteBuffer;
      
      private int bufferSize;

      public TestResource(String resourcePath, String contentType) throws IOException
      {

         try
         {
            this.contentType = contentType;
            FileInputStream fs = new FileInputStream(resourcePath);
            
            bufferSize = fs.read(byteBuffer);
            
         }
         catch (FileNotFoundException e)
         {
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
         // testTesources.add(new TestResource("../resources/index/test_index.pdf", "application/pdf"));

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
         TestResource r = testTesources.get(i);

         String nodeName = rootNodeName + "/" + context.generateUniqueName("node");

         HttpOutputStream outStream = new HttpOutputStream();

         HTTPResponse response = item.addNode(nodeName, r.byteBuffer, r.contentType);

         outStream.close();
         if(response.getStatusCode() >= 400){
            System.out.println("ERROR: Server returned Status " + response.getStatusCode() + " : " + response.getData());
         }

      }
      finally
      {
         item.stop();
      }

   }

   @Override
   public void doFinish(TestCase tc, WebdavTestContext context) throws Exception
   {
      super.doFinish(tc, context);
   }
}
