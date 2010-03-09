/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.jcr.benchmark.usecases.portal;

import org.exoplatform.services.jcr.impl.core.RepositoryImpl;
import org.exoplatform.services.jcr.util.IdGenerator;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

/**
 * Extends AbstractAction by introducing method, creating node of genericNode type and filling 
 * all the expected properties (please have a look at "exo:genericNode" type).
 * 
 * @author <a href="mailto:nikolazius@gmail.com">Nikolay Zamosenchuk</a>
 * @version $Id: AbstractWriteAction.java 34360 2009-07-22 23:58:59Z nzamosenchuk $
 *
 */
public abstract class AbstractWriteAction extends AbstractAction
{

   private String stringValue;

   private byte[] binaryValue;

   private int multiValueSize;

   private Random random;

   /**
    * @param repository
    *        Repository instance
    * @param workspace
    *        Workspace name
    * @param rootName
    *        Test's root node name
    * @param stringValue
    *        Sting value, that is used to initialize string properties
    * @param binaryValue
    *        Binary value, used to initialize binary properties
    * @param multiValueSize
    *        How many items will contain multi-valued property
    */
   public AbstractWriteAction(RepositoryImpl repository, String workspace, String rootName, int depth,
      String stringValue, byte[] binaryValue, int multiValueSize)
   {
      super(repository, workspace, rootName, depth);
      this.random = new Random();
      this.binaryValue = binaryValue;
      this.stringValue = stringValue;
      this.multiValueSize = multiValueSize;
   }

   /**
    * Fills in all the properties defined in exo:genericNode type.
    * 
    * @param root
    *        Parent node, where node is created
    * @param valueFactory
    *        Value factory instace
    * @return
    * @throws RepositoryException
    */
   public Node createGenericNode(Node root, ValueFactory valueFactory) throws RepositoryException
   {
      String id = IdGenerator.generate();
      Node target = root.addNode(id, "exo:genericNode");
      for (int i = 1; i <= 9; i++)
      {
         {
            // multivalue
            String propName = PropertyType.nameFromValue(i) + "-m";
            target.setProperty(propName, createValues(target, i, true, valueFactory), i);
         }
         {
            // single value
            String propName = PropertyType.nameFromValue(i) + "-s";
            target.setProperty(propName, createValues(target, i, false, valueFactory)[0], i);
         }
      }
      return target;
   }

   public Value[] createValues(Node node, int propType, boolean isMultivalued, ValueFactory valueFactory)
      throws RepositoryException
   {

      Value[] values = new Value[isMultivalued ? multiValueSize : 1];

      switch (propType)
      {

         case 1 : //STRING
         {
            for (int i = 0; i < values.length; i++)
            {
               values[i] = valueFactory.createValue(stringValue);
            }
            return values;
         }
         case 2 : //BINARY
         {
            for (int i = 0; i < values.length; i++)
            {
               values[i] = valueFactory.createValue(new ByteArrayInputStream(binaryValue));
            }
            return values;
         }
         case 3 : //LONG
         {
            for (int i = 0; i < values.length; i++)
            {
               values[i] = valueFactory.createValue(random.nextLong());
            }
            return values;
         }
         case 4 : //DOUBLE
         {
            for (int i = 0; i < values.length; i++)
            {
               values[i] = valueFactory.createValue(random.nextDouble());
            }
            return values;
         }
         case 5 : //DATE
         {
            for (int i = 0; i < values.length; i++)
            {
               values[i] = valueFactory.createValue(Calendar.getInstance());
            }
            return values;
         }
         case 6 : //BOOLEAN
         {
            for (int i = 0; i < values.length; i++)
            {
               values[i] = valueFactory.createValue(true);
            }
            return values;
         }
         case 7 : //NAME
         {
            for (int i = 0; i < values.length; i++)
            {
               values[i] = valueFactory.createValue(node.getName(), PropertyType.NAME);
            }
            return values;
         }
         case 8 : //PATH
         {
            for (int i = 0; i < values.length; i++)
            {
               values[i] = valueFactory.createValue(node.getPath(), PropertyType.PATH);
            }
            return values;
         }
         case 9 : //REFERENCE
         {
            for (int i = 0; i < values.length; i++)
            {
               values[i] = valueFactory.createValue(node);
            }
            return values;
         }
         default :
            // TODO is there exception needed?
            return null;
      }

   }

}
