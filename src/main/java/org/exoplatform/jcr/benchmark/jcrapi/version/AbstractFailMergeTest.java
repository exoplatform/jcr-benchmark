/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.jcr.benchmark.jcrapi.version;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.Version;
import javax.jcr.Property;
import javax.jcr.Value;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractGetItemTest;

import org.exoplatform.services.jcr.impl.core.JCRPath;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author : Natasha Vakulenko
 * 
 */
public abstract class AbstractFailMergeTest extends AbstractGetItemTest
{
   private List<Version> versions = new ArrayList<Version>();

   private static final String workspaceName = "system";

   private Session sysSession;

   private volatile int versionIndex = 0;

   protected Version nextVersion()
   {
      return versions.get(versionIndex++);
   }

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      // create 2 independent versions for a node and its corresponding node
      // so merge fails for this node
      Node vnode = parent.addNode(context.generateUniqueName("versionableNode"));
      vnode.addMixin("mix:versionable");
      parent.save();
      vnode.checkin();// v.1
      vnode.checkout();

      // create corr node in system ws with own content
      sysSession = context.getSession().getRepository().login(context.getCredentials(), workspaceName);

      if (!sysSession.itemExists(vnode.getParent().getPath()))
      {
         // create parent, no support for SNS!!!
         Node ctxNode = sysSession.getRootNode();
         for (String nname : vnode.getParent().getPath().split("/"))
         {
            try
            {
               if (nname.equals(""))
                  nname = JCRPath.THIS_RELPATH;
               ctxNode = ctxNode.getNode(nname);
            }
            catch (PathNotFoundException e)
            {
               ctxNode = ctxNode.addNode(nname);
               sysSession.save();
            }
         }
      }

      sysSession.getWorkspace().clone(context.getSession().getWorkspace().getName(), vnode.getPath(), vnode.getPath(),
         false);
      Node sysVNode = sysSession.getNodeByUUID(vnode.getUUID());

      vnode.addNode("Subnode").setProperty("Property", "property of subnode");
      vnode.save();
      vnode.checkin();// v.2
      vnode.checkout();

      // change content, create separated branches
      sysVNode.addNode("Sys Subnode").setProperty("Sys Property", "property of system subnode");
      sysVNode.save();
      sysVNode.checkin();// v.3
      sysVNode.checkout();

      // merge versioned nodes
      vnode.merge(workspaceName, true);
      addNode(vnode);

      // get mergeFailed property 
      Property mergeFailedProperty = vnode.getProperty("jcr:mergeFailed");
      Value[] mergeFailedReferences = mergeFailedProperty.getValues();
      String uuid = mergeFailedReferences[0].getString();

      // add version for merge continuation methods
      versions.add((Version)sysSession.getNodeByUUID(uuid));
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);

      try
      {
         sysSession.getRootNode().getNode(rootNodeName).remove();
         sysSession.save();
      }
      catch (RepositoryException e)
      {
         e.printStackTrace();
      }
   }
}
