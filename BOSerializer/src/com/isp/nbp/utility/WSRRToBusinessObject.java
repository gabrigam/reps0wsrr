package com.isp.nbp.utility;
import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;


import com.lombardisoftware.core.TWObject;

import teamworks.TWList;
import teamworks.TWIndexedMap;
import teamworks.TWObjectFactory;

public class WSRRToBusinessObject {

	// 150117 prima scrittura

	// 180117 versione testata ok
	
	// 11052017 nella creazione degli endpoint sostituito "NO" con "N" (si tratta del flag header) 
	
	// 21052017 inserita gestione flag ispheader
	
	// 27052017 inserita gestione timeout
	
	String query1="/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']";
	
	//String[] SCHOST_ATT="{}"

	public WSRRToBusinessObject() {


	}
	
	private TWObject makeServiceVersionBO(String name,String version,String url,String user,String password) throws XPathExpressionException {
		
		query1=query1.replaceAll("%CATALOGNAME%", name).replaceAll("%VERSION%", version);
		String result; 
		
		WSRRUtility wsrrutility =new WSRRUtility();
		
		result=wsrrutility.generalWSRRQuery(query1, url, user, password);
		
		TWObject BV_BO = null;
		
		if (result==null) return null;
		
		//recupero il tipo dell'oggetto
		
		//determino il tipo e sottotipo dell'oggetto
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		//result=result.replaceAll("(\\r|\\n)", "");
		InputSource source = new InputSource(new StringReader(result));
		Document doc=null;
		int count =0;
		String current=null;
		String value=null;
		String classification=null;
		String type=null;
		String subType=null;
		
		try {
			//BV_BO = (TWObject) TWObjectFactory.createObject();
			
			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			count = Integer.parseInt(xpath.evaluate("count(/resources/resource/classifications/classification)", doc));
			
			for (int i=1; i<=count; i++)
			{
			    current="/resources/resource/classifications/classification["+String.valueOf(i)+"]/@uri";
			    classification= xpath.evaluate(current, doc);
			    if (classification !=null && classification.indexOf("http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel#") != -1) {
			    	type=classification.substring(classification.indexOf("#")+1, classification.length());
			    }
			    if (classification !=null && classification.indexOf("http://isp/#") != -1) {
			    	subType=classification.substring(classification.indexOf("#")+1, classification.length());
			    }
			    current=null;
			}
			
			count  = Integer.parseInt(xpath.evaluate("count(/resources/resource/properties/property)", doc));
			
			for (int i=1; i<=count; i++)
			{
			    current="/resources/resource/properties/property["+String.valueOf(i)+"]/@name";
                value="/resources/resource/properties/property["+String.valueOf(i)+"]/@value";
			    System.out.println(xpath.evaluate(current, doc)+"="+xpath.evaluate(value, doc));
			    current=null;
			    value=null;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		

		
		System.out.println(type);
		System.out.println(subType);
		
		return null;
	}
	
	public void testGab() throws Exception {
		
		TWObject p=WSRRToBusinessObject.getPerson("email0");
		TWObject p1=WSRRToBusinessObject.getPerson1("email1");
		TWObject all=WSRRToBusinessObject.getPerson3(p,p1);
		
		TWList list=(TWList)all.getPropertyValue("p3");
		
		System.out.println(((TWObject)all.getPropertyValue("p")).getPropertyValue("lastName"));
		System.out.println(((TWObject)all.getPropertyValue("p1")).getPropertyValue("lastName"));
		
		System.out.println(((TWObject)all.getPropertyValue("p")).getPropertyValue("age"));
		System.out.println(((TWObject)all.getPropertyValue("p1")).getPropertyValue("firstName1"));
		System.out.println("--------------------");
		System.out.println(((TWObject)list.getArrayData(0)).getPropertyValue("email"));
		System.out.println(((TWObject)list.getArrayData(1)).getPropertyValue("email1"));
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
		 
		 TWList items = TWObjectFactory.createList();
		 
		 items.addArrayData(0,p);
		 items.addArrayData(1,p1);
		 
		 allperson.setPropertyValue("p3", items);
		 
		 return allperson; 
	}
	
	public  static TWObject gabTEST2() throws Exception {
		 // Create twobject person
		
		System.out.println("111111111111111111111111111111111111111111111111111111111111");
		
		 TWObject o1 = (TWObject) TWObjectFactory.createObject();
		 o1.setPropertyValue("campo1", "campo1_o1"); 
		 o1.setPropertyValue("campo2", "campo2_o1");
		 
		 TWObject o2 = (TWObject) TWObjectFactory.createObject();
		 o2.setPropertyValue("campo1", "campo1_o2"); 
		 o2.setPropertyValue("campo2", "campo1_o2");
		 
		 TWObject o3 = (TWObject) TWObjectFactory.createObject();
		 o3.setPropertyValue("campo1", "campo1_o3"); 
		 o3.setPropertyValue("campo2", "campo1_o3");
		 
		 TWList o4 = TWObjectFactory.createList(); 
		 o4.addArrayData(0,o3);
		 
		 TWObject o5 = (TWObject) TWObjectFactory.createObject();
		 o5.setPropertyValue("campo1", o1); 
		 o5.setPropertyValue("campo2", o2);
		 o5.setPropertyValue("campo3", o4);
		 o5.setPropertyValue("campo4", "ciaoooo");
		 
		 System.out.println("22222222222222222222222222222222222222222222222222222222222");
		 
		 return o5; 
		}

	public static void parse() throws XPathExpressionException {
		
		String xml = "<resp><status>good</status><msg>hi</msg></resp>";
		xml="<resources><resource bsrURI=\"478b8e47-9ac9-49c7.8f60.4b460b4b607b\" type=\"GenericObject\" governanceRootBsrURI=\"478b8e47-9ac9-49c7.8f60.4b460b4b607b\"><properties><property name=\"bsrURI\" value=\"478b8e47-9ac9-49c7.8f60.4b460b4b607b\"/><property name=\"name\" value=\"CUGNA11\"/><property name=\"namespace\" value=\"\"/><property name=\"version\" value=\"00\"/><property name=\"description\" value=\"A\"/><property name=\"owner\" value=\"SOAGov1\"/><property name=\"lastModified\" value=\"1480424920515\"/><property name=\"creationTimestamp\" value=\"1474810448761\"/><property name=\"lastModifiedBy\" value=\"gabriele\"/><property name=\"primaryType\" value=\"http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel#SHOSTServiceVersion\"/><property name=\"gep63_SHOST_TRANS_SERVIZIO\" value=\"aa\"/><property name=\"ale63_ownerEmail\" value=\"\"/><property name=\"ale63_assetType\" value=\"\"/><property name=\"ale63_remoteState\" value=\"\"/><property name=\"ale63_fullDescription\" value=\"\"/><property name=\"gep63_ATTIVATO_IN_APPL\" value=\"2013.05.25.18.01.26\"/><property name=\"gep63_SHOST_PGM_SERVIZIO\" value=\"AA\"/><property name=\"ale63_communityName\" value=\"\"/><property name=\"gep63_consumerIdentifier\" value=\"\"/><property name=\"gep63_SHOST_CONVNULL\" value=\"Y\"/><property name=\"gep63_SHOST_ID_SERVIZIO\" value=\"aa\"/><property name=\"ale63_guid\" value=\"\"/><property name=\"gep63_ATTIVATO_IN_SYST\" value=\"2013.05.25.18.01.26\"/><property name=\"gep63_SHOST_NOME_CPY_INP\" value=\"aa\"/><property name=\"ale63_assetOwners\" value=\"\"/><property name=\"gep63_SHOST_NOME_CPY_OUT\" value=\"aa\"/><property name=\"gep63_DESC_ESTESA\" value=\"A\"/><property name=\"gep63_ATTIVATO_IN_PROD\" value=\"2013.05.25.18.01.26\"/><property name=\"gep63_PID_PROCESSO_GOV\" value=\"574\"/><property name=\"gep63_SHOST_PGM_MD\" value=\"XICHACAL\"/><property name=\"gep63_DATA_PUBBLICAZIONE\" value=\"2016-09-26\"/><property name=\"gep63_PUBBLICATORE_SERV\" value=\"gabriele\"/><property name=\"gep63_SHOST_PGM_MD_X_MPE\" value=\"\"/><property name=\"gep63_TIPOLOGIA\" value=\"\"/><property name=\"gep63_MATR_PUBBLICATORE_CREAZ_SERV\" value=\"\"/><property name=\"gep63_DOC_ANALISI_DETTAGLIO\" value=\"\"/><property name=\"gep63_DATA_PUBBL_CREAZ_SERV\" value=\"\"/><property name=\"gep63_ABILITAZ_INFRASTR\" value=\"\"/><property name=\"gep63_TIPOLOGIA_OGGETTO_ESISTENTE\" value=\"\"/><property name=\"gep63_versionTerminationDate\" value=\"\"/><property name=\"gep63_MATR_RICH_MODIFICA\" value=\"\"/><property name=\"gep63_VINCOLI_RIUSO\" value=\"\"/><property name=\"gep63_UTILIZ_PIU_BAN_CLONI\" value=\"\"/><property name=\"gep63_NOME_SERVIZIO_PRECEDENTE\" value=\"\"/><property name=\"gep63_DOC_ANALISI_FUNZIONALE\" value=\"\"/><property name=\"gep63_DOC_ANALISI_TECNICA\" value=\"\"/><property name=\"gep63_INFO_COSTO\" value=\"\"/><property name=\"gep63_FLG_CTRL_TIPOLOGIA\" value=\"\"/><property name=\"gep63_DISP_SERV\" value=\"\"/><property name=\"gep63_DERIVANTE_DA_ALTRI_SERV\" value=\"\"/><property name=\"gep63_SECURITY_ROLE\" value=\"\"/><property name=\"gep63_MATR_RICH_CREAZIONE\" value=\"\"/><property name=\"gep63_DATA_RITIRO_SERV\" value=\"\"/><property name=\"gep63_PIATT_EROG\" value=\"\"/><property name=\"gep63_versionAvailabilityDate\" value=\"\"/><property name=\"ale63_requirementsLink\" value=\"\"/><property name=\"ale63_assetWebLink\" value=\"\"/></properties><relationships><relationship name=\"ale63_artifacts\"/><relationship name=\"gep63_interfaceSpecifications\"/><relationship name=\"gep63_providedWebServices\"/><relationship name=\"gep63_provides\" targetBsrURI=\"9b8f859b-11a8-4895.986e.d8eafbd86eb6\" targetType=\"GenericObject\" primaryType=\"http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel#ServiceLevelDefinition\"/><relationship name=\"ale63_dependency\"/><relationship name=\"gep63_SHOST_CPY_INP\" targetBsrURI=\"f2cebdf2-d1ee-4e84.85da.f476bff4daca\" targetType=\"GenericDocument\"/><relationship name=\"gep63_providedRESTServices\"/><relationship name=\"ale63_owningOrganization\" targetBsrURI=\"5025de50-ea22-4207.b68d.d18338d18d06\" targetType=\"GenericObject\" primaryType=\"http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel#Organization\"/><relationship name=\"gep63_consumes\"/><relationship name=\"gep63_SHOST_CPY_OUT\" targetBsrURI=\"4ae40a4a-db67-47e7.b88c.16bc31168c0b\" targetType=\"GenericDocument\"/><relationship name=\"gep63_providedSCAModules\"/><relationship name=\"gep63_SHOST_DFDL_INP\"/><relationship name=\"gep63_SHOST_DFDL_OUT\"/></relationships><classifications><classification uri=\"http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel#SHOSTServiceVersion\"/><classification uri=\"http://www.ibm.com/xmlns/prod/serviceregistry/lifecycle/v6r3/LifecycleDefinition#SOALifecycle_InImmissione\" governanceState=\"true\"/><classification uri=\"http://isp/#RTGEN\"/></classifications></resource></resources>";

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		InputSource source = new InputSource(new StringReader(xml));
		Document doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
		String status = xpath.evaluate("/resources/resource/properties/property[@name=\"version\"]/@value", doc);
		String status1 = xpath.evaluate("/resources/resource[@bsrURI=\"478b8e47-9ac9-49c7.8f60.4b460b4b607b\"]/classifications/classification[1]/@uri", doc);
		String status2 = xpath.evaluate("/resources/resource/classifications/classification[2]/@uri", doc);
		String status3 = xpath.evaluate("count(/resources/resource/classifications/classification)", doc);
		//count(/resources/resource/relationships/relationship)

		
		//String msg = xpath.evaluate(status1, doc);

		System.out.println("status=" + status);
		System.out.println("Message=" + status1);
		System.out.println("Message=" + status2);
		System.out.println("Message=" + status3);
		 
	}
	
	
	public static void main (String[] a) {
		
		try {
			WSRRToBusinessObject boi= new WSRRToBusinessObject();
			boi.makeServiceVersionBO("CUGNA11", "00", "https://WIN-MT67KKLQ7LO:9443/WSRR/8.5", "gabriele", "viviana");
			WSRRToBusinessObject.parse();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
