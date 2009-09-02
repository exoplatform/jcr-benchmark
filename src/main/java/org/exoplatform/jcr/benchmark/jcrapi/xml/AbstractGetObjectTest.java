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
package org.exoplatform.jcr.benchmark.jcrapi.xml;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.sax.TransformerHandler;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.jcrapi.AbstractItemsTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 */

public abstract class AbstractGetObjectTest
   extends AbstractItemsTest
{

   private List<String> paths = new ArrayList<String>();

   private List<OutputStream> outputStreams = new ArrayList<OutputStream>();

   private List<InputStream> inputStreams = new ArrayList<InputStream>();

   private List<TransformerHandler> transformerHandlers = new ArrayList<TransformerHandler>();

   private volatile int iterationPath = 0;

   private volatile int iterationOutputStream = 0;

   private volatile int iterationInputStream = 0;

   private volatile int iterationTransformerHandlers = 0;

   protected String nextPath()
   {
      return paths.get(iterationPath++);
   }

   protected OutputStream nextOutputStream()
   {
      return outputStreams.get(iterationOutputStream++);
   }

   protected InputStream nextInputStream()
   {
      return inputStreams.get(iterationInputStream++);
   }

   protected TransformerHandler nextTransformerHandler()
   {
      return transformerHandlers.get(iterationTransformerHandlers++);
   }

   protected void addPath(String name)
   {
      paths.add(name);
   }

   protected void addOutputStream(OutputStream outputStream)
   {
      outputStreams.add(outputStream);
   }

   protected void addInputStream(InputStream inputStream)
   {
      inputStreams.add(inputStream);
   }

   protected void addTransformerHandler(TransformerHandler transformerHandler)
   {
      transformerHandlers.add(transformerHandler);
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);
      paths.clear();
      paths = null;
      for (int i = 0; i < iterationOutputStream; i++)
      {
         outputStreams.get(i).close();
      }
      for (int i = 0; i < iterationInputStream; i++)
      {
         inputStreams.get(i).close();
      }
      outputStreams.clear();
      outputStreams = null;
      inputStreams.clear();
      inputStreams = null;
      transformerHandlers.clear();
      transformerHandlers = null;
   }

}
