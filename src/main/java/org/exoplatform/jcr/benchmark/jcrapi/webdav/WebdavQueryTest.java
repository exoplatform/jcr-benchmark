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
package org.exoplatform.jcr.benchmark.jcrapi.webdav;

import com.sun.japex.TestCase;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class WebdavQueryTest extends AbstractWebdavTest
{

   private boolean searcheable = true;
   
   private String searcheableContent = "Create test what will be run over WebDav what will be use full text search." +
   		"For example add n-different content string to the different nodes with length more then 100. ";
   private String fakeContent = "Collections: The ability to create sets of documents and to retrieve" +
   		"a hierarchical membership listing (like a directory listing in a file system).";
   
   private String query = "select * from nt:base where contains(*, '" + searcheableContent + "')";
   
   
   /**
    * @see org.exoplatform.jcr.benchmark.jcrapi.webdav.AbstractWebdavTest#createContent(java.lang.String, com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.jcrapi.webdav.WebdavTestContext)
    */
   @Override
   protected void createContent(String parentNodeName, TestCase tc, WebdavTestContext context) throws Exception
   {
      String nodeName = parentNodeName + "/" + context.generateUniqueName(this.getClass().getName());
      if(searcheable){
         item.addNode(nodeName, searcheableContent.getBytes());
      } else {
         item.addNode(nodeName, fakeContent.getBytes());
      }
      searcheable = !searcheable;
      addNode(nodeName);
   }

   /**
    * @see org.exoplatform.jcr.benchmark.jcrapi.webdav.AbstractWebdavTest#doRun(com.sun.japex.TestCase, org.exoplatform.jcr.benchmark.jcrapi.webdav.WebdavTestContext)
    */
   @Override
   public void doRun(TestCase tc, WebdavTestContext context) throws Exception
   {
      item.sqlQuery(query);
      
   }

}
