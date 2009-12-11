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
package org.exoplatform.jcr.benchmark.jcrapi.webdav;

import com.sun.japex.TestCase;

import org.exoplatform.common.http.client.CookieModule;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public abstract class AbstractWebdavTest
{

   protected JCRWebdavConnection item;
   protected String nodeName;

   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doPrepare(TestCase tc, WebdavTestContext context) throws Exception
   {
      CookieModule.setCookiePolicyHandler(null);
      item = new JCRWebdavConnection(context);
      nodeName = context.generateUniqueName(this.getClass().getName());
   }
   
   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doFinish(TestCase tc, WebdavTestContext context) throws Exception
   {
      item.stop();
   }
   
   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public abstract void doRun(final TestCase tc, WebdavTestContext context) throws Exception;
}
