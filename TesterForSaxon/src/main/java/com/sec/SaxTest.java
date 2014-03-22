package com.sec;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;



public class SaxTest 
{
	
	
	public static void main(String[] args) throws TransformerException 
	{
		System.out.println("started");
		File xslFile = new File("src/main/resources/transforms.xslt");
		TransformerFactory factory = TransformerFactory.newInstance();
		Source xslt = new StreamSource(xslFile);
		Transformer transformer = factory.newTransformer(xslt);
		
		
		
		File input1File = new File("src/main/resources/input1.xml");
		Source text = new StreamSource(input1File);
		
		File input2File = new File("src/main/resources/input2.xml");
		Source text2 = new StreamSource(input2File);
		
		transformer.setParameter("updates", text2);
		
		transformer.transform(text, new StreamResult(new File("src/main/resources/output.xml")));
		System.out.println("finished");
	}
	
	public static Document getDoc(File file) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		return docBuilder.parse(file);
		
	}
}
