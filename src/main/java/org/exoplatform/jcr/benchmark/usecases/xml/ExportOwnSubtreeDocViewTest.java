/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.usecases.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.jcr.Node;

import org.exoplatform.services.log.Log;
import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class ExportOwnSubtreeDocViewTest
   extends JCRTestBase
{
   /*
    * This test measures performance of exporting mechanism using docview method, each thread has own
    * node subtree of nodes
    */

   public static Log log = ExoLogger.getLogger("jcr.benchmark");

   private Node rootNode = null;

   private String name = "";

   private File destFile = null;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      name = context.generateUniqueName("rootNode");
      rootNode = context.getSession().getRootNode().addNode(name);
      Node file = rootNode.addNode("child1").addNode("child2").addNode("file", "nt:file");
      Node content = file.addNode("jcr:content", "nt:resource");
      content.setProperty("jcr:data", new FileInputStream("../resources/benchmark.pdf"));
      content.setProperty("jcr:mimeType", "application/pdf");
      content.setProperty("jcr:lastModified", Calendar.getInstance());
      context.getSession().save();
      destFile = File.createTempFile(name, ".xml");
      destFile.deleteOnExit();
      // log.info(destFile.getAbsolutePath());
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      OutputStream out = new FileOutputStream(destFile);
      context.getSession().exportDocumentView("/" + name, out, false, false);
      out.close();
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      rootNode.remove();
      context.getSession().save();
   }

}
