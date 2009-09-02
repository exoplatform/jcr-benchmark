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
package org.exoplatform.jcr.benchmark.organization.usecases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.jcr.benchmark.organization.AbstractOrganizationTest;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserProfile;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: FindGroupByIdTest.java 111 2008-11-11 11:11:11Z $
 */
public class CreateNewUserTest
   extends AbstractOrganizationTest
{

   private List<User> users;

   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);
      users = new ArrayList<User>();
   }

   @Override
   public void doRun(TestCase tc, JCRTestContext context) throws Exception
   {
      Date date = new Date();

      // Create new user
      User user = uHandler.createUserInstance(context.generateUniqueName("User"));
      user.setEmail("user@localhost");
      user.setFirstName("FirstName");
      user.setLastName("LastName");
      user.setPassword("password");
      user.setLastLoginTime(date);
      user.setCreatedDate(date);
      users.add(user);

      uHandler.createUser(user, false);

      // Create default user profile
      UserProfile up = upHandler.createUserProfileInstance();
      up.setUserName(user.getUserName());
      upHandler.saveUserProfile(up, false);

      // Assign group and membership to a new created user
      Group group = gHandler.findGroupById("/platform/users");
      MembershipType mt = mtHandler.findMembershipType("member");
      mHandler.linkMembership(user, group, mt, false);
   }

   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      for (User user : users)
      {
         uHandler.removeUser(user.getUserName(), false);
      }

      super.doFinish(tc, context);
   }
}
