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
public class MoveToSameLocationTest
   extends AbstractMoveUsecaseTest
{

   protected static Log LOG = ExoLogger.getLogger("MoveToSameLocationTest");

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      NodeIterator testNodes = nextNode().getNodes();
      while (testNodes.hasNext())
      {
         Node nodeToMove = testNodes.nextNode();

         Node catalog = rootNode.getNode("catalog");

         // move using Session.move
         long start = System.currentTimeMillis();
         String src = nodeToMove.getPath();
         String dest = catalog.getPath() + "/" + nodeToMove.getName();
         // rootNode.getSession().move(src, dest);
         // rootNode.save();
         rootNode.getSession().getWorkspace().move(src, dest);

         LOG.info(src + " --> " + dest + " -- " + (System.currentTimeMillis() - start) + "ms");
      }
   }
}
