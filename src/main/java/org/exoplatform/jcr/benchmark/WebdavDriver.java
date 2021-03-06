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
import com.sun.japex.TestCase;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

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
   private static final Log LOG = ExoLogger.getLogger(WebdavDriver.class);

   public static final String WEBDAV_HOST = "webdav.host";

   public static final String WEBDAV_PORT = "webdav.port";

   public static final String WEBDAV_REALM = "webdav.realm";

   public static final String WEBDAV_USER = "webdav.user";

   public static final String WEBDAV_PASSWORD = "webdav.password";

   public static final String WEBDAV_WORKSPACE_PATH = "webdav.workspacePath";

   protected WebdavTestContext context;

   private AbstractWebdavTest test;

   public void initializeDriver()
   {
      if (!hasParam(WEBDAV_HOST))
         throw new RuntimeException("<webdav.host> parameter required");
      if (!hasParam(WEBDAV_PORT))
         throw new RuntimeException("<webdav.port> parameter required");
      if (!hasParam(WEBDAV_REALM))
         throw new RuntimeException("<webdav.realm> parameter required");
      if (!hasParam(WEBDAV_USER))
         throw new RuntimeException("<webdav.user> parameter required");
      if (!hasParam(WEBDAV_PASSWORD))
         throw new RuntimeException("<webdav.password> parameter required");
      if (!hasParam(WEBDAV_WORKSPACE_PATH))
         throw new RuntimeException("<webdav.workspacePath> parameter required");
      
      // System.out.println("Start...");
      context = new WebdavTestContext();
      context = initContext(context);
      // System.out.println("initContext done...");
   }

   @Override
   public void prepare(final TestCase tc)
   {
      try
      {
         test = testInstance(tc);
         // System.out.println("testInstance done...");
         test.doPrepare(tc, context);
         // System.out.println("doPrepare done...");
      }
      catch (Exception e)
      {
         LOG.error(e.getMessage(), e);
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
         LOG.error(e.getMessage(), e);
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
         LOG.error(e.getMessage(), e);
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
      catch (Throwable exception)//NOSONAR
      {
         LOG.error(exception.getMessage(), exception);
         throw new RuntimeException(exception.getMessage(), exception);
      }
   }

   private synchronized WebdavTestContext initContext(WebdavTestContext context)
   {
      context.put(WEBDAV_HOST, getParam(WEBDAV_HOST));
      context.put(WEBDAV_PORT, getParam(WEBDAV_PORT));
      context.put(WEBDAV_USER, getParam(WEBDAV_USER));
      context.put(WEBDAV_PASSWORD, getParam(WEBDAV_PASSWORD));
      context.put(WEBDAV_REALM, getParam(WEBDAV_REALM));
      context.put(WEBDAV_WORKSPACE_PATH, getParam(WEBDAV_WORKSPACE_PATH));

      return context;
   }
}