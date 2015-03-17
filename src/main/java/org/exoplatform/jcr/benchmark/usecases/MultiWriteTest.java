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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS. <br/>Date:
 * 
 * @author <a href="karpenko.sergiy@gmail.com">Karpenko Sergiy</a>
 * @version $Id: MultiWriteTest.java 111 2008-11-11 11:11:11Z serg $
 */
public class MultiWriteTest
   extends JCRTestBase
{

   /**
    * Count of string blocks writed to file.
    */
   private static final int SIZE = 10 * 1024;

   /**
    * Data block size.
    */
   private static final int BLOCK_SIZE = 1024;

   /**
    * Global variable, used to automatically numerate threads.
    */
   static int threadnum = 0;

   /**
    * Property owner node.
    */
   private Node parent;

   /**
    * Property to store.
    */
   private Property prop;

   /**
    * File with content to store in node.
    */
   private File file;

   /**
    * Current thread number.
    */
   int curnum;

   private InputStream getStream() throws IOException
   {
      return new FileInputStream(file);
   }

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {

      curnum = threadnum++;
      file = File.createTempFile("Thread", "_" + curnum);

      // create values

      OutputStream out = new FileOutputStream(file);

      byte[] buf = createBuf(BLOCK_SIZE, String.valueOf(curnum));

      for (int j = 0; j < SIZE; j++)
      {
         out.write(buf);
      }
      out.close();

      LOG.info(file.getAbsolutePath() + "   added");

      Session session = context.getSession();
      Node rootNode = session.getRootNode();
      if (rootNode.hasNode("parentNode"))
      {
         parent = rootNode.getNode("parentNode");
         LOG.info(curnum + " get node");
      }
      else
      {
         parent = rootNode.addNode("parentNode");
         session.save();
         LOG.info(curnum + " ADD node");
      }

      if (parent.hasProperty("prop"))
      {
         prop = parent.getProperty("prop");
         LOG.info(curnum + " get prop");
      }
      else
      {
         // create big file
         File f = File.createTempFile("Thread", "_first");
         out = new FileOutputStream(f);
         buf = createBuf(BLOCK_SIZE, "a");
         for (int j = 0; j < SIZE; j++)
         {
            out.write(buf);
         }
         out.close();
         LOG.info(f.getAbsolutePath() + "   added");

         prop = parent.setProperty("prop", new FileInputStream(f));
         session.save();
         LOG.info(curnum + " ADD prop");
      }

   }

   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      try
      {
         LOG.info("doRun " + curnum);
         prop.setValue(getStream());
         parent.save();
         LOG.info(" save finished " + curnum);
      }
      catch (Exception e)
      {
         LOG.info("====================" + curnum + " thread : ");
         LOG.error(e.getMessage(), e);
      }
   }

   public void doFinish(final TestCase tc, JCRTestContext context) throws Exception
   {

      Session ses = context.getSession();
      ses.refresh(false);

      InputStream in = ses.getRootNode().getNode("parentNode").getProperty("prop").getStream();

      byte[] buf = new byte[4];
      in.read(buf);
      in.close();
      LOG.info(curnum + " - " + new String(buf));
   }

   private byte[] createBuf(int size, String val)
   {
      byte[] s = val.getBytes();
      byte[] buf = new byte[s.length * size];

      for (int i = 0; i < buf.length;)
      {
         System.arraycopy(s, 0, buf, i, s.length);
         i += s.length;
      }

      return buf;
   }

}
