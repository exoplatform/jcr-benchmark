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

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.Session;

//import org.exoplatform.services.jcr.ext.replication.async.AsyncReplication;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS. <br/>Date:
 * 
 * @author <a href="karpenko.sergiy@gmail.com">Karpenko Sergiy</a>
 * @version $Id: ConflictSynchronizeTest.java 111 2008-11-11 11:11:11Z serg $
 */
public class ConflictSynchronizeTest
   extends AsyncTestBase
{

   public static String FOLDER_NAME = "conflictFolder";

   public Node root;

   @Override
   public void doPrepare(TestCase tc, AsyncTestContext context) throws Exception
   {
      super.doPrepare(tc, context);
      Session s = context.getSession();
      root = s.getRootNode().addNode(FOLDER_NAME);
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
          contentNodeOfNodeToAdd.setProperty("jcr:mimeType", "plain/text");
          contentNodeOfNodeToAdd.setProperty("jcr:lastModified", Calendar.getInstance());
          // System.out.println(nodeToAdd.getName() + " file added");
        }
        s.save();
        log.info(i + " log saved");
      }

      AsyncReplication rep = context.getReplicationServer();
      rep.synchronize();
      log.info("Synchronize ADD started.");

      // wait for synchronization end
      while (rep.isActive()) {
        Thread.sleep(3000);
      }

      log.info("Synchronization ADD  done.");*/
   }

   @Override
   public void doFinish(TestCase tc, AsyncTestContext context) throws Exception
   {
      Session s = context.getSession();
      if (s.getRootNode().hasNode(FOLDER_NAME))
      {
         root = s.getRootNode().getNode(FOLDER_NAME);
         root.remove();
         s.save();
      }
      super.doFinish(tc, context);
   }

}
