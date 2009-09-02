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

import org.exoplatform.services.log.Log;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:Sergey.Kabashnyuk@gmail.com">Sergey Kabashnyuk</a>
 * @version $Id: $
 */
public class ChangeNamespace
   extends AbstractNamespaceTest
{
   /**
    * Class logger.
    */
   private static final Log LOG = ExoLogger.getLogger(ChangeNamespace.class);

   @Override
   public void prepare(TestCase arg0, JCRTestContext context) throws Exception
   {
      if (runIterations <= 0)
         throw new Exception("japex.runIterations should be a positive number, but " + runIterations);

      for (int i = 0; i < runIterations; i++)
      {
         String prefix = context.generateUniqueName("addNamespace");
         namespaceRegistry.registerNamespace(prefix, "http://" + prefix + ".uri/jcr");
         namespaces.add(prefix);
      }
   }

   @Override
   public void doRun(TestCase arg0, JCRTestContext arg1) throws Exception
   {
      String ns = nextNamespace();
      namespaceRegistry.registerNamespace(ns, "http://" + ns + ".changed/jcr");
   }
}
