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

import java.io.FileInputStream;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

//import org.exoplatform.services.jcr.ext.replication.async.AsyncReplication;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS. <br/>Date:
 * 
 * @author <a href="karpenko.sergiy@gmail.com">Karpenko Sergiy</a>
 * @version $Id: ConflictSunchronizeMoveTest.java 111 2008-11-11 11:11:11Z serg $
 */
public class ConflictSunchronizeMoveTest
   extends AsyncTestBase
{

   public static String FOLDER_NAME = "conflictMoveFolder";

   public Node root;

   @Override
   public void doPrepare(TestCase tc, AsyncTestContext context) throws Exception
   {
      super.doPrepare(tc, context);
      Session s = context.getSession();
      String rootFolder = tc.getParam("ext.rootFolder");
      root = s.getRootNode().addNode(rootFolder);
      s.save();
   }

   @Override
   public void doRun(final TestCase tc, AsyncTestContext context) throws Exception
   {
      //TODO
      /*
          // create dest folder
          Session s = context.getSession();
          s.getRootNode().addNode(FOLDER_NAME);
          s.save();
          
          AsyncReplication rep = context.getReplicationServer();
          
          rep.synchronize();
          log.info("Synchronize root folder started.");
          // wait for synchronization end
          while (rep.isActive()) {
            Thread.sleep(3000);
          }
          log.info("Synchronize root folder ended.");

          // create content
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

          log.info(" MOVE to dest folder ");
          // move to dest folder
          NodeIterator it = root.getNodes();
          while (it.hasNext()) {
            s.move(it.nextNode().getPath(), "/" + FOLDER_NAME + "/" + it.nextNode().getName());
            s.save();
          }

          rep.synchronize();
          log.info("Synchronize MOVE started.");

          // wait for synchronization end
          while (rep.isActive()) {
            Thread.sleep(3000);
          }
          log.info("Synchronize MOVE done.");*/
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
