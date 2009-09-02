/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.session;

import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class SessionGetNodeByUUIDTest
   extends JCRTestBase
{

   private String uuid = "";

   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      Node node = context.getSession().getRootNode().addNode(context.generateUniqueName("testNode"));
      node.addMixin("mix:referenceable");
      context.getSession().save();
      uuid = node.getUUID();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      context.getSession().getNodeByUUID(uuid);
   }

}
