/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.property.read;

import javax.jcr.Property;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractRootNodeTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class PropertyGetNodeTest
   extends AbstractRootNodeTest
{

   private Property property = null;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);
      node.addMixin("mix:referenceable");
      property = node.setProperty("testProperty", node);
      context.getSession().save();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      property.getNode();
   }

}
