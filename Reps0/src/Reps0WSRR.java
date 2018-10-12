import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

import com.isp.wsrr.utility.WSRRUtility;

import teamworks.TWList;

public class Reps0WSRR {

	// 150117 prima scrittura

	// 180117 versione testata ok

	// 11052017 nella creazione degli endpoint sostituito "NO" con "N" (si
	// tratta del flag header)

	// 21052017 inserita gestione flag ispheader

	// 27052017 inserita gestione timeout in creazione

	// 29052017 inserita gestione timout in modifica (ma serve?)

	// 30052017 modifica settaggio timeout

	// 09112017 adattamento anche per la modifica
	
	// attenzione tra queste due date sono state eseguite modifiche non inserite in testata (vedere nel codice)
	
	// 12032018 filtraggio campi note
	
	// 14042018 aggiunta modifica per poter gestire il codice anche quando sono in UPD (aggiunta modifica nuovo EP) inserito test su presenza proxy
	//          esempio noteUser != null && noteUser.length() != 0 && uriproxySystemTest !=null
	
	// 11/10/2018 inserito codice per creazione endpoint di tipo poun quando il servizio ha sicurezza SI-APIGatewayTarget
	//            viene eseguito un check della url se quest'ultimanon è valida si ha una condizione di errore
	
	public Reps0WSRR() {

		// notes
	}

	public boolean updateEndPointAndWSProxyData(String bsrURISLD, String interfaceType, TWList notes,
			TWList endPointbsrURI, TWList endpontProxybsrURI, TWList securizedUrls, TWList flagISPHeader,
			TWList timeout, TWList espostocomeAPI, String sicurezza, String proxyendpointApplication,String registry, String user, String password) {

		WSRRUtility wsrrutility = new WSRRUtility();
		WSDLLoaderBPM envelopes = new WSDLLoaderBPM();

		String uriendpointApplication = (String) endPointbsrURI.getArrayData(0);
		String uriendpointSystemTest = (String) endPointbsrURI.getArrayData(1);
		String uriendpointProduction = (String) endPointbsrURI.getArrayData(2);
		String uriendpointIndependent = (String) endPointbsrURI.getArrayData(3);
		String uriendpointUserAcceptance = (String) endPointbsrURI.getArrayData(4);

		String uriproxyApplication = (String) endpontProxybsrURI.getArrayData(0);
		String uriproxySystemTest = (String) endpontProxybsrURI.getArrayData(1);
		String uriproxyProduction = (String) endpontProxybsrURI.getArrayData(2);
		String uriproxyIndependent = (String) endpontProxybsrURI.getArrayData(3);
		String uriproxyUserAcceptance = (String) endpontProxybsrURI.getArrayData(4);

		String applicationUrlSecurized = (String) securizedUrls.getArrayData(0);
		String systemTestUrlSecurized = (String) securizedUrls.getArrayData(1);
		String productionUrlSecurized = (String) securizedUrls.getArrayData(2);
		String independentSecurized = (String) securizedUrls.getArrayData(3);
		String userAcceptanceUrlSecurized = (String) securizedUrls.getArrayData(4);

		String applicationFlagISPHeader = (String) flagISPHeader.getArrayData(0);
		String systemTestFlagISPHeader = (String) flagISPHeader.getArrayData(1);
		String productionFlagISPHeader = (String) flagISPHeader.getArrayData(2);
		String independentFlagISPHeader = (String) flagISPHeader.getArrayData(3);
		String userAcceptanceFlagISPHeader = (String) flagISPHeader.getArrayData(4);

		String applicationTimeout = (String) timeout.getArrayData(0);
		String systemTestTimeout = (String) timeout.getArrayData(1);
		String productionTimeout = (String) timeout.getArrayData(2);
		String independentTimeout = (String) timeout.getArrayData(3);
		String userAcceptanceTimeout = (String) timeout.getArrayData(4);

		////////////////////////////////////////////////////////////////////////////////////// *
		String applicationespostoComeAPI = (String) espostocomeAPI.getArrayData(0);
		String systemTestespostoComeAPI = (String) espostocomeAPI.getArrayData(1);
		String productionespostoComeAPI = (String) espostocomeAPI.getArrayData(2);
		String independentespostoComeAPI = (String) espostocomeAPI.getArrayData(3);
		String userAcceptanceespostoComeAPI = (String) espostocomeAPI.getArrayData(4);
		
		//11/10/2018
		boolean urlNotValid=false;

		if (uriendpointSystemTest == null)
			systemTestespostoComeAPI = applicationespostoComeAPI;
		if (uriendpointProduction == null)
			productionespostoComeAPI = applicationespostoComeAPI;
		if (uriendpointIndependent == null)
			independentespostoComeAPI = applicationespostoComeAPI;
		if (uriendpointUserAcceptance == null)
			userAcceptanceespostoComeAPI = applicationespostoComeAPI;

		///////////////////////////////////////////////////////////////////////////////////////

		if (uriendpointSystemTest == null)
			systemTestFlagISPHeader = applicationFlagISPHeader;
		if (uriendpointProduction == null)
			productionFlagISPHeader = applicationFlagISPHeader;
		if (uriendpointIndependent == null)
			independentFlagISPHeader = applicationFlagISPHeader;
		if (uriendpointUserAcceptance == null)
			userAcceptanceFlagISPHeader = applicationFlagISPHeader;

		// 30052017
		if (uriendpointSystemTest == null)
			systemTestTimeout = applicationTimeout;
		if (uriendpointProduction == null)
			productionTimeout = applicationTimeout;
		if (uriendpointIndependent == null)
			independentTimeout = applicationTimeout;
		if (uriendpointUserAcceptance == null)
			userAcceptanceTimeout = applicationTimeout;

		String noteUser = (String) notes.getArrayData(0);
		String noteDP = (String) notes.getArrayData(1);
		String noteError = (String) notes.getArrayData(2);

		Boolean result = false;
		String bsrURI = null;

		boolean log = true;
		Reps0WSRR.logMe(">RepsoWSRR mapper V20.7 October 2018", true);

		Reps0WSRR.logMe(">>>>>>WSRRoutine parametri::", log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + bsrURISLD, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + uriendpointApplication, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + uriendpointProduction, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + uriendpointIndependent, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + uriendpointUserAcceptance, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + uriendpointSystemTest, log);

		Reps0WSRR.logMe(">>>>>>WSRRoutine " + uriproxyApplication, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + uriproxySystemTest, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + uriproxyProduction, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + uriproxyIndependent, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + uriproxyUserAcceptance, log);

		Reps0WSRR.logMe(">>>>>>WSRRoutine " + applicationUrlSecurized, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + systemTestUrlSecurized, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + productionUrlSecurized, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + independentSecurized, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + userAcceptanceUrlSecurized, log);
		
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + applicationFlagISPHeader, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + systemTestFlagISPHeader, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + productionFlagISPHeader, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + independentFlagISPHeader, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + userAcceptanceFlagISPHeader, log);

