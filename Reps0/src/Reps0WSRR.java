import com.isp.wsrr.utility.WSRRUtility;

import teamworks.TWList;

public class Reps0WSRR {

	// 150117 prima scrittura

	// 180117 versione testata ok
	
	// 11052017 nella creazione degli endpoint sostituito "NO" con "N" (si tratta del flag header) 
	
	// 21052017 inserita gestione flag ispheader
	
	// 27052017 inserita gestione timeout in creazione
	
	// 29052017 inserita gestione timout in modifica (ma serve?)
	
	// 30052017 modifica settaggio timeout

	public Reps0WSRR() {

		// notes 
	}

	public boolean updateEndPointAndWSProxyData(String bsrURISLD, String interfaceType, TWList notes,
			TWList endPointbsrURI, TWList endpontProxybsrURI, TWList securizedUrls, TWList flagISPHeader,TWList timeout,String registry, String user,
			String password) {

		WSRRUtility wsrrutility = new WSRRUtility();
		WSDLLoaderBPM envelopes = new WSDLLoaderBPM();

		String uriendpointApplication = (String) endPointbsrURI.getArrayData(0);
		String uriendpointSystemTest = (String) endPointbsrURI.getArrayData(1);
		String uriendpointProduction = (String) endPointbsrURI.getArrayData(2);
		String uriendpointIndependent= (String) endPointbsrURI.getArrayData(3);
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
		String systemTestTimeout  = (String) timeout.getArrayData(1);
		String productionTimeout  = (String) timeout.getArrayData(2);
		String independentTimeout = (String) timeout.getArrayData(3);
		String userAcceptanceTimeout  = (String) timeout.getArrayData(4);
				
		if (uriendpointSystemTest == null ) systemTestFlagISPHeader=applicationFlagISPHeader;
		if (uriendpointProduction == null ) productionFlagISPHeader=applicationFlagISPHeader;
		if (uriendpointIndependent == null ) independentFlagISPHeader=applicationFlagISPHeader;
		if (uriendpointUserAcceptance == null ) userAcceptanceFlagISPHeader=applicationFlagISPHeader;
		
		//30052017
		if (uriendpointSystemTest == null ) systemTestTimeout=applicationTimeout;
		if (uriendpointProduction == null ) productionTimeout=applicationTimeout;
		if (uriendpointIndependent == null ) independentTimeout=applicationTimeout;
		if (uriendpointUserAcceptance == null ) userAcceptanceTimeout=applicationTimeout;

		String noteUser = (String) notes.getArrayData(0);
		String noteDP = (String) notes.getArrayData(1);
		String noteError = (String) notes.getArrayData(2);

		Boolean result = false;
		String bsrURI = null;

		try {

			result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointApplication, "name", applicationUrlSecurized,
					registry, user, password);

			if (result) {

				if (noteUser != null && noteUser.length() != 0) {
					result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyApplication, "sm63_NOTE_GEN_WSPROXY",
							noteUser, registry, user, password);
				}

				if (result) {

					if (noteDP != null && noteDP.length() != 0) {
						result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyApplication, "sm63_NOTE", noteDP,
								registry, user, password);
					}

				}

				if (result) {

					if (noteError != null && noteError.length() != 0) {
						result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyApplication,
								"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
					}

				}

				if (result) {

					if (uriendpointSystemTest != null) {

						result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointSystemTest, "name",
								systemTestUrlSecurized, registry, user, password);

						if (result) {

							if (noteUser != null && noteUser.length() != 0) {

								result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest,
										"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
							}
							
							//29052017
							if (result) {

								result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest,
										"sm63_Timeout", systemTestTimeout, registry, user, password);
							}
							
							if (result) {

								if (noteDP != null && noteDP.length() != 0) {

									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest, "sm63_NOTE",
											noteDP, registry, user, password);
								}

							}

