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

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.exoplatform.jcr.benchmark.ext.asyncrep.AsyncTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.webdav.AbstractWebdavTest;
import org.exoplatform.jcr.benchmark.jcrapi.webdav.WebdavTestContext;

import com.sun.japex.JapexDriverBase;
import com.sun.japex.Params;
import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 2009
 *
 * @author <a href="mailto:alex.reshetnyak@exoplatform.com.ua">Alex Reshetnyak</a> 
 * @version $Id$
 */
public class WebdavDriver
   extends JapexDriverBase
{
   protected WebdavTestContext context;
   
   private AbstractWebdavTest test;

   public void initializeDriver()
   {
      /*if (!hasParam("jcr.user"))
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
      }*/
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
      }
   }

   @Override
   public void finish(final TestCase tc)
   {
      try
      {
         test.doFinish(tc, context);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   private synchronized AbstractWebdavTest testInstance(TestCase tc)
   {

      if (!tc.hasParam("exo.testClass"))
         throw new RuntimeException("<exo.testClass> parameter required");

      try
      {
         String testCaseName = tc.getParam("exo.testClass");
         return (AbstractWebdavTest) Class.forName(testCaseName).newInstance();

      }
      catch (Throwable exception)
      {
         exception.printStackTrace();
         throw new RuntimeException(exception.getMessage(), exception);
      }
   }

   private synchronized WebdavTestContext initContext(TestCase tc, WebdavTestContext context)
   {
      return context;
   }
}