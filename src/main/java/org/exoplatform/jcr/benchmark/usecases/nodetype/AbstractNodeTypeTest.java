/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.jcr.benchmark.usecases.nodetype;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.nodetype.NoSuchNodeTypeException;

import org.exoplatform.services.log.Log;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.core.nodetype.ExtendedNodeTypeManager;
import org.exoplatform.services.jcr.core.nodetype.NodeTypeValue;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

public abstract class AbstractNodeTypeTest
   extends JCRTestBase
{

   private static final Log LOG = ExoLogger.getLogger(AbstractNodeTypeTest.class);

   protected ExtendedNodeTypeManager extNodeTypeManager;

   protected List<NodeTypeValue> nodeTypes = new ArrayList<NodeTypeValue>();

   protected int iteration = 0;

   protected int runIterations;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);
      extNodeTypeManager = (ExtendedNodeTypeManager) context.getSession().getWorkspace().getNodeTypeManager();
      runIterations = tc.getIntParam("japex.runIterations");

      if (tc.hasParam("japex.warmupIterations"))
      {
         int warmUpIterations = tc.getIntParam("japex.warmupIterations");
         if (warmUpIterations>0) 
         {
            runIterations += warmUpIterations;
         }
      }
      prepare(tc, context);
   }

   protected NodeTypeValue nextNodeTypeValue()
   {
      return nodeTypes.get(iteration++);
   }

   @Override
   public void doFinish(TestCase arg0, JCRTestContext arg1) throws Exception
   {
      super.doFinish(arg0, arg1);
      for (NodeTypeValue nt : nodeTypes)
      {
         try
         {
            extNodeTypeManager.getNodeType(nt.getName());
            extNodeTypeManager.unregisterNodeType(nt.getName());
         }
         catch (NoSuchNodeTypeException e)
         {
            // ok
         }

      }
      nodeTypes.clear();
   }

   public abstract void prepare(TestCase tc, JCRTestContext context) throws Exception;
}
