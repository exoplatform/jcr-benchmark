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

import org.exoplatform.services.jcr.core.CredentialsImpl;
import org.exoplatform.services.jcr.core.ExtendedSession;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;
import org.exoplatform.services.security.IdentityConstants;

import java.util.Random;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyType;
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

   private Random random;

   private int depth;

   /**
    * @param repository
    *        Repository instance
    * @param workspace
    *        Workspace name
    * @param rootName
    *        Test's root node name
    */
   public AbstractAction(RepositoryImpl repository, String workspace, String rootName, int depth)
   {
      super();
      this.repository = repository;
      this.workspace = workspace;
      this.rootName = rootName;
      this.depth = depth;
      this.random = new Random();
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
      {
         return (ExtendedSession)repository.login(new CredentialsImpl(IdentityConstants.ANONIM, "".toCharArray()),
            workspace);
      }
      else
      {
         return (ExtendedSession)repository.login(new CredentialsImpl(IdentityConstants.SYSTEM, "".toCharArray()),
            workspace);
      }
   }

   /**
    * Returns next node from hierarchy tree in stochastic (random) order
    * 
    * @param testRoot
    *        Node to start from
    * @return
    */
   public Node nextNode(Node testRoot) throws RepositoryException
   {
      return getNodeOnLevel(testRoot, depth);
   }

   /**
    * Returns next node from hierarchy tree in stochastic (random) order
    * 
    * @param testRoot
    * @return
    * @throws RepositoryException
    */
   public Node nextParent(Node testRoot) throws RepositoryException
   {
      return getNodeOnLevel(testRoot, depth - 1);
   }

   /**
    * @param testRoot
    * @param level
    * @return
    * @throws RepositoryException
    */
   protected Node getNodeOnLevel(Node testRoot, int level) throws RepositoryException
   {
      Node target = testRoot;
      for (int i = 0; i < level; i++)
      {
         NodeIterator iterator = target.getNodes();
         iterator.skip(random.nextInt((int)iterator.getSize()));
         target = iterator.nextNode();
         // Child node should exist!
         if (target == null)
         {
            throw new ItemNotFoundException("Unexpected repository structure, possibly some nodes were deleted.");
         }
      }
      return target;
   }

   /**
    * Generates next random property name by selecting random property type and single/multi
    * value option.
    * 
    * @return
    */
   public String nextPropertyName()
   {
      // we have 9 types of properties, starting from 1 to 9.
      return PropertyType.nameFromValue(random.nextInt(9) + 1) + (random.nextBoolean() ? "-m" : "-s");
   }

   /**
    * Performs action on the page (possibly read, write or what ever)
    */
   public abstract void perform() throws RepositoryException;
}