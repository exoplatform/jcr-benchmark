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
package org.exoplatform.jcr.benchmark.init;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.jcr.RepositoryService;

import com.sun.japex.Params;

/**
 * Created by The eXo Platform SAS . Initializes the local (Current) eXo Repository registered in
 * Standalone eXo Container
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class EXOJCRStandaloneInitializer
   extends JCRInitializer
{
   public void initialize(Params params)
   {
      if (!params.hasParam("exo.jaasConf"))
         throw new RuntimeException("<exo.jaasConf> parameter required");

      if (!params.hasParam("exo.containerConf"))
         throw new RuntimeException("<exo.containerConf> parameter required");

      String jaasConf = params.getParam("exo.jaasConf");
      String containerConf = params.getParam("exo.containerConf");
      try
      {

         StandaloneContainer.addConfigurationPath(containerConf);
         StandaloneContainer container = StandaloneContainer.getInstance();
         if (System.getProperty("java.security.auth.login.config") == null)
            System.setProperty("java.security.auth.login.config", Thread.currentThread().getContextClassLoader()
                     .getResource(jaasConf).toString());
         RepositoryService repositoryService =
                  (RepositoryService) container.getComponentInstanceOfType(RepositoryService.class);
         repository = repositoryService.getCurrentRepository();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

}
