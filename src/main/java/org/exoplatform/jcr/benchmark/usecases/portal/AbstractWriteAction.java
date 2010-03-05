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
   public AbstractWriteAction(RepositoryImpl repository, String workspace, String rootName, String stringValue,
      byte[] binaryValue, int multiValueSize)
   {
      super(repository, workspace, rootName);
      this.random = new Random();
      this.binaryValue = binaryValue;
      this.stringValue = stringValue;
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
      // String
      {
         target.setProperty("String-s", stringValue);
         Value[] values = new Value[multiValueSize];
         for (int i = 0; i < values.length; i++)
         {
            values[i] = valueFactory.createValue(stringValue);
         }
         target.setProperty("String-m", values);
      }

      // Binary
      {
         target.setProperty("Binary-s", valueFactory.createValue(new ByteArrayInputStream(binaryValue)));
         Value[] values = new Value[multiValueSize];
         for (int i = 0; i < values.length; i++)
         {
            values[i] = valueFactory.createValue(new ByteArrayInputStream(binaryValue));
         }
         target.setProperty("Binary-m", values);
      }

      // Boolean
      {
         target.setProperty("Boolean-s", valueFactory.createValue(true));
         Value[] values = new Value[multiValueSize];
         for (int i = 0; i < values.length; i++)
         {
            values[i] = valueFactory.createValue(true);
         }
         target.setProperty("Boolean-m", values);
      }

      // Long
      {
         target.setProperty("Long-s", valueFactory.createValue(random.nextLong()));
         Value[] values = new Value[multiValueSize];
         for (int i = 0; i < values.length; i++)
         {
            values[i] = valueFactory.createValue(random.nextLong());
         }
         target.setProperty("Long-m", values);
      }

      // Double
      {
         target.setProperty("Double-s", valueFactory.createValue(random.nextDouble()));
         Value[] values = new Value[multiValueSize];
         for (int i = 0; i < values.length; i++)
         {
            values[i] = valueFactory.createValue(random.nextDouble());
         }
         target.setProperty("Double-m", values);
      }

      // Date
      {
         target.setProperty("Date-s", valueFactory.createValue(Calendar.getInstance()));
         Value[] values = new Value[multiValueSize];
         for (int i = 0; i < values.length; i++)
         {
            values[i] = valueFactory.createValue(Calendar.getInstance());
         }
         target.setProperty("Date-m", values);
      }

      // Path
      {
         target.setProperty("Path-s", valueFactory.createValue(target.getPath(), PropertyType.PATH));
         Value[] values = new Value[multiValueSize];
         for (int i = 0; i < values.length; i++)
         {
            values[i] = valueFactory.createValue(target.getPath(), PropertyType.PATH);
         }
         target.setProperty("Path-m", values);
      }

      // Name
      {
         target.setProperty("Name-s", valueFactory.createValue(target.getName(), PropertyType.NAME));
         Value[] values = new Value[multiValueSize];
         for (int i = 0; i < values.length; i++)
         {
            values[i] = valueFactory.createValue(target.getName(), PropertyType.NAME);
         }
         target.setProperty("Name-m", values);
      }

      // Reference
      {
         target.setProperty("Reference-s", valueFactory.createValue(target));
         Value[] values = new Value[multiValueSize];
         for (int i = 0; i < values.length; i++)
         {
            values[i] = valueFactory.createValue(target);
         }
         target.setProperty("Reference-m", values);
      }
      return target;
   }
}