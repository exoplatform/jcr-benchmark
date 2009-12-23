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

import com.sun.japex.TestCase;

import org.apache.tools.ant.taskdefs.condition.Http;
import org.exoplatform.common.http.client.HTTPResponse;
import org.exoplatform.common.http.client.HttpOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 
 *
 * @author <a href="karpenko.sergiy@gmail.com">Karpenko Sergiy</a> 
 * @version $Id: WebdavAddBLOBTest.java 111 2008-11-11 11:11:11Z serg $
 */
/**
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 * @version $Id: exo-jboss-codetemplates.xml 34360 2009-07-22 23:58:59Z aheritier $
 *
 */
public class WebdavAddBLOBTest extends AbstractWebdavTest
{

   private int sizeInMb = 0;

   //File blobFile = null;

   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doPrepare(TestCase tc, WebdavTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      sizeInMb = tc.getIntParam("blobFileSize");
   }
   
   /**
    * {@inheritDoc}
    */
   protected void createContent(String parentNodeName, TestCase tc, WebdavTestContext context) throws Exception
   {
      String nodeName = parentNodeName + "/" + context.generateUniqueName(this.getClass().getName());
      
      addNode(nodeName);
   }

   /**
    * {@inheritDoc}
    */
   public void doRun(TestCase tc, WebdavTestContext context) throws Exception
   {
      HttpOutputStream stream = new HttpOutputStream();

      HTTPResponse response = item.addNode(nextNodePath(), stream);
      loadStream(stream, sizeInMb * 1024);
      stream.close();
      response.getStatusCode();

   }

   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doFinish(TestCase tc, WebdavTestContext context) throws Exception
   {
      super.doFinish(tc, context);
   }

   protected void loadStream(HttpOutputStream stream, int sizeInKb) throws IOException
   {
      byte[] data = new byte[1024]; // 1Kb

      Random random = new Random();

      for (int i = 0; i < sizeInKb; i++)
      {
         random.nextBytes(data);
         stream.write(data);
      }
   }
}
