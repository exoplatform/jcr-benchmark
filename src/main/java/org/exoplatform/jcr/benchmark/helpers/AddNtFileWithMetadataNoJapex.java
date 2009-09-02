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

public class AddNtFileWithMetadataNoJapex
{
   /*
    * This test calculates the time (ms or tps) of adding of one nodes of type nt:file (including
    * addNode(), setProperty(), addMixin(), save() methods).
    */

   private static final int NUMBER_OF_ITERATIONS = 500;

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
         Node rootNode = session.getRootNode().addNode("rootNode", "nt:unstructured");
         session.save();
         long start = System.currentTimeMillis();
         for (int i = 0; i < NUMBER_OF_ITERATIONS; i++)
         {
            Node nodeToAdd = rootNode.addNode("node" + i, "nt:file");
            Node contentNodeOfNodeToAdd = nodeToAdd.addNode("jcr:content", "nt:resource");
            contentNodeOfNodeToAdd.setProperty("jcr:data", new FileInputStream("../resources/benchmark.pdf"));
            contentNodeOfNodeToAdd.setProperty("jcr:mimeType", "application/pdf");
            contentNodeOfNodeToAdd.setProperty("jcr:lastModified", Calendar.getInstance());
            session.save();
         }
         long end = System.currentTimeMillis();
         rootNode.remove();
         session.save();
         System.out.println("===AddNtFileWithMetadataNoJapex, TIME : " + (end - start) / NUMBER_OF_ITERATIONS + " ms");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.exit(1);
      }
      System.exit(0);
   }

}
