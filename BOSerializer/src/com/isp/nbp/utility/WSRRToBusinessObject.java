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

	// 11052017 nella creazione degli endpoint sostituito "NO" con "N" (si
	// tratta del flag header)

	// 21052017 inserita gestione flag ispheader

	// 27052017 inserita gestione timeout

	String query1 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']";
	String query2 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@primaryType='http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23%TYPE%']";
    String query3 = "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/ale63_owningOrganization(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')]";
	String query4= "/Metadata/XML/GraphQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides(.)/gep63_availableEndpoints(.)";

	public WSRRToBusinessObject() {

	}

	public TWObject createServiceVersionBO(String name, String version, String url, String user, String password)
			throws XPathExpressionException {

		
		TWObject SV_BO = null;
		TWObject BS_BO = null;
		TWObject ACR_BO = null;
		
		TWList EP_BO_REST_APPL=null;
		TWList EP_BO_REST_SYST=null;
		TWList EP_BO_REST_PROD=null;
		TWList EP_BO_REST_UAT=null;
		TWList EP_BO_REST_INDEP=null;
		
		
		TWList EP_BO_SOAP_APPL=null;
		TWList EP_BO_SOAP_SYST=null;
		TWList EP_BO_SOAP_PROD=null;
		TWList EP_BO_SOAP_UAT=null;
		TWList EP_BO_SOAP_INDEP=null;
		
		TWList EP_BO_CALLABLE_APPL=null;
		TWList EP_BO_CALLABLE_SYST=null;
		TWList EP_BO_CALLABLE_PROD=null;
		TWList EP_BO_CALLABLE_UAT=null;
		TWList EP_BO_CALLABLE_INDEP=null;
		
		TWList EP_BO_ZRES_APPL=null;
		TWList EP_BO_ZRES_SYST=null;
		TWList EP_BO_ZRES_PROD=null;
		TWList EP_BO_ZRES_UAT=null;
		TWList EP_BO_ZRES_INDEP=null;
		
		TWList EP_BO_WOLA_APPL=null;
		TWList EP_BO_WOLA_SYST=null;
		TWList EP_BO_WOLA_PROD=null;
		TWList EP_BO_WOLA_UAT=null;
		TWList EP_BO_WOLA_INDEP=null;
		
		TWList EP_BO_MQ_APPL=null;
		TWList EP_BO_MQ_SYST=null;
		TWList EP_BO_MQ_PROD=null;
		TWList EP_BO_MQ_UAT=null;
		TWList EP_BO_MQ_INDEP=null;
		
		
		
		String SCHOST = "[type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_DESC_ESTESA, gep63_ABILITAZ_INFRASTR, ale63_assetType, gep63_SCHOST_NOME_CPY_OUT, ale63_guid, gep63_SCHOST_NOME_CPY_INP, ale63_remoteState, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_PIATT_EROG, gep63_FLG_CTRL_TIPOLOGIA, gep63_DISP_SERV, gep63_MATR_RICH_MODIFICA, gep63_PID_PROCESSO_GOV, gep63_TIPOLOGIA, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_SCHOST_PGM_MD_X_MPE, gep63_VINCOLI_RIUSO, gep63_SCHOST_CONVNULL, gep63_INFO_COSTO, gep63_ATTIVATO_IN_PROD, gep63_consumerIdentifier, gep63_SCHOST_PGM_SERVIZIO, gep63_UTILIZ_PIU_BAN_CLONI, gep63_SCHOST_ID_SERVIZIO, ale63_ownerEmail, gep63_ATTIVATO_IN_SYST, ale63_communityName, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SCHOST_PGM_MD_X_INTEROPER, ale63_fullDescription, gep63_SCHOST_PGM_MD, gep63_SCHOST_TRANS_SERVIZIO, gep63_ATTIVATO_IN_APPL, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_SCHOST_COD_VERSIONE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		String SHOST = " [type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_DESC_ESTESA, gep63_ABILITAZ_INFRASTR, ale63_assetType, gep63_SHOST_TRANS_SERVIZIO, ale63_guid, gep63_SHOST_CONVNULL, gep63_SHOST_PGM_SERVIZIO, ale63_remoteState, gep63_SHOST_NOME_CPY_INP, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_PIATT_EROG, gep63_FLG_CTRL_TIPOLOGIA, gep63_DISP_SERV, gep63_MATR_RICH_MODIFICA, gep63_PID_PROCESSO_GOV, gep63_TIPOLOGIA, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_SHOST_PGM_MD_X_INTEROPER, gep63_SHOST_NOME_CPY_OUT, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_SHOST_PGM_MD_X_MPE, gep63_VINCOLI_RIUSO, gep63_INFO_COSTO, gep63_ATTIVATO_IN_PROD, gep63_consumerIdentifier, gep63_UTILIZ_PIU_BAN_CLONI, ale63_ownerEmail, gep63_ATTIVATO_IN_SYST, ale63_communityName, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SHOST_ID_SERVIZIO, gep63_SHOST_PGM_MD, ale63_fullDescription, gep63_ATTIVATO_IN_APPL, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		String SCOPEN = "[type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_DESC_ESTESA, gep63_ABILITAZ_INFRASTR, gep63_SCOPEN_AMBITO_IMPLEMENTAZIONE, ale63_assetType, ale63_guid, gep63_SCOPEN_DIM_MAX_MSG, ale63_remoteState, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_FLG_CTRL_TIPOLOGIA, gep63_PIATT_EROG, gep63_MATR_RICH_MODIFICA, gep63_DISP_SERV, gep63_PID_PROCESSO_GOV, gep63_TIPOLOGIA, gep63_SCOPEN_STATO_ATTUALE_FUNZ, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_SCOPEN_DOWNTIME_PIANIFICATO, gep63_SCOPEN_DIM_MIN_MSG, gep63_VINCOLI_RIUSO, gep63_ATTIVATO_IN_PROD, gep63_INFO_COSTO, gep63_consumerIdentifier, gep63_UTILIZ_PIU_BAN_CLONI, gep63_SCOPEN_ATTACHMENT_TYPE, ale63_ownerEmail, gep63_SCOPEN_LINK_SIN_APPS_EST, gep63_ATTIVATO_IN_SYST, gep63_SCOPEN_REPS0, ale63_communityName, gep63_SCOPEN_NUM_CHIAMATE_PICCO, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SCOPEN_EAR_SERVIZIO, gep63_SCOPEN_FLG_CONTIENE_ATTACHMENT, gep63_SCOPEN_AMBIENTE_FISICO, gep63_SCOPEN_RIF_CHIAMANTI_INT, ale63_fullDescription, gep63_SCOPEN_VOLUME_GIORN, gep63_ATTIVATO_IN_APPL, gep63_SCOPEN_RIF_CHIAMANTI_EST, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		String SOPEN = " [type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_ABILITAZ_INFRASTR, gep63_DESC_ESTESA, gep63_SOPEN_LINK_SIN_APPS_EST, gep63_SOPEN_ATTACHMENT_TYPE, gep63_SOPEN_DOWNTIME_PIANIFICATO, gep63_SOPEN_REPS0, ale63_assetType, gep63_SOPEN_NUM_CHIAMATE_PICCO, ale63_guid, gep63_SOPEN_RIF_CHIAMANTI_EST, gep63_SOPEN_STATO_ATTUALE_FUNZ, ale63_remoteState, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_FLG_CTRL_TIPOLOGIA, gep63_PIATT_EROG, gep63_MATR_RICH_MODIFICA, gep63_DISP_SERV, gep63_PID_PROCESSO_GOV, gep63_SOPEN_RIF_CHIAMANTI_INT, gep63_TIPOLOGIA, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_SOPEN_EAR_SERVIZIO, gep63_SOPEN_AMBIENTE_FISICO, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_VINCOLI_RIUSO, gep63_ATTIVATO_IN_PROD, gep63_INFO_COSTO, gep63_consumerIdentifier, gep63_SOPEN_VOLUME_GIORN, gep63_SOPEN_AMBITO_IMPLEMENTAZIONE, gep63_UTILIZ_PIU_BAN_CLONI, ale63_ownerEmail, gep63_ATTIVATO_IN_SYST, gep63_SOPEN_DIM_MAX_MSG, ale63_communityName, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SOPEN_DIM_MIN_MSG, gep63_SOPEN_FLG_CONTIENE_ATTACHMENT, ale63_fullDescription, gep63_ATTIVATO_IN_APPL, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, g*ep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		
		String target = null;

		query1 = query1.replaceAll("%CATALOGNAME%", name).replaceAll("%VERSION%", version);
		String result;

		WSRRUtility wsrrutility = new WSRRUtility();

		result = wsrrutility.generalWSRRQuery(query1, url, user, password);


		

		if (result == null)
			return null;

		// System.out.println(result);

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		// result=result.replaceAll("(\\r|\\n)", "");
		InputSource source = new InputSource(new StringReader(result));
		Document doc = null;
		int count = 0;
		String current = null;
		String classification = null;
		String relation=null;
		String type = null;
		String subType = null;

		try {
			BS_BO = (TWObject) TWObjectFactory.createObject();
			SV_BO = (TWObject) TWObjectFactory.createObject();
			ACR_BO =(TWObject) TWObjectFactory.createObject();

			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			count = Integer.parseInt(xpath.evaluate("count(/resources/resource/classifications/classification)", doc));

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

			SV_BO=WSRRToBusinessObject.makeBO(result, type, subType, url, user, password);
			
			//Ho trovato il Service Version ora recupero il Business Service
			
			//Ricavo il nome del servizio 
			
			query2=query2.replaceAll("%CATALOGNAME%", name).replaceAll("%TYPE%", type.substring(0, type.length()-7));
			
			result = wsrrutility.generalWSRRQuery(query2, url, user, password);
			
			BS_BO=WSRRToBusinessObject.makeBO(result, type.substring(0, type.length()-7), subType, url, user, password);
			
			//Ricavo acronimo
			
			query3=query3.replaceAll("%BSRURI%", (String) BS_BO.getPropertyValue("bsrURI"));
			
			result = wsrrutility.generalWSRRQuery(query3, url, user, password);
			
			ACR_BO=WSRRToBusinessObject.makeBO(result, "Organization", null, url, user, password);
			
			//Ricavo Endpoint

			query4=query4.replaceAll("%BSRURI%", (String) SV_BO.getPropertyValue("bsrURI"));
			
			result = wsrrutility.generalWSRRQuery(query4, url, user, password);
			
			System.out.println("Query4 " +result);
			
			if (result !=null) {
				
				//navigo gli endpoints 
				
				
				
				//////////////////////////////////////////////////////////////////////////////////////////////
				
				
				xpathFactory = XPathFactory.newInstance();
				xpath = xpathFactory.newXPath();
				// result=result.replaceAll("(\\r|\\n)", "");
				source = new InputSource(new StringReader(result));
				doc = null;
				count = 0;
				int countag=0;
				current = null;
				String value = null;
				String current_ = null;
				String value_ = null;
				String environment=null;
				String proxy=null;
				String proxybsrURI=null;
				
				try {
					
					//NBP_BO = (TWObject) TWObjectFactory.createObject();

					doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
					
					countag = Integer.parseInt(xpath.evaluate("count(/resources/resource/properties)", doc));
					
					for (int ii = 1; ii <= countag; ii++) {
										
                    //recupero l'ambiente operativo dell 'endpoint						
				    count = Integer.parseInt(xpath.evaluate("count(/resources/resource["+String.valueOf(ii)+"]/classifications/classification)", doc));
				    
					type=null;
				    environment=null;
				    
					for (int i = 1; i <= count; i++) {
						current = "/resources/resource["+String.valueOf(ii)+"]/classifications/classification[" + String.valueOf(i) + "]/@uri";
						classification = xpath.evaluate(current, doc);
						
						if (environment == null && classification != null && classification.indexOf("http://www.ibm.com/xmlns/prod/serviceregistry/6/1/GovernanceProfileTaxonomy#") != -1) {
							environment = classification.substring(classification.indexOf("#") + 1, classification.length());
						}
						current = null;
					}
				    
					//recupero eventuale Proxy/MQ manual
					count = Integer.parseInt(xpath.evaluate("count(/resources/resource["+String.valueOf(ii)+"]/relationships/relationship)", doc));
					
					relation=null;
					proxybsrURI=null;
					proxy=null;
					
					for (int i = 1; i <= count; i++) {
						current = "/resources/resource["+String.valueOf(ii)+"]/relationships/relationship[" + String.valueOf(i) + "]/@name";
						
						relation = xpath.evaluate(current, doc);
						
						if (proxy == null && relation != null && relation.indexOf("rest80_CALLABLEProxy") != -1) {
							proxy="CALLABLE";
							current = "/resources/resource["+String.valueOf(ii)+"]/relationships/relationship[" + String.valueOf(i) + "]/@targetBsrURI";
							proxybsrURI=xpath.evaluate(current, doc);
						}
						
						if (proxy == null && relation != null && relation.indexOf("sm63_SOAPProxy") != -1) {
							proxy="SOAP";
							current = "/resources/resource["+String.valueOf(ii)+"]/relationships/relationship[" + String.valueOf(i) + "]/@targetBsrURI";
							proxybsrURI=xpath.evaluate(current, doc);
						}
						
						if (proxy == null && relation != null && relation.indexOf("rest80_RESTProxy") != -1) {
							proxy="REST";
							current = "/resources/resource["+String.valueOf(ii)+"]/relationships/relationship[" + String.valueOf(i) + "]/@targetBsrURI";
							proxybsrURI=xpath.evaluate(current, doc);
						}
						
						if (proxy == null && relation != null && relation.indexOf("sm63_mqEndpoint") != -1) {
							proxy="MQMANUAL";
							current = "/resources/resource["+String.valueOf(ii)+"]/relationships/relationship[" + String.valueOf(i) + "]/@targetBsrURI";
							proxybsrURI=xpath.evaluate(current, doc);
						}

						current = null;
					}
					
					/*
					 * ServiceModel#SOAPServiceEndpoint
					 * http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel#MQServiceEndpoint
					 * http://www.ibm.com/xmlns/prod/serviceregistry/profile/v8r0/RESTModel#RESTServiceEndpoint
					 * http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel#CICSServiceEndpoint
					*/
					
					count = Integer.parseInt(xpath.evaluate("count(/resources/resource["+String.valueOf(ii)+"]/properties/property)", doc));

					for (int i = 1; i <= count; i++) {

						current = "/resources/resource["+String.valueOf(ii)+"]/properties/property[" + String.valueOf(i) + "]/@name";
						value = "/resources/resource["+String.valueOf(ii)+"]/properties/property[" + String.valueOf(i) + "]/@value";
						
						
						System.out.println("///////////////////////////////////////////////////////////////////");
						System.out.println(current);
						System.out.println(value);
						System.out.println("///////////////////////////////////////////////////////////////////");

						current_ = (String) xpath.evaluate(current, doc);
						value_ = (String) xpath.evaluate(value, doc);

						//if (target.indexOf(current_) != -1) {
					    if (true) {
							
							if (current_.equals("namespace"))
								current_ = "nspace";

							if (value_ == null)
								value_ = "";
							
							
							if (current_ != null && current_.equals("primaryType")) {
								
								if (type == null && value_ != null && value_.indexOf(
										"#") != -1) {
									type = value_.substring(value_.indexOf("#") + 1, value_.length());
								}
														
							}

							System.out.println("********>>>>>**  "+current_+"="+value_);
							//NBP_BO.setPropertyValue(current_, value_);

							current = null;
							current_ = null;
							value = null;
							value_ = null;

						} else {
							
							System.out.println("Bypass_for "+current_ +" 4 type "+type);
						}

					}
					//
					System.out.println("V1OOOOOOOOOOOOOOOOOOOOOOOOOOO = "+type);
					System.out.println("V2OOOOOOOOOOOOOOOOOOOOOOOOOOO = "+environment);
					System.out.println("V3OOOOOOOOOOOOOOOOOOOOOOOOOOO = "+proxy);
					System.out.println("V4OOOOOOOOOOOOOOOOOOOOOOOOOOO = "+proxybsrURI);
					
					
					}
					
					//System.out.println(NBP_BO.getPropertyNames());
					

				} catch (Exception e) {

					System.out.println("*********************************************KO************************************+");
					System.out.println(e.getMessage());
					System.out.println("*********************************************KO************************************+");
					e.printStackTrace();
				}

				
				///////////////////////////////////////////////////////////////////////////////////////////////
			}

		} catch (Exception e) {

			System.out.println("*********************************************KO************************************+");
			System.out.println(e.getMessage());
			System.out.println("*********************************************KO************************************+");
			e.printStackTrace();
		}

		return SV_BO;
	}
	
	
	public static TWObject makeBO(String data, String type,String subType, String url, String user, String password)
			throws XPathExpressionException {

		final  String SCHOST = "[type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_DESC_ESTESA, gep63_ABILITAZ_INFRASTR, ale63_assetType, gep63_SCHOST_NOME_CPY_OUT, ale63_guid, gep63_SCHOST_NOME_CPY_INP, ale63_remoteState, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_PIATT_EROG, gep63_FLG_CTRL_TIPOLOGIA, gep63_DISP_SERV, gep63_MATR_RICH_MODIFICA, gep63_PID_PROCESSO_GOV, gep63_TIPOLOGIA, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_SCHOST_PGM_MD_X_MPE, gep63_VINCOLI_RIUSO, gep63_SCHOST_CONVNULL, gep63_INFO_COSTO, gep63_ATTIVATO_IN_PROD, gep63_consumerIdentifier, gep63_SCHOST_PGM_SERVIZIO, gep63_UTILIZ_PIU_BAN_CLONI, gep63_SCHOST_ID_SERVIZIO, ale63_ownerEmail, gep63_ATTIVATO_IN_SYST, ale63_communityName, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SCHOST_PGM_MD_X_INTEROPER, ale63_fullDescription, gep63_SCHOST_PGM_MD, gep63_SCHOST_TRANS_SERVIZIO, gep63_ATTIVATO_IN_APPL, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_SCHOST_COD_VERSIONE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		final String SHOST = " [type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_DESC_ESTESA, gep63_ABILITAZ_INFRASTR, ale63_assetType, gep63_SHOST_TRANS_SERVIZIO, ale63_guid, gep63_SHOST_CONVNULL, gep63_SHOST_PGM_SERVIZIO, ale63_remoteState, gep63_SHOST_NOME_CPY_INP, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_PIATT_EROG, gep63_FLG_CTRL_TIPOLOGIA, gep63_DISP_SERV, gep63_MATR_RICH_MODIFICA, gep63_PID_PROCESSO_GOV, gep63_TIPOLOGIA, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_SHOST_PGM_MD_X_INTEROPER, gep63_SHOST_NOME_CPY_OUT, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_SHOST_PGM_MD_X_MPE, gep63_VINCOLI_RIUSO, gep63_INFO_COSTO, gep63_ATTIVATO_IN_PROD, gep63_consumerIdentifier, gep63_UTILIZ_PIU_BAN_CLONI, ale63_ownerEmail, gep63_ATTIVATO_IN_SYST, ale63_communityName, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SHOST_ID_SERVIZIO, gep63_SHOST_PGM_MD, ale63_fullDescription, gep63_ATTIVATO_IN_APPL, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		final String SCOPEN = "[type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_DESC_ESTESA, gep63_ABILITAZ_INFRASTR, gep63_SCOPEN_AMBITO_IMPLEMENTAZIONE, ale63_assetType, ale63_guid, gep63_SCOPEN_DIM_MAX_MSG, ale63_remoteState, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_FLG_CTRL_TIPOLOGIA, gep63_PIATT_EROG, gep63_MATR_RICH_MODIFICA, gep63_DISP_SERV, gep63_PID_PROCESSO_GOV, gep63_TIPOLOGIA, gep63_SCOPEN_STATO_ATTUALE_FUNZ, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_SCOPEN_DOWNTIME_PIANIFICATO, gep63_SCOPEN_DIM_MIN_MSG, gep63_VINCOLI_RIUSO, gep63_ATTIVATO_IN_PROD, gep63_INFO_COSTO, gep63_consumerIdentifier, gep63_UTILIZ_PIU_BAN_CLONI, gep63_SCOPEN_ATTACHMENT_TYPE, ale63_ownerEmail, gep63_SCOPEN_LINK_SIN_APPS_EST, gep63_ATTIVATO_IN_SYST, gep63_SCOPEN_REPS0, ale63_communityName, gep63_SCOPEN_NUM_CHIAMATE_PICCO, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SCOPEN_EAR_SERVIZIO, gep63_SCOPEN_FLG_CONTIENE_ATTACHMENT, gep63_SCOPEN_AMBIENTE_FISICO, gep63_SCOPEN_RIF_CHIAMANTI_INT, ale63_fullDescription, gep63_SCOPEN_VOLUME_GIORN, gep63_ATTIVATO_IN_APPL, gep63_SCOPEN_RIF_CHIAMANTI_EST, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		final String SOPEN = " [type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, gep63_ABILITAZ_INFRASTR, gep63_DESC_ESTESA, gep63_SOPEN_LINK_SIN_APPS_EST, gep63_SOPEN_ATTACHMENT_TYPE, gep63_SOPEN_DOWNTIME_PIANIFICATO, gep63_SOPEN_REPS0, ale63_assetType, gep63_SOPEN_NUM_CHIAMATE_PICCO, ale63_guid, gep63_SOPEN_RIF_CHIAMANTI_EST, gep63_SOPEN_STATO_ATTUALE_FUNZ, ale63_remoteState, gep63_DATA_RITIRO_SERV, gep63_MATR_RICH_CREAZIONE, ale63_assetOwners, gep63_FLG_CTRL_TIPOLOGIA, gep63_PIATT_EROG, gep63_MATR_RICH_MODIFICA, gep63_DISP_SERV, gep63_PID_PROCESSO_GOV, gep63_SOPEN_RIF_CHIAMANTI_INT, gep63_TIPOLOGIA, gep63_MATR_PUBBLICATORE_CREAZ_SERV, gep63_SOPEN_EAR_SERVIZIO, gep63_SOPEN_AMBIENTE_FISICO, gep63_DERIVANTE_DA_ALTRI_SERV, gep63_VINCOLI_RIUSO, gep63_ATTIVATO_IN_PROD, gep63_INFO_COSTO, gep63_consumerIdentifier, gep63_SOPEN_VOLUME_GIORN, gep63_SOPEN_AMBITO_IMPLEMENTAZIONE, gep63_UTILIZ_PIU_BAN_CLONI, ale63_ownerEmail, gep63_ATTIVATO_IN_SYST, gep63_SOPEN_DIM_MAX_MSG, ale63_communityName, gep63_DATA_PUBBL_CREAZ_SERV, gep63_SECURITY_ROLE, gep63_NOME_SERVIZIO_PRECEDENTE, gep63_SOPEN_DIM_MIN_MSG, gep63_SOPEN_FLG_CONTIENE_ATTACHMENT, ale63_fullDescription, gep63_ATTIVATO_IN_APPL, gep63_TIPOLOGIA_OGGETTO_ESISTENTE, gep63_versionTerminationDate, gep63_DOC_ANALISI_FUNZIONALE, gep63_versionAvailabilityDate, gep63_DOC_ANALISI_DETTAGLIO, gep63_DOC_ANALISI_TECNICA, ale63_requirementsLink, ale63_assetWebLink]";
		final String SERVICEVERSION="[type, subType, bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, ale63_remoteState, ale63_communityName, ale63_assetOwners, ale63_assetType, ale63_guid, ale63_fullDescription, ale63_ownerEmail, ale63_requirementsLink, ale63_assetWebLink]";
		final String ACRONIMO="[bsrURI, name, nspace, version, description, owner, lastModified, creationTimestamp, lastModifiedBy, primaryType, ale63_RESP_FUNZIONALE_MATRICOLA, ale63_RESP_ATTIVITA_NOMINATIVO, ale63_RESP_FUNZIONALE_NOMINATIVO, ale63_DESC_AMBITO, ale63_RESP_SERVIZIO_NOMINATIVO, ale63_contact, ale63_contactEmail, ale63_CODICE_SISTEMA_APPLICATIVO, ale63_RESP_ATTIVITA_MATRICOLA, ale63_RESP_SERVIZIO_MATRICOLA, ale63_RESP_UFFICIO_MATRICOLA, ale63_RESP_TECNICO_MATRICOLA, ale63_RESP_UFFICIO_NOMINATIVO, ale63_RESP_TECNICO_NOMINATIVO, ale63_AMBITO]";
		
		String target = "UNDEF";


		TWObject NBP_BO = null;

		if (data == null)
			return null;

		// System.out.println(result);

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		// result=result.replaceAll("(\\r|\\n)", "");
		InputSource source = new InputSource(new StringReader(data));
		Document doc = null;
		int count = 0;
		String current = null;
		String value = null;
		String current_ = null;
		String value_ = null;
		
		try {
			NBP_BO = (TWObject) TWObjectFactory.createObject();

			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			count = Integer.parseInt(xpath.evaluate("count(/resources/resource/properties/property)", doc));

			
			if (type != null && type.indexOf("SOPENServiceVersion") != -1 && target.equals("UNDEF"))
				target = SOPEN;
			if (type != null && type.indexOf("SCOPENServiceVersion" ) != -1 && target.equals("UNDEF"))
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
			
			
			
			if( type != null) NBP_BO.setPropertyValue("type", type);
			
			if (subType != null) NBP_BO.setPropertyValue("subType", subType);
			
			System.out.println(target);

			for (int i = 1; i <= count; i++) {
				current = "/resources/resource/properties/property[" + String.valueOf(i) + "]/@name";
				value = "/resources/resource/properties/property[" + String.valueOf(i) + "]/@value";

				current_ = (String) xpath.evaluate(current, doc);
				value_ = (String) xpath.evaluate(value, doc);

				if (target.indexOf(current_) != -1) {
				//if (true) {
					
					if (current_.equals("namespace"))
						current_ = "nspace";

					if (value_ == null)
						value_ = "";

					//System.out.println("********>>>>> "+current_+"="+value_);
					NBP_BO.setPropertyValue(current_, value_);

					current = null;
					current_ = null;
					value = null;
					value_ = null;

				} else {
					
					System.out.println("Bypass_for "+current_ +" 4 type "+type);
				}

			}
			
			System.out.println(NBP_BO.getPropertyNames());
			

		} catch (Exception e) {

			System.out.println("*********************************************KO************************************+");
			System.out.println(e.getMessage());
			System.out.println("*********************************************KO************************************+");
			e.printStackTrace();
		}

		return NBP_BO;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void testGab() throws Exception {

		TWObject p = WSRRToBusinessObject.getPerson("email0");
		TWObject p1 = WSRRToBusinessObject.getPerson1("email1");
		TWObject all = WSRRToBusinessObject.getPerson3(p, p1);

		TWList list = (TWList) all.getPropertyValue("p3");

		System.out.println(((TWObject) all.getPropertyValue("p")).getPropertyValue("lastName"));
		System.out.println(((TWObject) all.getPropertyValue("p1")).getPropertyValue("lastName"));

		System.out.println(((TWObject) all.getPropertyValue("p")).getPropertyValue("age"));
		System.out.println(((TWObject) all.getPropertyValue("p1")).getPropertyValue("firstName1"));
		System.out.println("--------------------");
		System.out.println(((TWObject) list.getArrayData(0)).getPropertyValue("email"));
		System.out.println(((TWObject) list.getArrayData(1)).getPropertyValue("email1"));
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

	public static TWObject getPerson3(TWObject p, TWObject p1) throws Exception {
		// Create twobject person
		TWObject allperson = (TWObject) TWObjectFactory.createObject();
		allperson.setPropertyValue("p", p);
		allperson.setPropertyValue("p1", p1);

		TWList items = TWObjectFactory.createList();

		items.addArrayData(0, p);
		items.addArrayData(1, p1);

		allperson.setPropertyValue("p3", items);

		return allperson;
	}

	public static TWObject gabTEST2() throws Exception {
		// Create twobject person

		System.out.println("111111111111111111111111111111111111111111111111111111111111*");

		TWObject o1 = (TWObject) TWObjectFactory.createObject();
		o1.setPropertyValue("campo1", "campo1_o1");
		o1.setPropertyValue("campo2", "campo2_o1");

		TWObject o2 = (TWObject) TWObjectFactory.createObject();
		o2.setPropertyValue("campo1", "campo1_o2");
		o2.setPropertyValue("campo2", "campo2_o2");

		TWObject o3 = (TWObject) TWObjectFactory.createObject();
		o3.setPropertyValue("campo1", "campo1_o3");
		o3.setPropertyValue("campo2", "campo2_o3");

		TWObject o31 = (TWObject) TWObjectFactory.createObject();
		o3.setPropertyValue("campo1", "campo1_o31");
		o3.setPropertyValue("campo2", "campo2_o31");
		
		TWObject o42 = (TWObject) TWObjectFactory.createObject();
		o3.setPropertyValue("campo42", "maciaooooo");
		
		System.out.println("********************!11111111111111111111111111111111111");

		TWList o4 = TWObjectFactory.createList();
		o4.addArrayData(0, o3);
		o4.addArrayData(1, o31);
		o4.addArrayData(2, o42);
		
		System.out.println("mvvvvvvvvvvvvvvvvvvv1");
		
		System.out.println("SIZE "+o4.getArraySize());
		
		System.out.println("mvvvvvvvvvvvvvvvvvvv2");

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
		xml = "<resources><resource bsrURI=\"478b8e47-9ac9-49c7.8f60.4b460b4b607b\" type=\"GenericObject\" governanceRootBsrURI=\"478b8e47-9ac9-49c7.8f60.4b460b4b607b\"><properties><property name=\"bsrURI\" value=\"478b8e47-9ac9-49c7.8f60.4b460b4b607b\"/><property name=\"name\" value=\"CUGNA11\"/><property name=\"namespace\" value=\"\"/><property name=\"version\" value=\"00\"/><property name=\"description\" value=\"A\"/><property name=\"owner\" value=\"SOAGov1\"/><property name=\"lastModified\" value=\"1480424920515\"/><property name=\"creationTimestamp\" value=\"1474810448761\"/><property name=\"lastModifiedBy\" value=\"gabriele\"/><property name=\"primaryType\" value=\"http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel#SHOSTServiceVersion\"/><property name=\"gep63_SHOST_TRANS_SERVIZIO\" value=\"aa\"/><property name=\"ale63_ownerEmail\" value=\"\"/><property name=\"ale63_assetType\" value=\"\"/><property name=\"ale63_remoteState\" value=\"\"/><property name=\"ale63_fullDescription\" value=\"\"/><property name=\"gep63_ATTIVATO_IN_APPL\" value=\"2013.05.25.18.01.26\"/><property name=\"gep63_SHOST_PGM_SERVIZIO\" value=\"AA\"/><property name=\"ale63_communityName\" value=\"\"/><property name=\"gep63_consumerIdentifier\" value=\"\"/><property name=\"gep63_SHOST_CONVNULL\" value=\"Y\"/><property name=\"gep63_SHOST_ID_SERVIZIO\" value=\"aa\"/><property name=\"ale63_guid\" value=\"\"/><property name=\"gep63_ATTIVATO_IN_SYST\" value=\"2013.05.25.18.01.26\"/><property name=\"gep63_SHOST_NOME_CPY_INP\" value=\"aa\"/><property name=\"ale63_assetOwners\" value=\"\"/><property name=\"gep63_SHOST_NOME_CPY_OUT\" value=\"aa\"/><property name=\"gep63_DESC_ESTESA\" value=\"A\"/><property name=\"gep63_ATTIVATO_IN_PROD\" value=\"2013.05.25.18.01.26\"/><property name=\"gep63_PID_PROCESSO_GOV\" value=\"574\"/><property name=\"gep63_SHOST_PGM_MD\" value=\"XICHACAL\"/><property name=\"gep63_DATA_PUBBLICAZIONE\" value=\"2016-09-26\"/><property name=\"gep63_PUBBLICATORE_SERV\" value=\"gabriele\"/><property name=\"gep63_SHOST_PGM_MD_X_MPE\" value=\"\"/><property name=\"gep63_TIPOLOGIA\" value=\"\"/><property name=\"gep63_MATR_PUBBLICATORE_CREAZ_SERV\" value=\"\"/><property name=\"gep63_DOC_ANALISI_DETTAGLIO\" value=\"\"/><property name=\"gep63_DATA_PUBBL_CREAZ_SERV\" value=\"\"/><property name=\"gep63_ABILITAZ_INFRASTR\" value=\"\"/><property name=\"gep63_TIPOLOGIA_OGGETTO_ESISTENTE\" value=\"\"/><property name=\"gep63_versionTerminationDate\" value=\"\"/><property name=\"gep63_MATR_RICH_MODIFICA\" value=\"\"/><property name=\"gep63_VINCOLI_RIUSO\" value=\"\"/><property name=\"gep63_UTILIZ_PIU_BAN_CLONI\" value=\"\"/><property name=\"gep63_NOME_SERVIZIO_PRECEDENTE\" value=\"\"/><property name=\"gep63_DOC_ANALISI_FUNZIONALE\" value=\"\"/><property name=\"gep63_DOC_ANALISI_TECNICA\" value=\"\"/><property name=\"gep63_INFO_COSTO\" value=\"\"/><property name=\"gep63_FLG_CTRL_TIPOLOGIA\" value=\"\"/><property name=\"gep63_DISP_SERV\" value=\"\"/><property name=\"gep63_DERIVANTE_DA_ALTRI_SERV\" value=\"\"/><property name=\"gep63_SECURITY_ROLE\" value=\"\"/><property name=\"gep63_MATR_RICH_CREAZIONE\" value=\"\"/><property name=\"gep63_DATA_RITIRO_SERV\" value=\"\"/><property name=\"gep63_PIATT_EROG\" value=\"\"/><property name=\"gep63_versionAvailabilityDate\" value=\"\"/><property name=\"ale63_requirementsLink\" value=\"\"/><property name=\"ale63_assetWebLink\" value=\"\"/></properties><relationships><relationship name=\"ale63_artifacts\"/><relationship name=\"gep63_interfaceSpecifications\"/><relationship name=\"gep63_providedWebServices\"/><relationship name=\"gep63_provides\" targetBsrURI=\"9b8f859b-11a8-4895.986e.d8eafbd86eb6\" targetType=\"GenericObject\" primaryType=\"http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel#ServiceLevelDefinition\"/><relationship name=\"ale63_dependency\"/><relationship name=\"gep63_SHOST_CPY_INP\" targetBsrURI=\"f2cebdf2-d1ee-4e84.85da.f476bff4daca\" targetType=\"GenericDocument\"/><relationship name=\"gep63_providedRESTServices\"/><relationship name=\"ale63_owningOrganization\" targetBsrURI=\"5025de50-ea22-4207.b68d.d18338d18d06\" targetType=\"GenericObject\" primaryType=\"http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel#Organization\"/><relationship name=\"gep63_consumes\"/><relationship name=\"gep63_SHOST_CPY_OUT\" targetBsrURI=\"4ae40a4a-db67-47e7.b88c.16bc31168c0b\" targetType=\"GenericDocument\"/><relationship name=\"gep63_providedSCAModules\"/><relationship name=\"gep63_SHOST_DFDL_INP\"/><relationship name=\"gep63_SHOST_DFDL_OUT\"/></relationships><classifications><classification uri=\"http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel#SHOSTServiceVersion\"/><classification uri=\"http://www.ibm.com/xmlns/prod/serviceregistry/lifecycle/v6r3/LifecycleDefinition#SOALifecycle_InImmissione\" governanceState=\"true\"/><classification uri=\"http://isp/#RTGEN\"/></classifications></resource></resources>";

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		InputSource source = new InputSource(new StringReader(xml));
		Document doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
		String status = xpath.evaluate("/resources/resource/properties/property[@name=\"version\"]/@value", doc);
		String status1 = xpath.evaluate(
				"/resources/resource[@bsrURI=\"478b8e47-9ac9-49c7.8f60.4b460b4b607b\"]/classifications/classification[1]/@uri",
				doc);
		String status2 = xpath.evaluate("/resources/resource/classifications/classification[2]/@uri", doc);
		String status3 = xpath.evaluate("count(/resources/resource/classifications/classification)", doc);
		// count(/resources/resource/relationships/relationship)

		// String msg = xpath.evaluate(status1, doc);

		System.out.println("status=" + status);
		System.out.println("Message=" + status1);
		System.out.println("Message=" + status2);
		System.out.println("Message=" + status3);

	}

	public static void main(String[] a) {

		try {
			WSRRToBusinessObject boi = new WSRRToBusinessObject();
			boi.createServiceVersionBO("CUGNA11", "00", "https://WIN-MT67KKLQ7LO:9443/WSRR/8.5", "gabriele", "viviana");
			WSRRToBusinessObject.parse();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
