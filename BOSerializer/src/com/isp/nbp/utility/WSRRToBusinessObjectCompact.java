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

public class WSRRToBusinessObjectCompact {

	String query1 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']";
	String query2 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@primaryType='http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23%TYPE%']";
	String query3 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/ale63_owningOrganization(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')]";
	String query4 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)/gep63_availableEndpoints(.)";
	String query5 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']";
	String query6 = "dynamic";
	String query7 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)/gep63_serviceInterface(.)"; // interfaccia
	String query8 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)";

	public WSRRToBusinessObjectCompact() {

	}

	public TWObject createNBPBOCompact(boolean usoSpecializzazione,String specializzazione,String name, String version, String url, String user, String password)
			throws XPathExpressionException {

		TWObject NBP_BO = null;
		TWObject SV_BO = null;
		TWObject BS_BO = null;
		TWObject ACR_BO = null;
		TWObject INTERF_BO = null;
		TWObject SLD_BO = null;

		TWObject EP_BO = null;
		TWObject PROXY_BO = null;

		TWList EP_BO_SOAP = null;
		TWList EP_BO_REST = null;
		TWList EP_BO_CALLABLE = null;
		TWList EP_BO_ZRES = null;
		TWList EP_BO_WOLA = null;
		TWList EP_BO_CICS = null;
		TWList EP_BO_MQ = null;

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

		query1 = query1.replaceAll("%CATALOGNAME%", name).replaceAll("%VERSION%", version);
		String result;

		WSRRUtility wsrrutility = new WSRRUtility();

		result = wsrrutility.generalWSRRQuery(query1, url, user, password);

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

			EP_BO_REST = TWObjectFactory.createList();
			EP_BO_SOAP = TWObjectFactory.createList();
			EP_BO_CALLABLE = TWObjectFactory.createList();
			EP_BO_ZRES = TWObjectFactory.createList();
			EP_BO_WOLA = TWObjectFactory.createList();
			EP_BO_CICS = TWObjectFactory.createList();
			EP_BO_MQ = TWObjectFactory.createList();

			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			count = Integer.parseInt(xpath.evaluate("count(/resources/resource/classifications/classification)", doc));

			if (count==0) return null;

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
				current = null;
			}

			SV_BO = WSRRToBusinessObjectCompact.makeBO(result, type, subType, url, user, password);

			NBP_BO.setPropertyValue("SOPEN", null);
			NBP_BO.setPropertyValue("SCOPEN", null);
			NBP_BO.setPropertyValue("SHOST", null);
			NBP_BO.setPropertyValue("SCHOST", null);

			if (type.equals("SOPENServiceVersion")) NBP_BO.setPropertyValue("SOPEN", SV_BO);
			if (type.equals("SCOPENServiceVersion")) NBP_BO.setPropertyValue("SCOPEN", SV_BO);
			if (type.equals("SHOSTServiceVersion")) NBP_BO.setPropertyValue("SHOST", SV_BO);
			if (type.equals("SCHOSTServiceVersion"))NBP_BO.setPropertyValue("SCHOST", SV_BO);

			// Ho trovato il Service Version ora recupero il Business Service

			query2 = query2.replaceAll("%CATALOGNAME%", name).replaceAll("%TYPE%",
					type.substring(0, type.length() - 7));

			result = wsrrutility.generalWSRRQuery(query2, url, user, password);

			BS_BO = WSRRToBusinessObjectCompact.makeBO(result, type.substring(0, type.length() - 7), subType, url, user,password);
			NBP_BO.setPropertyValue("BS", BS_BO);

			// Ricavo acronimo

			if (BS_BO ==null) {
				NBP_BO.setPropertyValue("ACRO", BS_BO);
			} else {
				query3 = query3.replaceAll("%BSRURI%", (String) BS_BO.getPropertyValue("bsrURI"));

				result = wsrrutility.generalWSRRQuery(query3, url, user, password);

				ACR_BO = WSRRToBusinessObjectCompact.makeBO(result, "Organization", null, url, user, password);
				NBP_BO.setPropertyValue("ACRO", ACR_BO);
			}

			// Ricavo Interfaccia

			if (BS_BO==null) {

				NBP_BO.setPropertyValue("INTERF", BS_BO);

			} else {

				query7 = query7.replaceAll("%BSRURI%", (String) SV_BO.getPropertyValue("bsrURI"));

				result = wsrrutility.generalWSRRQuery(query7, url, user, password);

				INTERF_BO = WSRRToBusinessObjectCompact.makeBO(result, "Interface", null, url, user, password);
				NBP_BO.setPropertyValue("INTERF", INTERF_BO);
			}

			// Ricavo SLD

			if (BS_BO ==null){

				NBP_BO.setPropertyValue("SLD", BS_BO);

			} else {

				query8 = query8.replaceAll("%BSRURI%", (String) SV_BO.getPropertyValue("bsrURI"));

				result = wsrrutility.generalWSRRQuery(query8, url, user, password);

				SLD_BO = WSRRToBusinessObjectCompact.makeBO(result, "SLD", null, url, user, password);
				SLD_BO.setPropertyValue("SLD", SLD_BO);

			}

			NBP_BO.setPropertyValue("ENDPOINT_SOAP", null);
			NBP_BO.setPropertyValue("ENDPOINT_REST", null);
			NBP_BO.setPropertyValue("ENDPOINT_ZRES", null);
			NBP_BO.setPropertyValue("ENDPOINT_WOLA", null);
			NBP_BO.setPropertyValue("ENDPOINT_CICS", null);
			NBP_BO.setPropertyValue("ENDPOINT_MQ", null);
			NBP_BO.setPropertyValue("ENDPOINT_CALLABLE", null);

			// Ricavo Endpoint

			query4 = query4.replaceAll("%BSRURI%", (String) SV_BO.getPropertyValue("bsrURI"));

			result = wsrrutility.generalWSRRQuery(query4, url, user, password);

			// navigo gli endpoints

			xpathFactory = XPathFactory.newInstance();
			xpath = xpathFactory.newXPath();
			source = new InputSource(new StringReader(result));
			doc = null;
			count = 0;
			int countag = 0;
			current = null;
			String value = null;
			String current_ = null;
			String value_ = null;

			try {
				int specializzazioniContatore = 0;
				boolean specializzazioneTrovata = false;
				boolean scrittoRecord=false;

				NBP_BO.setPropertyValue("ENDPOINT_INCONGRUENTE",false);

				doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

				countag = Integer.parseInt(xpath.evaluate("count(/resources/resource/properties)", doc));

				if (countag !=0) {

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

							if (type == null && classification != null && classification.indexOf(EP_MQ) != -1) {
								type = "MQ_EP";
								target = MQ_EP;
							}

							if (type == null && classification != null && classification.indexOf(EP_SOAP) != -1) {
								type = "SOAP_EP";
								target = SOAP_EP;
							}

							if (type == null && classification != null && classification.indexOf(EP_REST) != -1) {
								type = "REST_EP";
								target = REST_EP;
							}

							if (type == null && classification != null && classification.indexOf(EP_ZRES) != -1) {
								type = "ZRES_EP";
								target = ZRES_EP;
							}

							if (type == null && classification != null && classification.indexOf(EP_WOLA) != -1) {
								type = "WOLA_EP";
								target = WOLA_EP;
							}

							if (type == null && classification != null && classification.indexOf(EP_CALLABLE) != -1) {
								type = "CALLABLE_EP";
								target = CALLABLE_EP;
							}

							if (type == null && classification != null && classification.indexOf(EP_CICS) != -1) {
								type = "CICS_EP";
								target = CICS_EP;
								;
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

							if (proxy == null && relation != null && relation.indexOf("rest80_CALLABLEProxy") != -1) {

								current = "/resources/resource[" + String.valueOf(ii) + "]/relationships/relationship["
										+ String.valueOf(i) + "]/@targetBsrURI";
								if (xpath.evaluate(current, doc) != null) {
									proxybsrURI = xpath.evaluate(current, doc);
									proxy = "CALLABLE";
									query6 = query5;
									query6 = query6.replaceAll("%BSRURI%", proxybsrURI);
									result = wsrrutility.generalWSRRQuery(query6, url, user, password);
									PROXY_BO = WSRRToBusinessObjectCompact.makeBO(result, proxy, null, url, user,
											password);
								}

							}

							if (proxy == null && relation != null && relation.indexOf("sm63_SOAPProxy") != -1) {

								current = "/resources/resource[" + String.valueOf(ii) + "]/relationships/relationship["
										+ String.valueOf(i) + "]/@targetBsrURI";
								if (xpath.evaluate(current, doc) != null) {
									proxybsrURI = xpath.evaluate(current, doc);
									proxy = "SOAP";
									query6 = query5;
									query6 = query6.replaceAll("%BSRURI%", proxybsrURI);
									result = wsrrutility.generalWSRRQuery(query6, url, user, password);
									PROXY_BO = WSRRToBusinessObjectCompact.makeBO(result, proxy, null, url, user,
											password);
								}
							}

							if (proxy == null && relation != null && relation.indexOf("rest80_RESTProxy") != -1) {

								current = "/resources/resource[" + String.valueOf(ii) + "]/relationships/relationship["
										+ String.valueOf(i) + "]/@targetBsrURI";
								if (xpath.evaluate(current, doc) != null) {
									proxybsrURI = xpath.evaluate(current, doc);
									proxy = "REST";
									query6 = query5;
									query6 = query6.replaceAll("%BSRURI%", proxybsrURI);
									result = wsrrutility.generalWSRRQuery(query6, url, user, password);
									PROXY_BO = WSRRToBusinessObjectCompact.makeBO(result, proxy, null, url, user,
											password);
								}
							}

							if (proxy == null && relation != null && relation.indexOf("sm63_mqEndpoint") != -1) {

								current = "/resources/resource[" + String.valueOf(ii) + "]/relationships/relationship["
										+ String.valueOf(i) + "]/@targetBsrURI";
								if (xpath.evaluate(current, doc) != null) {
									proxybsrURI = xpath.evaluate(current, doc);
									proxy = "MQMANUAL";
									query6 = query5;
									query6 = query6.replaceAll("%BSRURI%", proxybsrURI);
									result = wsrrutility.generalWSRRQuery(query6, url, user, password);
									PROXY_BO = WSRRToBusinessObjectCompact.makeBO(result, proxy, null, url, user,
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

							if (target.indexOf(current_) != -1) {

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
										scrittoRecord=true;
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

							} else {

								System.out.println("Bypass_for " + current_ + " 4 type " + type);
							}

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

							if (type.equals("SOAP_EP")) {

								EP_BO_SOAP.addArrayData(EP_BO_SOAP.getArraySize(), EP_BO);
							}

							if (type.equals("REST_EP")) {

								EP_BO_REST.addArrayData(EP_BO_REST.getArraySize(), EP_BO);

							}

							if (type.equals("CALLABLE_EP")) {

								EP_BO_CALLABLE.addArrayData(EP_BO_CALLABLE.getArraySize(), EP_BO);

							}

							if (type.equals("WOLA_EP")) {

								EP_BO_WOLA.addArrayData(EP_BO_WOLA.getArraySize(), EP_BO);

							}

							if (type.equals("ZRES_EP")) {

								EP_BO_ZRES.addArrayData(EP_BO_ZRES.getArraySize(), EP_BO);

							}

							if (type.equals("CICS_EP")) {

								EP_BO_CICS.addArrayData(EP_BO_CICS.getArraySize(), EP_BO);

							}

							if (type.equals("MQ_EP")) {

								EP_BO_MQ.addArrayData(EP_BO_MQ.getArraySize(), EP_BO);

							}

						}

						NBP_BO.setPropertyValue("ENDPOINT_SOAP", EP_BO_SOAP);
						NBP_BO.setPropertyValue("ENDPOINT_REST", EP_BO_REST);
						NBP_BO.setPropertyValue("ENDPOINT_CALLABLE", EP_BO_CALLABLE);
						NBP_BO.setPropertyValue("ENDPOINT_ZRES", EP_BO_ZRES);
						NBP_BO.setPropertyValue("ENDPOINT_WOLA", EP_BO_WOLA);
						NBP_BO.setPropertyValue("ENDPOINT_CICS", EP_BO_CICS);
						NBP_BO.setPropertyValue("ENDPOINT_MQ", EP_BO_MQ);

						scrittoRecord=false;
					}

					if (specializzazioniContatore != 1 && usoSpecializzazione) {

						NBP_BO.setPropertyValue("ENDPOINT_SOAP", null);
						NBP_BO.setPropertyValue("ENDPOINT_REST", null);
						NBP_BO.setPropertyValue("ENDPOINT_CALLABLE", null);
						NBP_BO.setPropertyValue("ENDPOINT_ZRES", null);
						NBP_BO.setPropertyValue("ENDPOINT_WOLA", null);
						NBP_BO.setPropertyValue("ENDPOINT_CICS", null);
						NBP_BO.setPropertyValue("ENDPOINT_MQ", null);

						NBP_BO.setPropertyValue("ENDPOINT_INCONGRUENTE",true);

					}
				}
			} catch (Exception e) {

				System.out.println(
						"*********************************************KO************************************+");
				System.out.println(e.getMessage());
				System.out.println(
						"*********************************************KO************************************+");
				e.printStackTrace();
				NBP_BO= null;
			}


		} catch (Exception e) {

			System.out.println("*********************************************KO************************************+");
			System.out.println(e.getMessage());
			System.out.println("*********************************************KO************************************+");
			e.printStackTrace();
			NBP_BO= null;
		}

		// System.out.println(">>>" +NBP_BO.getPropertyNames());
		//System.out.println(">>>" +NBP_BO.toXMLString());

		return NBP_BO;
	}

	public static TWObject makeBO(String data, String type, String subType, String url, String user, String password)
			throws XPathExpressionException {

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

			if (count==0) return null;

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

			if (type != null && type.indexOf("Interface") != -1 && target.equals("UNDEF"))
				target = INTERF;

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
				current = "/resources/resource/properties/property[" + String.valueOf(i) + "]/@name";
				value = "/resources/resource/properties/property[" + String.valueOf(i) + "]/@value";

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

					System.out.println("Bypass_for " + current_ + " 4 type " + type);
				}
			}

			// System.out.println(">>>" +CURRENT_BO.getPropertyNames());

		} catch (Exception e) {

			System.out.println("*********************************************KO************************************+");
			System.out.println(e.getMessage());
			System.out.println("*********************************************KO************************************+");
			e.printStackTrace();
			CURRENT_BO=null;
		}

		return CURRENT_BO;
	}

}
