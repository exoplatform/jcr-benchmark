/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
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
package org.exoplatform.jcr.benchmark.jcrapi.node.read;


/**
 * Created by The eXo Platform SAS.
 * 
 * <br/>Date: 2011
 *
 * @author <a href="mailto:alex.reshetnyak@exoplatform.com.ua">Alex Reshetnyak</a> 
 * @version $Id$
 */
public abstract class AbstractExectlyPrefixWildCardNamePatternTest
   extends AbstractPatternTest
{

   protected String nodeNamePatern = "jcr:exectly_node_*";

   protected String properyNamePatern = "jcr:exectly_property_*";

   /**
    * {@inheritDoc}
    */
   protected String getNodeNameAsPattern()
   {
      return "jcr:exectly_node_name";
   }

   /**
    * {@inheritDoc}
    */
   protected String getProperyNameAsPattern()
   {
      return "jcr:exectly_property_name";
   }

   /**
    * {@inheritDoc}
    */
   protected String getNodeNamePattern()
   {
      return nodeNamePatern;
   }

   /**
    * {@inheritDoc}
    */
   protected String getProperyNamePattern()
   {
      return properyNamePatern;
   }

}
