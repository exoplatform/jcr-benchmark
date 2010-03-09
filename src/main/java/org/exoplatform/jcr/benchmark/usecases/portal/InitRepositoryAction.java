/*
 * Copyright (C) 2010 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.usecases.portal;

import org.exoplatform.services.jcr.impl.core.RepositoryImpl;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;

/**
 * Action, performing repository initialization, by generating the structure of nodes with defined
 * parameters, like depth and maximum number of node per level (number of child-nodes)
 * 
 * @author <a href="mailto:nikolazius@gmail.com">Nikolay Zamosenchuk</a>
 * @version $Id: InitDataBaseAction.java 34360 2009-07-22 23:58:59Z nzamosenchuk $
 *
 */
public class InitRepositoryAction extends AbstractWriteAction
{
   private Session session;

   private int depth;

   private int nodesPerLevel;

   /**
    * @param session
    *        Within this session, repository is initialised
    * @param rootName
    *        Name of test's root node
    * @param stringValue
    *        Constant for string values
    * @param binaryValue
    *        Constant for binary values
    * @param multiValueSize
    *        Number of items in multi-valued property
    * @param depth
    *        Depth of initialization
    * @param nodesPerLevel
    *        Number of child nodes on each level
    */
   public InitRepositoryAction(Session session, String rootName, String stringValue, byte[] binaryValue,
      int multiValueSize, int depth, int nodesPerLevel)
   {
      super((RepositoryImpl)session.getRepository(), session.getWorkspace().getName(), rootName, depth, stringValue,
         binaryValue, multiValueSize);
      this.session = session;
      this.depth = depth;
      this.nodesPerLevel = nodesPerLevel;
   }

   /**
    * @see org.exoplatform.jcr.benchmark.usecases.portal.AbstractAction#perform()
    */
   @Override
   public void perform() throws RepositoryException
   {
      Node testRoot = session.getRootNode().getNode(getRootNodeName());
      recursivelyFill(testRoot, depth, session.getValueFactory());
   }

   /**
    * Recursively fills repository for currentLevel levels containing maxPerLevel nodes in each.
    * 
    * @param root
    * @param currentLevel
    * @throws RepositoryException
    */
   private void recursivelyFill(Node root, int currentLevel, ValueFactory valueFactory) throws RepositoryException
   {
      if (currentLevel > 0)
      {
         for (int i = 0; i < nodesPerLevel; i++)
         {
            Node target = createGenericNode(root, valueFactory);
            recursivelyFill(target, currentLevel - 1, valueFactory);
         }
      }
   }

}
