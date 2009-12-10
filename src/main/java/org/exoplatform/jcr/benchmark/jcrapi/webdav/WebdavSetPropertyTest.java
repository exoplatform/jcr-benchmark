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

import org.exoplatform.jcr.benchmark.JCRTestContext;

import javax.jcr.Node;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class WebdavSetPropertyTest extends AbstractWebdavTest
{
   
   private String node;
   private String property = "DAV:testProp";
   
   /**
    * @see org.exoplatform.jcr.benchmark.jcrapi.webdav.AbstractWebdavTest#doPrepare(com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.JCRTestContext)
    */
   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);
      
      Node rootNode = context.getSession().getRootNode();
      node = context.generateUniqueName("testNode");
      Node testNode = rootNode.addNode(node, "nt:unstructured");
      
   }

   /**
    * @see org.exoplatform.jcr.benchmark.JCRTestBase#doRun(com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.JCRTestContext)
    */
   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      item.setProperty(node, property, "testValue");
   }

}
