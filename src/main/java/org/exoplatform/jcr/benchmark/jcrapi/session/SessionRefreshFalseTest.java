/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.session;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class SessionRefreshFalseTest
   extends AbstractGetSessionTest
{

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      nextSession().refresh(false);
   }

}
