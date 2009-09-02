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

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.jcr.benchmark.JCRTestContext;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserProfile;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: AbstractOrganizationTest.java 111 2008-11-11 11:11:11Z $
 */
public abstract class AbstractUserProfileTest
   extends AbstractOrganizationTest
{

   private List<User> users;

   private List<UserProfile> userProfiles;

   private int indexUser = 0;

   private int indexUserProfile = 0;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      int runIterations = tc.getIntParam("japex.runIterations");

      if (tc.hasParam("japex.warmupIterations"))
      {
         int warmUpIterations = tc.getIntParam("japex.warmupIterations");
         if (warmUpIterations>0) 
         {
            runIterations += warmUpIterations;
         }
      }
      
      userProfiles = new ArrayList<UserProfile>();
      users = new ArrayList<User>();

      for (int i = 0; i < runIterations; i++)
      {
         User user = uHandler.createUserInstance(context.generateUniqueName("UserName"));
         user.setFirstName(context.generateUniqueName("First"));
         user.setLastName(context.generateUniqueName("Last"));
         user.setPassword(context.generateUniqueName("Password"));
         user.setEmail(context.generateUniqueName("Eemail"));
         users.add(user);

         UserProfile userProfile = upHandler.createUserProfileInstance(user.getUserName());
         userProfile.setAttribute("attr1", "value1");
         userProfile.setAttribute("attr2", "value2");
         userProfile.setAttribute("attr3", "value3");
         userProfile.setAttribute("attr4", "value4");
         userProfile.setAttribute("attr5", "value5");
         userProfile.setAttribute("attr6", "value6");
         userProfile.setAttribute("attr7", "value7");
         userProfile.setAttribute("attr8", "value8");
         userProfile.setAttribute("attr9", "value9");
         userProfile.setAttribute("attr10", "value10");
         userProfiles.add(userProfile);
      }
   }

   protected void createContent() throws Exception
   {
      for (User user : users)
      {
         uHandler.createUser(user, false);
      }

      for (UserProfile userProfile : userProfiles)
      {
         upHandler.saveUserProfile(userProfile, false);
      }
   }

   protected UserProfile nextUserProfile()
   {
      return userProfiles.get(indexUserProfile++);
   }

   protected UserProfile nextUser()
   {
      return userProfiles.get(indexUser++);
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      // UserProfiles will be removed automatically
      for (User user : users)
      {
         String name = user.getUserName();
         user = uHandler.findUserByName(name);
         if (user != null)
         {
            uHandler.removeUser(name, false);
         }
      }

      users.clear();
      users = null;

      userProfiles.clear();
      userProfiles = null;

      super.doFinish(tc, context);
   }
}
