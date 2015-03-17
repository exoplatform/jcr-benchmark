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
package org.exoplatform.jcr.benchmark.usecases.versioning;

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.impl.core.SessionImpl;
import org.exoplatform.services.jcr.impl.storage.jdbc.JDBCStorageConnection;
import org.exoplatform.services.jcr.impl.storage.jdbc.JDBCWorkspaceDataContainer;
import org.exoplatform.services.jcr.impl.storage.jdbc.statistics.StatisticsJDBCStorageConnection;
import org.exoplatform.services.jcr.storage.WorkspaceStorageConnection;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class CheckinCheckoutOwnNodeCleanDBOracleTest
   extends JCRTestBase
{
   /*
    * Each thread makes a lot of versions of his own node using chekin-checkout methods (between them
    * should be some node operations like adding) many times.
    */

   public static Log LOG = ExoLogger.getLogger("jcr.benchmark");

   public static WorkspaceStorageConnection workspaceStorageConnection = null;

   public static boolean dataBaseDropped = false;

   private Node rootNode = null;

   private String name = "";

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      name = context.generateUniqueName("rootNode");
      rootNode = context.getSession().getRootNode().addNode(name);
      rootNode.addMixin("mix:versionable");
      context.getSession().save();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      rootNode.checkin();
      rootNode.checkout();
      rootNode.addNode(context.generateUniqueName("child"));
      context.getSession().save();
      rootNode.checkin();
      rootNode.checkout();
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      // rootNode.remove();context.getSession().save();
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
                   LOG.error(e.getMessage(), e);
               }
            }
            // ================================
            dbConnection.commit();
            dataBaseDropped = true;
         }
      }
      catch (Exception e)
      {
         LOG.error(e.getMessage(), e);
         throw new RuntimeException(e);
      }
   }
}
