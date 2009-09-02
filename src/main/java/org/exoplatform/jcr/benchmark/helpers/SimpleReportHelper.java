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
package org.exoplatform.jcr.benchmark.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public class SimpleReportHelper
{

   public static Date TODAY = new Date();

   /**
    * @param args
    */
   public static void main(String[] args)
   {
      // copying additional configs
      String reportsDir = "../reports/";
      String lastDirName = reportsDir + "last/";
      String srcConfig1 = "../config/test-configuration-benchmark.xml";
      String destConfig1 = lastDirName + "test-configuration-benchmark.xml";
      String srcConfig2 = "../config/test-jcr-config-benchmark.xml";
      String destConfig2 = lastDirName + "test-jcr-config-benchmark.xml";
      String message = "Copying passed ";
      try
      {
         {
            FileChannel srcChannel = new FileInputStream(srcConfig1).getChannel();
            FileChannel destChannel = new FileOutputStream(destConfig1).getChannel();
            destChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            destChannel.close();
         }
         {
            FileChannel srcChannel = new FileInputStream(srcConfig2).getChannel();
            FileChannel destChannel = new FileOutputStream(destConfig2).getChannel();
            destChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            destChannel.close();
         }
         message += "SUCCESSFULLY";
      }
      catch (IOException e)
      {
         e.printStackTrace();
         message += "ERROR";
      }
      System.out.println(message);
      // renaming reports directory
      message = "Renaming of 'last' directory passed ";
      DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
      String newDirName = reportsDir + df.format(TODAY) + "-exo";
      File oldDir = new File(lastDirName);
      File newDir = new File(newDirName);
      boolean success = oldDir.renameTo(newDir);
      if (success)
      {
         message += "SUCCESSFULLY";
      }
      else
      {
         message += "ERROR";
      }
      System.out.println(message);
   }

}
