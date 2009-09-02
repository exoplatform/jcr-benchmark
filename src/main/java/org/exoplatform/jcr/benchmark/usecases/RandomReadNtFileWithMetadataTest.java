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

import java.io.InputStream;
import java.util.Iterator;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class RandomReadNtFileWithMetadataTest
   extends JCRTestBase
{
   /*
    * This test will read randomly one of 1 million nodes of type nt:file that has been created
    * beforehand /download/node0..8/node0..49/node0..49/0..7-0..49-0..49-0..49.txt. Digits are
    * genereted randomly using levelXNodesCount parameters.
    */
   private Random rand = new Random();

   private int level1 = 0;

   private int level2 = 0;

   private int level3 = 0;

   private int level4 = 0;

   private int startIndex = 0;

   private String rootPath = "download";

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      rootPath = tc.getParam("jcr.rootPath");
      startIndex = tc.getIntParam("jcr.startIndex");
      level1 = tc.getIntParam("jcr.level1NodesCount");
      level2 = tc.getIntParam("jcr.level2NodesCount");
      level3 = tc.getIntParam("jcr.level3NodesCount");
      level4 = tc.getIntParam("jcr.level4NodesCount");
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      int level1Index = rand.nextInt(level1);
      int level2Index = rand.nextInt(level2);
      int level3Index = rand.nextInt(level3);
      int level4Index = rand.nextInt(level4);
      String path =
               rootPath + "/node" + (startIndex + level1Index) + "/node" + level2Index + "/node" + level3Index + "/"
                        + level1Index + "-" + level2Index + "-" + level3Index + "-" + level4Index + ".txt";
      Node node = context.getSession().getRootNode().getNode(path);
      try
      {
         Node contentNode = node.getNode("jcr:content");
         contentNode.getProperty("jcr:mimeType").getString();
         contentNode.getProperty("jcr:lastModified").getDate();
         contentNode.getProperty("dc:title").getValues()[0].getString();
         contentNode.getProperty("dc:subject").getValues()[0].getString();
         contentNode.getProperty("dc:creator").getValues()[0].getString();
         InputStream stream = contentNode.getProperty("jcr:data").getStream();
         int length = 0;
         int len;
         byte buf[] = new byte[1024];
         while ((len = stream.read(buf)) > 0)
            length += len;
      }
      catch (PathNotFoundException e)
      {
         System.out.println("[error] can not find property for node, parent is : " + node.getPath());
         e.printStackTrace();
      }
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
   }

}
