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
package org.exoplatform.jcr.benchmark.usecases;

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.impl.core.SessionImpl;
import org.exoplatform.services.jcr.impl.storage.jdbc.JDBCStorageConnection;
import org.exoplatform.services.jcr.impl.storage.jdbc.JDBCWorkspaceDataContainer;
import org.exoplatform.services.jcr.impl.storage.jdbc.statistics.StatisticsJDBCStorageConnection;
import org.exoplatform.services.jcr.storage.WorkspaceStorageConnection;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class AddNtFileWithMetadataTestCleanDBOracle
   extends JCRTestBase
{
   /*
    * This test calculates the time (ms or tps) of adding of one nodes of type nt:file (including
    * addNode(), setProperty(), addMixin(), save() methods).
    */

   public static WorkspaceStorageConnection workspaceStorageConnection = null;

   public static boolean dataBaseDropped = false;

   public static byte[] contentOfFile = null;

   private Node rootNode = null;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      if (contentOfFile == null)
      {
         contentOfFile = new byte[(int) new File("../resources/benchmark.pdf").length()];
         InputStream is = new FileInputStream("../resources/benchmark.pdf");
         int offset = 0;
         int numRead = 0;
         while (offset < contentOfFile.length
                  && (numRead = is.read(contentOfFile, offset, contentOfFile.length - offset)) >= 0)
         {
            offset += numRead;
         }
         if (offset < contentOfFile.length)
         {
            throw new IOException("Could not completely read file ");
         }
         is.close();
      }
      rootNode = context.getSession().getRootNode().addNode(context.generateUniqueName("rootNode"), "nt:unstructured");
      context.getSession().save();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      Node nodeToAdd = rootNode.addNode(context.generateUniqueName("node"), "nt:file");
      Node contentNodeOfNodeToAdd = nodeToAdd.addNode("jcr:content", "nt:resource");
      contentNodeOfNodeToAdd.setProperty("jcr:data", new ByteArrayInputStream(contentOfFile));
      contentNodeOfNodeToAdd.setProperty("jcr:mimeType", "application/pdf");
      contentNodeOfNodeToAdd.setProperty("jcr:lastModified", Calendar.getInstance());
      // dc:elementset property will be setted automatically
      context.getSession().save();
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      cleanDB(context);
   }

   private synchronized void cleanDB(JCRTestContext context)
   {
      try
      {
         if (!dataBaseDropped)
         {
            Connection dbConnection;
            JDBCStorageConnection storageConnection;
            JDBCWorkspaceDataContainer workspaceDataContainer =
                     (JDBCWorkspaceDataContainer) ((SessionImpl) context.getSession()).getContainer()
                              .getComponentInstanceOfType(JDBCWorkspaceDataContainer.class);
            if (workspaceStorageConnection == null)
            {
               workspaceStorageConnection = workspaceDataContainer.openConnection();
            }
            else
            {
               workspaceStorageConnection = workspaceDataContainer.reuseConnection(workspaceStorageConnection);
            }
            WorkspaceStorageConnection wsc = workspaceStorageConnection;
            if (wsc instanceof StatisticsJDBCStorageConnection)
            {
               wsc = ((StatisticsJDBCStorageConnection)wsc).getNestedWorkspaceStorageConnection();
            }
            
            storageConnection = (JDBCStorageConnection) wsc;
            dbConnection = storageConnection.getJdbcConnection();
            // =============ORACLE=============
            List<String> oracleQueryList = new ArrayList<String>();
            oracleQueryList.add("DROP TABLE jcr_config");
            oracleQueryList.add("DROP TABLE jcr_scontainer");
            oracleQueryList.add("DROP TABLE jcr_svalue");
            oracleQueryList.add("DROP TABLE jcr_sref");
            oracleQueryList.add("DROP TABLE jcr_sitem");
            oracleQueryList.add("DROP SEQUENCE JCR_SVALUE_SEQ");
            for (String query : oracleQueryList)
            {
               try
               {
                  dbConnection.prepareStatement(query).execute();
               }
               catch (Exception e)
               {
               }
            }
            // ================================
            dbConnection.commit();
            dataBaseDropped = true;
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

}
