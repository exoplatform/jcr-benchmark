/***************************************************************************
 * Copyright 2001-2010 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.version;

import javax.jcr.Node;
import javax.jcr.version.Version;

import org.exoplatform.jcr.benchmark.jcrapi.version.AbstractFailMergeTest;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vakulenko Natasha
 * 
 */

public class NodeDoneMergeTest extends AbstractFailMergeTest
{

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      nextNode().doneMerge(nextVersion());// doneMerge with version
   }
}
