/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
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

import org.exoplatform.jcr.benchmark.JCRTestContext;

import javax.jcr.RepositoryException;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 2011
 *
 * @author <a href="mailto:alex.reshetnyak@exoplatform.com.ua">Alex Reshetnyak</a> 
 * @version $Id$
 */
public abstract class AbstractPatternTest
   extends AbstractNodeTest
{

   protected int getChildNodesCount()
   {
      return 0;
   }

   protected int getChildPropertiesCount()
   {
      return 0;
   }

   protected abstract String getNodeNamePattern();

   protected abstract String getProperyNamePattern();
   
   protected abstract String getNodeNameAsPattern();

   protected abstract String getProperyNameAsPattern();

   protected boolean isCached()
   {
      return false;
   }

   /**
    * {@inheritDoc}
    */
   protected void initChildNodes(JCRTestContext context) throws RepositoryException
   {
      addChildNodes(context, getChildNodesCount() - 1);
      
      node.addNode(getNodeNameAsPattern());

      if (isCached())
      {
         node.getNodes(getNodeNamePattern());
      }
   }

   /**
    * {@inheritDoc}
    */
   protected void initChildProperties(JCRTestContext context) throws RepositoryException
   {
      addChildProperties(context, getChildPropertiesCount() - 1);

      node.setProperty(getProperyNameAsPattern(), node);

      if (isCached())
      {
         node.getProperties(getProperyNamePattern());
      }
   }

   /**
    * {@inheritDoc}
    */
   protected void addChildNodes(JCRTestContext context, int amount) throws RepositoryException
   {
      for (int i = 0; i < amount; i++)
      {
         node.addNode(context.generateUniqueName("node"));
      }
   }

   /**
    * {@inheritDoc}
    */
   protected void addChildProperties(JCRTestContext context, int amount) throws RepositoryException
   {
      for (int i = 0; i < amount; i++)
      {
         node.setProperty(context.generateUniqueName("property"), node);
      }
   }
}
