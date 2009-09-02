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
package org.exoplatform.jcr.benchmark.registryservice;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.registry.RegistryEntry;
import org.exoplatform.services.jcr.ext.registry.RegistryService;
import org.exoplatform.services.security.ConversationState;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS<br>
 * It's abstract test for {@link RegistryService} testings. This class provides
 * basic preparations, such as acquiring RegistryService and some additional
 * instances.
 * 
 * @author Nikolay Zamosenchuk
 * @version $Id:$
 */
public abstract class AbstractRegistryServiceTest extends JCRTestBase {

  protected StandaloneContainer container;

  protected RegistryService     registryService;

  protected SessionProvider     sessionProvider;

  private List<String>          users         = new ArrayList<String>();

  private int                   iteration     = 0;

  protected int                 sumIterations = 0;

  protected static final String GENERIC_ENTRY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                                  + "<node-navigation><owner-type>portal</owner-type><owner-id>portalone</owner-id>"
                                                  + "<access-permissions>*:/guest</access-permissions><page-nodes><node>"
                                                  + "<uri>portalone::home</uri><name>home</name><label>Home</label>"
                                                  + "<page-reference>portal::portalone::content</page-reference></node>"
                                                  + "</page-nodes></node-navigation>";

  @Override
  public void doPrepare(TestCase tc, JCRTestContext context) throws Exception {
    super.doPrepare(tc, context);
    container = StandaloneContainer.getInstance();
    registryService = (RegistryService) container.getComponentInstanceOfType(RegistryService.class);
    sessionProvider = new SessionProvider(ConversationState.getCurrent());
    // fill class field sumIterations with adequate number
    if ((tc.hasParam("japex.runIterations")) && (tc.getIntParam("japex.runIterations") > 0)) {
      sumIterations += tc.getIntParam("japex.runIterations");
    }
    if ((tc.hasParam("japex.warmupIterations")) && (tc.getIntParam("japex.warmupIterations") > 0)) {
      sumIterations += tc.getIntParam("japex.warmupIterations");
    }
    // create content. May be overridden in child class with stub method to
    // avoid any preparation.
    createContent(tc, context);
  }

  /**
   * Creates content in registry on doPrepare() phase. Used for read, update,
   * delete tests.
   * 
   * @param tc
   * @param context
   * @throws Exception
   */
  protected void createContent(TestCase tc, JCRTestContext context) throws Exception {
    // create content
    RegistryEntry registryEntry = RegistryEntry.parse(GENERIC_ENTRY.getBytes());
    // create list of user names and add generic entry to each user
    for (int i = 0; i <= sumIterations; i++) {
      String name = context.generateUniqueName("username");
      addUser(name);
      registryService.createEntry(sessionProvider,
                                  RegistryService.EXO_USERS + "/" + name,
                                  registryEntry);
    }
  }

  /**
   * @return next saved name in internal list.
   */
  public String nextUser() {
    return users.get(iteration++);
  }

  /**
   * Adds name to internal list of names. Can later be accessed by nextUser(). *
   * 
   * @param name
   */
  public void addUser(String name) {
    users.add(name);
  }

  @Override
  public void doFinish(TestCase tc, JCRTestContext context) throws Exception {
    super.doFinish(tc, context);
    // clear all children
    registryService.getRegistry(sessionProvider).getNode().remove();
    context.getSession().save();
  }
}
