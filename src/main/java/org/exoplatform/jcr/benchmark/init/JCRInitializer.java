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

import javax.jcr.Repository;

import com.sun.japex.Params;

/**
 * Created by The eXo Platform SAS . Abstract class encapsulates mechanizm of repository
 * initialization
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public abstract class JCRInitializer
{

   protected Repository repository;

   /**
    * Initializes repository
    * 
    * @param params
    */
   public abstract void initialize(Params params);

   /**
    * @return repository
    */
   public final Repository getRepository()
   {
      return repository;
   }

}
