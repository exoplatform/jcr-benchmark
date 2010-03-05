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

import org.exoplatform.services.jcr.core.ExtendedSession;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Action performing read usecase.
 * 
 * @author <a href="mailto:nikolazius@gmail.com">Nikolay Zamosenchuk</a>
 * @version $Id: Action.java 34360 2009-07-22 23:58:59Z nzamosenchuk $
 */
public abstract class AbstractAction
{

   private RepositoryImpl repository;

   private String workspace;

   private String rootName;

   /**
    * @param repository
    *        Repository instance
    * @param workspace
    *        Workspace name
    * @param rootName
    *        Test's root node name
    */
   public AbstractAction(RepositoryImpl repository, String workspace, String rootName)
   {
      super();
      this.repository = repository;
      this.workspace = workspace;
      this.rootName = rootName;
   }

   /**
    * @return The name of test's root node 
    */
   public String getRootNodeName()
   {
      return rootName;
   }

   /**
    * Returns session, using ThreadLocalSessionProvider
    * 
    * @param anonymous
    *        If true, then anonymous session is returned
    * @return
    * @throws RepositoryException
    */
   public Session getSession(boolean anonymous) throws RepositoryException
   {
      if (anonymous)
         return (ExtendedSession)repository.login(workspace);
      else
         return (ExtendedSession)repository.getSystemSession(workspace);
   }

   /**
    * @param testRoot
    * @return
    */
   public Node nextNode(Node testRoot)
   {
      // TODO: implement next node selection
      return testRoot;
   }

   /**
    * Performs action on the page (possibly read, write or what ever)
    */
   abstract void perform() throws RepositoryException;
}