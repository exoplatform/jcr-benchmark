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
package org.exoplatform.jcr.benchmark.usecases.lock;

import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class LockUnlockOwnNodeTest
   extends JCRTestBase
{
   /*
    * This test measures performance of lock mechanism, each thread has own node
    */

   private Node rootNode = null;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      rootNode = context.getSession().getRootNode().addNode(context.generateUniqueName("rootNode"), "nt:unstructured");
      rootNode.addMixin("mix:lockable");
      context.getSession().save();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      if (!rootNode.isLocked())
      {
         rootNode.lock(true, true);
      }
      rootNode.addNode(context.generateUniqueName("node"), "nt:unstructured");
      context.getSession().save();
      if (rootNode.isLocked())
      {
         rootNode.unlock();
      }
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      rootNode.remove();
      context.getSession().save();
   }

}
