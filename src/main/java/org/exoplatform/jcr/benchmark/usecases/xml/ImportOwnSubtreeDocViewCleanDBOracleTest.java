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
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;

import org.exoplatform.services.log.Log;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.impl.core.SessionImpl;
import org.exoplatform.services.jcr.impl.storage.jdbc.JDBCStorageConnection;
import org.exoplatform.services.jcr.impl.storage.jdbc.JDBCWorkspaceDataContainer;
import org.exoplatform.services.jcr.storage.WorkspaceStorageConnection;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class ImportOwnSubtreeDocViewCleanDBOracleTest
   extends JCRTestBase
{
   /*
    * This test measures performance of exporting mechanism using docview method, each thread has own
    * node subtree of nodes
    */

   public static Log log = ExoLogger.getLogger("jcr.benchmark");

   public static WorkspaceStorageConnection workspaceStorageConnection = null;

   public static boolean dataBaseDropped = false;

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
      content.setProperty("jcr:data", new FileInputStream("../resources/benchmark.gif"));
      content.setProperty("jcr:mimeType", "image/gif");
      content.setProperty("jcr:lastModified", Calendar.getInstance());
      context.getSession().save();
      destFile = File.createTempFile(name, ".xml");
      destFile.deleteOnExit();
      OutputStream out = new FileOutputStream(destFile);
      context.getSession().exportDocumentView("/" + name, out, false, false);
      out.close();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      String name2 = context.generateUniqueName("name2");
      rootNode.addNode(name2);
      context.getSession().save();
      InputStream is = new FileInputStream(destFile);
      context.getSession().importXML("/" + name + "/" + name2, is, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      // rootNode.remove(); context.getSession().save();
      cleanDB(context);
   }

   private synchronized void cleanDB(JCRTestContext context)
   {
      try
      {
         if (!dataBaseDropped)
         {
            Connection dbConnection;

            log.info("sleep 10 sec");
            Thread.sleep(10000);
            log.info("sleep 10 sec done");

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
            storageConnection = (JDBCStorageConnection) workspaceStorageConnection;
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
            dbConnection.commit();
            dataBaseDropped = true;
            // ============DELETING TEMP FOLDER=============
            boolean successfullyDeleted = deleteDir(new File("../temp"));
            if (successfullyDeleted)
            {
               log.info("Folder 'temp' successfully deleted");
            }
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   private synchronized boolean deleteDir(File dir)
   {
      if (dir.isDirectory())
      {
         String[] children = dir.list();
         for (int i = 0; i < children.length; i++)
         {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success)
            {
               log.warn("Can not delete: " + dir + ", " + children[i]);
               return false;
            }
         }
      }
      // The directory is now empty so delete it
      return dir.delete();
   }

}
