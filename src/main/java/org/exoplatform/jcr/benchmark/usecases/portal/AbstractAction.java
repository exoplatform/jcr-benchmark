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
import org.exoplatform.services.jcr.ext.app.ThreadLocalSessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;

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

   private JCRTestContext context;

   private ThreadLocalSessionProviderService threadLocalSessionProviderService;

   /**
    * Page-usecase action.
    * 
    * @param repository
    *    repository instance
    */
   public AbstractAction(RepositoryImpl repository, JCRTestContext context)
   {
      super();
      this.repository = repository;
      this.context = context;
      this.threadLocalSessionProviderService = new ThreadLocalSessionProviderService();
   }

   /**
    * Returns session, using ThreadLocalSessionProvider
    * 
    * @param anonymous
    * @return
    * @throws RepositoryException
    */
   protected Session getSession(boolean anonymous) throws RepositoryException
   {
      if (threadLocalSessionProviderService.getSessionProvider("") == null)
      {
         threadLocalSessionProviderService.setSessionProvider("", anonymous ? SessionProvider.createAnonimProvider()
            : SessionProvider.createSystemProvider());
      }
      return threadLocalSessionProviderService.getSessionProvider("").getSession(
         context.getSession().getWorkspace().getName(), repository);
   }

   /**
    * Performs action on the page (possibly read, write or what ever)
    */
   abstract void perform() throws RepositoryException;
}