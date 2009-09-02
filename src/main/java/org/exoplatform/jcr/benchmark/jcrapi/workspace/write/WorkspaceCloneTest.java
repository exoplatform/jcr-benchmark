/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.workspace.write;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class WorkspaceCloneTest
   extends AbstractItemsInDifferentWorkspacesTest
{

   private List<String> ws1AbsPaths = new ArrayList<String>();

   private List<String> ws2AbsPaths = new ArrayList<String>();

   @Override
   protected void ws1CreateContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      String ws1AbsPath = parent.getPath() + "/" + context.generateUniqueName("dest");
      ws1AbsPaths.add(ws1AbsPath);
   }

   @Override
   protected void ws2CreateContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      String ws2AbsPath = parent.addNode(context.generateUniqueName("src")).getPath();
      ws2AbsPaths.add(ws2AbsPath);
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      // Currently logged into WS1 (collaboration).
      // Will be copied from WS2 (system) into WS1 (collaboration).
      context.getSession().getWorkspace().clone(WS2, ws2AbsPaths.remove(0), ws1AbsPaths.remove(0), true);
   }

}
