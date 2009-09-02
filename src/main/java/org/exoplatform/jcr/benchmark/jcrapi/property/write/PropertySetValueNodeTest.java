/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.property.write;

import javax.jcr.Node;
import javax.jcr.Property;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractGetItemTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class PropertySetValueNodeTest
   extends AbstractGetItemTest
{

   private Node parent2 = null;

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      if (!(parent.isSame(parent2)))
      {
         parent.addMixin("mix:referenceable");
         context.getSession().save();
         parent2 = parent;
      }
      Property property = parent.setProperty(context.generateUniqueName("testProperty"), parent2);
      addProperty(property);
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      nextProperty().setValue(parent2);
   }

}
