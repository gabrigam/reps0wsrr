package com.isp.nbp.utility;
import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.isp.wsrr.utility.WSRRUtility;
import com.lombardisoftware.core.TWObject;

import teamworks.TWList;
import teamworks.TWObjectFactory;

public class WSRRToBusinessObject {

	// 150117 prima scrittura

	// 180117 versione testata ok
	
	// 11052017 nella creazione degli endpoint sostituito "NO" con "N" (si tratta del flag header) 
	
	// 21052017 inserita gestione flag ispheader
	
	// 27052017 inserita gestione timeout

	public WSRRToBusinessObject() {

		// notes 
	}
	
	public void testGab() throws Exception {
		
		TWObject p=WSRRToBusinessObject.getPerson("email0");
		TWObject p1=WSRRToBusinessObject.getPerson1("email1");
		TWObject all=WSRRToBusinessObject.getPerson3(p,p1);
		System.out.println(((TWObject)all.getPropertyValue("p")).getPropertyValue("lastName"));
		System.out.println(((TWObject)all.getPropertyValue("p1")).getPropertyValue("lastName"));
		
		System.out.println(((TWObject)all.getPropertyValue("p")).getPropertyValue("age"));
		System.out.println(((TWObject)all.getPropertyValue("p1")).getPropertyValue("firstName1"));
	}
	
	public static TWObject getPerson(String email) throws Exception {
		 // Create twobject person
		 TWObject twPerson = (TWObject) TWObjectFactory.createObject();
		 twPerson.setPropertyValue("email", email); 
		 twPerson.setPropertyValue("firstName", "John1");
		 twPerson.setPropertyValue("lastName", "Doe1");
		 twPerson.setPropertyValue("age", 251); 
		 twPerson.setPropertyValue("phone", "512-777-9999_1");
		 return twPerson; 
		}
	
	public static TWObject getPerson1(String email) throws Exception {
		 // Create twobject person
		 TWObject twPerson = (TWObject) TWObjectFactory.createObject();
		 twPerson.setPropertyValue("email1", email); 
		 twPerson.setPropertyValue("firstName1", "John2");
		 twPerson.setPropertyValue("lastName1", "Doe2");
		 twPerson.setPropertyValue("ag1e", 252); 
		 twPerson.setPropertyValue("phone1", "512-777-9999_2");
		 return twPerson; 
		}
	
	public static TWObject getPerson3(TWObject p,TWObject p1) throws Exception {
		 // Create twobject person
		 TWObject allperson = (TWObject) TWObjectFactory.createObject();
		 allperson.setPropertyValue("p", p);
		 allperson.setPropertyValue("p1", p1);
		 return allperson; 
	}

	public static void parse() throws XPathExpressionException {
		
		String xml = "<resp><status>good</status><msg>hi</msg></resp>";

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		InputSource source = new InputSource(new StringReader(xml));
		Document doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
		String status = xpath.evaluate("/resp/status", doc);
		String msg = xpath.evaluate("/resp/msg", doc);

		System.out.println("status=" + status);
		System.out.println("Message=" + msg);
		 
	}
	
	
	public static void main (String[] a) {
		
		try {
			WSRRToBusinessObject.parse();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
