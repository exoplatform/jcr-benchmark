/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.jcr.benchmark.usecases.nodetype;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.PropertyType;

import org.exoplatform.services.log.Log;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.core.nodetype.ExtendedNodeTypeManager;
import org.exoplatform.services.jcr.core.nodetype.NodeTypeValue;
import org.exoplatform.services.jcr.core.nodetype.PropertyDefinitionValue;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:Sergey.Kabashnyuk@gmail.com">Sergey Kabashnyuk</a>
 * @version $Id: $
 */
public class UnregisterNodeType
   extends AbstractNodeTypeTest
{
   /**
    * Class logger.
    */
   private static final Log LOG = ExoLogger.getLogger(UnregisterNodeType.class);

   @Override
   public void doRun(TestCase arg0, JCRTestContext context) throws Exception
   {
      NodeTypeValue nodeTypeValue = nextNodeTypeValue();

      extNodeTypeManager.unregisterNodeType(nodeTypeValue.getName());
   }

   @Override
   public void prepare(TestCase arg0, JCRTestContext context) throws Exception
   {
      if (runIterations <= 0)
         throw new Exception("japex.runIterations should be a positive number, but " + runIterations);

      for (int i = 0; i < runIterations; i++)
      {

         extNodeTypeManager = (ExtendedNodeTypeManager) context.getSession().getWorkspace().getNodeTypeManager();

         NodeTypeValue nodeTypeValue = new NodeTypeValue();

         List<String> superType = new ArrayList<String>();
         superType.add("nt:base");
         nodeTypeValue.setName(context.generateUniqueName("exo:unregisterNodeType"));
         nodeTypeValue.setPrimaryItemName("");
         nodeTypeValue.setDeclaredSupertypeNames(superType);
         List<PropertyDefinitionValue> props = new ArrayList<PropertyDefinitionValue>();

         props.add(new PropertyDefinitionValue("*", false, false, 1, false, new ArrayList<String>(), false,
                  PropertyType.STRING, new ArrayList<String>()));
         nodeTypeValue.setDeclaredPropertyDefinitionValues(props);

         extNodeTypeManager.registerNodeType(nodeTypeValue, ExtendedNodeTypeManager.FAIL_IF_EXISTS);
         nodeTypes.add(nodeTypeValue);
      }
   }
}
