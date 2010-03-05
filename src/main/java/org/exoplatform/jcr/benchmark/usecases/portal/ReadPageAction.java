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

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Accesses the JCR in anonymous/connected mode to x JCR nodes and for each 
 * JCR Node accesses to y JCR properties (x and y are set in the configuration)
 * 
 * @author <a href="mailto:nikolazius@gmail.com">Nikolay Zamosenchuk</a>
 * @version $Id: ReadPageAction.java 34360 2009-07-22 23:58:59Z nzamosenchuk $
 *
 */
public class ReadPageAction extends AbstractAction
{

   private int nodeCount;

   private int propertyCount;

   private boolean anonymous;

   /**
    * @param repository
    *        Repository instance
    * @param workspace
    *        Workspace name
    * @param rootName
    *        Name of test's root node
    * @param nodeCount
    *        Number of nodes to read
    * @param propertyCount
    *        Number of properties to read
    * @param anonymous
    *        Is true, then anonymous session is used.
    */
   public ReadPageAction(RepositoryImpl repository, String workspace, String rootName, int nodeCount,
      int propertyCount, boolean anonymous)
   {
      super(repository, workspace, rootName);
      this.nodeCount = nodeCount;
      this.propertyCount = propertyCount;
      this.anonymous = anonymous;
   }

   /**
    * @see org.exoplatform.jcr.benchmark.usecases.portal.AbstractAction#perform(javax.jcr.Session)
    */
   @Override
   void perform() throws RepositoryException
   {
      Session session = null;
      try
      {
         session = getSession(anonymous);
         // TODO: Operation goes here
      }
      finally
      {
         if (session != null)
         {
            session.logout();
         }
      }
   }

}