		Reps0WSRR.logMe(">>>>>>WSRRoutine " + applicationTimeout, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + systemTestTimeout, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + productionTimeout, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + independentTimeout, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + userAcceptanceTimeout, log);

		Reps0WSRR.logMe(">>>>>>WSRRoutine " + noteUser, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + noteDP, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine " + noteError, log);
		
		//12032018 filtro le note codificando " < >
		
	    if (noteUser!=null) {
	    	noteUser = noteUser.replaceAll("\"","&quote;");
	    	noteUser = noteUser.replaceAll("<","&lt;");
	    	noteUser = noteUser.replaceAll(">","&gt;");	    	
	    }
	    
	    if (noteDP!=null) {
	    	noteDP = noteDP.replaceAll("\"","&quote;");
	    	noteDP = noteDP.replaceAll("<","&lt;");
	    	noteDP = noteDP.replaceAll(">","&gt;");	    	
	    }
	    
	    if (noteError!=null) {
	    	noteError = noteError.replaceAll("\"","&quote;");
	    	noteError = noteError.replaceAll("<","&lt;");
	    	noteError = noteError.replaceAll(">","&gt;");	    	
	    }
		
	    if (userAcceptanceUrlSecurized !=null )Reps0WSRR.logMe(">>>>>>WSRRoutine " + userAcceptanceUrlSecurized.length(), log);

		try {
			Reps0WSRR.logMe(">>>>>>WSRRoutine P1", log);
			result = Reps0WSRR.updateSinglePropertyJSONFormat(uriendpointApplication, "name", applicationUrlSecurized,
					registry, user, password);
			Reps0WSRR.logMe(">>>>>>WSRRoutine P2", log);
			if (result) {
				Reps0WSRR.logMe(">>>>>>WSRRoutine P3", log);
				if (noteUser != null && noteUser.length() != 0) {
					result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyApplication, "sm63_NOTE_GEN_WSPROXY",
							noteUser, registry, user, password);
					Reps0WSRR.logMe(">>>>>>WSRRoutine P4", log);
				}

				if (result) {
					Reps0WSRR.logMe(">>>>>>WSRRoutine P5", log);
					if (noteDP != null && noteDP.length() != 0) {
						result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyApplication, "sm63_NOTE", noteDP,
								registry, user, password);
						Reps0WSRR.logMe(">>>>>>WSRRoutine P6", log);
					}
				}

				if (result) {
					Reps0WSRR.logMe(">>>>>>WSRRoutine P7", log);
					if (noteError != null && noteError.length() != 0) {
						result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyApplication,
								"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
						Reps0WSRR.logMe(">>>>>>WSRRoutine P8", log);
					}

				}
				
				//11/10/2018 
				if (result) {
					if (sicurezza.equals("SI-APIGatewayTarget")) {
						
						String envelope=null;
						
						String pounEndpoint=Reps0WSRR.trasformURL(proxyendpointApplication);
						
						if (pounEndpoint.equals("ERRORE")) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine Attenzione la URL dell'enpoint endpoint securizzato "+applicationUrlSecurized +" bsrURI "+ uriendpointApplication+" non Ha un formato valido", log);
							pounEndpoint=proxyendpointApplication;
							urlNotValid=true;
						}							
						
						if (interfaceType.equalsIgnoreCase("SOAP")) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine PDEV1", log);

							envelope = envelopes.createSoapEndpointXMLDAta(pounEndpoint, applicationTimeout,
									applicationFlagISPHeader, "Application", "", null, sicurezza);
							Reps0WSRR.logMe(">>>>>>WSRRoutine PDEV2", log);
						}

						if (interfaceType.equalsIgnoreCase("REST")) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine PDEV3", log);
							envelope = envelopes.createRestEndpointXMLDAta(pounEndpoint, applicationTimeout,
									"Application", "", null, sicurezza, applicationespostoComeAPI);
							Reps0WSRR.logMe(">>>>>>WSRRoutine PDEV4", log);
						}

