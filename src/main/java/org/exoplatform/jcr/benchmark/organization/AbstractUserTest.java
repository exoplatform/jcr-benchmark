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

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: AbstractOrganizationTest.java 111 2008-11-11 11:11:11Z $
 */
public abstract class AbstractUserTest extends AbstractOrganizationTest
{

   private List<User> users;

   private int index = 0;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      int runIterations = tc.getIntParam("japex.runIterations");

      if (tc.hasParam("japex.warmupIterations"))
      {
         int warmUpIterations = tc.getIntParam("japex.warmupIterations");
         if (warmUpIterations > 0)
         {
            runIterations += warmUpIterations;
         }
      }

      users = new ArrayList<User>();
      for (int i = 0; i < runIterations; i++)
      {
         User user = uHandler.createUserInstance(context.generateUniqueName("UserName"));
         user.setFirstName(context.generateUniqueName("First"));
         user.setLastName(context.generateUniqueName("Last"));
         user.setPassword(context.generateUniqueName("Password"));
         user.setEmail(context.generateUniqueName("Eemail"));
         users.add(user);
      }
   }

   protected void createContent() throws Exception
   {
      for (User user : users)
      {
         uHandler.createUser(user, false);
      }
   }

   protected List<User> getUsers()
   {
      return users;
   }

   protected User nextUser()
   {
      return users.get(index++);
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
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

      super.doFinish(tc, context);
   }
}
