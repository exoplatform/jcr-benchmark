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

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.jcr.ext.registry.RegistryEntry;
import org.exoplatform.services.jcr.ext.registry.RegistryService;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Nikolay Zamosenchuk
 * @version $Id:$
 */
public class CreateEntry extends AbstractRegistryServiceTest {

  @Override
  protected void createContent(TestCase tc, JCRTestContext context) throws Exception {
    // don't create any content on preparation
  }

  @Override
  public void doRun(TestCase tc, JCRTestContext context) throws Exception {
    registryService.createEntry(sessionProvider, RegistryService.EXO_USERS + "/"
        + context.generateUniqueName("username"), RegistryEntry.parse(GENERIC_ENTRY.getBytes()));
  }
}
