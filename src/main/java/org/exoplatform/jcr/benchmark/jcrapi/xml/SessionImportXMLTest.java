/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.xml;

import javax.jcr.ImportUUIDBehavior;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class SessionImportXMLTest
   extends AbstractContentCreatorForImportTest
{

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      context.getSession().importXML(nextPath(), nextInputStream(), ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
   }

}
