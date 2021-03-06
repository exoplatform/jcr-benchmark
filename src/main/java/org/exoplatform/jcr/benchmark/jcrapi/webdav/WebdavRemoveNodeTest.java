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

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class WebdavRemoveNodeTest extends AbstractWebdavTest
{
   
   /**
    * {@inheritDoc}
    */
   protected void createContent(String parentNodeName, TestCase tc, WebdavTestContext context) throws Exception
   {
      String nodeName = parentNodeName + "/" + context.generateUniqueName(this.getClass().getName());
      
      item.addNode(nodeName, "".getBytes());
      
      addNode(nodeName);
   }
   
   /**
    * @see org.exoplatform.jcr.benchmark.JCRTestBase#doRun(com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.JCRTestContext)
    */
   @Override
   public void doRun(TestCase tc, WebdavTestContext context) throws Exception
   {
      item.removeNode(nextNodePath());
   }
}
