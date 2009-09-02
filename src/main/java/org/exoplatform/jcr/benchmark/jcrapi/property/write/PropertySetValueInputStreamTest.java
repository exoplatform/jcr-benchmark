/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.property.write;

import java.io.ByteArrayInputStream;

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

public class PropertySetValueInputStreamTest
   extends AbstractGetItemTest
{

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      Property property =
               parent.setProperty(context.generateUniqueName("testProperty"), new ByteArrayInputStream(new byte[1024]));
      addProperty(property);
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      nextProperty().setValue(new ByteArrayInputStream(new byte[1024]));
   }

}