							if (result) {

								if (noteError != null && noteError.length() != 0) {

									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest,
											"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
								}

							}

						}

					} else {

						String envelope = null;

						if (interfaceType.equalsIgnoreCase("SOAP")) {

							envelope = envelopes.createSoapEndpointXMLDAta(systemTestUrlSecurized, systemTestTimeout, systemTestFlagISPHeader,
									"SystemTest", "", null, "SI-Datapower");
						}

						if (interfaceType.equalsIgnoreCase("REST")) {

							envelope = envelopes.createRestEndpointXMLDAta(systemTestUrlSecurized,systemTestTimeout, "SystemTest",
									"", null, "SI-Datapower");
						}

						if (interfaceType.equalsIgnoreCase("CALLABLE")) {

							envelope = envelopes.createCallableEndpointXMLDAta(systemTestUrlSecurized, systemTestTimeout,
									"SystemTest", "", null, "SI-Datapower");
						}

						bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);
						
						if (bsrURI==null) {
							result=false;
						} else {
							
							result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI, registry,
									user, password);
						}

					}

					bsrURI = null;

					if (result) {

						if (uriendpointProduction != null) {

							result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointProduction, "name",
									productionUrlSecurized, registry, user, password);

							if (result) {

								if (noteUser != null && noteUser.length() != 0) {
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction,
											"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
								}
								
								//29052017
								if (result) {

									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction,
											"sm63_Timeout", productionTimeout, registry, user, password);
								}

								if (result) {

									if (noteDP != null && noteDP.length() != 0) {

										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction,
												"sm63_NOTE", noteDP, registry, user, password);
									}
								}

								if (result) {

									if (noteError != null && noteError.length() != 0) {

										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction,
												"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
									}

								}

							}

						} else {

							String envelope = null;

							if (interfaceType.equalsIgnoreCase("SOAP")) {

								envelope = envelopes.createSoapEndpointXMLDAta(productionUrlSecurized, productionTimeout, productionFlagISPHeader,
										"Produzione", "", null, "SI-Datapower");
							}

							if (interfaceType.equalsIgnoreCase("REST")) {

								envelope = envelopes.createRestEndpointXMLDAta(productionUrlSecurized, productionTimeout,
										"Produzione", "", null, "SI-Datapower");
							}

							if (interfaceType.equalsIgnoreCase("CALLABLE")) {

								envelope = envelopes.createCallableEndpointXMLDAta(productionUrlSecurized, productionTimeout,
										"Produzione", "", null, "SI-Datapower");
							}

							bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);

							if (bsrURI == null)
								result = false;
							else {
								result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI,
										registry, user, password);
							}
						}

					}
                    //////////////////////////////
					bsrURI = null;

					if (result) {

						if (uriendpointIndependent != null) {

							result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointIndependent, "name",
									independentSecurized, registry, user, password);

							if (result) {

								if (noteUser != null && noteUser.length() != 0) {
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyIndependent,
											"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
								}

								//29052017
								if (result) {

									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyIndependent,
											"sm63_Timeout", independentTimeout, registry, user, password);
								}
								
								if (result) {

									if (noteDP != null && noteDP.length() != 0) {

										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyIndependent,
												"sm63_NOTE", noteDP, registry, user, password);
									}
								}

								if (result) {

									if (noteError != null && noteError.length() != 0) {

										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyIndependent,
												"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
									}

								}

							}

						} else {

							String envelope = null;

							if (interfaceType.equalsIgnoreCase("SOAP")) {

								envelope = envelopes.createSoapEndpointXMLDAta(independentSecurized, independentTimeout, independentFlagISPHeader,
										"IndipendentTest", "", null, "SI-Datapower");
							}

							if (interfaceType.equalsIgnoreCase("REST")) {

								envelope = envelopes.createRestEndpointXMLDAta(independentSecurized, independentTimeout,
										"IndipendentTest", "", null, "SI-Datapower");
							}

							if (interfaceType.equalsIgnoreCase("CALLABLE")) {

								envelope = envelopes.createCallableEndpointXMLDAta(independentSecurized, independentTimeout,
										"IndipendentTest", "", null, "SI-Datapower");
							}

							bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);

							if (bsrURI == null)
								result = false;
							else {
								result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI,
										registry, user, password);
							}
						}

					}
					//////////////////////////////
					bsrURI = null;

					if (result) {

						if (uriendpointUserAcceptance != null) {

							result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointUserAcceptance, "name",
									userAcceptanceUrlSecurized, registry, user, password);

							if (result) {

								if (noteUser != null && noteUser.length() != 0) {
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
											"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
								}
								
								//29052017
								if (result) {

									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
											"sm63_Timeout", userAcceptanceTimeout, registry, user, password);
								}

								if (result) {

									if (noteDP != null && noteDP.length() != 0) {

										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
												"sm63_NOTE", noteDP, registry, user, password);
									}
								}

								if (result) {

									if (noteError != null && noteError.length() != 0) {

										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
												"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
									}

								}

							}

						} else {

							String envelope = null;

							if (interfaceType.equalsIgnoreCase("SOAP")) {

								envelope = envelopes.createSoapEndpointXMLDAta(userAcceptanceUrlSecurized, userAcceptanceTimeout, userAcceptanceFlagISPHeader,
										"UserAcceptanceTest", "", null, "SI-Datapower");
							}

							if (interfaceType.equalsIgnoreCase("REST")) {

								envelope = envelopes.createRestEndpointXMLDAta(userAcceptanceUrlSecurized, userAcceptanceTimeout,
										"UserAcceptanceTest", "", null, "SI-Datapower");
							}

							if (interfaceType.equalsIgnoreCase("CALLABLE")) {

								envelope = envelopes.createCallableEndpointXMLDAta(userAcceptanceUrlSecurized, userAcceptanceTimeout,
										"UserAcceptanceTest", "", null, "SI-Datapower");
							}

							bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);

							if (bsrURI == null)
								result = false;
							else {
								result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI,
										registry, user, password);
							}
						}

					}
					//////////////////////////////
				}

			}
		} catch (Exception ex) {
			result = false;
		}

		return result;

	}
	
}
