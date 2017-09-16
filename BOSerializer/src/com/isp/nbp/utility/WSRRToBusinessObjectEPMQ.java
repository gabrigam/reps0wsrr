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
//test
public class WSRRToBusinessObjectEPMQ {

	//String query1 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']";
	//String query2 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@primaryType='http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23%TYPE%']";
	//String query3 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/ale63_owningOrganization(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')]";
	//String query4 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)/gep63_availableEndpoints(.)[@primaryType='http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel%23MQServiceEndpoint']";
	String query5 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']";
	String query6 = "dynamic";
	//String query7 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)/gep63_serviceInterface(.)"; // interfaccia
	//String query8 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)";

	public WSRRToBusinessObjectEPMQ() {

	}

	public String WSRRBOSerializer(boolean usoSpecializzazione, String specializzazione, String data, String url,
			String user, String password) throws XPathExpressionException {

		TWObject NBP_BO = null;

		TWObject EP_BO = null;
		TWObject PROXY_BO = null;

		TWList EP_BO_MQ = null;

		String result = null;
		WSRRUtility wsrrutility = new WSRRUtility();

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		InputSource source = new InputSource(new StringReader(data));
		Document doc = null;
		int count = 0;
		String current = null;
		String classification = null;
		String relation = null;
		String type = null;
		String environment = null;
		String proxy = null;
		String proxybsrURI = null;
		int specializzazioniContatore = 0;
		boolean specializzazioneTrovata = false;
		boolean scrittoRecord = false;

		try {
			NBP_BO = (TWObject) TWObjectFactory.createObject();
			EP_BO_MQ = TWObjectFactory.createList();
			// navigo gli endpoints

			xpathFactory = XPathFactory.newInstance();
			xpath = xpathFactory.newXPath();
			source = new InputSource(new StringReader(data));
			doc = null;
			count = 0;
			int countag = 0;
			current = null;
			String value = null;
			String current_ = null;
			String value_ = null;

			System.out.println("DATA" +data);
			
			try {

				NBP_BO.setPropertyValue("ENDPOINT_INCONGRUENTE", false);

				doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

				countag = Integer.parseInt(xpath.evaluate("count(/resources/resource/properties)", doc));

				if (countag != 0) {

					for (int ii = 1; ii <= countag; ii++) {

						// recupero l'ambiente operativo e il tipo dell'endpoint

						count = Integer.parseInt(xpath.evaluate(
								"count(/resources/resource[" + String.valueOf(ii) + "]/classifications/classification)",
								doc));

						type = null;
						environment = null;

						EP_BO = (TWObject) TWObjectFactory.createObject();
						PROXY_BO = (TWObject) TWObjectFactory.createObject();

						for (int i = 1; i <= count; i++) {

							current = "/resources/resource[" + String.valueOf(ii) + "]/classifications/classification["
									+ String.valueOf(i) + "]/@uri";
							classification = xpath.evaluate(current, doc);

							if (environment == null && classification != null && classification.indexOf(
									"http://www.ibm.com/xmlns/prod/serviceregistry/6/1/GovernanceProfileTaxonomy#") != -1) {
								environment = classification.substring(classification.indexOf("#") + 1,
										classification.length());
							}

							current = null;
						}

						// recupero eventuale Proxy/MQ manual
						count = Integer.parseInt(xpath.evaluate(
								"count(/resources/resource[" + String.valueOf(ii) + "]/relationships/relationship)",
								doc));

						relation = null;
						proxybsrURI = null;
						proxy = null;

						for (int i = 1; i <= count; i++) {

							current = "/resources/resource[" + String.valueOf(ii) + "]/relationships/relationship["
									+ String.valueOf(i) + "]/@name";

							relation = xpath.evaluate(current, doc);

							if (proxy == null && relation != null && relation.indexOf("sm63_mqEndpoint") != -1) {

								current = "/resources/resource[" + String.valueOf(ii) + "]/relationships/relationship["
										+ String.valueOf(i) + "]/@targetBsrURI";
								if (xpath.evaluate(current, doc) != null) {

									proxybsrURI = xpath.evaluate(current, doc);
									proxy = "MQMANUAL";
									query6 = query5;
									query6 = query6.replaceAll("%BSRURI%", proxybsrURI);

									result = wsrrutility.generalWSRRQuery(query6, url, user, password,true);
									PROXY_BO = WSRRToBusinessObjectEPMQ.makeBO(result, proxy, null, url, user,
											password);

								}
							}

							current = null;
						}

						// navigo le prorieta' del singolo endpoint

						count = Integer.parseInt(xpath.evaluate(
								"count(/resources/resource[" + String.valueOf(ii) + "]/properties/property)", doc));

						for (int i = 1; i <= count; i++) {

							current = "/resources/resource[" + String.valueOf(ii) + "]/properties/property["
									+ String.valueOf(i) + "]/@name";
							value = "/resources/resource[" + String.valueOf(ii) + "]/properties/property["
									+ String.valueOf(i) + "]/@value";

							current_ = (String) xpath.evaluate(current, doc);

							value_ = (String) xpath.evaluate(value, doc);

							if (current_.equals("namespace"))
								current_ = "nspace";

							if (value_ == null)
								value_ = "";

							if (current_ != null && current_.equals("primaryType")) {

								if (type == null && value_ != null && value_.indexOf("#") != -1) {
									type = value_.substring(value_.indexOf("#") + 1, value_.length());

								}

							}

							// verifico quando sto leggendo
							// sm63_SPECIALIZZAZIONE

							if (current_ != null && current_.equals("sm63_SPECIALIZZAZIONE") && usoSpecializzazione) {

								if (value_ != null && value_.equals(specializzazione) && !specializzazioneTrovata) {
									specializzazioneTrovata = true;
									scrittoRecord = true;
								}

								if (value_ != null && value_.equals(specializzazione)) {
									specializzazioniContatore++;
								}

							}

							EP_BO.setPropertyValue(current_, value_);

							current = null;
							current_ = null;
							value = null;
							value_ = null;

						}

						if (!usoSpecializzazione || scrittoRecord) {
							// Setto Ambiente dell'endpoint

							EP_BO.setPropertyValue("environment", environment);

							if (proxybsrURI != null && proxybsrURI.length() != 0) {

								EP_BO.setPropertyValue("PROXY", PROXY_BO); // collego

								// proxy/MQManual
								// a
								// endpoint
							}

							EP_BO_MQ.addArrayData(EP_BO_MQ.getArraySize(), EP_BO);

						}

						NBP_BO.setPropertyValue("ENDPOINT_MQ", EP_BO_MQ);

						scrittoRecord = false;
					}

					if (specializzazioniContatore !=1 && usoSpecializzazione) {

						NBP_BO.setPropertyValue("ENDPOINT_MQ", null);

						NBP_BO.setPropertyValue("ENDPOINT_INCONGRUENTE", true);

						return null;

					}
				} else {

					return null;

				}

			} catch (Exception e) {

				System.out.println(
						"*********************************************KO************************************+");
				System.out.println(e.getMessage());
				System.out.println(
						"*********************************************KO************************************+");
				e.printStackTrace();
			}

		} catch (Exception e) {

			System.out.println("*********************************************KO************************************+");
			System.out.println(e.getMessage());
			System.out.println("*********************************************KO************************************+");
			e.printStackTrace();
			NBP_BO = null;
		}

		// System.out.println(">>>" +NBP_BO.getPropertyNames());

		System.out.println(">>>" + NBP_BO.toXMLString());

		return NBP_BO.toXMLString();
	}

