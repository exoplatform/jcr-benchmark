/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 2009
 *
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a> 
 * @version $Id$
 */
public class NodeGetPropertiesTraverseTest extends AbstractNodeTest
{

   @Override
   protected void initChildProperties(JCRTestContext context) throws RepositoryException
   {
      addChildProperties(context, 1000);
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      PropertyIterator props = node.getProperties();
      while (props.hasNext())
      {
         props.next();
      }
   }
}
