/***************************************************************************
 * Copyright 2001-2010 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.jcr.benchmark.jcrapi.workspace.write;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Session;

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestContext;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Natasha Vakulenko
 */

public class WorkspaceNodeUpdateTest extends AbstractItemsInDifferentWorkspacesTest
{
   private List<Node> nodesForUpdate = new ArrayList<Node>();

   @Override
   protected void ws1CreateContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      // Create node to clone in workspace WS1 (collaboration) and save changes
      nodesForUpdate.add(parent.addNode(context.generateUniqueName("dst")));
      parent.save();
   }

   @Override
   protected void ws2CreateContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      Node node = nodesForUpdate.get(nodesForUpdate.size() - 1);

      // Will be cloned from WS1 (collaboration) into WS2 (system).		
      ws2Session.getWorkspace().clone(WS1, node.getPath(), parent.getPath() + "/" + node.getName(), false);

      parent.getNode(node.getName()).addNode("testCorrespondingNodeUpdate");
      ws2Session.save();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      // Currently logged into WS1 (collaboration).
      // Will be update from WS2 (system) into WS1 (collaboration).
      nodesForUpdate.remove(0).update(WS2);
   }
}
