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

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;
import org.exoplatform.services.jcr.util.IdGenerator;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Portal-like JCR usecase test. Originally developed for testing cluster performance and scalability.
 *  
 * @author <a href="mailto:nikolazius@gmail.com">Nikolay Zamosenchuk</a>
 * @version $Id: PageUsecases.java 34360 2009-07-22 23:58:59Z nzamosenchuk $
 *
 */
public class PageUsecasesTest extends JCRTestBase
{
   static final Log log = ExoLogger.getLogger(PageUsecasesTest.class.getName());

   // Option names
   private static final String PARAM_DEPTH = "exo.prepare.depth";

   private static final String PARAM_NODES_PER_LEVEL = "exo.prepare.nodesPerLevel";

   private static final String PARAM_STRING_LENGTH = "exo.data.stringLength";

   private static final String PARAM_MULTI_SIZE = "exo.data.multiValueSize";

   private static final String PARAM_BIN_PATH = "exo.data.binaryPath";

   private static final String PARAM_BIN_SIZE = "exo.data.binarySize";

   // Test options
   private int depth = 2;

   private int nodesPerLevel = 10;

   private int stringLength = 64;

   private int multiValueSize = 2;

   private String binaryPath = null;

   private int binarySize = 1024;

   // Fields
   private String rootNodeName;

   private Random random = new Random();

   // Default values
   private String stringValue = null;

   private byte[] binaryValue = null;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);
      // get parameters from context
      if (tc.hasParam(PARAM_DEPTH))
      {
         depth = tc.getIntParam(PARAM_DEPTH);
      }
      if (tc.hasParam(PARAM_NODES_PER_LEVEL))
      {
         nodesPerLevel = tc.getIntParam(PARAM_NODES_PER_LEVEL);
      }
      if (tc.hasParam(PARAM_STRING_LENGTH))
      {
         stringLength = tc.getIntParam(PARAM_STRING_LENGTH);
      }
      if (tc.hasParam(PARAM_MULTI_SIZE))
      {
         multiValueSize = tc.getIntParam(PARAM_MULTI_SIZE);
      }
      if (tc.hasParam(PARAM_BIN_PATH))
      {
         binaryPath = tc.getParam(PARAM_BIN_PATH);
      }
      if (tc.hasParam(PARAM_BIN_SIZE))
      {
         binarySize = tc.getIntParam(PARAM_BIN_SIZE);
      }

      // Store value in memory, to avoid unnecessary FS IO operations.
      // Once generate string content
      byte[] bytes = new byte[stringLength];
      random.nextBytes(bytes);
      stringValue = new String(bytes);

      // once read in memory FS content
      if (binaryPath == null)
      {
         binaryValue = new byte[stringLength];
         random.nextBytes(binaryValue);
      }
      else
      {
         File file = new File(binaryPath);
         FileInputStream fin = new FileInputStream(file);
         binaryValue = new byte[(int)file.length()];
         fin.read(binaryValue);
         fin.close();
      }

      // initialization
      Session session = context.getSession();
      RepositoryImpl repository = (RepositoryImpl)context.getSession().getRepository();

      // root node for the test
      rootNodeName = IdGenerator.generate();
      Node testRoot = session.getRootNode().addNode("rootNode", "exo:genericNode");
      session.save();
      // fill the repository 
      InitRepositoryAction initAction =
         new InitRepositoryAction(repository, context, stringValue, bytes, multiValueSize, session, depth,
            nodesPerLevel, testRoot);
      // perform initialize action
      initAction.perform();
      session.save();

      // Synch?
      // testing: check repository is filled correctly
      printRepo(testRoot, "");
      // 
      // Prepare
   }

   public void parse(String line) throws Exception
   {
      // matching "22*read(2,5,4,6)"
      Pattern fullNotation = Pattern.compile("(\\d++)[*](\\w++)[(]([,0-9]*+)[)]");
      // matching "read(2,5,4,6)"
      Pattern singleNotation = Pattern.compile("(\\w++)[(]([,0-9]*+)[)]");

      // split scenario into usecases, skipping whitespaces 
      String[] actions = line.split("\\s*+;\\s*+");
      // parse each action string
      for (String actionLine : actions)
      {
         int times;
         String actionName;
         int[] params;

         // try parse as full notation
         Matcher fullMatcher = fullNotation.matcher(actionLine);
         if (fullMatcher.matches())
         {
            times = Integer.parseInt(fullMatcher.group(1));
            actionName = fullMatcher.group(2);
            String[] paramList = fullMatcher.group(3).split("\\s*+,\\s*+");
            params = new int[paramList.length];
            for (int i = 0; i < paramList.length; i++)
            {
               params[i] = Integer.parseInt(paramList[i]);
            }
            System.out.println(actionName + " x " + times + " " + Arrays.toString(params));
         }
         else
         {
            // try parse as notation without multiplier 
            Matcher singleMatcher = singleNotation.matcher(actionLine);
            if (singleMatcher.matches())
            {
               times = 1;
               actionName = singleMatcher.group(1);
               String[] paramList = singleMatcher.group(2).split("\\s*+,\\s*+");
               params = new int[paramList.length];
               for (int i = 0; i < paramList.length; i++)
               {
                  params[i] = Integer.parseInt(paramList[i]);
               }
               System.out.println(actionName + " x " + times + " " + Arrays.toString(params));
            }
            else
            {
               throw new Exception("Illegal scenario element:"+actionLine);
            }
         }
      }
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      // TODO: perform
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doFinish(tc, context);
   }

   /**
    * Recursively prints repository content
    * (for testing purposes only)
    * 
    * @param root
    * @param tabs
    * @throws RepositoryException
    */
   private void printRepo(Node root, String tabs) throws RepositoryException
   {
      System.out.println(tabs + root.getName());
      NodeIterator iterator = root.getNodes();
      while (iterator.hasNext())
      {
         printRepo(iterator.nextNode(), tabs + "\t");
      }
   }

}
