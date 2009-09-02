/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.Node;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public abstract class AbstractContentCreatorForImportTest
   extends AbstractGetObjectTest
{

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      Node node = parent.addNode(context.generateUniqueName("testNode"));
      addPath(parent.getPath());

      File file = File.createTempFile(context.generateUniqueName("testExportImport"), ".xml");
      file.deleteOnExit();
      OutputStream outputStream = new FileOutputStream(file);
      context.getSession().exportDocumentView(node.getPath(), outputStream, false, false);

      outputStream.close();
      node.remove();

      InputStream inputStream = new FileInputStream(file);
      addInputStream(inputStream);// do not forget to add this method to be sure that os is closed
   }

}
