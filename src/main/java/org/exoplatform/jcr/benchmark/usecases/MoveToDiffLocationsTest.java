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

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;

import org.exoplatform.services.log.Log;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 11.09.2008
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id$
 */
public class MoveToDiffLocationsTest
   extends AbstractMoveUsecaseTest
{

   protected static Log LOG = ExoLogger.getLogger("MoveToDiffDirectionsTest");

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      NodeIterator testNodes = nextNode().getNodes();
      while (testNodes.hasNext())
      {
         Node nodeToMove = testNodes.nextNode();

         // prepare destenation location
         Calendar fileDate = nodeToMove.getProperty("jcr:content/jcr:lastModified").getDate();
         String year = String.valueOf(fileDate.get(Calendar.YEAR));
         String month = String.valueOf(fileDate.get(Calendar.MONTH));
         String day = String.valueOf(fileDate.get(Calendar.DAY_OF_MONTH));

         Node catalog = rootNode.getNode("catalog");
         Node y;
         Node m;
         Node d;

         try
         {
            y = catalog.getNode(year);
         }
         catch (PathNotFoundException e)
         {
            y = catalog.addNode(year);
         }

         try
         {
            m = y.getNode(month);
         }
         catch (PathNotFoundException e)
         {
            m = y.addNode(month);
         }

         try
         {
            d = m.getNode(day);
         }
         catch (PathNotFoundException e)
         {
            d = m.addNode(day);
         }

         // move using Session.move
         long start = System.currentTimeMillis();
         String src = nodeToMove.getPath();
         String dest = d.getPath() + "/" + nodeToMove.getName();
         // rootNode.getSession().move(src, dest);
         // rootNode.save();
         rootNode.getSession().getWorkspace().move(src, dest);

         LOG.info(src + " --> " + dest + " -- " + (System.currentTimeMillis() - start) + "ms");
      }
   }
}
