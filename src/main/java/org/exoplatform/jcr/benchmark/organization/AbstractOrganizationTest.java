/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.organization;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.jcr.benchmark.JCRTestBase;
import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipTypeHandler;
import org.exoplatform.services.organization.UserHandler;
import org.exoplatform.services.organization.UserProfileHandler;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: AbstractOrganizationTest.java 111 2008-11-11 11:11:11Z $
 */
public abstract class AbstractOrganizationTest
   extends JCRTestBase
{

   protected StandaloneContainer container;

   protected OrganizationService organizationService;

   protected GroupHandler gHandler;

   protected MembershipTypeHandler mtHandler;

   protected MembershipHandler mHandler;

   protected UserHandler uHandler;

   protected UserProfileHandler upHandler;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      container = StandaloneContainer.getInstance();
      organizationService = (OrganizationService) container.getComponentInstance(OrganizationService.class);
      gHandler = organizationService.getGroupHandler();
      mtHandler = organizationService.getMembershipTypeHandler();
      mHandler = organizationService.getMembershipHandler();
      uHandler = organizationService.getUserHandler();
      upHandler = organizationService.getUserProfileHandler();
   }
}
