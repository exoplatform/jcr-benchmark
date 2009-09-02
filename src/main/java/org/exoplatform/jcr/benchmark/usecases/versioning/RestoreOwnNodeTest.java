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
package org.exoplatform.jcr.benchmark.usecases.versioning;

import java.util.Random;

import javax.jcr.Node;
import javax.jcr.version.Version;

import org.exoplatform.services.log.Log;
import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class RestoreOwnNodeTest
   extends JCRTestBase
{
   /*
    * Each thread restores own node to some of few versions which has been created before
    */

   public static Log log = ExoLogger.getLogger("jcr.benchmark");

   private Node rootNode = null;

   private Random rand = new Random();

   private String name = "";

   private Version version0 = null;

   private Version version1 = null;

   private Version version2 = null;

   private Version version3 = null;

   private Version version4 = null;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      name = context.generateUniqueName("rootNode");
      rootNode = context.getSession().getRootNode().addNode(name);
      rootNode.addMixin("mix:versionable");
      context.getSession().save();
      version0 = rootNode.checkin();
      rootNode.checkout();
      rootNode.addNode("someChildNode");
      context.getSession().save();
      version1 = rootNode.checkin();
      rootNode.checkout();
      // rootNode.setProperty("someProperty", "someValue");//JCR-370
      rootNode.getNode("someChildNode").remove();
      context.getSession().save();
      version2 = rootNode.checkin();
      rootNode.checkout();
      rootNode.addNode("otherChildNode1").addNode("otherChildNode2");
      context.getSession().save();
      version3 = rootNode.checkin();
      rootNode.checkout();
      rootNode.getNode("otherChildNode1/otherChildNode2").remove();
      context.getSession().save();
      version4 = rootNode.checkin();
      rootNode.checkout();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      int index = rand.nextInt(5);
      switch (index)
      {
         case 0 :
            rootNode.restore(version0, true);
            break;
         case 1 :
            rootNode.restore(version1, true);
            break;
         case 2 :
            rootNode.restore(version2, true);
            break;
         case 3 :
            rootNode.restore(version3, true);
            break;
         case 4 :
            rootNode.restore(version3, true);
            break;
      }
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      rootNode.remove();
      context.getSession().save();
   }

}
