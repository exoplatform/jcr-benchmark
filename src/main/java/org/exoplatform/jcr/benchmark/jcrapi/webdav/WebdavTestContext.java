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
package org.exoplatform.jcr.benchmark.jcrapi.webdav;

import java.util.HashMap;

/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 2009
 *
 * @author <a href="mailto:alex.reshetnyak@exoplatform.com.ua">Alex Reshetnyak</a> 
 * @version $Id$
 */
public class WebdavTestContext
   extends HashMap<String, Object>
{
   protected static int threadCounter = 0;

   private static final String sessionId = "" + System.currentTimeMillis();
   
   private int counter = 0;

   private String name;
   
   public WebdavTestContext()
   {
      this.name = sessionId + "-" + (threadCounter++);   
   }
   
   public String generateUniqueName(String prefix)
   {
      return prefix + "-" + name + "-" + (counter++);
   }
}
