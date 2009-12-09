/*
 * Copyright (C) 2009 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.jcrapi.webdav;

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestContext;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class WebdavAddNodeTest extends AbstractWebdavTest
{

   /**
    * @see org.exoplatform.jcr.benchmark.JCRTestBase#doRun(com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.JCRTestContext)
    */
   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      item.addNode(context.generateUniqueName("node"));
   }

}
