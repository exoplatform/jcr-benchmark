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

import java.util.HashMap;

import javax.jcr.Credentials;
import javax.jcr.Session;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public final class JCRTestContext
   extends HashMap<String, Object>
{

   protected static int threadCounter = 0;

   private static final String sessionId = "" + System.currentTimeMillis();

   public static final String SESSION = "session";

   public static final String CREDENTIALS = "credentials";

   private int counter = 0;

   private String name;

   public JCRTestContext()
   {
      this.name = sessionId + "-" + (threadCounter++);
   }

   public void setSession(Session session)
   {
      put(SESSION, session);
   }

   public Session getSession()
   {
      return (Session) get(SESSION);
   }

   public void setCredentials(Credentials credentials)
   {
      put(CREDENTIALS, credentials);
   }

   public Credentials getCredentials()
   {
      return (Credentials) get(CREDENTIALS);
   }

   public String generateUniqueName(String prefix)
   {
      return prefix + "-" + name + "-" + (counter++);
   }

}
