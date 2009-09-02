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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jcr.Item;
import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class TraverseInnerFoldersTest
   extends JCRTestBase
{
   /*
    * This test calculates the time (ms or tps) of random reading of node of type nt:folder (using
    * getItem() method), the nodes have structure like tree. Required parameters:
    * jcr.amountOfInnerFolders - the count of folders to create (for further getting it)
    * jcr.depthOfStructure - the count of levels of tree structure
    */
   private int amountOfInnerFolders = 0;

   private int depthOfStructure = 0;

   private List<String> names = new ArrayList<String>();

   private Random rand = new Random();

   private Node rootNode = null;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      // required params: jcr.amountOfInnerFolders, jcr.depthOfStructure
      amountOfInnerFolders = tc.getIntParam("jcr.amountOfInnerFolders");
      depthOfStructure = tc.getIntParam("jcr.depthOfStructure");
      int depth = depthOfStructure;
      int count = amountOfInnerFolders;
      rootNode = context.getSession().getRootNode().addNode(context.generateUniqueName("rootNode"), "nt:unstructured");
      createFoldersRecursively(context, rootNode, depth, count);
      context.getSession().save();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      int index = rand.nextInt((int) Math.pow(amountOfInnerFolders, depthOfStructure));
      Item item = rootNode.getSession().getItem(names.get(index));
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      rootNode.remove();
      context.getSession().save();
   }

   private void createFoldersRecursively(JCRTestContext context, Node rootNode, int depth, int count) throws Exception
   {
      if (depth < 1)
      {
         return;
      }
      for (int i = 0; i < count; i++)
      {
         String name = context.generateUniqueName("folder-" + i);
         Node current = rootNode.addNode(name, "nt:folder");
         names.add(current.getPath());
         createFoldersRecursively(context, current, depth - 1, count);
      }
   }

}
