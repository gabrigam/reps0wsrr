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
public class WSRRToBusinessObjectCompactEPMQ {


	String query1 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[classifiedByAnyOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23SOPENServiceVersion','http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23SCOPENServiceVersion','http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23SHOSTServiceVersion','http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23SCHOSTServiceVersion')%20and%20@name='%CATALOGNAME%'%20and%20@version='%VERSION%']";
	String query2 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@primaryType='http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23%TYPE%']";
	String query3 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/ale63_owningOrganization(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')]";
	String query4 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)/gep63_availableEndpoints(.)[@primaryType='http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel%23MQServiceEndpoint']";
	String query5 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']";
	String query6 = "dynamic";
	String query7 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)/gep63_serviceInterface(.)"; // interfaccia
	String query8 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)";

	public WSRRToBusinessObjectCompactEPMQ() {

	}

	public TWObject WSRRBOSerializer(boolean usoSpecializzazione,String specializzazione,String name, String version, String url, String user, String password,boolean debug)
			throws XPathExpressionException {

		TWObject NBP_BO = null;
		TWObject SV_BO = null;

		TWObject EP_BO = null;
		TWObject PROXY_BO = null;

		TWList EP_BO_MQ = null;

		final String EP_MQ = "http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel#MQServiceEndpoint";

		final String MQ_EP = "[bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, sm63_PGM_DEST, sm63_serviceVersion, sm63_EXPIRY, sm63_serviceName, sm63_DATA_PRIMO_UTILIZZO, sm63_LUNGH_OUT, sm63_endpointType, sm63_PGM_DEST_RISP,sm63_FLAG_3LINK, sm63_STATO_OPER, sm63_serviceNamespace, sm63_PGM_QUADRATURA, sm63_USO_SICUREZZA, sm63_ID_APPL, sm63_TIPO_OPER, sm63_LUNGH_IN, sm63_TRACCIATURA, sm63_ALTER_COLL, sm63_TGT_SERVER, sm63_PRIORITY, sm63_PGM_FORM, sm63_DATA_ULTIMO_UTILIZZO, sm63_CALL_HEADER, sm63_MOD_COLLOQUIO, sm63_Timeout, sm63_ID_TGT_DES, sm63_SPECIALIZZAZIONE, sm63_BACKOUT_COUNT]";

		System.out.println("########################################################################################################################################");
		System.out.println("WSRRToBusinessObjectCompactEPMQ V1.0 Sept 2017");
		System.out.println("########################################################################################################################################");
		System.out.println("Parametri - Censimento : "+name+" versione : "+version+ " wsrr : "+url +" considero specializzazione? : "+usoSpecializzazione +" specializzazione : "+specializzazione);
		System.out.println("########################################################################################################################################");
		query1 = query1.replaceAll("%CATALOGNAME%", name).replaceAll("%VERSION%", version);
		String result;

		WSRRUtility wsrrutility = new WSRRUtility();

		result = wsrrutility.generalWSRRQuery(query1, url, user, password,debug);
		
		if (result == null)
			
			return null;
		
		WSRRToBusinessObjectCompactEPMQ.log("Censimento : "+name +" versione : "+version+" Trovato procedo con l'analisi...", debug);
		if (usoSpecializzazione)
		WSRRToBusinessObjectCompactEPMQ.log("E' stata richiesta la verifica della specializzazione...", debug);

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
			SV_BO = (TWObject) TWObjectFactory.createObject();
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

			WSRRToBusinessObjectCompactEPMQ.log("Trovato Censimento di tipo : "+type+ " sottotipo : "+subType, debug);
			
			SV_BO = WSRRToBusinessObjectCompactEPMQ.makeBO(result, type, subType, url, user, password,debug);

			NBP_BO.setPropertyValue("ENDPOINT_MQ", null);
		
			// Ricavo Endpoint

			query4 = query4.replaceAll("%BSRURI%", (String) SV_BO.getPropertyValue("bsrURI"));

			result = wsrrutility.generalWSRRQuery(query4, url, user, password,debug);

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

							current = null;
						}

						WSRRToBusinessObjectCompactEPMQ.log("Trovato endpoint di tipo : "+type, debug);
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
									result = wsrrutility.generalWSRRQuery(query6, url, user, password,debug);
									PROXY_BO = WSRRToBusinessObjectCompactEPMQ.makeBO(result, proxy, null, url, user,
											password,debug);
									WSRRToBusinessObjectCompactEPMQ.log("Creato PROXY_BO per tipo MQ (sm63_mqEndpoint)", debug);
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

								WSRRToBusinessObjectCompactEPMQ.log("Proprieta' : "+current_+" non presente nel template : "+target, debug);
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


							if (type.equals("MQ_EP")) {

								EP_BO_MQ.addArrayData(EP_BO_MQ.getArraySize(), EP_BO);

							}

						}

						NBP_BO.setPropertyValue("ENDPOINT_MQ", EP_BO_MQ);

						scrittoRecord=false;
					}

					if (specializzazioniContatore !=1 && usoSpecializzazione) {

						NBP_BO.setPropertyValue("ENDPOINT_MQ", null);

						NBP_BO.setPropertyValue("ENDPOINT_INCONGRUENTE",true);
						WSRRToBusinessObjectCompactEPMQ.log("Trovati endpoint Incongruenti", debug);


					}
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
			NBP_BO=null;
		}

		// System.out.println(">>>" +NBP_BO.getPropertyNames());
		System.out.println(">>>" +NBP_BO.toXMLString());

		return NBP_BO;
	}

	public static TWObject makeBO(String data, String type, String subType, String url, String user, String password,boolean debug)
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
			
			WSRRToBusinessObjectCompactEPMQ.log("Chiamata a makeBO per tipo : "+type, debug);


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

					WSRRToBusinessObjectCompactEPMQ.log("Proprieta' : "+current_+" non presente nel template : "+target, debug);
				}
			}

		} catch (Exception e) {

			System.out.println("*********************************************KO************************************+");
			System.out.println(e.getMessage());
			System.out.println("*********************************************KO************************************+");
			e.printStackTrace();
			CURRENT_BO=null;
		}

		return CURRENT_BO;
	}
	private static void log(String message,boolean logging) {
		
		if (logging) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println(message);
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		}
	}
}
