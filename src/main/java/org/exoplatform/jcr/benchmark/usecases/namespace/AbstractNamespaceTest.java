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
package org.exoplatform.jcr.benchmark.usecases.namespace;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.NamespaceException;

import org.exoplatform.services.log.Log;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.impl.core.NamespaceRegistryImpl;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

public abstract class AbstractNamespaceTest
   extends JCRTestBase
{

   private static final Log LOG = ExoLogger.getLogger(AbstractNamespaceTest.class);

   protected NamespaceRegistryImpl namespaceRegistry;

   protected List<String> namespaces = new ArrayList<String>();

   protected int iteration = 0;

   protected int runIterations;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);
      namespaceRegistry = (NamespaceRegistryImpl) context.getSession().getWorkspace().getNamespaceRegistry();
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

   protected String nextNamespace()
   {
      return namespaces.get(iteration++);
   }

   @Override
   public void doFinish(TestCase arg0, JCRTestContext arg1) throws Exception
   {
      super.doFinish(arg0, arg1);
      for (String ns : namespaces)
      {

         try
         {

            namespaceRegistry.getNamespaceURIByPrefix(ns);
            namespaceRegistry.unregisterNamespace(ns);
         }
         catch (NamespaceException e)//NOSONAR
         {
            // ok
         }

      }
      namespaces.clear();
   }

   public abstract void prepare(TestCase tc, JCRTestContext context) throws Exception;
}
