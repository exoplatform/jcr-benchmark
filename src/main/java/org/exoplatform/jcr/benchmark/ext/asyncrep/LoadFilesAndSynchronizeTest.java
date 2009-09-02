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
package org.exoplatform.jcr.benchmark.ext.asyncrep;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.Session;

import org.exoplatform.jcr.benchmark.JCRTestContext; //import org.exoplatform.services.jcr.ext.replication.async.AsyncReplication;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS. <br/>Date:
 * 
 * @author <a href="karpenko.sergiy@gmail.com">Karpenko Sergiy</a>
 * @version $Id: LoadFilesAndSynchronizeTest.java 111 2008-11-11 11:11:11Z serg $
 */
public class LoadFilesAndSynchronizeTest
   extends AsyncTestBase
{

   private Node root;

   @Override
   public void doPrepare(TestCase tc, AsyncTestContext context) throws Exception
   {

      super.doPrepare(tc, context);
      String rootFolder = tc.getParam("ext.rootFolder");
      Session s = context.getSession();
      root = s.getRootNode().addNode(rootFolder);
      s.save();
   }

   @Override
   public void doRun(final TestCase tc, AsyncTestContext context) throws Exception
   {
      //TODO
      /*Session s = context.getSession();

      for (int i = 0; i < sc; i++) {
        for (int j = 0; j < fc; j++) {
          // create file
          Node nodeToAdd = root.addNode("node_" + i + "_" + j, "nt:file");
          Node contentNodeOfNodeToAdd = nodeToAdd.addNode("jcr:content", "nt:resource");
          contentNodeOfNodeToAdd.setProperty("jcr:data", new FileInputStream(content));
          contentNodeOfNodeToAdd.setProperty("jcr:mimeType", "application/pdf");
          contentNodeOfNodeToAdd.setProperty("jcr:lastModified", Calendar.getInstance());
      //    System.out.println(nodeToAdd.getName() + " file added");
        }
        s.save();
        log.info(i + " log saved");
      }

      AsyncReplication rep = context.getReplicationServer();
      rep.synchronize();
      log.info("Synchronize started.");

      // wait for synchronization end
      while (rep.isActive()) {
        Thread.sleep(3000);
      }

      // check files
      String opRootName = tc.getParam("ext.oponentRootFolder");

      if (!s.getRootNode().hasNode(opRootName)) {
        log.info("FAIL: there is no merged opponent folder.");
      } else {
        log.info("OK : opponent folder is merged.");
        Node opRoot = s.getRootNode().getNode(opRootName);
        if (opRoot.getNodes().getSize() != (sc * fc)) {
          log.info("FAIL: add files count is not expected. There is "
              + opRoot.getNodes().getSize() + " but must " + (sc * fc));
        } else {
          log.info("OK : opponent files is added.");
        }
      }*/
   }

   @Override
   public void doFinish(TestCase tc, AsyncTestContext context) throws Exception
   {
      root.remove();
      context.getSession().save();
   }
}
