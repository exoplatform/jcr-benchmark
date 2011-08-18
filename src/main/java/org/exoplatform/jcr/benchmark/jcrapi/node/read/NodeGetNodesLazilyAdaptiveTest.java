/*
 * Copyright (C) 2011 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.jcr.benchmark.jcrapi.node.read;

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.core.ExtendedNode;

import javax.jcr.NodeIterator;

/**
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 * @version $Id: exo-jboss-codetemplates.xml 34360 2009-07-22 23:58:59Z aheritier $
 *
 */
public class NodeGetNodesLazilyAdaptiveTest extends NodeGetNodesAdaptiveTest
{

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      NodeIterator nodeIterator = ((ExtendedNode)node).getNodesLazily();
      if (readNumber > 0)
      {
         if (skipNumber > 0)
         {
            nodeIterator.skip(skipNumber);
         }
         for (int i = 0; i < readNumber; i++)
         {
            nodeIterator.nextNode();
         }
      }
   }

}
