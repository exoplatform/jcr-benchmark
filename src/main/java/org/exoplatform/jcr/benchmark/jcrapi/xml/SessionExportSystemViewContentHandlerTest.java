/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.xml;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class SessionExportSystemViewContentHandlerTest
   extends AbstractContentCreatorForExportTest
{

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      context.getSession().exportSystemView(nextPath(), nextTransformerHandler(), false, false);
   }

}
