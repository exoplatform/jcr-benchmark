/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public abstract class JCRTestBase
{

   protected static Log log = ExoLogger.getLogger("jcr.benchmark");

   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doPrepare(final TestCase tc, JCRTestContext context) throws Exception
   {
   }

   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public void doFinish(final TestCase tc, JCRTestContext context) throws Exception
   {
   }

   /**
    * @param tc
    * @param context
    * @throws Exception
    */
   public abstract void doRun(final TestCase tc, JCRTestContext context) throws Exception;

}
