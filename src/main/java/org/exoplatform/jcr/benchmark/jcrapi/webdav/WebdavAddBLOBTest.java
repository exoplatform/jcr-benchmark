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
public class WebdavAddBLOBTest extends AbstractWebdavTest
{

   private int sizeInKb = 0;

   //File blobFile = null;

   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doPrepare(TestCase tc, WebdavTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      //create blob file
      sizeInKb = tc.getIntParam("blobFileSize");
      //      blobFile = createBLOBTempFile("bench", sizeInKb);
   }

   @Override
   public void doRun(TestCase tc, WebdavTestContext context) throws Exception
   {
      // TODO Auto-generated method stub
      HttpOutputStream stream = new HttpOutputStream();

      item.addNode(context.generateUniqueName("blobnode"), stream);
      loadStream(stream, sizeInKb * 1024);

   }

   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doFinish(TestCase tc, WebdavTestContext context) throws Exception
   {
      //blobFile.delete();
      super.doFinish(tc, context);
   }

   protected File createBLOBTempFile(String prefix, int sizeInKb) throws IOException
   {
      // create test file
      byte[] data = new byte[1024]; // 1Kb

      File testFile = File.createTempFile(prefix, ".tmp");
      FileOutputStream tempOut = new FileOutputStream(testFile);
      Random random = new Random();

      for (int i = 0; i < sizeInKb; i++)
      {
         random.nextBytes(data);
         tempOut.write(data);
      }
      tempOut.close();
      testFile.deleteOnExit(); // delete on test exit
      //      if (log.isDebugEnabled())
      //      {
      //         log.debug("Temp file created: " + testFile.getAbsolutePath() + " size: " + testFile.length());
      //      }
      return testFile;
   }

   protected void loadStream(HttpOutputStream stream, int sizeInKb) throws IOException
   {
      // create test file
      byte[] data = new byte[1024]; // 1Kb

      Random random = new Random();

      for (int i = 0; i < sizeInKb; i++)
      {
         random.nextBytes(data);
         stream.write(data);
      }
   }
}
