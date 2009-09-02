/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.xml;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractGetItemNameTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class SessionGetImportContentHandlerTest
   extends AbstractGetItemNameTest
{

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      parent.addNode(context.generateUniqueName("node"));
      addName(parent.getPath());
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      context.getSession().getImportContentHandler(nextName(), ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
   }

}
