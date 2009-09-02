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
package org.exoplatform.jcr.benchmark.helpers;

import java.io.FileInputStream;
import java.util.Calendar;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.impl.core.SessionImpl;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class QueryNodeLoader
{
   /*
    * This test calculates the time (ms or tps) of adding of one nodes of type nt:file (including
    * addNode(), setProperty(), addMixin(), save() methods). Make sure that resources are exists.
    */

   private static final int NUMBER_OF_NODES = 10;

   public static void main(String[] args)
   {
      Repository repository = null;
      try
      {
         StandaloneContainer.addConfigurationPath("../config/test-configuration-benchmark.xml");
         StandaloneContainer container = StandaloneContainer.getInstance();
         if (System.getProperty("java.security.auth.login.config") == null)
            System.setProperty("java.security.auth.login.config", Thread.currentThread().getContextClassLoader()
                     .getResource("login.conf").toString());
         RepositoryService repositoryService =
                  (RepositoryService) container.getComponentInstanceOfType(RepositoryService.class);
         repository = repositoryService.getCurrentRepository();
         Credentials credentials = new SimpleCredentials("admin", "admin".toCharArray());
         Session session = (SessionImpl) repository.login(credentials, "collaboration");
         Node rootNode = session.getRootNode().getNode("download").getNode("node0").getNode("node1").getNode("node2");
         for (int i = 0; i < NUMBER_OF_NODES; i++)
         {
            Node nodeToAdd = rootNode.addNode("query-0-1-2-" + i + ".html", "nt:file");
            nodeToAdd.addMixin("dc:elementSet");
            nodeToAdd.setProperty("dc:title", new String[]
            {"Exoplatform"});
            Node contentNodeOfNodeToAdd = nodeToAdd.addNode("jcr:content", "nt:resource");
            contentNodeOfNodeToAdd.setProperty("jcr:data", new FileInputStream("../resources/query-0-1-2-" + i
                     + ".html"));
            contentNodeOfNodeToAdd.setProperty("jcr:mimeType", "text/html");
            contentNodeOfNodeToAdd.setProperty("jcr:lastModified", Calendar.getInstance());
            session.save();
         }
         session.save();
         System.out.println("===Nodes for query tests loaded successfuly");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.exit(1);
      }
      System.exit(0);
   }

}
