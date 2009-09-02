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
package org.exoplatform.jcr.benchmark.organization.user;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.organization.AbstractUserTest;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: FindGroupByIdTest.java 111 2008-11-11 11:11:11Z $
 */
public class FindUsersTest
   extends AbstractUserTest
{

   org.exoplatform.services.organization.Query query;

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      query = new org.exoplatform.services.organization.Query();
      query.setUserName(nextUser().getUserName());

      uHandler.findUsers(query);
   }
}
