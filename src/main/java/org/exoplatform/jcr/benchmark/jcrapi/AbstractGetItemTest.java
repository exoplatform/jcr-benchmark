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
package org.exoplatform.jcr.benchmark.jcrapi;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: AbstractGetItemTest.java 13537 2008-04-22 08:22:36Z vetalok $
 */

public abstract class AbstractGetItemTest
   extends AbstractItemsTest
{

   private List<Node> nodes = new ArrayList<Node>();

   private List<Property> properties = new ArrayList<Property>();

   private volatile int iteration = 0;

   /**
    * Should be used before nextNode() call.
    * 
    * @return
    */
   protected int getCurrentIteration()
   {
      return iteration;
   }

   protected Node nextNode()
   {
      return nodes.get(iteration++);
   }

   protected void addNode(Node node)
   {
      nodes.add(node);
   }

   protected Property nextProperty()
   {
      return properties.get(iteration++);
   }

   protected void addProperty(Property property)
   {
      properties.add(property);
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);

      nodes.clear();
      nodes = null;
      properties.clear();
      properties = null;
   }

}
