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

import com.sun.japex.TestCase;

import org.exoplatform.common.http.client.HTTPConnection;
import org.exoplatform.common.http.client.ModuleException;

import java.io.IOException;

/**
 * @author <a href="mailto:dmitry.kataev@exoplatform.com">Dmytro Katayev</a>
 * @version $Id$
 *
 */
public class JCRWebdavConnection extends HTTPConnection
{

   private String realm;

   private String user;

   private String pass;

   private String workspacePath;

   public JCRWebdavConnection(TestCase tc)
   {
      super(tc.getParam("webdav.host"), tc.getIntParam("webdav.port"));

      user = tc.getParam("webdav.user");
      pass = tc.getParam("webdav.pass");
      realm = tc.getParam("webdav.realm");

      addBasicAuthorization(realm, user, pass);

      workspacePath = tc.getParam("webdav.workspacePath");
   }
   
   public void addNode(String name, byte[] data) throws IOException, ModuleException
   {
      Put(workspacePath + name, data);
   }

   
   public void removeNode(String name) throws IOException, ModuleException
   {
      Delete(workspacePath + name);
   }
   
   public void getNodeByPAth(String path)
   {
      
   }
   
   public void getNodeByUUID(String UUID)
   {
      
   }
   
   public void createProperty(String name)
   {
      
   }
   
   public void setProperty(String nodeName, String property, String value)
   {
      
   }
   
   public void removeProperty(String nodeName, String propertyName)
   {
      
   }
   
   public void getPropertyValue(String nodeName, String propertyName)
   {
      
   }

}
