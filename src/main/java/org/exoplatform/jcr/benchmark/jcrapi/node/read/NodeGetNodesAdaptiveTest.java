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

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

/**
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 * @version $Id: exo-jboss-codetemplates.xml 34360 2009-07-22 23:58:59Z aheritier $
 *
 */
public class NodeGetNodesAdaptiveTest extends AbstractNodeTest
{

   protected int childNodesNumber;

   protected int childPropertiesNumber;

   protected int skipNumber;

   protected int readNumber;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      // Initialize test case paramaters
      if (!tc.hasParam("childNodesNumber"))
      {
         throw new RuntimeException("<childNodesNumber> parameter required");
      }
      this.childNodesNumber = tc.getIntParam("childNodesNumber");

      if (!tc.hasParam("childPropertiesNumber"))
      {
         throw new RuntimeException("<childPropertiesNumber> parameter required");
      }
      this.childPropertiesNumber = tc.getIntParam("childPropertiesNumber");

      if (!tc.hasParam("skipNumber"))
      {
         throw new RuntimeException("<skipNumber> parameter required");
      }
      this.skipNumber = tc.getIntParam("skipNumber");

      if (!tc.hasParam("readNumber"))
      {
         throw new RuntimeException("<readNumber> parameter required");
      }
      this.readNumber = tc.getIntParam("readNumber");

      super.doPrepare(tc, context);
   }

   @Override
   protected void initChildNodes(JCRTestContext context) throws RepositoryException
   {
      addChildNodes(context, childNodesNumber);
   }

   @Override
   protected void initChildProperties(JCRTestContext context) throws RepositoryException
   {
      addChildProperties(context, childPropertiesNumber);
   }

   /**
   * {@inheritDoc}
   */
   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      NodeIterator nodeIterator = node.getNodes();
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
