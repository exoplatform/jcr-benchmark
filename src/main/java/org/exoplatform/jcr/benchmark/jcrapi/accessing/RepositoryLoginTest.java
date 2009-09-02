/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.accessing;

import javax.jcr.Repository;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class RepositoryLoginTest
   extends JCRTestBase
{

   private Repository repository = null;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      repository = context.getSession().getRepository();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      repository.login(context.getCredentials());
   }

}
