/***************************************************************************
 * Copyright 2001-2008 The eXo Platform SAS          All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.jcr.benchmark.jcrapi.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.jcr.Node;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.exoplatform.jcr.benchmark.JCRTestContext;

import com.sun.japex.TestCase;

/**
 * Created by The eXo Platform SAS
 * 
 * @author Vitaliy Obmanyuk
 */

public abstract class AbstractContentCreatorForExportTest
   extends AbstractGetObjectTest
{

   @Override
   protected void createContent(Node parent, TestCase tc, JCRTestContext context) throws Exception
   {
      Node node = parent.addNode(context.generateUniqueName("testNode"));
      addPath(node.getPath());

      File destFile = File.createTempFile(context.generateUniqueName("testExportImport"), ".xml");
      destFile.deleteOnExit();
      OutputStream outputStream = new FileOutputStream(destFile);
      SAXTransformerFactory saxFact = (SAXTransformerFactory) TransformerFactory.newInstance();
      TransformerHandler transformerHandler = saxFact.newTransformerHandler();
      transformerHandler.setResult(new StreamResult(outputStream));
      addOutputStream(outputStream);// do not forget to add this method to be sure that os is closed
      addTransformerHandler(transformerHandler);
   }

}
