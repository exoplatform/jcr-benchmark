/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.usecases.query;

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 2011
 *
 * @author <a href="mailto:alex.reshetnyak@exoplatform.com.ua">Alex Reshetnyak</a> 
 * @version $Id: SearchNodesByContentUsingLikePathOperationTest.java 111 2011-11-11 11:11:11Z rainf0x $
 */
public class SearchNodesByContentUsingLikePathOperationTest extends JCRTestBase
{
   /*
    * This test calculates the time of query execution, dedicated structure has been created.
    */

   public static Log log = ExoLogger.getLogger("jcr.benchmark");

   private int RESULT_NODES = 5;

   private String sqlQuery = "";

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      String rootPath = tc.getParam("jcr.rootPath");

      sqlQuery = "select * from nt:base where contains(*,'fournodes') and jcr:path like '/" + rootPath + "/%'";
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      QueryManager manager = context.getSession().getWorkspace().getQueryManager();
      Query query = manager.createQuery(sqlQuery, Query.SQL);
      QueryResult queryResult = query.execute();
      NodeIterator nodeIterator = queryResult.getNodes();
      if (nodeIterator.getSize() != RESULT_NODES && nodeIterator.getSize() != RESULT_NODES - 1) //TODO never save this!!!!
      {
         log.error("Must be founded " + RESULT_NODES + " nodes but was: " + nodeIterator.getSize());
      }
      while (nodeIterator.hasNext())
      {
         Node node = nodeIterator.nextNode();
      }
   }

}
