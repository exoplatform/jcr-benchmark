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
import org.exoplatform.services.jcr.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

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

   private int missingNodeCount;

   private int missingPropertyCount;

   private boolean anonymous;

   private List<String> queires;

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
   public ReadPageAction(RepositoryImpl repository, String workspace, String rootName, int depth, int nodeCount,
      int propertyCount, int missingNodeCount, int missingPropertyCount, boolean anonymous)
   {
      //List<String> s = Collections.emptyList();
      this(repository, workspace, rootName, depth, nodeCount, propertyCount, missingNodeCount, missingPropertyCount,
         anonymous, null);
   }

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
    * @param queries
    *        List of queries to perform inside operation
    */
   public ReadPageAction(RepositoryImpl repository, String workspace, String rootName, int depth, int nodeCount,
      int propertyCount, int missingNodeCount, int missingPropertyCount, boolean anonymous, List<String> queries)
   {
      super(repository, workspace, rootName, depth);
      this.nodeCount = nodeCount;
      this.propertyCount = propertyCount;
      this.anonymous = anonymous;
      this.missingNodeCount = missingNodeCount;
      this.missingPropertyCount = missingPropertyCount;
      this.queires = new ArrayList<String>();
      if (queries != null)
      {
         this.queires.addAll(queries);
      }
   }

   /**
    * @see org.exoplatform.jcr.benchmark.usecases.portal.AbstractAction#perform(javax.jcr.Session)
    */
   @Override
   public void perform() throws RepositoryException
   {
      Session session = null;
      try
      {
         session = getSession(anonymous);
         Node testRoot = session.getRootNode().getNode(getRootNodeName());
         Node target = null;
         // get node configured times
         for (int i = 0; i < nodeCount; i++)
         {
            target = nextNode(testRoot);
            // for each node access properties configured times
            for (int j = 0; j < propertyCount; j++)
            {
               try
               {
                  // access random property
                  target.getProperty(nextPropertyName());
               }
               catch (PathNotFoundException e)
               {
                  // possibly property not exist, but this is still the
                  // access to the property.
               }
            }
         }
         // access missing items
         target = nextNode(testRoot);
         for (int i = 0; i < missingPropertyCount; i++)
         {
            try
            {
               // access missing property
               target.getProperty(IdGenerator.generate());
            }
            catch (PathNotFoundException e)
            {
               // this exception is expected
            }
         }
         for (int i = 0; i < missingNodeCount; i++)
         {
            try
            {
               // access missing node
               testRoot.getNode(IdGenerator.generate());
            }
            catch (PathNotFoundException e)
            {
               // this exception is expected
            }
         }

         for (String queryLine : queires)
         {
            // get QueryManager
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            // make SQL query
            Query query = queryManager.createQuery(queryLine.trim(), Query.SQL);
            // execute query
            QueryResult result = query.execute();
         }

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