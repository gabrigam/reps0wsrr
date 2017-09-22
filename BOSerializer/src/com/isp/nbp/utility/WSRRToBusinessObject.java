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
import teamworks.TWObjectFactory;

//by RG
public class WSRRToBusinessObject {

	String query1 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[classifiedByAnyOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23SOPENServiceVersion','http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23SCOPENServiceVersion','http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23SHOSTServiceVersion','http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23SCHOSTServiceVersion')%20and%20@name='%CATALOGNAME%'%20and%20@version='%VERSION%']";
	String query2 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@primaryType='http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23%TYPE%']";
	String query3 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/ale63_owningOrganization(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')]";
	String query4 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)/gep63_availableEndpoints(.)";
	String query4bis = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_availableEndpoints(.)";
	String query5 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']";
	String query6 = "dynamic";
	String query7 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)/gep63_serviceInterface(.)"; // interfaccia
	String query7bis = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_serviceInterface(.)"; // interfaccia
	String query8 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)";

	public WSRRToBusinessObject() {

	}

	public TWObject WSRRBOSerializer(String name, String version, String url, String user, String password,
			boolean debug) throws XPathExpressionException {

		TWObject NBP_BO = null;
		TWObject SV_BO = null;
		TWObject BS_BO = null;
		TWObject ACR_BO = null;
		TWObject INTERF_BO = null;
		TWObject SLD_BO = null;

		TWObject EP_BO = null;
		TWObject PROXY_BO = null;

		TWList SLD_BO_LIST = null;
		TWList EP_BO_REST_APPL = null;
		TWList EP_BO_REST_SYST = null;
		TWList EP_BO_REST_PROD = null;
		TWList EP_BO_REST_UAT = null;
		TWList EP_BO_REST_INDEP = null;

		TWList EP_BO_SOAP_APPL = null;
		TWList EP_BO_SOAP_SYST = null;
		TWList EP_BO_SOAP_PROD = null;
		TWList EP_BO_SOAP_UAT = null;
		TWList EP_BO_SOAP_INDEP = null;

		TWList EP_BO_CALLABLE_APPL = null;
		TWList EP_BO_CALLABLE_SYST = null;
		TWList EP_BO_CALLABLE_PROD = null;
		TWList EP_BO_CALLABLE_UAT = null;
		TWList EP_BO_CALLABLE_INDEP = null;

		TWList EP_BO_ZRES_APPL = null;
		TWList EP_BO_ZRES_SYST = null;
		TWList EP_BO_ZRES_PROD = null;
		TWList EP_BO_ZRES_UAT = null;
		TWList EP_BO_ZRES_INDEP = null;

		TWList EP_BO_WOLA_APPL = null;
		TWList EP_BO_WOLA_SYST = null;
		TWList EP_BO_WOLA_PROD = null;
		TWList EP_BO_WOLA_UAT = null;
		TWList EP_BO_WOLA_INDEP = null;

		TWList EP_BO_CICS_APPL = null;
		TWList EP_BO_CICS_SYST = null;
		TWList EP_BO_CICS_PROD = null;
		TWList EP_BO_CICS_UAT = null;
		TWList EP_BO_CICS_INDEP = null;

		TWList EP_BO_MQ_APPL = null;
		TWList EP_BO_MQ_SYST = null;
		TWList EP_BO_MQ_PROD = null;
		TWList EP_BO_MQ_UAT = null;
		TWList EP_BO_MQ_INDEP = null;

		final String EP_MQ = "http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel#MQServiceEndpoint";
		final String EP_REST = "http://www.ibm.com/xmlns/prod/serviceregistry/profile/v8r0/RESTModel#RESTServiceEndpoint";
		final String EP_CICS = "http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel#CICSServiceEndpoint";
		final String EP_ZRES = "http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel#ZRESServiceEndpoint";
		final String EP_SOAP = "http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel#SOAPServiceEndpoint";
		final String EP_WOLA = "http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel#WOLAServiceEndpoint";
		final String EP_CALLABLE = "http://www.ibm.com/xmlns/prod/serviceregistry/profile/v8r0/RESTModel#CALLABLEServiceEndpoint";

		final String REST_EP = "[bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, rest80_baseURL, sm63_serviceVersion, sm63_serviceName, sm63_DATA_PRIMO_UTILIZZO, sm63_endpointType, sm63_DATA_ULTIMO_UTILIZZO, sm63_Timeout, sm63_serviceNamespace, rest80_ESPOSTO_COME_API, sm63_USO_SICUREZZA, sm63_SPECIALIZZAZIONE, rest80_ISPHEADER_FLAG]";
		final String ZRES_EP = "[bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_serviceVersion, sm63_serviceName, sm63_DATA_PRIMO_UTILIZZO, sm63_endpointType, sm63_DATA_ULTIMO_UTILIZZO, sm63_Timeout, sm63_serviceNamespace, sm63_USO_SICUREZZA, sm63_NOME_CPY, sm63_SPECIALIZZAZIONE]";
		final String SOAP_EP = "[bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_serviceVersion, sm63_serviceName, sm63_DATA_PRIMO_UTILIZZO, sm63_endpointType, sm63_ISPHEADER_FLAG, sm63_DATA_ULTIMO_UTILIZZO, sm63_Timeout, sm63_serviceNamespace, sm63_USO_SICUREZZA, sm63_SPECIALIZZAZIONE]";
		final String WOLA_EP = "[bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_serviceVersion, sm63_NOME_CPY_OUT, sm63_serviceName, sm63_DATA_PRIMO_UTILIZZO, sm63_endpointType, sm63_serviceNamespace, sm63_USO_SICUREZZA, sm63_NOME_CPY_INP, sm63_PGM_MD, sm63_DATA_ULTIMO_UTILIZZO, sm63_Timeout, sm63_NOME_CPY_SIST, sm63_SPECIALIZZAZIONE]";
		final String CALLABLE_EP = "[bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, rest80_baseURL, sm63_serviceVersion, sm63_serviceName, sm63_DATA_PRIMO_UTILIZZO, rest80_CALLABLE_ISPHEADER_FLAG, sm63_endpointType, sm63_DATA_ULTIMO_UTILIZZO, sm63_Timeout, sm63_serviceNamespace, sm63_USO_SICUREZZA, sm63_SPECIALIZZAZIONE]";
		final String MQ_EP = "[bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_PGM_DEST, sm63_serviceVersion, sm63_EXPIRY, sm63_serviceName, sm63_DATA_PRIMO_UTILIZZO, sm63_LUNGH_OUT, sm63_endpointType, sm63_PGM_DEST_RISP,sm63_FLAG_3LINK, sm63_STATO_OPER, sm63_serviceNamespace, sm63_PGM_QUADRATURA, sm63_USO_SICUREZZA, sm63_ID_APPL, sm63_TIPO_OPER, sm63_LUNGH_IN, sm63_TRACCIATURA, sm63_ALTER_COLL, sm63_TGT_SERVER, sm63_PRIORITY, sm63_PGM_FORM, sm63_DATA_ULTIMO_UTILIZZO, sm63_CALL_HEADER, sm63_MOD_COLLOQUIO, sm63_Timeout, sm63_ID_TGT_DES, sm63_SPECIALIZZAZIONE, sm63_BACKOUT_COUNT]";
		final String CICS_EP = "[bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_serviceVersion, sm63_serviceName, sm63_DATA_PRIMO_UTILIZZO, sm63_endpointType, sm63_Stage, sm63_DATA_ULTIMO_UTILIZZO, sm63_Timeout, sm63_serviceNamespace, sm63_USO_SICUREZZA, sm63_SPECIALIZZAZIONE]";

		System.out.println(
				"########################################################################################################################################");
		System.out.println("WSRRToBusinessObject V1.0 Sept 2017");
		System.out.println(
				"########################################################################################################################################");
		System.out.println("Parametri - Censimento : " + name + " versione : " + version + " wsrr : " + url);
		System.out.println(
				"########################################################################################################################################");

		query1 = query1.replaceAll("%CATALOGNAME%", name).replaceAll("%VERSION%", version);
		String result;
		String resultINT;
		String resultSLD;
		
		WSRRUtility wsrrutility = new WSRRUtility();

		result = wsrrutility.generalWSRRQuery(query1, url, user, password, debug);

		if (result == null)

			return null;

		WSRRToBusinessObject
		.log("Censimento : " + name + " versione : " + version + " Trovato procedo con l'analisi...", debug);
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		InputSource source = new InputSource(new StringReader(result));
		Document doc = null;
		int count = 0;
		String current = null;
		String classification = null;
		String relation = null;
		String type = null;
		String subType = null;
		String governanceState=null;
		String environment = null;
		String proxy = null;
		String proxybsrURI = null;
		String target = "UNDEF";

		try {
			NBP_BO = (TWObject) TWObjectFactory.createObject();
			BS_BO = (TWObject) TWObjectFactory.createObject();
			SV_BO = (TWObject) TWObjectFactory.createObject();
			ACR_BO = (TWObject) TWObjectFactory.createObject();
			INTERF_BO = (TWObject) TWObjectFactory.createObject();
			SLD_BO = (TWObject) TWObjectFactory.createObject();

			EP_BO_REST_APPL = TWObjectFactory.createList();
			EP_BO_REST_SYST = TWObjectFactory.createList();
			EP_BO_REST_PROD = TWObjectFactory.createList();
			EP_BO_REST_UAT = TWObjectFactory.createList();
			EP_BO_REST_INDEP = TWObjectFactory.createList();

			EP_BO_SOAP_APPL = TWObjectFactory.createList();
			EP_BO_SOAP_SYST = TWObjectFactory.createList();
			EP_BO_SOAP_PROD = TWObjectFactory.createList();
			EP_BO_SOAP_UAT = TWObjectFactory.createList();
			EP_BO_SOAP_INDEP = TWObjectFactory.createList();

			EP_BO_CALLABLE_APPL = TWObjectFactory.createList();
			EP_BO_CALLABLE_SYST = TWObjectFactory.createList();
			EP_BO_CALLABLE_PROD = TWObjectFactory.createList();
			EP_BO_CALLABLE_UAT = TWObjectFactory.createList();
			EP_BO_CALLABLE_INDEP = TWObjectFactory.createList();

			EP_BO_ZRES_APPL = TWObjectFactory.createList();
			EP_BO_ZRES_SYST = TWObjectFactory.createList();
			EP_BO_ZRES_PROD = TWObjectFactory.createList();
			EP_BO_ZRES_UAT = TWObjectFactory.createList();
			EP_BO_ZRES_INDEP = TWObjectFactory.createList();

			EP_BO_WOLA_APPL = TWObjectFactory.createList();
			EP_BO_WOLA_SYST = TWObjectFactory.createList();
			EP_BO_WOLA_PROD = TWObjectFactory.createList();
			EP_BO_WOLA_UAT = TWObjectFactory.createList();
			EP_BO_WOLA_INDEP = TWObjectFactory.createList();

			EP_BO_CICS_APPL = TWObjectFactory.createList();
			EP_BO_CICS_SYST = TWObjectFactory.createList();
			EP_BO_CICS_PROD = TWObjectFactory.createList();
			EP_BO_CICS_UAT = TWObjectFactory.createList();
			EP_BO_CICS_INDEP = TWObjectFactory.createList();

			EP_BO_MQ_APPL = TWObjectFactory.createList();
			EP_BO_MQ_SYST = TWObjectFactory.createList();
			EP_BO_MQ_PROD = TWObjectFactory.createList();
			EP_BO_MQ_UAT = TWObjectFactory.createList();
			EP_BO_MQ_INDEP = TWObjectFactory.createList();
			SLD_BO_LIST = TWObjectFactory.createList();

			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			count = Integer.parseInt(xpath.evaluate("count(/resources/resource/classifications/classification)", doc));

			if (count == 0)
				return null;

			for (int i = 1; i <= count; i++) {
				current = "/resources/resource/classifications/classification[" + String.valueOf(i) + "]/@uri";
				classification = xpath.evaluate(current, doc);
				if (classification != null && classification.indexOf(
						"http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel#") != -1) {
					type = classification.substring(classification.indexOf("#") + 1, classification.length());
				}
				if (classification != null && classification.indexOf("http://isp/#") != -1) {
					subType = classification.substring(classification.indexOf("#") + 1, classification.length());
				}
				
				//governance object state				
				if (classification != null && classification.indexOf("http://www.ibm.com/xmlns/prod/serviceregistry/lifecycle/v6r3/LifecycleDefinition#") != -1) {
					governanceState = classification.substring(classification.indexOf("#") + 1, classification.length());
				}
				
				current = null;
			}

			WSRRToBusinessObject.log("Censimento di tipo : " + type + " sottotipo : " + subType, debug);

			NBP_BO.setPropertyValue("SERVICE_INDEX", type + "#" + subType);
			
			NBP_BO.setPropertyValue("GOVERNANCE_STATE",governanceState);

			SV_BO = WSRRToBusinessObject.makeBO(result, -1, type, subType, url, user, password, debug);

			WSRRToBusinessObject.log("Creato oggetto SV_BO", debug);

			NBP_BO.setPropertyValue("SOPEN", null);
			NBP_BO.setPropertyValue("SCOPEN", null);
			NBP_BO.setPropertyValue("SHOST", null);
			NBP_BO.setPropertyValue("SCHOST", null);

			if (type.equals("SOPENServiceVersion"))
				NBP_BO.setPropertyValue("SOPEN", SV_BO);
			if (type.equals("SCOPENServiceVersion"))
				NBP_BO.setPropertyValue("SCOPEN", SV_BO);
			if (type.equals("SHOSTServiceVersion"))
				NBP_BO.setPropertyValue("SHOST", SV_BO);
			if (type.equals("SCHOSTServiceVersion"))
				NBP_BO.setPropertyValue("SCHOST", SV_BO);

			// Ho trovato il Service Version ora recupero il Business Service

			query2 = query2.replaceAll("%CATALOGNAME%", name).replaceAll("%TYPE%",
					type.substring(0, type.length() - 7));

			result = wsrrutility.generalWSRRQuery(query2, url, user, password, debug);

			BS_BO = WSRRToBusinessObject.makeBO(result, -1, type.substring(0, type.length() - 7), subType, url, user,
					password, debug);
			NBP_BO.setPropertyValue("BS", BS_BO);
			WSRRToBusinessObject.log("Creato oggetto BS_BO", debug);

			// Ricavo acronimo

			if (BS_BO == null) {
				NBP_BO.setPropertyValue("ACRO", BS_BO);
			} else {
				query3 = query3.replaceAll("%BSRURI%", (String) BS_BO.getPropertyValue("bsrURI"));

				result = wsrrutility.generalWSRRQuery(query3, url, user, password, debug);

				ACR_BO = WSRRToBusinessObject.makeBO(result, -1, "Organization", null, url, user, password, debug);
				NBP_BO.setPropertyValue("ACRO", ACR_BO);
				WSRRToBusinessObject.log("Creato oggetto ACR_BO", debug);
			}

			// Ricavo SLD

			if (BS_BO == null) {

				NBP_BO.setPropertyValue("SLD", BS_BO);

			} else {

				query8 = query8.replaceAll("%BSRURI%", (String) SV_BO.getPropertyValue("bsrURI"));

				resultSLD = wsrrutility.generalWSRRQuery(query8, url, user, password, debug);
				
				xpathFactory = XPathFactory.newInstance();
				xpath = xpathFactory.newXPath();
				// result=result.replaceAll("(\\r|\\n)", "");
				source = new InputSource(new StringReader(resultSLD));
				doc = null;
				count = 0;
				int countagSLD = 0;
				int countag = 0;
				current = null;
				String bsrURISLD = null;
				String interfaceTypes=null;

				doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

				countagSLD = Integer.parseInt(xpath.evaluate("count(/resources/resource)", doc));
				SLD_BO = null;

				if (countagSLD != 0) {
					
					WSRRToBusinessObject.log("Trovati - " + countagSLD+" oggetti di tipo SLD ", debug);

					for (int iii = 1; iii <= countagSLD; iii++) {

						SLD_BO = null;
						SLD_BO = WSRRToBusinessObject.makeBO(resultSLD, iii, "SLD", null, url, user, password, debug);
						bsrURISLD = (String) SLD_BO.getPropertyValue("bsrURI");
												
						interfaceTypes=(String)NBP_BO.getPropertyValue("INTERFACE_INDEX");
						
						if (interfaceTypes==null)
							NBP_BO.setPropertyValue("INTERFACE_INDEX", ((String)  SLD_BO.getPropertyValue("description")));
						else
							NBP_BO.setPropertyValue("INTERFACE_INDEX", interfaceTypes+"_"+(String)  SLD_BO.getPropertyValue("description"));
						
						query7bis = query7bis.replaceAll("%BSRURI%", (String) bsrURISLD);
						resultINT = wsrrutility.generalWSRRQuery(query7bis, url, user, password, debug);
						INTERF_BO = WSRRToBusinessObject.makeBO(resultINT, -1, "Interface", null, url, user, password,
								debug);
						SLD_BO.setPropertyValue("INTERF", INTERF_BO);
						WSRRToBusinessObject.log("Creato oggetto INTERF_BO", debug);						
						
						WSRRToBusinessObject.log("Creato oggetto SLD_BO", debug);

						// }

						SLD_BO.setPropertyValue("ENDPOINT_SOAP_APPL", null);
						SLD_BO.setPropertyValue("ENDPOINT_SOAP_SYST", null);
						SLD_BO.setPropertyValue("ENDPOINT_SOAP_PROD", null);
						SLD_BO.setPropertyValue("ENDPOINT_SOAP_UAT", null);
						SLD_BO.setPropertyValue("ENDPOINT_SOAP_INDEP", null);

						SLD_BO.setPropertyValue("ENDPOINT_REST_APPL", null);
						SLD_BO.setPropertyValue("ENDPOINT_REST_SYST", null);
						SLD_BO.setPropertyValue("ENDPOINT_REST_PROD", null);
						SLD_BO.setPropertyValue("ENDPOINT_REST_UAT", null);
						SLD_BO.setPropertyValue("ENDPOINT_REST_INDEP", null);

						SLD_BO.setPropertyValue("ENDPOINT_ZRES_APPL", null);
						SLD_BO.setPropertyValue("ENDPOINT_ZRES_SYST", null);
						SLD_BO.setPropertyValue("ENDPOINT_ZRES_PROD", null);
						SLD_BO.setPropertyValue("ENDPOINT_ZRES_UAT", null);
						SLD_BO.setPropertyValue("ENDPOINT_ZRES_INDEP", null);

						SLD_BO.setPropertyValue("ENDPOINT_WOLA_APPL", null);
						SLD_BO.setPropertyValue("ENDPOINT_WOLA_SYST", null);
						SLD_BO.setPropertyValue("ENDPOINT_WOLA_PROD", null);
						SLD_BO.setPropertyValue("ENDPOINT_WOLA_UAT", null);
						SLD_BO.setPropertyValue("ENDPOINT_WOLA_INDEP", null);

						SLD_BO.setPropertyValue("ENDPOINT_CICS_APPL", null);
						SLD_BO.setPropertyValue("ENDPOINT_CICS_SYST", null);
						SLD_BO.setPropertyValue("ENDPOINT_CICS_PROD", null);
						SLD_BO.setPropertyValue("ENDPOINT_CICS_UAT", null);
						SLD_BO.setPropertyValue("ENDPOINT_CICS_INDEP", null);

						SLD_BO.setPropertyValue("ENDPOINT_MQ_APPL", null);
						SLD_BO.setPropertyValue("ENDPOINT_MQ_SYST", null);
						SLD_BO.setPropertyValue("ENDPOINT_MQ_PROD", null);
						SLD_BO.setPropertyValue("ENDPOINT_MQ_UAT", null);
						SLD_BO.setPropertyValue("ENDPOINT_MQ_INDEP", null);

						SLD_BO.setPropertyValue("ENDPOINT_CALLABLE_APPL", null);
						SLD_BO.setPropertyValue("ENDPOINT_CALLABLE_SYST", null);
						SLD_BO.setPropertyValue("ENDPOINT_CALLABLE_PROD", null);
						SLD_BO.setPropertyValue("ENDPOINT_CALLABLE_UAT", null);
						SLD_BO.setPropertyValue("ENDPOINT_CALLABLE_INDEP", null);

						// Ricavo Endpoint

						query6 = query4bis;
						query6 = query6.replaceAll("%BSRURI%", bsrURISLD);
						result = wsrrutility.generalWSRRQuery(query6, url, user, password, debug);


						// navigo gli endpoints

						xpathFactory = XPathFactory.newInstance();
						xpath = xpathFactory.newXPath();
						// result=result.replaceAll("(\\r|\\n)", "");
						source = new InputSource(new StringReader(result));
						doc = null;
						count = 0;
						countag = 0;
						current = null;
						String value = null;
						String current_ = null;
						String value_ = null;


						try {

							doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

							countag = Integer.parseInt(xpath.evaluate("count(/resources/resource/properties)", doc));

							if (countag != 0) {

								for (int ii = 1; ii <= countag; ii++) {

									// recupero l'ambiente operativo e il tipo
									// dell'endpoint

									count = Integer.parseInt(xpath.evaluate("count(/resources/resource["
											+ String.valueOf(ii) + "]/classifications/classification)", doc));

									type = null;
									environment = null;

									EP_BO = (TWObject) TWObjectFactory.createObject();
									PROXY_BO = (TWObject) TWObjectFactory.createObject();

									for (int i = 1; i <= count; i++) {

										current = "/resources/resource[" + String.valueOf(ii)
										+ "]/classifications/classification[" + String.valueOf(i) + "]/@uri";
										classification = xpath.evaluate(current, doc);

										if (environment == null && classification != null && classification.indexOf(
												"http://www.ibm.com/xmlns/prod/serviceregistry/6/1/GovernanceProfileTaxonomy#") != -1) {
											environment = classification.substring(classification.indexOf("#") + 1,
													classification.length());
										}

										if (type == null && classification != null
												&& classification.indexOf(EP_MQ) != -1) {
											type = "MQ_EP";
											target = MQ_EP;
										}

										if (type == null && classification != null
												&& classification.indexOf(EP_SOAP) != -1) {
											type = "SOAP_EP";
											target = SOAP_EP;
										}

										if (type == null && classification != null
												&& classification.indexOf(EP_REST) != -1) {
											type = "REST_EP";
											target = REST_EP;
										}

										if (type == null && classification != null
												&& classification.indexOf(EP_ZRES) != -1) {
											type = "ZRES_EP";
											target = ZRES_EP;
										}

										if (type == null && classification != null
												&& classification.indexOf(EP_WOLA) != -1) {
											type = "WOLA_EP";
											target = WOLA_EP;
										}

										if (type == null && classification != null
												&& classification.indexOf(EP_CALLABLE) != -1) {
											type = "CALLABLE_EP";
											target = CALLABLE_EP;
										}

										if (type == null && classification != null
												&& classification.indexOf(EP_CICS) != -1) {
											type = "CICS_EP";
											target = CICS_EP;
										}

										current = null;
									}

									//NBP_BO.setPropertyValue("ENDPOINT_INDEX", type);
									WSRRToBusinessObject.log(
											"Trovato endpoint di tipo : " + type + " elemento corrente " + ii, debug);

									// recupero eventuale Proxy/MQ manual
									count = Integer.parseInt(xpath.evaluate("count(/resources/resource["
											+ String.valueOf(ii) + "]/relationships/relationship)", doc));

									relation = null;
									proxybsrURI = null;
									proxy = null;

									for (int i = 1; i <= count; i++) {

										current = "/resources/resource[" + String.valueOf(ii)
										+ "]/relationships/relationship[" + String.valueOf(i) + "]/@name";

										relation = xpath.evaluate(current, doc);

										if (proxy == null && relation != null
												&& relation.indexOf("rest80_CALLABLEProxy") != -1) {

											current = "/resources/resource[" + String.valueOf(ii)
											+ "]/relationships/relationship[" + String.valueOf(i)
											+ "]/@targetBsrURI";
											if (xpath.evaluate(current, doc) != null) {
												proxybsrURI = xpath.evaluate(current, doc);
												proxy = "CALLABLE";
												query6 = query5;
												query6 = query6.replaceAll("%BSRURI%", proxybsrURI);
										
												result = wsrrutility.generalWSRRQuery(query6, url, user, password,
														debug);
												PROXY_BO = WSRRToBusinessObject.makeBO(result, -1, proxy, null, url,
														user, password, debug);
												WSRRToBusinessObject.log(
														"Creato PROXY_BO per tipo CALLABLE (rest80_CALLABLEProxy)",
														debug);
											}

										}

										if (proxy == null && relation != null
												&& relation.indexOf("sm63_SOAPProxy") != -1) {

											current = "/resources/resource[" + String.valueOf(ii)
											+ "]/relationships/relationship[" + String.valueOf(i)
											+ "]/@targetBsrURI";
											if (xpath.evaluate(current, doc) != null) {
												proxybsrURI = xpath.evaluate(current, doc);
												proxy = "SOAP";
												query6 = query5;
												query6 = query6.replaceAll("%BSRURI%", proxybsrURI);
												result = wsrrutility.generalWSRRQuery(query6, url, user, password,
														debug);
										
												PROXY_BO = WSRRToBusinessObject.makeBO(result, -1, proxy, null, url,
														user, password, debug);
												WSRRToBusinessObject
												.log("Creato PROXY_BO per tipo SOAP (sm63_SOAPProxy)", debug);
											}
										}

										if (proxy == null && relation != null
												&& relation.indexOf("rest80_RESTProxy") != -1) {

											current = "/resources/resource[" + String.valueOf(ii)
											+ "]/relationships/relationship[" + String.valueOf(i)
											+ "]/@targetBsrURI";
											if (xpath.evaluate(current, doc) != null) {
												proxybsrURI = xpath.evaluate(current, doc);
												proxy = "REST";
												query6 = query5;
												query6 = query6.replaceAll("%BSRURI%", proxybsrURI);
											
												result = wsrrutility.generalWSRRQuery(query6, url, user, password,
														debug);
												PROXY_BO = WSRRToBusinessObject.makeBO(result, -1, proxy, null, url,
														user, password, debug);
												WSRRToBusinessObject
												.log("Creato PROXY_BO per tipo REST (rest80_RESTProxy)", debug);
											}
										}

										if (proxy == null && relation != null
												&& relation.indexOf("sm63_mqEndpoint") != -1) {

											current = "/resources/resource[" + String.valueOf(ii)
											+ "]/relationships/relationship[" + String.valueOf(i)
											+ "]/@targetBsrURI";
											if (xpath.evaluate(current, doc) != null) {
												proxybsrURI = xpath.evaluate(current, doc);
												proxy = "MQMANUAL";
												query6 = query5;
												query6 = query6.replaceAll("%BSRURI%", proxybsrURI);
											
												result = wsrrutility.generalWSRRQuery(query6, url, user, password,
														debug);
												PROXY_BO = WSRRToBusinessObject.makeBO(result, -1, proxy, null, url,
														user, password, debug);
												WSRRToBusinessObject
												.log("Creato PROXY_BO per tipo MQ (sm63_mqEndpoint)", debug);
											}
										}

										current = null;
									}

									// navigo le prorieta' del singolo endpoint

									count = Integer.parseInt(xpath.evaluate("count(/resources/resource["
											+ String.valueOf(ii) + "]/properties/property)", doc));

									for (int i = 1; i <= count; i++) {

										current = "/resources/resource[" + String.valueOf(ii) + "]/properties/property["
												+ String.valueOf(i) + "]/@name";
										value = "/resources/resource[" + String.valueOf(ii) + "]/properties/property["
												+ String.valueOf(i) + "]/@value";

										current_ = (String) xpath.evaluate(current, doc);
										value_ = (String) xpath.evaluate(value, doc);

										if (target.indexOf(current_) != -1) {
											// if (true) {

											if (current_.equals("namespace"))
												current_ = "nspace";

											if (value_ == null)
												value_ = "";

											if (current_ != null && current_.equals("primaryType")) {

												if (type == null && value_ != null && value_.indexOf("#") != -1) {
													type = value_.substring(value_.indexOf("#") + 1, value_.length());
												}

											}

											EP_BO.setPropertyValue(current_, value_);

											current = null;
											current_ = null;
											value = null;
											value_ = null;

										} else {

											WSRRToBusinessObject.log("Proprieta' : " + current_
													+ " non presente nel template : " + target, debug);
										}

									}

									// Setto Ambiente dell'endpoint


									EP_BO.setPropertyValue("environment", environment);

									if (proxybsrURI != null && proxybsrURI.length() != 0) {

										EP_BO.setPropertyValue("PROXY", PROXY_BO); // collego
										// proxy/MQManual
										// a
										// endpoint
									}

									if (type.equals("SOAP_EP")) {

										switch (environment) {

										case "Application":
											EP_BO_SOAP_APPL.addArrayData(EP_BO_SOAP_APPL.getArraySize(), EP_BO);
											break;

										case "SystemTest":
											EP_BO_SOAP_SYST.addArrayData(EP_BO_SOAP_SYST.getArraySize(), EP_BO);
											break;

										case "Produzione":
											EP_BO_SOAP_PROD.addArrayData(EP_BO_SOAP_PROD.getArraySize(), EP_BO);
											break;

										case "UserAcceptanceTest":
											EP_BO_SOAP_UAT.addArrayData(EP_BO_SOAP_UAT.getArraySize(), EP_BO);
											break;

										case "IndipendentTest":
											EP_BO_SOAP_INDEP.addArrayData(EP_BO_SOAP_INDEP.getArraySize(), EP_BO);
											break;

										}
									}

									if (type.equals("REST_EP")) {

										switch (environment) {

										case "Application":

											EP_BO_REST_APPL.addArrayData(EP_BO_REST_APPL.getArraySize(), EP_BO);
											break;

										case "SystemTest":

											EP_BO_REST_SYST.addArrayData(EP_BO_REST_SYST.getArraySize(), EP_BO);
											break;

										case "Produzione":

											EP_BO_REST_PROD.addArrayData(EP_BO_REST_PROD.getArraySize(), EP_BO);
											break;

										case "UserAcceptanceTest":

											EP_BO_REST_UAT.addArrayData(EP_BO_REST_UAT.getArraySize(), EP_BO);
											break;

										case "IndipendentTest":

											EP_BO_REST_INDEP.addArrayData(EP_BO_REST_INDEP.getArraySize(), EP_BO);
											break;

										}
									}

									if (type.equals("CALLABLE_EP")) {

										switch (environment) {

										case "Application":

											EP_BO_CALLABLE_APPL.addArrayData(EP_BO_CALLABLE_APPL.getArraySize(), EP_BO);
											break;

										case "SystemTest":

											EP_BO_CALLABLE_SYST.addArrayData(EP_BO_CALLABLE_SYST.getArraySize(), EP_BO);
											break;

										case "Produzione":

											EP_BO_CALLABLE_PROD.addArrayData(EP_BO_CALLABLE_PROD.getArraySize(), EP_BO);
											break;

										case "UserAcceptanceTest":

											EP_BO_CALLABLE_UAT.addArrayData(EP_BO_CALLABLE_UAT.getArraySize(), EP_BO);
											break;

										case "IndipendentTest":

											EP_BO_CALLABLE_INDEP.addArrayData(EP_BO_CALLABLE_INDEP.getArraySize(),
													EP_BO);
											break;

										}
									}

									if (type.equals("WOLA_EP")) {

										switch (environment) {

										case "Application":

											EP_BO_WOLA_APPL.addArrayData(EP_BO_WOLA_APPL.getArraySize(), EP_BO);
											break;

										case "SystemTest":

											EP_BO_WOLA_SYST.addArrayData(EP_BO_WOLA_SYST.getArraySize(), EP_BO);
											break;

										case "Produzione":

											EP_BO_WOLA_PROD.addArrayData(EP_BO_WOLA_PROD.getArraySize(), EP_BO);
											break;

										case "UserAcceptanceTest":

											EP_BO_WOLA_UAT.addArrayData(EP_BO_WOLA_UAT.getArraySize(), EP_BO);
											break;

										case "IndipendentTest":

											EP_BO_WOLA_INDEP.addArrayData(EP_BO_WOLA_INDEP.getArraySize(), EP_BO);
											break;

										}
									}

									if (type.equals("ZRES_EP")) {

										switch (environment) {

										case "Application":

											EP_BO_ZRES_APPL.addArrayData(EP_BO_ZRES_APPL.getArraySize(), EP_BO);
											break;

										case "SystemTest":

											EP_BO_ZRES_SYST.addArrayData(EP_BO_ZRES_SYST.getArraySize(), EP_BO);
											break;

										case "Produzione":

											EP_BO_ZRES_PROD.addArrayData(EP_BO_ZRES_PROD.getArraySize(), EP_BO);
											break;

										case "UserAcceptanceTest":

											EP_BO_ZRES_UAT.addArrayData(EP_BO_ZRES_UAT.getArraySize(), EP_BO);
											break;

										case "IndipendentTest":

											EP_BO_ZRES_INDEP.addArrayData(EP_BO_ZRES_INDEP.getArraySize(), EP_BO);
											break;

										}
									}

									if (type.equals("CICS_EP")) {

										switch (environment) {

										case "Application":

											EP_BO_CICS_APPL.addArrayData(EP_BO_CICS_APPL.getArraySize(), EP_BO);
											break;

										case "SystemTest":

											EP_BO_CICS_SYST.addArrayData(EP_BO_CICS_SYST.getArraySize(), EP_BO);
											break;

										case "Produzione":

											EP_BO_CICS_PROD.addArrayData(EP_BO_CICS_PROD.getArraySize(), EP_BO);
											break;

										case "UserAcceptanceTest":

											EP_BO_CICS_UAT.addArrayData(EP_BO_CICS_UAT.getArraySize(), EP_BO);
											break;

										case "IndipendentTest":

											EP_BO_CICS_INDEP.addArrayData(EP_BO_CICS_INDEP.getArraySize(), EP_BO);
											break;

										}

									}

									if (type.equals("MQ_EP")) {

										switch (environment) {

										case "Application":

											EP_BO_MQ_APPL.addArrayData(EP_BO_MQ_APPL.getArraySize(), EP_BO);
											break;

										case "SystemTest":

											EP_BO_MQ_SYST.addArrayData(EP_BO_MQ_SYST.getArraySize(), EP_BO);
											break;

										case "Produzione":

											EP_BO_MQ_PROD.addArrayData(EP_BO_MQ_PROD.getArraySize(), EP_BO);
											break;

										case "UserAcceptanceTest":

											EP_BO_MQ_UAT.addArrayData(EP_BO_MQ_UAT.getArraySize(), EP_BO);
											break;

										case "IndipendentTest":

											EP_BO_MQ_INDEP.addArrayData(EP_BO_MQ_INDEP.getArraySize(), EP_BO);
											break;

										}

									}

									SLD_BO.setPropertyValue("ENDPOINT_INDEX",type);

								}


								SLD_BO.setPropertyValue("ENDPOINT_SOAP_APPL", EP_BO_SOAP_APPL);
								SLD_BO.setPropertyValue("ENDPOINT_SOAP_SYST", EP_BO_SOAP_SYST);
								SLD_BO.setPropertyValue("ENDPOINT_SOAP_PROD", EP_BO_SOAP_PROD);
								SLD_BO.setPropertyValue("ENDPOINT_SOAP_UAT", EP_BO_SOAP_UAT);
								SLD_BO.setPropertyValue("ENDPOINT_SOAP_INDEP", EP_BO_SOAP_INDEP);

								SLD_BO.setPropertyValue("ENDPOINT_REST_APPL", EP_BO_REST_APPL);
								SLD_BO.setPropertyValue("ENDPOINT_REST_SYST", EP_BO_REST_SYST);
								SLD_BO.setPropertyValue("ENDPOINT_REST_PROD", EP_BO_REST_PROD);
								SLD_BO.setPropertyValue("ENDPOINT_REST_UAT", EP_BO_REST_UAT);
								SLD_BO.setPropertyValue("ENDPOINT_REST_INDEP", EP_BO_REST_INDEP);

								SLD_BO.setPropertyValue("ENDPOINT_CALLABLE_APPL", EP_BO_CALLABLE_APPL);
								SLD_BO.setPropertyValue("ENDPOINT_CALLABLE_SYST", EP_BO_CALLABLE_SYST);
								SLD_BO.setPropertyValue("ENDPOINT_CALLABLE_PROD", EP_BO_CALLABLE_PROD);
								SLD_BO.setPropertyValue("ENDPOINT_CALLABLE_UAT", EP_BO_CALLABLE_UAT);
								SLD_BO.setPropertyValue("ENDPOINT_CALLABLE_INDEP", EP_BO_CALLABLE_INDEP);

								SLD_BO.setPropertyValue("ENDPOINT_ZRES_APPL", EP_BO_ZRES_APPL);
								SLD_BO.setPropertyValue("ENDPOINT_ZRES_SYST", EP_BO_ZRES_SYST);
								SLD_BO.setPropertyValue("ENDPOINT_ZRES_PROD", EP_BO_ZRES_PROD);
								SLD_BO.setPropertyValue("ENDPOINT_ZRES_UAT", EP_BO_ZRES_UAT);
								SLD_BO.setPropertyValue("ENDPOINT_ZRES_INDEP", EP_BO_ZRES_INDEP);

								SLD_BO.setPropertyValue("ENDPOINT_WOLA_APPL", EP_BO_WOLA_APPL);
								SLD_BO.setPropertyValue("ENDPOINT_WOLA_SYST", EP_BO_WOLA_SYST);
								SLD_BO.setPropertyValue("ENDPOINT_WOLA_PROD", EP_BO_WOLA_PROD);
								SLD_BO.setPropertyValue("ENDPOINT_WOLA_UAT", EP_BO_WOLA_UAT);
								SLD_BO.setPropertyValue("ENDPOINT_WOLA_INDEP", EP_BO_WOLA_INDEP);

								SLD_BO.setPropertyValue("ENDPOINT_CICS_APPL", EP_BO_CICS_APPL);
								SLD_BO.setPropertyValue("ENDPOINT_CICS_SYST", EP_BO_CICS_SYST);
								SLD_BO.setPropertyValue("ENDPOINT_CICS_PROD", EP_BO_CICS_PROD);
								SLD_BO.setPropertyValue("ENDPOINT_CICS_UAT", EP_BO_CICS_UAT);
								SLD_BO.setPropertyValue("ENDPOINT_CICS_INDEP", EP_BO_CICS_INDEP);

								SLD_BO.setPropertyValue("ENDPOINT_MQ_APPL", EP_BO_MQ_APPL);
								SLD_BO.setPropertyValue("ENDPOINT_MQ_SYST", EP_BO_MQ_SYST);
								SLD_BO.setPropertyValue("ENDPOINT_MQ_PROD", EP_BO_MQ_PROD);
								SLD_BO.setPropertyValue("ENDPOINT_MQ_UAT", EP_BO_MQ_UAT);
								SLD_BO.setPropertyValue("ENDPOINT_MQ_INDEP", EP_BO_MQ_INDEP);


							} // endpoint

						} catch (Exception e) {

							System.out.println(
									"*********************************************KO************************************+");
							System.out.println(e.getMessage());
							System.out.println(
									"*********************************************KO************************************+");
							e.printStackTrace();
							NBP_BO = null;
						}
						
					    SLD_BO_LIST.addArrayData(SLD_BO_LIST.getArraySize(), SLD_BO);
						
					}//for

				}//count tag SLF
			} //base
		} catch (Exception e) {

			System.out.println("*********************************************KO************************************+");
			System.out.println(e.getMessage());
			System.out.println("*********************************************KO************************************+");
			e.printStackTrace();
			NBP_BO = null;
		}

		//reverse INTERFACE_INDEX
		//String interfaceTypes=(String)NBP_BO.getPropertyValue("INTERFACE_INDEX");
		//interfaceTypes=new StringBuilder(interfaceTypes).reverse().toString();
		//NBP_BO.setPropertyValue("INTERFACE_INDEX", interfaceTypes);
		
		// attacco SLD a NBP_BO
		NBP_BO.setPropertyValue("SLD", SLD_BO_LIST);
		
		WSRRToBusinessObject.log("Risultato NBP_BO",debug);
		WSRRToBusinessObject.log(NBP_BO.toXMLString(),debug);
		WSRRToBusinessObject.log("Fine Estrattore",true);
		return NBP_BO;
	}

	public static TWObject makeBO(String data, int resIndex, String type, String subType, String url, String user,
			String password, boolean debug) throws XPathExpressionException {

		final String SCHOST = "[type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_DESC_ESTESA, gep63_ABILITAZ_INFRASTR, ale63_assetType, gep63_SCHOST_NOME_CPY_OUT, ale63_guid, gep63_SCHOST_NOME_CPY_INP, ale63_remoteState, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_PIATT_EROG, gep63_FLG_CTRL_TIPOLOGIA, gep63_DISP_SERV, gep63_MATR_RICH_MODIFICA, gep63_PID_PROCESSO_GOV, gep63_TIPOLOGIA, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_SCHOST_PGM_MD_X_MPE, gep63_VINCOLI_RIUSO, gep63_SCHOST_CONVNULL, gep63_INFO_COSTO, gep63_ATTIVATO_IN_PROD, gep63_consumerIdentifier, gep63_SCHOST_PGM_SERVIZIO, gep63_UTILIZ_PIU_BAN_CLONI, gep63_SCHOST_ID_SERVIZIO, ale63_ownerEmail, gep63_ATTIVATO_IN_SYST, ale63_communityName, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SCHOST_PGM_MD_X_INTEROPER, ale63_fullDescription, gep63_SCHOST_PGM_MD, gep63_SCHOST_TRANS_SERVIZIO, gep63_ATTIVATO_IN_APPL, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_SCHOST_COD_VERSIONE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		final String SHOST = " [type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_DESC_ESTESA, gep63_ABILITAZ_INFRASTR, ale63_assetType, gep63_SHOST_TRANS_SERVIZIO, ale63_guid, gep63_SHOST_CONVNULL, gep63_SHOST_PGM_SERVIZIO, ale63_remoteState, gep63_SHOST_NOME_CPY_INP, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_PIATT_EROG, gep63_FLG_CTRL_TIPOLOGIA, gep63_DISP_SERV, gep63_MATR_RICH_MODIFICA, gep63_PID_PROCESSO_GOV, gep63_TIPOLOGIA, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_SHOST_PGM_MD_X_INTEROPER, gep63_SHOST_NOME_CPY_OUT, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_SHOST_PGM_MD_X_MPE, gep63_VINCOLI_RIUSO, gep63_INFO_COSTO, gep63_ATTIVATO_IN_PROD, gep63_consumerIdentifier, gep63_UTILIZ_PIU_BAN_CLONI, ale63_ownerEmail, gep63_ATTIVATO_IN_SYST, ale63_communityName, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SHOST_ID_SERVIZIO, gep63_SHOST_PGM_MD, ale63_fullDescription, gep63_ATTIVATO_IN_APPL, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		final String SCOPEN = "[type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_DESC_ESTESA, gep63_ABILITAZ_INFRASTR, gep63_SCOPEN_AMBITO_IMPLEMENTAZIONE, ale63_assetType, ale63_guid, gep63_SCOPEN_DIM_MAX_MSG, ale63_remoteState, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_FLG_CTRL_TIPOLOGIA, gep63_PIATT_EROG, gep63_MATR_RICH_MODIFICA, gep63_DISP_SERV, gep63_PID_PROCESSO_GOV, gep63_TIPOLOGIA, gep63_SCOPEN_STATO_ATTUALE_FUNZ, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_SCOPEN_DOWNTIME_PIANIFICATO, gep63_SCOPEN_DIM_MIN_MSG, gep63_VINCOLI_RIUSO, gep63_ATTIVATO_IN_PROD, gep63_INFO_COSTO, gep63_consumerIdentifier, gep63_UTILIZ_PIU_BAN_CLONI, gep63_SCOPEN_ATTACHMENT_TYPE, ale63_ownerEmail, gep63_SCOPEN_LINK_SIN_APPS_EST, gep63_ATTIVATO_IN_SYST, gep63_SCOPEN_REPS0, ale63_communityName, gep63_SCOPEN_NUM_CHIAMATE_PICCO, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SCOPEN_EAR_SERVIZIO, gep63_SCOPEN_FLG_CONTIENE_ATTACHMENT, gep63_SCOPEN_AMBIENTE_FISICO, gep63_SCOPEN_RIF_CHIAMANTI_INT, ale63_fullDescription, gep63_SCOPEN_VOLUME_GIORN, gep63_ATTIVATO_IN_APPL, gep63_SCOPEN_RIF_CHIAMANTI_EST, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		final String SOPEN = " [type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_ABILITAZ_INFRASTR, gep63_DESC_ESTESA, gep63_SOPEN_LINK_SIN_APPS_EST, gep63_SOPEN_ATTACHMENT_TYPE, gep63_SOPEN_DOWNTIME_PIANIFICATO, gep63_SOPEN_REPS0, ale63_assetType, gep63_SOPEN_NUM_CHIAMATE_PICCO, ale63_guid, gep63_SOPEN_RIF_CHIAMANTI_EST, gep63_SOPEN_STATO_ATTUALE_FUNZ, ale63_remoteState, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_FLG_CTRL_TIPOLOGIA, gep63_PIATT_EROG, gep63_MATR_RICH_MODIFICA, gep63_DISP_SERV, gep63_PID_PROCESSO_GOV, gep63_SOPEN_RIF_CHIAMANTI_INT, gep63_TIPOLOGIA, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_SOPEN_EAR_SERVIZIO, gep63_SOPEN_AMBIENTE_FISICO, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_VINCOLI_RIUSO, gep63_ATTIVATO_IN_PROD, gep63_INFO_COSTO, gep63_consumerIdentifier, gep63_SOPEN_VOLUME_GIORN, gep63_SOPEN_AMBITO_IMPLEMENTAZIONE, gep63_UTILIZ_PIU_BAN_CLONI, ale63_ownerEmail, gep63_ATTIVATO_IN_SYST, gep63_SOPEN_DIM_MAX_MSG, ale63_communityName, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SOPEN_DIM_MIN_MSG, gep63_SOPEN_FLG_CONTIENE_ATTACHMENT, ale63_fullDescription, gep63_ATTIVATO_IN_APPL, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		final String SERVICEVERSION = "[type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, ale63_remoteState, ale63_communityName, ale63_assetOwners, ale63_assetType, ale63_guid, ale63_fullDescription, ale63_ownerEmail, ale63_requirementsLink, ale63_assetWebLink]";
		final String ACRONIMO = "[bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, ale63_RESP_FUNZIONALE_MATRICOLA, ale63_RESP_ATTIVITA_NOMINATIVO, ale63_RESP_FUNZIONALE_NOMINATIVO, ale63_DESC_AMBITO, ale63_RESP_SERVIZIO_NOMINATIVO, ale63_contact, ale63_contactEmail, ale63_CODICE_SISTEMA_APPLICATIVO, ale63_RESP_ATTIVITA_MATRICOLA, ale63_RESP_SERVIZIO_MATRICOLA, ale63_RESP_UFFICIO_MATRICOLA, ale63_RESP_TECNICO_MATRICOLA, ale63_RESP_UFFICIO_NOMINATIVO, ale63_RESP_TECNICO_NOMINATIVO, ale63_AMBITO]";

		final String INTERF = "[type, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_interfaceVersion,sm63_interfaceNamespace, sm63_interfaceName, rest80_webLink]";
		final String SLD = "[type, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_consumerIdentifierLocationInfo, gep63_contextIdentifierLocationInfo]";
		final String SOAP_PROXY = "[type, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_DATAPOWER_DEDICATO, sm63_FLG_USO_DATAPOWER_DEDICATO, sm63_ESPOSIZIONE, sm63_ESPOSTO_INTRANET, sm63_ERRORE_GENERAZIONE_WSPROXY, sm63_NOTE, sm63_FLG_ESP_CONTROPARTE_ESTERNA, sm63_FLG_ESPOSTO_SOCIETA_GRUPPO, sm63_NOTE_GEN_WSPROXY, sm63_SOCIETA_CHE_ESPONE_SERVIZIO, sm63_FLG_RICHIAMABILE_DA_CICS, sm63_FLG_CONTROPARTE_DSI, sm63_FLG_CONTROPARTE_INTERNET, sm63_Timeout]";
		final String REST_PROXY = "[type, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_DATAPOWER_DEDICATO, sm63_FLG_USO_DATAPOWER_DEDICATO, sm63_ESPOSIZIONE, sm63_ESPOSTO_INTRANET, sm63_ERRORE_GENERAZIONE_WSPROXY, sm63_NOTE, sm63_FLG_ESP_CONTROPARTE_ESTERNA, sm63_FLG_ESPOSTO_SOCIETA_GRUPPO, sm63_NOTE_GEN_WSPROXY, sm63_SOCIETA_CHE_ESPONE_SERVIZIO, sm63_FLG_RICHIAMABILE_DA_CICS, sm63_FLG_CONTROPARTE_DSI, sm63_FLG_CONTROPARTE_INTERNET, sm63_Timeout]";
		final String CALLABLE_PROXY = "[type, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_DATAPOWER_DEDICATO, sm63_FLG_USO_DATAPOWER_DEDICATO, sm63_ESPOSIZIONE, sm63_ESPOSTO_INTRANET, sm63_ERRORE_GENERAZIONE_WSPROXY, sm63_NOTE, sm63_FLG_ESP_CONTROPARTE_ESTERNA, sm63_FLG_ESPOSTO_SOCIETA_GRUPPO, sm63_NOTE_GEN_WSPROXY, sm63_SOCIETA_CHE_ESPONE_SERVIZIO, sm63_FLG_RICHIAMABILE_DA_CICS, sm63_FLG_CONTROPARTE_DSI, sm63_FLG_CONTROPARTE_INTERNET, sm63_Timeout]";
		final String MQ_MANUAL = "[type, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_serviceVersion, sm63_DATA_ULTIMO_UTILIZZO_MQM, sm63_PRODOTTO, sm63_DIR_ID_TGT_DES, sm63_serviceName, sm63_serviceNamespace, sm63_responseQMgr, sm63_portName, sm63_requestQMgr, sm63_MOD_COLLOQUIO_MQM, sm63_PROC_DEST, sm63_ID_MACCHINA, sm63_requestQName, sm63_DATA_PRIMO_UTILIZZO_MQM, sm63_PGM_AREE, sm63_responseQName]";
		String target = "UNDEF";

		TWObject CURRENT_BO = null;

		if (data == null)
			return null;

		WSRRToBusinessObject.log("Chiamata a makeBO per tipo : " + type, debug);
		
		//WSRRToBusinessObject.log("target : " + target, debug);

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		InputSource source = new InputSource(new StringReader(data));
		Document doc = null;
		int count = 0;
		String current = null;
		String value = null;
		String current_ = null;
		String value_ = null;
		String path = null;
		String countString = null;

		try {
			CURRENT_BO = (TWObject) TWObjectFactory.createObject();

			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

			if (resIndex == -1)
				path = "/resources/resource/properties";
			else
				path = "/resources/resource[" + resIndex + "]/properties";

			// count(/resources/resource/properties/property)
			countString = "count(" + path + "/property)";
			count = Integer.parseInt(xpath.evaluate(countString, doc));

			//WSRRToBusinessObject.log("Contati : " + count, debug);

			if (count == 0)
				return null;

			if (type != null && type.indexOf("SOPENServiceVersion") != -1 && target.equals("UNDEF"))
				target = SOPEN;
			if (type != null && type.indexOf("SCOPENServiceVersion") != -1 && target.equals("UNDEF"))
				target = SCOPEN;
			if (type != null && type.indexOf("SHOSTServiceVersion") != -1 && target.equals("UNDEF"))
				target = SHOST;
			if (type != null && type.indexOf("SCHOSTServiceVersion") != -1 && target.equals("UNDEF"))
				target = SCHOST;

			if (type != null && type.indexOf("SOPENService") != -1 && target.equals("UNDEF"))
				target = SERVICEVERSION;
			if (type != null && type.indexOf("SCOPENService") != -1 && target.equals("UNDEF"))
				target = SERVICEVERSION;
			if (type != null && type.indexOf("SHOSTService") != -1 && target.equals("UNDEF"))
				target = SERVICEVERSION;
			if (type != null && type.indexOf("SCHOSTService") != -1 && target.equals("UNDEF"))
				target = SERVICEVERSION;

			if (type != null && type.indexOf("Organization") != -1 && target.equals("UNDEF"))
				target = ACRONIMO;

			if (type != null && type.indexOf("Interface") != -1 && target.equals("UNDEF")) {
				target = INTERF;
				//WSRRToBusinessObject.log("***Sono in Interace***", debug);
			}
			if (type != null && type.indexOf("SLD") != -1 && target.equals("UNDEF"))
				target = SLD;

			if (type != null && type.indexOf("SOAP") != -1 && target.equals("UNDEF"))
				target = SOAP_PROXY;

			if (type != null && type.indexOf("REST") != -1 && target.equals("UNDEF"))
				target = REST_PROXY;

			if (type != null && type.indexOf("CALLABLE") != -1 && target.equals("UNDEF"))
				target = CALLABLE_PROXY;

			if (type != null && type.indexOf("MQMANUAL") != -1 && target.equals("UNDEF"))
				target = MQ_MANUAL;

			if (type != null)
				CURRENT_BO.setPropertyValue("type", type);

			if (subType != null)
				CURRENT_BO.setPropertyValue("subType", subType);

			for (int i = 1; i <= count; i++) {

				// current = "/resources/resource/properties/property[" +
				// String.valueOf(i) + "]/@name";
				// value = "/resources/resource/properties/property[" +
				// String.valueOf(i) + "]/@value";

				current = path + "/property[" + String.valueOf(i) + "]/@name";
				value = path + "/property[" + String.valueOf(i) + "]/@value";

				current_ = (String) xpath.evaluate(current, doc);
				value_ = (String) xpath.evaluate(value, doc);

				if (target.indexOf(current_) != -1) {

					if (current_.equals("namespace"))
						current_ = "nspace";

					if (value_ == null)
						value_ = "";

					CURRENT_BO.setPropertyValue(current_, value_);

					current = null;
					current_ = null;
					value = null;
					value_ = null;

				} else {

					WSRRToBusinessObject.log("Proprieta' : " + current_ + " non presente nel template : " + target,
							debug);
				}
			}
			WSRRToBusinessObject.log("makeBO eseguito per tipo : " + type, debug);
			//WSRRToBusinessObject.log("target : " + target, debug);

		} catch (Exception e) {

			System.out.println("*********************************************KO************************************+");
			System.out.println(e.getMessage());
			System.out.println("*********************************************KO************************************+");
			e.printStackTrace();
			CURRENT_BO = null;
		}

		return CURRENT_BO;
	}

	private static void log(String message, boolean logging) {

		if (logging) {
			System.out.println(
					">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println(message);
			System.out.println(
					"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		}
	}
}
