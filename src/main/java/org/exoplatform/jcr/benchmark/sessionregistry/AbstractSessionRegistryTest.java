/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.sessionregistry;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.core.CredentialsImpl;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;
import org.exoplatform.services.jcr.impl.core.SessionImpl;
import org.exoplatform.services.jcr.impl.core.SessionRegistry;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: AbstractOrganizationTest.java 111 2008-11-11 11:11:11Z $
 */
public abstract class AbstractSessionRegistryTest
   extends JCRTestBase
{

   protected SessionRegistry sessionRegistry;

   private List<SessionImpl> sessionList;

   private List<String> sessionIdList;

   private int indexSession = 0;

   private int indexSessionId = 0;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      int runIterations = tc.getIntParam("japex.runIterations");

      if (tc.hasParam("japex.warmupIterations"))
      {
         int warmUpIterations = tc.getIntParam("japex.warmupIterations");
         if (warmUpIterations>0) 
         {
            runIterations += warmUpIterations;
         }
      }
      
      CredentialsImpl credentials = new CredentialsImpl("root", "exo".toCharArray());
      RepositoryImpl repository = (RepositoryImpl) context.getSession().getRepository();

      sessionRegistry =
               (SessionRegistry) ((SessionImpl) context.getSession()).getContainer().getComponentInstanceOfType(
                        SessionRegistry.class);

      sessionList = new ArrayList<SessionImpl>();
      sessionIdList = new ArrayList<String>();
      int sessionCount = tc.getIntParam("japex.numberOfThreads") * runIterations;
      for (int i = 0; i < sessionCount; i++)
      {
         sessionList.add((SessionImpl) repository.login(credentials, "system"));

         SessionImpl workSession = (SessionImpl) repository.login(credentials, "system");
         sessionRegistry.registerSession(workSession);
         sessionIdList.add(workSession.getId());
      }
   }

   protected SessionImpl nextSession()
   {
      return sessionList.get(indexSession++);
   }

   protected String nextSessionId()
   {
      return sessionIdList.get(indexSessionId++);
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      sessionList.clear();
      sessionList = null;
      sessionIdList.clear();
      sessionIdList = null;

      super.doFinish(tc, context);
   }
}
