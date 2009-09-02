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
import javax.jcr.Session;

//import org.exoplatform.services.jcr.ext.replication.async.AsyncReplication;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS. <br/>Date:
 * 
 * @author <a href="karpenko.sergiy@gmail.com">Karpenko Sergiy</a>
 * @version $Id: ConflictSynchronizeAndDeleteTest.java 111 2008-11-11 11:11:11Z
 *          serg $
 */
public class ConflictSynchronizeAndDeleteTest
   extends ConflictSynchronizeTest
{

   @Override
   public void doRun(final TestCase tc, AsyncTestContext context) throws Exception
   {

      /*super.doRun(tc, context);

      Session s = context.getSession();
      log.info("Start Delete");

      String pr = tc.getParam("ext.nodePriority");
      if (pr.equalsIgnoreCase("high")) {
        // do update folder content
        for (int i = 0; i < sc/2; i++) {
          for (int j = 0; j < fc/2; j++) {
            // create file
            Node nodeToAdd = root.addNode("node_secadd_" + i + "_" + j, "nt:file");
            Node contentNodeOfNodeToAdd = nodeToAdd.addNode("jcr:content", "nt:resource");
            contentNodeOfNodeToAdd.setProperty("jcr:data", new FileInputStream(content));
            contentNodeOfNodeToAdd.setProperty("jcr:mimeType", "plain/text");
            contentNodeOfNodeToAdd.setProperty("jcr:lastModified", Calendar.getInstance());
            // System.out.println(nodeToAdd.getName() + " file added");
          }
          s.save();
          log.info(i + " log saved");
        }
      } else if (pr.equalsIgnoreCase("low")) {
        // do delete folder
        root = s.getRootNode().getNode("conflictFolder");
        root.remove();
        s.save();
      } else
        throw new Exception("Invalid parameter ext.nodePriority [" + pr + "]");

      // Start synchronization

      AsyncReplication rep = context.getReplicationServer();
      rep.synchronize();
      log.info("Synchronize DELETE started.");

      // wait for synchronization end
      while (rep.isActive()) {
        Thread.sleep(3000);
      }
      log.info("Synchronization DELETE done");*/
   }

   @Override
   public void doFinish(TestCase tc, AsyncTestContext context) throws Exception
   {
      super.doFinish(tc, context);
   }

}
