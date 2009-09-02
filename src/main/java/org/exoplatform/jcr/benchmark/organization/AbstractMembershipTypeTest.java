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
import org.exoplatform.services.organization.MembershipType;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:anatoliy.bazko@exoplatform.com.ua">Anatoliy Bazko</a>
 * @version $Id: AbstractOrganizationTest.java 111 2008-11-11 11:11:11Z $
 */
public abstract class AbstractMembershipTypeTest
   extends AbstractOrganizationTest
{

   private List<MembershipType> mts;

   private int index = 0;

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
      
      mts = new ArrayList<MembershipType>();
      for (int i = 0; i < runIterations; i++)
      {
         MembershipType mt = mtHandler.createMembershipTypeInstance();
         mt.setName(context.generateUniqueName("membershiptype"));
         mts.add(mt);
      }
   }

   protected void createContent() throws Exception
   {
      for (MembershipType mt : mts)
      {
         mtHandler.createMembershipType(mt, false);
      }

      mts.clear();
      mts = (List<MembershipType>) mtHandler.findMembershipTypes();
      for (int i = mts.size() - 1; i >= 0; i--)
      {
         if (!mts.get(i).getName().startsWith("membershiptype"))
         {
            mts.remove(i);
         }
      }
   }

   protected MembershipType nextMembershipType()
   {
      return mts.get(index++);
   }

   @Override
   public void doFinish(TestCase tc, JCRTestContext context) throws Exception
   {
      for (MembershipType mt : mts)
      {
         String name = mt.getName();
         mt = mtHandler.findMembershipType(name);
         if (mt != null)
         {
            mtHandler.removeMembershipType(name, false);
         }
      }

      mts.clear();
      mts = null;

      super.doFinish(tc, context);
   }
}
