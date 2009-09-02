/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.node.write;

import javax.jcr.Node;
import javax.jcr.Value;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractAddItemTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class NodeSetValuePropertyTest
   extends AbstractAddItemTest
{

   private Value value = null;

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      value = context.getSession().getValueFactory().createValue("testValue");
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      nextParent().setProperty(context.generateUniqueName("property"), value);
   }

}
