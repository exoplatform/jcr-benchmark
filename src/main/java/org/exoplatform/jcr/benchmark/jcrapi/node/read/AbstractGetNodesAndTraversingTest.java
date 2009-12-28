/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.jcrapi.node.read;

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import javax.jcr.Node;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 2009
 *
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a> 
 * @version $Id$
 */
public abstract class AbstractGetNodesAndTraversingTest extends JCRTestBase
{

   protected Node root;

   protected String nodeName;

   protected String propertyName;

   protected Node node;

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);

      node.remove();
      context.getSession().save();
   }

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      root = context.getSession().getRootNode().addNode(context.generateUniqueName("testRoot"));
      node = root.addNode(nodeName = context.generateUniqueName("testNode"));

      for (int i = 0; i < 100; i++)
      {
         node.addNode(context.generateUniqueName("node"));
      }

      context.getSession().save();
   }

}
