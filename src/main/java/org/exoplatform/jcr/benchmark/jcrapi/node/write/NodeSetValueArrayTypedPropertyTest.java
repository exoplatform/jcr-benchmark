/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.node.write;

import javax.jcr.PropertyType;
import javax.jcr.Value;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class NodeSetValueArrayTypedPropertyTest
   extends AbstractAddItemEmptyContentTest
{

   private Value[] values = new Value[1];

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      nextParent().setProperty(context.generateUniqueName("property"), values, PropertyType.STRING);
   }

}
