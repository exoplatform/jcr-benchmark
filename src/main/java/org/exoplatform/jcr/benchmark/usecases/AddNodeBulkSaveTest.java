/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.usecases;

import javax.jcr.Node;
import javax.jcr.Session;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */
public class AddNodeBulkSaveTest
   extends JCRTestBase
{

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      Session session = context.getSession();
      try
      {
         for (int i = 0; i < 100; i++)
         {
            String name = context.generateUniqueName("node");
            Node node = session.getRootNode().addNode(name, "nt:unstructured");
            node.setProperty("My property", "My data, I like the data, it's cool I have a data, it's very very nice.");
         }
         session.save();
      }
      catch (Exception e)
      {
         LOG.error(e.getMessage(), e);
         session.refresh(false);
      }
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      // System.out.println("DO FINISH AddNodeTest "+runtime);
   }

}
