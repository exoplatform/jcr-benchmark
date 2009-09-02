/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.session;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Session;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public abstract class AbstractGetSessionTest
   extends JCRTestBase
{

   private List<Session> sessions = new ArrayList<Session>();

   private List<String> parentNames = new ArrayList<String>();

   private Node testRoot = null;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      int runIterations = tc.getIntParam("japex.runIterations");
  
      if (tc.hasParam("japex.warmupIterations"))
      {
         int warmUpIterations = tc.getIntParam("japex.warmupIterations");
         if (warmUpIterations>0) 
         {
            runIterations += warmUpIterations;
         }
      }      // 1. Create root node for this test using current session
      testRoot = context.getSession().getRootNode().addNode(context.generateUniqueName("testRoot"));
      context.getSession().save();
      // 2. Create parents (see AbstractItemsTest for details)
      String parentName = "";
      for (int i = 0; i < runIterations; i++)
      {
         if (i % 100 == 0)
         {
            parentName = testRoot.addNode(context.generateUniqueName("testParent")).getPath();
            context.getSession().save();
         }
         parentNames.add(parentName);
      }
      // 3. Create sessions, one session has one unsaved node
      for (int i = 0; i < runIterations; i++)
      {
         Session session = context.getSession().getRepository().login();
         Node parentNode = (Node) session.getItem(parentNames.remove(0));
         parentNode.addNode(context.generateUniqueName("testNode"));
         sessions.add(session);
      }
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      testRoot.remove();
      context.getSession().save();
      sessions.clear();
      parentNames.clear();
   }

   protected Session nextSession()
   {
      return sessions.remove(0);
   }
}
