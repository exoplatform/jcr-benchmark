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

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class WebdavIndexerTest extends AbstractWebdavTest
{

   private InputStream[] resources = new InputStream[7];

   /**
    * @see org.exoplatform.jcr.benchmark.jcrapi.webdav.AbstractWebdavTest#doPrepare(com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.jcrapi.webdav.WebdavTestContext)
    */
   @Override
   public void doPrepare(TestCase tc, WebdavTestContext context) throws Exception
   {
      //super.doPrepare(tc, context);

      resources[0] = WebdavIndexerTest.class.getResourceAsStream("../resources/index/test_index.doc");
      resources[1] = WebdavIndexerTest.class.getResourceAsStream("../resources/index/test_index.htm");
      resources[2] = WebdavIndexerTest.class.getResourceAsStream("../resources/index/test_index.pdf");
      resources[3] = WebdavIndexerTest.class.getResourceAsStream("../resources/index/test_index.ppt");
      resources[4] = WebdavIndexerTest.class.getResourceAsStream("../resources/index/test_index.txt");
      resources[5] = WebdavIndexerTest.class.getResourceAsStream("../resources/index/test_index.xls");
      resources[6] = WebdavIndexerTest.class.getResourceAsStream("../resources/index/test_index.xml");

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
      HttpOutputStream outStream = new HttpOutputStream();

      int i = new Random().nextInt(7);
      InputStream inStream = resources[i];
      String contentType = getContentType(i);
      HTTPResponse response = item.addNode(nextNodePath(), outStream, contentType);

      writeToOutputStream(inStream, outStream);

      outStream.close();
      response.getStatusCode();
   }

   private void writeToOutputStream(InputStream inStream, HttpOutputStream outStream) throws IOException
   {
      int b;
      while ((b = inStream.read()) != -1)
      {
         outStream.write(b);
      }
   }

   private String getContentType(int i)
   {
      switch (i)
      {
         case 0 :
            return ("application/msword");
         case 1 :
            return ("text/html");
         case 2 :
            return ("application/pdf");
         case 3 :
            return ("application/vnd.ms-powerpoint");
         case 4 :
            return ("application/vnd.ms-excel");
         case 5 :
            return ("application/msword");
         case 6 :
            return ("text/xml");
         default :
            return ("application/octet-stream");
      }
   }

}
