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

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ReadPageAction extends AbstractAction
{

   private int nodeCount;

   private int propertyCount;

   private boolean anonymous;

   /**
    * @param repository
    * @param context
    */
   public ReadPageAction(RepositoryImpl repository, JCRTestContext context, int nodeCount, int propertyCount,
      boolean anonymous)
   {
      super(repository, context);
      this.nodeCount = nodeCount;
      this.propertyCount = propertyCount;
      this.anonymous = anonymous;
   }

   /**
    * @see org.exoplatform.jcr.benchmark.usecases.portal.AbstractAction#perform(javax.jcr.Session)
    */
   @Override
   void perform()  throws RepositoryException
   {
      Session session = null;
      try
      {
         session = getSession(anonymous);
         // TODO: Thread-safe operation goes here
      }
      catch (Exception e)
      {
         // TODO: handle exception
         PageUsecasesTest.log.error("Error performing read page usecase.", e);
      }
      finally
      {
         if (session != null)
         {
            session.logout(); // TODO: Do logout or not? Should the session be cached
         }
      }
   }

}