	public static TWObject makeBO(String data, String type, String subType, String url, String user, String password)
			throws XPathExpressionException {

		final String MQ_MANUAL = "[type, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_serviceVersion, sm63_DATA_ULTIMO_UTILIZZO_MQM, sm63_PRODOTTO, sm63_DIR_ID_TGT_DES, sm63_serviceName, sm63_serviceNamespace, sm63_responseQMgr, sm63_portName, sm63_requestQMgr, sm63_MOD_COLLOQUIO_MQM, sm63_PROC_DEST, sm63_ID_MACCHINA, sm63_requestQName, sm63_DATA_PRIMO_UTILIZZO_MQM, sm63_PGM_AREE, sm63_responseQName]";
		String target = "UNDEF";

		TWObject CURRENT_BO = null;

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		InputSource source = new InputSource(new StringReader(data));
		Document doc = null;
		int count = 0;
		String current = null;
		String value = null;
		String current_ = null;
		String value_ = null;

		try {
			CURRENT_BO = (TWObject) TWObjectFactory.createObject();

			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			count = Integer.parseInt(xpath.evaluate("count(/resources/resource/properties/property)", doc));

			if (count == 0)
				return null;

			if (type != null && type.indexOf("MQMANUAL") != -1 && target.equals("UNDEF"))
				target = MQ_MANUAL;

			if (type != null)
				CURRENT_BO.setPropertyValue("type", type);

			if (subType != null)
				CURRENT_BO.setPropertyValue("subType", subType);

			for (int i = 1; i <= count; i++) {
				current = "/resources/resource/properties/property[" + String.valueOf(i) + "]/@name";
				value = "/resources/resource/properties/property[" + String.valueOf(i) + "]/@value";

				current_ = (String) xpath.evaluate(current, doc);
				value_ = (String) xpath.evaluate(value, doc);

				if (current_.equals("namespace"))
					current_ = "nspace";

				if (value_ == null)
					value_ = "";

				CURRENT_BO.setPropertyValue(current_, value_);

				current = null;
				current_ = null;
				value = null;
				value_ = null;

			}

		} catch (Exception e) {

			System.out.println("*********************************************KO************************************+");
			System.out.println(e.getMessage());
			System.out.println("*********************************************KO************************************+");
			e.printStackTrace();
			CURRENT_BO = null;
		}

		return CURRENT_BO;
	}

}
