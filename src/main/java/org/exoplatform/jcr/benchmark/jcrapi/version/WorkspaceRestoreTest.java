/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.version;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.version.Version;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractGetItemTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: WorkspaceRestoreTest.java 13537 2008-04-22 08:22:36Z vetalok $
 */

public class WorkspaceRestoreTest
   extends AbstractGetItemTest
{

   private List<Version[]> versions = new ArrayList<Version[]>();

   /**
    * See JSR-170 for details 8.2.8 Restoring a Group of Versions
    */
   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      Node nodeA = parent.addNode(context.generateUniqueName("versionableNodeA"));
      nodeA.addMixin("mix:versionable");
      parent.save();
      nodeA.checkin();// v.1
      nodeA.checkout();

      Node nodeB = nodeA.addNode("Subnode B");
      nodeA.save();
      nodeB.addMixin("mix:versionable");
      nodeA.save();
      nodeB.checkin();// B v.1

      Node nodeC = nodeA.addNode("Subnode C");
      nodeA.save();
      nodeC.addMixin("mix:versionable");
      nodeA.save();
      nodeC.checkin();// C v.1
      nodeC.checkout();
      nodeC.setProperty("Property Y", nodeB); // ref to Subnode B
      nodeC.save();
      Version vC = nodeC.checkin();// C v.2
      nodeC.checkout();

      nodeB.checkout();
      nodeB.setProperty("Property X", nodeC); // ref to Subnode C
      nodeB.save();
      Version vB = nodeB.checkin();// B v.2
      nodeB.checkout();

      // add some stuff
      nodeA.setProperty("Property", "property of subnode");
      nodeB.remove();
      nodeC.remove();
      nodeA.save();
      Version vA = nodeA.checkin();// v.3
      nodeA.checkout();

      versions.add(new Version[]
      {vA, vB, vC});

      addNode(nodeA);
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      Version[] vs = versions.get(getCurrentIteration());
      nextNode().getSession().getWorkspace().restore(vs, true);// restore A v.2, B v.2, C v.2
   }

}
