/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.jcrapi.node.read;

import java.io.ByteArrayInputStream;
import java.util.Calendar;

import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class NodeGetPrimaryItemTest
   extends AbstractNodeTest
{

   private Node ntfile;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      ntfile = root.addNode(context.generateUniqueName("childNode"), "nt:file");
      Node content = ntfile.addNode("jcr:content", "nt:resource");
      content.setProperty("jcr:data", new ByteArrayInputStream(new byte[1]));
      content.setProperty("jcr:mimeType", "application/octet-stream");
      content.setProperty("jcr:lastModified", Calendar.getInstance());
      root.save();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      ntfile.getPrimaryItem();
   }
}
