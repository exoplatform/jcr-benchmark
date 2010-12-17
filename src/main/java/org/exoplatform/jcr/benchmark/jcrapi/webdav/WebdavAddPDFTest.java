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

import java.io.File;
import java.io.FileInputStream;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class WebdavAddPDFTest extends AbstractWebdavTest
{
   private byte fileContent[];

   /**
    * {@inheritDoc}
    */
   @Override
   protected void createContent(String parentNodeName, TestCase tc, WebdavTestContext context) throws Exception
   {
      String nodeName = parentNodeName + "/" + context.generateUniqueName(this.getClass().getName());
      addNode(nodeName);
   }

   @Override
   public void doPrepare(TestCase tc, WebdavTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      for (int i = 0; i < 100; i++)
      {
         item.addDir(rootNodeName + "/" + context.generateUniqueName("node"));
         addNode(rootNodeName + "/" + context.generateUniqueName("node"));
      }

      File file = new File("../resources/benchmark.pdf");
      FileInputStream fin = new FileInputStream(file);
      fileContent = new byte[(int)file.length()];
      fin.read(fileContent);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void doRun(TestCase tc, WebdavTestContext context) throws Exception
   {
      item.addNode(nextNodePath(), fileContent, "application/pdf");
   }
}
