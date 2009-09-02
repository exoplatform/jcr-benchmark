/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.query;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.query.QueryManager;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id: AbstractQueryTest.java 12446 2008-03-28 08:28:14Z pnedonosko $
 */

public abstract class AbstractQueryTest
   extends JCRTestBase
{

   private static final String[] CONTENT =
            new String[]
            {
                     "If the current session does not have sufficient permissions to perform the operation, then an AccessDeniedException is thrown",
                     "If the specified srcWorkspace does not exist, a NoSuchWorkspaceException is thrown",
                     "An InvalidItemStateException is thrown if this Session (not necessarily this Node) has pending unsaved changes",
                     "A LockException is thrown if a lock prevents the merge",
                     "A RepositoryException is thrown if another error occurs",
                     "If successful, the changes are persisted immediately, there is no need to call save",
                     "A MergeException is thrown if bestEffort is false and a versionable node is encountered whose corresponding node's base version",
                     "If this node is not versionable, an UnsupportedRepositoryOperationException is thrown",
                     "If there are unsaved changes pending on this node, an InvalidItemStateException is thrown",
                     "If this node is already checked-in, this method has no effect but returns the current base version of this node"};

   protected Node root;

   protected Node node;

   protected QueryManager queryManager;

   protected final String SQL_QUERY_STATEMENT = "select * from nt:unstructured where jcr:data like '%merge%'";

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);

      root.remove();
      context.getSession().save();
   }

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      queryManager = context.getSession().getWorkspace().getQueryManager();

      root = context.getSession().getRootNode().addNode(context.generateUniqueName("testRoot"));

      node = root.addNode(context.generateUniqueName("testNode"));
      node.addMixin("mix:referenceable");
      context.getSession().save();

      for (int i = 0; i < CONTENT.length; i++)
      {
         Node cont = node.addNode("content" + i, "nt:unstructured");
         cont.setProperty("data", CONTENT[i]);
      }
      /*  for (int i = 0; i < CONTENT.length; i++) {
          Node ntfile = node.addNode(context.generateUniqueName("ntfile"), "nt:file");
          Node jctContent = ntfile.addNode("jcr:content", "nt:resource");
          jctContent.setProperty("jcr:data", CONTENT[i]);
          jctContent.setProperty("jcr:mimeType", "text/plain");
          jctContent.setProperty("jcr:lastModified", Calendar.getInstance());
        }*/

      root.save();
   }

}
