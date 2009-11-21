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
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.User;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: AbstractOrganizationTest.java 111 2008-11-11 11:11:11Z $
 */
public abstract class AbstractMembershipTest extends AbstractOrganizationTest
{

   private List<Membership> memberships;

   private List<User> users;

   private List<Group> groups;

   private List<MembershipType> membershipTypes;

   private int indexUser = 0;

   private int indexGroup = 0;

   private int indexMembershipType = 0;

   private int indexMembership = 0;

   private int runIterations;

   @Override
   public void doPrepare(TestCase tc, JCRTestContext context) throws Exception
   {
      super.doPrepare(tc, context);

      runIterations = tc.getIntParam("japex.runIterations");

      if (tc.hasParam("japex.warmupIterations"))
      {
         int warmUpIterations = tc.getIntParam("japex.warmupIterations");
         if (warmUpIterations > 0)
         {
            runIterations += warmUpIterations;
         }
      }

      users = new ArrayList<User>();
      groups = new ArrayList<Group>();
      membershipTypes = new ArrayList<MembershipType>();
      memberships = new ArrayList<Membership>();
      for (int i = 0; i < runIterations; i++)
      {
         User user = uHandler.createUserInstance(context.generateUniqueName("UserName"));
         user.setFirstName(context.generateUniqueName("First"));
         user.setLastName(context.generateUniqueName("Last"));
         user.setPassword(context.generateUniqueName("Password"));
         user.setEmail(context.generateUniqueName("Email"));
         users.add(user);

         Group group = gHandler.createGroupInstance();
         group.setGroupName(context.generateUniqueName("group"));
         group.setLabel(context.generateUniqueName("label"));
         groups.add(group);

         MembershipType mt = mtHandler.createMembershipTypeInstance();
         mt.setName(context.generateUniqueName("membershiptype"));
         membershipTypes.add(mt);
      }
   }

   protected void createContent(boolean linkMembership) throws Exception
   {
      for (int i = 0; i < runIterations; i++)
      {
         User user = users.get(i);
         uHandler.createUser(user, false);

         Group g = groups.get(i);
         gHandler.createGroup(g, false);

         MembershipType mt = membershipTypes.get(i);
         mtHandler.createMembershipType(mt, false);

         if (linkMembership)
         {
            mHandler.linkMembership(user, g, mt, false);

            Membership m =
               mHandler.findMembershipByUserGroupAndType(user.getUserName(), "/" + g.getGroupName(), mt.getName());
            memberships.add(m);
         }
      }
   }

   protected User nextUser()
   {
      return users.get(indexUser++);
   }

   protected Group nextGroup()
   {
      return groups.get(indexGroup++);
   }

   protected MembershipType nextMembershipType()
   {
      return membershipTypes.get(indexMembershipType++);
   }

   protected Membership nextMembership()
   {
      return memberships.get(indexMembership++);
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

      for (Group g : groups)
      {
         Group group = gHandler.findGroupById("/" + g.getGroupName());
         if (group != null)
         {
            gHandler.removeGroup(group, false);
         }
      }

      for (MembershipType mt : membershipTypes)
      {
         String name = mt.getName();
         mt = mtHandler.findMembershipType(name);
         if (mt != null)
         {
            mtHandler.removeMembershipType(name, false);
         }
      }

      users.clear();
      users = null;

      membershipTypes.clear();
      membershipTypes = null;

      groups.clear();
      groups = null;

      super.doFinish(tc, context);
   }
}