						if (interfaceType.equalsIgnoreCase("CALLABLE")) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine PDEV5", log);

							envelope = envelopes.createCallableEndpointXMLDAta(pounEndpoint,
									applicationTimeout, "Application", "", null, sicurezza);
							Reps0WSRR.logMe(">>>>>>WSRRoutine PDEV6", log);
						}
						
						bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);
						
						if (bsrURI !=null) {
							result = Reps0WSRR.updateSinglePropertyJSONFormat(bsrURI, "sm63_SPECIALIZZAZIONE",
									"POUN", registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine PDEV7", log);
						}
						result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI,
								registry, user, password);
						Reps0WSRR.logMe(">>>>>>WSRRoutine PDEV8", log);
					}
					
				}

				if (result) {
					Reps0WSRR.logMe(">>>>>>WSRRoutine P9", log);

					if (uriendpointSystemTest != null) {
						Reps0WSRR.logMe(">>>>>>WSRRoutine P10", log);

						result = Reps0WSRR.updateSinglePropertyJSONFormat(uriendpointSystemTest, "name",
								systemTestUrlSecurized, registry, user, password);
						Reps0WSRR.logMe(">>>>>>WSRRoutine P11", log);

						if (result) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P12", log);
							if (noteUser != null && noteUser.length() != 0 && uriproxySystemTest !=null) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P13", log);
								result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxySystemTest,
										"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P4", log);
							}

							// 29052017
							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P15", log);
								//170118 sm63_Timeout NON va aggiornato x uriproxy
								/**
								result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxySystemTest, "sm63_Timeout",
										systemTestTimeout, registry, user, password);
								**/
								Reps0WSRR.logMe(">>>>>>WSRRoutine P16", log);
							}

							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P17", log);

								if (noteDP != null && noteDP.length() != 0 && uriproxySystemTest !=null) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P18", log);
									result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxySystemTest, "sm63_NOTE",
											noteDP, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P19", log);
								}

							}

							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P20", log);
								if (noteError != null && noteError.length() != 0 && uriproxySystemTest !=null) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P21", log);
									result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxySystemTest,
											"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P22", log);
								}

							}

						}

					} else {

						String envelope = null;

						Reps0WSRR.logMe(">>>>>>WSRRoutine P23", log);
						if (interfaceType.equalsIgnoreCase("SOAP")) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P24", log);

							envelope = envelopes.createSoapEndpointXMLDAta(systemTestUrlSecurized, systemTestTimeout,
									systemTestFlagISPHeader, "SystemTest", "", null, sicurezza);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P25", log);
						}

						if (interfaceType.equalsIgnoreCase("REST")) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P26", log);
							envelope = envelopes.createRestEndpointXMLDAta(systemTestUrlSecurized, systemTestTimeout,
									"SystemTest", "", null, sicurezza, systemTestespostoComeAPI);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P27", log);
						}

						if (interfaceType.equalsIgnoreCase("CALLABLE")) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P28", log);

							envelope = envelopes.createCallableEndpointXMLDAta(systemTestUrlSecurized,
									systemTestTimeout, "SystemTest", "", null, sicurezza);//2812 sostituito "sicurezza" con sicurezza
							Reps0WSRR.logMe(">>>>>>WSRRoutine P29", log);
						}
						Reps0WSRR.logMe(">>>>>>WSRRoutine P30", log);
						bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);
						Reps0WSRR.logMe(">>>>>>WSRRoutine P31", log);
						if (bsrURI == null) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P32 " + result, log);
							result = false;
						} else {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P33 " + bsrURI, log);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P33 bis " + bsrURISLD, log);
							result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI,
									registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P34", log);
						}

					}

					bsrURI = null;

					if (result) {
						Reps0WSRR.logMe(">>>>>>WSRRoutine P35", log);
						if (uriendpointProduction != null) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P36", log);
							result = Reps0WSRR.updateSinglePropertyJSONFormat(uriendpointProduction, "name",
									productionUrlSecurized, registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P37", log);
							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P38", log);
								if (noteUser != null && noteUser.length() != 0 && uriproxyProduction !=null) {
									result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyProduction,
											"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P39", log);
								}

								// 29052017
								//170118 vedi systemTest
								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P40", log);
									/**
									result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyProduction,
											"sm63_Timeout", productionTimeout, registry, user, password);
									**/
									Reps0WSRR.logMe(">>>>>>WSRRoutine P41", log);
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P42", log);
									if (noteDP != null && noteDP.length() != 0 && uriproxyProduction !=null) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P43", log);
										result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyProduction,
												"sm63_NOTE", noteDP, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P44", log);
									}
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P45", log);
									if (noteError != null && noteError.length() != 0 && uriproxyProduction !=null) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P46", log);

										result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyProduction,
												"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P47", log);
									}

								}

							}

						} else {

							String envelope = null;
							Reps0WSRR.logMe(">>>>>>WSRRoutine P48", log);
							if (interfaceType.equalsIgnoreCase("SOAP")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P49", log);

								envelope = envelopes.createSoapEndpointXMLDAta(productionUrlSecurized,
										productionTimeout, productionFlagISPHeader, "Produzione", "", null, sicurezza);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P50", log);
							}

							if (interfaceType.equalsIgnoreCase("REST")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P51", log);
								envelope = envelopes.createRestEndpointXMLDAta(productionUrlSecurized,
										productionTimeout, "Produzione", "", null, sicurezza, productionespostoComeAPI);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P52", log);
							}

							if (interfaceType.equalsIgnoreCase("CALLABLE")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P53", log);
								envelope = envelopes.createCallableEndpointXMLDAta(productionUrlSecurized,
										productionTimeout, "Produzione", "", null, sicurezza);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P54", log);
							}
							Reps0WSRR.logMe(">>>>>>WSRRoutine P55", log);
							bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P56 " + bsrURI, log);

							if (bsrURI == null) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P57 " + result, log);
								result = false;
							} else {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P58", log);
								result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI,
										registry, user, password);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P59", log);
							}
						}

					}
					//////////////////////////////
					bsrURI = null;

					if (result) {
						Reps0WSRR.logMe(">>>>>>WSRRoutine P60", log);
						if (uriendpointIndependent != null) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P61", log);
							result = Reps0WSRR.updateSinglePropertyJSONFormat(uriendpointIndependent, "name",
									independentSecurized, registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P61", log);
							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P62", log);
								if (noteUser != null && noteUser.length() != 0 && uriproxyIndependent !=null) {
									result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyIndependent,
											"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P63", log);
								}

								// 29052017
								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P64", log);
									//170118 vedi systemTest
									/**
									result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyIndependent,
											"sm63_Timeout", independentTimeout, registry, user, password);
									**/
									Reps0WSRR.logMe(">>>>>>WSRRoutine P65", log);
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P66", log);
									if (noteDP != null && noteDP.length() != 0 && uriproxyIndependent !=null) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P67", log);

										result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyIndependent,
												"sm63_NOTE", noteDP, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P68", log);
									}
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P69", log);
									if (noteError != null && noteError.length() != 0 && uriproxyIndependent !=null) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P70", log);
										result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyIndependent,
												"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P71", log);
									}

								}

							}

						} else {
							if (independentSecurized != null && independentSecurized.length() !=0) { // creo
								// solo
								// se
								// ho
								// inserito
								// url
								// securizzata
								Reps0WSRR.logMe(">>>>>>WSRRoutine P72"+independentSecurized+"*", log);
								String envelope = null;

								if (interfaceType.equalsIgnoreCase("SOAP")) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P73", log);

									envelope = envelopes.createSoapEndpointXMLDAta(independentSecurized,
											independentTimeout, independentFlagISPHeader, "IndipendentTest", "", null,
											sicurezza);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P74", log);
								}

								if (interfaceType.equalsIgnoreCase("REST")) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P75", log);

									envelope = envelopes.createRestEndpointXMLDAta(independentSecurized,
											independentTimeout, "IndipendentTest", "", null, sicurezza,
											independentespostoComeAPI);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P76", log);
								}

								if (interfaceType.equalsIgnoreCase("CALLABLE")) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P77", log);
									envelope = envelopes.createCallableEndpointXMLDAta(independentSecurized,
											independentTimeout, "IndipendentTest", "", null, sicurezza);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P78", log);
								}
								Reps0WSRR.logMe(">>>>>>WSRRoutine P79", log);
								bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user,
										password);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P80 " + bsrURI, log);

								if (bsrURI == null) {
									result = false;
									Reps0WSRR.logMe(">>>>>>WSRRoutine P81 " + result, log);
								}

								else {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P82", log);
									result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints",
											bsrURI, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P83", log);

								}

							}
						}

					}
					//////////////////////////////
					bsrURI = null;

					if (result) {
						Reps0WSRR.logMe(">>>>>>WSRRoutine P85", log);

						if (uriendpointUserAcceptance != null) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P86", log);

							result = Reps0WSRR.updateSinglePropertyJSONFormat(uriendpointUserAcceptance, "name",
									userAcceptanceUrlSecurized, registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P87", log);
							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P88", log);
								if (noteUser != null && noteUser.length() != 0 && uriproxyUserAcceptance !=null) {
									result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
											"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P89", log);
								}

								// 29052017
								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P90", log);
									//170118 vedi systemTest
									/*
									result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
											"sm63_Timeout", userAcceptanceTimeout, registry, user, password);
									*/
									Reps0WSRR.logMe(">>>>>>WSRRoutine P91", log);
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P92", log);
									if (noteDP != null && noteDP.length() != 0 && uriproxyUserAcceptance !=null) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P93", log);

										result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
												"sm63_NOTE", noteDP, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P94", log);
									}
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P95", log);
									if (noteError != null && noteError.length() != 0 && uriproxyUserAcceptance !=null) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P96", log);
										result = Reps0WSRR.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
												"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P97", log);
									}

								}

							}

						} else {
							if (userAcceptanceUrlSecurized != null && userAcceptanceUrlSecurized.length() != 0) { // creo
								// solo
								// se
								// ho
								// inserito
								// url
								// securizzata
								String envelope = null;
								Reps0WSRR.logMe(">>>>>>WSRRoutine P99 "+userAcceptanceUrlSecurized+"*", log);

								if (interfaceType.equalsIgnoreCase("SOAP")) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P99", log);
									envelope = envelopes.createSoapEndpointXMLDAta(userAcceptanceUrlSecurized,
											userAcceptanceTimeout, userAcceptanceFlagISPHeader, "UserAcceptanceTest",
											"", null, sicurezza);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P100", log);
								}
								Reps0WSRR.logMe(">>>>>>WSRRoutine P101", log);
								if (interfaceType.equalsIgnoreCase("REST")) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P102", log);
									envelope = envelopes.createRestEndpointXMLDAta(userAcceptanceUrlSecurized,
											userAcceptanceTimeout, "UserAcceptanceTest", "", null, sicurezza,
											userAcceptanceespostoComeAPI);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P103", log);
								}

								if (interfaceType.equalsIgnoreCase("CALLABLE")) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P104", log);
									envelope = envelopes.createCallableEndpointXMLDAta(userAcceptanceUrlSecurized,
											userAcceptanceTimeout, "UserAcceptanceTest", "", null, sicurezza);//2812 sostituito "sicurezza" con sicurezza
									Reps0WSRR.logMe(">>>>>>WSRRoutine P105", log);
								}
								Reps0WSRR.logMe(">>>>>>WSRRoutine P106", log);
								bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user,
										password);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P107 " + bsrURI, log);

								if (bsrURI == null) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P108 " + false, log);
									result = false;
								} else {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P109", log);
									result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints",
											bsrURI, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P110", log);
								}

							}
						}

					}
					//////////////////////////////
				}

			}
		} catch (Exception ex) {
			result = true;
			Reps0WSRR.logMe(">>>>>>WSRRoutine P111 " + false, log);
			Reps0WSRR.logMe(">>>>>>WSRRoutine P112 " + ex.getMessage(), log);
			Reps0WSRR.logMe(">>>>>>WSRRoutine P113 " + ex.getStackTrace().toString(), log);
		}

		if (urlNotValid==true) {
		Reps0WSRR.logMe(">>>>>>WSRRoutine P200 " + result, log);
		return true;
		}else Reps0WSRR.logMe(">>>>>>WSRRoutine P200 " + !result, log);
		return !result;

	}

	private static void logMe(String data, boolean log) {

		if (log) {

			// System.out.println(">>>>>WSRRoutine@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println(data);
			// System.out.println("<<<<<WSRRoutine@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

		}
	}
    //
	public static boolean updateSinglePropertyJSONFormat(String bsrURIToChange, String propertyName,
			String propertyValue, String createURL, String user, String password) {

		// Create the variable to return
		boolean result = false;

		String query = "/Metadata/JSON/%BSRURI%/properties/%PROPERTYNAME%";
		//System.out.println("ERROREREPS0Updateinfo200! " + bsrURIToChange + " -  " + propertyName + "  -  "
		//		+ propertyValue + " -  " + createURL + "  -  " + user + "  -  " + "eiiij" + password + "*deewww");
		String value = "{\"value\":\"%VALUE%\"}";
		if (bsrURIToChange == null || bsrURIToChange.length() == 0)
			bsrURIToChange = "bsrURI_not_Specified";
		if (propertyName == null || propertyName.length() == 0)
			propertyName = "propertyName_not_Specified";

		query = query.replaceAll("%BSRURI%", bsrURIToChange);
		query = query.replaceAll("%PROPERTYNAME%", propertyName);
		value = value.replaceAll("%VALUE%", propertyValue);

		HttpURLConnection urlConnection = null;
		StringBuffer sb = new StringBuffer();
		sb.append(createURL).append(query);
		//System.out.println("ERROREREPS0Updateinfo300! " + sb.toString());
		//System.out.println("ERROREREPS0Updateinfo350! " + value);
		try {
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("PUT");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);

			}

			byte[] postDataBytes = value.getBytes("UTF-8");
			urlConnection.getOutputStream().write(postDataBytes);

			int returnCode = urlConnection.getResponseCode();

			if (returnCode == 200 || (returnCode == 201)) {

				//System.out.println(
				//		"ERROREREPS0Updateinfo200 : " + bsrURIToChange + " -  " + propertyName + " - " + propertyValue);
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				result = true;
				is.close();

			} else {
				//System.out.println("ERROREREPS0Update  : " + returnCode + " - " + bsrURIToChange + " -  " + propertyName
				//		+ " - " + propertyValue);
				// BufferedReader reader = new BufferedReader(new
				// InputStreamReader(urlConnection.getInputStream()));
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				//System.out.println("ERROREREPS0Update  Errore " + stringBuffer.toString());
				reader.close();
				throw new Exception("Unable to update WSRR GenericObject " + stringBuffer.toString());
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("ERROREREPS0Updateinfo600! " + e.toString());

		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return result;
	}
	
	private static String trasformURL(String url) {

    	String urldev=null;
    	
        URL aURL;
		try {
			aURL = new URL(url);
		} catch (MalformedURLException e) {
          return "ERRORE";
		}
    	
        String port="";
        if (aURL.getPort()!=-1) port=":"+aURL.getPort();
        
       urldev=aURL.getProtocol()+"://"+aURL.getHost()+port+"/dev"+aURL.getFile();
		
    	return urldev;
    }
}
