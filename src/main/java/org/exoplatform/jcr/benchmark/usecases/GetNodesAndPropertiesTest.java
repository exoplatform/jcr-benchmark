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

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;

import org.exoplatform.services.log.Log;
import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class GetNodesAndPropertiesTest
   extends JCRTestBase
{
   protected static Log log = ExoLogger.getLogger("GetNodesAndPropertiesTest");

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      Session session = context.getSession();
      String rootName = tc.getParam("jcr.rootNodeName");
      try
      {
         log.info("Get nodes start in " + rootName);

         Node node = (Node) session.getItem(rootName);

         int pcount = 0;
         PropertyIterator piter = node.getProperties();
         while (piter.hasNext())
         {
            try
            {
               piter.nextProperty().getString();
            }
            catch (ValueFormatException e)
            {
               log.error(e);
            }
            pcount++;
         }

         NodeIterator ni = node.getNodes();

         int ncount = 0;
         while (ni.hasNext())
         {
            ni.nextNode();
            ncount++;
         }

         log.info("Get nodes " + ncount + " (" + pcount + " properties) time in report ");
      }
      catch (PathNotFoundException e)
      {
         log.error(e);
      }
      catch (RepositoryException e)
      {
         log.error(e);
      }

   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      // System.out.println("DO FINISH AddNodeTest "+runtime);
   }

}
