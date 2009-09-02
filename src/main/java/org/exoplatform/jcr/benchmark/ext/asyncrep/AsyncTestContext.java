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
package org.exoplatform.jcr.benchmark.ext.asyncrep;

import java.util.HashMap;

import javax.jcr.Credentials;
import javax.jcr.Session;

//import org.exoplatform.services.jcr.ext.replication.async.AsyncReplication;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 
 *
 * @author <a href="karpenko.sergiy@gmail.com">Karpenko Sergiy</a> 
 * @version $Id: AsyncTestContext.java 111 2008-11-11 11:11:11Z serg $
 */
public class AsyncTestContext
   extends HashMap<String, Object>
{

   protected static int threadCounter = 0;

   private static final String sessionId = "" + System.currentTimeMillis();

   public static final String SESSION = "session";

   public static final String REPLICATION = "asyncrep";

   public static final String CREDENTIALS = "credentials";

   private int counter = 0;

   private String name;

   public AsyncTestContext()
   {
      super();
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

   //TODO
   /*public void setReplicationServer(AsyncReplication rep) {
     put(REPLICATION, rep);
   }

   public AsyncReplication getReplicationServer() {
     return (AsyncReplication) get(REPLICATION);
   }*/

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