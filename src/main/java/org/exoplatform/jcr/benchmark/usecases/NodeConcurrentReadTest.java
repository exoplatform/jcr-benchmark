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
package org.exoplatform.jcr.benchmark.usecases;

import com.sun.japex.TestCase;

import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.jcr.Node;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 */
public class NodeConcurrentReadTest extends JCRTestBase {

  private Node           rootNode    = null;

  private List<String>   parentNames = Collections.synchronizedList(new ArrayList<String>());

  private JCRTestContext context     = null;

  private volatile int   iterations  = 0;

  private int            threads     = 0;

  /**
   * @see org.exoplatform.jcr.benchmark.JCRTestBase#doPrepare(com.sun.japex.TestCase,
   *      org.exoplatform.jcr.benchmark.JCRTestContext)
   */
  @Override
  public void doPrepare(TestCase tc, JCRTestContext context) throws Exception {
    super.doPrepare(tc, context);

    this.context = context;

    iterations = tc.getIntParam("japex.runIterations");
    threads = tc.getIntParam("japex.numberOfThreads");

    rootNode = context.getSession().getRootNode().addNode(context.generateUniqueName("testRoot"));

    for (int i = 0; i < iterations; i++) {
      String parentName = context.generateUniqueName("parent");
      Node parent = rootNode.addNode(parentName);
      parent.setProperty("testProp", "testVal");
      context.getSession().save();
      parentNames.add(parentName);
    }

    for (int i = 0; i < threads; i++) {
      new Writer().start();
    }

  }

  /**
   * @see org.exoplatform.jcr.benchmark.JCRTestBase#doRun(com.sun.japex.TestCase,
   *      org.exoplatform.jcr.benchmark.JCRTestContext)
   */
  @Override
  public void doRun(TestCase tc, JCRTestContext context) throws Exception {
    readProp();
  }

  private class Writer extends Thread {

    /**
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
      super.run();
      try {
        String name = context.generateUniqueName(this.getName());
        Node writeNode = rootNode.addNode(name);
        writeNode.setProperty("testProp", this.getName());
        parentNames.add(name);
        context.getSession().save();
        iterations++;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void readProp() throws Exception {
    Node readNode = rootNode.getNode(parentNames.get(new Random().nextInt(iterations)));
    readNode.getProperty("testProp").getValue().getString();
  }

}
