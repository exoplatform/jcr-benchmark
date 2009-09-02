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
package org.exoplatform.jcr.benchmark.usecases;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.exoplatform.services.log.Log;
import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 11.09.2008
 * 
 * @author <a href="mailto:peter.nedonosko@exoplatform.com.ua">Peter Nedonosko</a>
 * @version $Id$
 */
public abstract class AbstractMoveUsecaseTest
   extends JCRTestBase
{

   protected static final int START_YEAR = 2005;

   protected static Log LOG = ExoLogger.getLogger("AbstractMoveUsecaseTest");

   protected Node rootNode = null;

   private volatile int iteration = 0;

   protected Node nextNode() throws PathNotFoundException, RepositoryException
   {
      return rootNode.getNode("iter" + (iteration++));
   }

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {

      long start = System.currentTimeMillis();

      rootNode = context.getSession().getRootNode(); //.addNode(context.generateUniqueName("moveTest")
      // )
      context.getSession().save();

      int runIterations = tc.getIntParam("japex.runIterations");

      if (tc.hasParam("japex.warmupIterations"))
      {
         int warmUpIterations = tc.getIntParam("japex.warmupIterations");
         if (warmUpIterations>0) 
         {
            runIterations += warmUpIterations;
         }
      }

      // create source for move
      final int nodesCount = tc.getIntParam("usecase.nodesCount");
      if (nodesCount <= 0)
         throw new Exception("usecase.nodesCount should be a positive number, but " + nodesCount);

      final boolean addMeatadata = tc.getBooleanParam("usecase.addMeatadata");

      Random rndYear = new Random();
      Random rndMonth = new Random();
      Random rndDay = new Random();

      for (int i = 0; i < runIterations; i++)
      {
         Node iterRoot = rootNode.addNode("iter" + i);
         rootNode.save();

         long istart = System.currentTimeMillis();
         for (int n = 0; n < nodesCount; n++)
         {
            // add nt:file (like WebDAV do on upload)
            Calendar fileDate = Calendar.getInstance();
            fileDate.set(Calendar.YEAR, START_YEAR + rndYear.nextInt(1)); // 2005-2006
            fileDate.set(Calendar.MONTH, rndMonth.nextInt(11)); // 0-11
            fileDate.set(Calendar.DAY_OF_MONTH, 1 + rndDay.nextInt(29)); // 1-30

            Node file = iterRoot.addNode("file." + i + "-" + n, "nt:file");
            Node fres = file.addNode("jcr:content", "nt:resource");
            fres.setProperty("jcr:encoding", "UTF-8");
            fres.setProperty("jcr:mimeType", "text/pdf");
            fres.setProperty("jcr:lastModified", fileDate);
            // fres.setProperty("jcr:data", new ByteArrayInputStream(("the usecase content " + i + "-" +
            // n).getBytes()));
            fres.setProperty("jcr:data", new FileInputStream("../resources/benchmark-tiff.pdf"));

            if (addMeatadata)
            {
               file.addMixin("dc:elementSet");
               file.setProperty("dc:title", new String[]
               {"Document #" + i + "-" + n});
               file.setProperty("dc:creator", new String[]
               {this.toString()});
               file.setProperty("dc:contributor", new String[]
               {"Contributor #" + i});
               file.setProperty("dc:date", new Value[]
               {rootNode.getSession().getValueFactory().createValue(fileDate)});
               file.setProperty("dc:language", new String[]
               {"EN"});
               file.setProperty("dc:rights", new String[]
               {"eXo Platform SAS, UA"});
            }
            iterRoot.save();
         }
         LOG.info("Prepare of iteration " + i + " done " + (System.currentTimeMillis() - istart) + "ms");
      }

      // destenation
      rootNode.addNode("catalog");
      rootNode.save();

      LOG.info("Prepare done " + (System.currentTimeMillis() - start) + "ms");
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      long start = System.currentTimeMillis();

      // rootNode.remove();
      // context.getSession().save();

      // LOG.info("Finish done " + (System.currentTimeMillis() - start) + "ms");
      LOG.info("Finish done. Nodes in workspace. " + (System.currentTimeMillis() - start) + "ms");
   }
}
