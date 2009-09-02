/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.jcr.benchmark.ext.asyncrep.AsyncTestBase;
import org.exoplatform.jcr.benchmark.ext.asyncrep.AsyncTestContext;
import org.exoplatform.services.jcr.RepositoryService; //import org.exoplatform.services.jcr.ext.replication.async.AsyncReplication;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.Params;
import com.sun.japex.ParamsImpl;
import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 
 *
 * @author <a href="karpenko.sergiy@gmail.com">Karpenko Sergiy</a> 
 * @version $Id: AsyncDriver.java 111 2008-11-11 11:11:11Z serg $
 */
public class AsyncDriver
   extends JapexDriverBase
{

   protected Repository repository;

   //TODO
   //  protected AsyncReplication replication;

   protected Session oneSession;

   protected Credentials credentials;

   protected String workspace;

   protected AsyncTestContext context;

   private AsyncTestBase test;

   @Override
   public void initializeDriver()
   {
      if (!hasParam("jcr.user"))
         throw new RuntimeException("<jcr.user> parameter required");
      if (!hasParam("jcr.password"))
         throw new RuntimeException("<jcr.password> parameter required");
      if (!hasParam("jcr.workspace"))
         throw new RuntimeException("<jcr.workspace> parameter required");

      String user = getParam("jcr.user");
      String password = getParam("jcr.password");
      Params params = new ParamsImpl();
      params.setParam("exo.jaasConf", getParam("exo.jaasConf"));
      params.setParam("exo.containerConf", getParam("exo.containerConf"));
      try
      {
         initialize(params);
         workspace = getParam("jcr.workspace");
         credentials = new SimpleCredentials(user, password.toCharArray());
         oneSession = repository.login(credentials, workspace);
         context = new AsyncTestContext();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   @Override
   public void prepare(final TestCase tc)
   {
      try
      {
         // System.out.println("Start...");
         context = initContext(tc, context);
         // System.out.println("initContext done...");
         test = testInstance(tc);
         // System.out.println("testInstance done...");
         test.doPrepare(tc, context);
         // System.out.println("doPrepare done...");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   @Override
   public void run(final TestCase tc)
   {
      try
      {
         test.doRun(tc, context);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         try
         {
            Session s = context.getSession();
            if (s != null)
               s.refresh(false);
         }
         catch (RepositoryException e1)
         {
            System.err.println("Can not refresh test session. Reason: " + e1.getMessage());
         }
      }
   }

   @Override
   public void finish(final TestCase tc)
   {
      try
      {
         test.doFinish(tc, context);
         context.getSession().logout();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   private synchronized AsyncTestBase testInstance(TestCase tc)
   {

      if (!tc.hasParam("exo.testClass"))
         throw new RuntimeException("<exo.testClass> parameter required");

      try
      {
         String testCaseName = tc.getParam("exo.testClass");
         return (AsyncTestBase) Class.forName(testCaseName).newInstance();

      }
      catch (Throwable exception)
      {
         exception.printStackTrace();
         throw new RuntimeException(exception.getMessage(), exception);
      }
   }

   private synchronized AsyncTestContext initContext(TestCase tc, AsyncTestContext context)
   {
      // context.setSession(oneSession);

      // TODO
      /*if (!hasParam("jcr.sessionPolicy"))
        throw new RuntimeException("<jcr.sessionPolicy> parameter required");
      String sessionPolicy = getParam("jcr.sessionPolicy");
      if (sessionPolicy.equalsIgnoreCase("single")) {
        context.setSession(oneSession);
        context.setCredentials(credentials);
        context.setReplicationServer(replication);
      } else if (sessionPolicy.equalsIgnoreCase("multiple"))
        try {
          context.setSession(repository.login(credentials, workspace));
          context.setCredentials(credentials);
          context.setReplicationServer(replication);
        } catch (LoginException e) {
          throw new RuntimeException(e);
        } catch (RepositoryException e) {
          throw new RuntimeException(e);
        }
      else
        throw new RuntimeException("<sessionPolicy> parameter expects 'single' or 'multiple' values. Found "
            + sessionPolicy);*/

      return context;
   }

   private void initialize(Params params)
   {
      //TODO
      /*if (!params.hasParam("exo.jaasConf"))
        throw new RuntimeException("<exo.jaasConf> parameter required");

      if (!params.hasParam("exo.containerConf"))
        throw new RuntimeException("<exo.containerConf> parameter required");

      String jaasConf = params.getParam("exo.jaasConf");
      String containerConf = params.getParam("exo.containerConf");
      try {

        StandaloneContainer.addConfigurationPath(containerConf);
        StandaloneContainer container = StandaloneContainer.getInstance();
        if (System.getProperty("java.security.auth.login.config") == null)
          System.setProperty("java.security.auth.login.config", Thread.currentThread()
                                                                      .getContextClassLoader()
                                                                      .getResource(jaasConf)
                                                                      .toString());
        RepositoryService repositoryService = (RepositoryService) container.getComponentInstanceOfType(RepositoryService.class);
        repository = repositoryService.getCurrentRepository();
        
        System.out.println();
        replication = (AsyncReplication) container.getComponentInstanceOfType(AsyncReplication.class);
        if(replication == null){
          throw new NullPointerException("Replication is null");
        }
        System.out.println("REPLICATION -"+replication.toString());
      } catch (Exception e) {
        e.printStackTrace();
      }*/
   }
}