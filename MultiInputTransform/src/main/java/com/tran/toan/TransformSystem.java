package com.tran.toan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class TransformSystem {
	public static void main(String[] args) throws TransformerException 
	{
		System.out.println("started");
		transformWithListInput();
		System.out.println("finished");
	}
	
	public static void transformWithListInput() throws TransformerException{
		File xslFile = new File("src/main/resources/transforms.xslt");
		TransformerFactory factory = TransformerFactory.newInstance();
		Source xslt = new StreamSource(xslFile);
		Transformer transformer = factory.newTransformer(xslt);
		
		
		
		File input1File = new File("src/main/resources/input1.xml");
		Source text = new StreamSource(input1File);
		
		File input2File = new File("src/main/resources/input2.xml");
		Source text2 = new StreamSource(input2File);
		
		transformer.setParameter("updates", text2);
		
		List<String> list = new ArrayList<String>();
		list.add("file1");
		list.add("file2");
		transformer.setParameter("list", list);
		
		transformer.transform(text, new StreamResult(new File("src/main/resources/output.xml")));
	}
	
}
