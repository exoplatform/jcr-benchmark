/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.workspace.write;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractGetItemTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class WorkspaceCopyTest
   extends AbstractGetItemTest
{

   private List<String> srcAbsPaths = new ArrayList<String>();

   private List<String> destAbsPaths = new ArrayList<String>();

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      String srcAbsPath = parent.addNode(context.generateUniqueName("src")).getPath();
      String destAbsPath = parent.getPath() + "/" + context.generateUniqueName("dest");
      srcAbsPaths.add(srcAbsPath);
      destAbsPaths.add(destAbsPath);
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      context.getSession().getWorkspace().copy(srcAbsPaths.remove(0), destAbsPaths.remove(0));
   }

}
