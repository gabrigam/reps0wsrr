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
			TWList endPointbsrURI, TWList endpontProxybsrURI, TWList securizedUrls, TWList flagISPHeader,TWList timeout,String sicurezza,String registry, String user,
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
		
		boolean log=true;
		
		Reps0WSRR.logMe(">>>>>>WSRRoutine parametri", log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+bsrURISLD, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+uriendpointApplication, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+uriendpointProduction, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+uriendpointIndependent, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+uriendpointUserAcceptance, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+uriendpointSystemTest, log);
		
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+uriproxyApplication, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+uriproxySystemTest, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+uriproxyProduction, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+uriproxyIndependent, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+uriproxyUserAcceptance, log);
		
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+applicationUrlSecurized, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+systemTestUrlSecurized, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+productionUrlSecurized, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+independentSecurized, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+userAcceptanceUrlSecurized, log);		

		Reps0WSRR.logMe(">>>>>>WSRRoutine "+applicationFlagISPHeader, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+systemTestFlagISPHeader, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+productionFlagISPHeader, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+independentFlagISPHeader, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+userAcceptanceFlagISPHeader, log);		
		
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+applicationTimeout, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+systemTestTimeout, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+productionTimeout, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+independentTimeout, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+userAcceptanceTimeout, log);	

		Reps0WSRR.logMe(">>>>>>WSRRoutine "+noteUser, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+noteDP, log);
		Reps0WSRR.logMe(">>>>>>WSRRoutine "+noteError, log);
		
		try {
			Reps0WSRR.logMe(">>>>>>WSRRoutine P1", log);
			result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointApplication, "name", applicationUrlSecurized,
					registry, user, password);
			Reps0WSRR.logMe(">>>>>>WSRRoutine P2", log);
			if (result) {
				Reps0WSRR.logMe(">>>>>>WSRRoutine P3", log);
				if (noteUser != null && noteUser.length() != 0) {
					result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyApplication, "sm63_NOTE_GEN_WSPROXY",
							noteUser, registry, user, password);
					Reps0WSRR.logMe(">>>>>>WSRRoutine P4", log);
				}

				if (result) {
					Reps0WSRR.logMe(">>>>>>WSRRoutine P5", log);
					if (noteDP != null && noteDP.length() != 0) {
						result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyApplication, "sm63_NOTE", noteDP,
								registry, user, password);
						Reps0WSRR.logMe(">>>>>>WSRRoutine P6", log);
					}

				}

				if (result) {
					Reps0WSRR.logMe(">>>>>>WSRRoutine P7", log);
					if (noteError != null && noteError.length() != 0) {
						result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyApplication,
								"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
						Reps0WSRR.logMe(">>>>>>WSRRoutine P8", log);
					}

				}

				if (result) {
					Reps0WSRR.logMe(">>>>>>WSRRoutine P9", log);

					if (uriendpointSystemTest != null) {
						Reps0WSRR.logMe(">>>>>>WSRRoutine P10", log);

						result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointSystemTest, "name",
								systemTestUrlSecurized, registry, user, password);
						Reps0WSRR.logMe(">>>>>>WSRRoutine P11", log);

						if (result) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P12", log);
							if (noteUser != null && noteUser.length() != 0) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P13", log);
								result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest,
										"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P4", log);
							}
							
							//29052017
							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P15", log);
								result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest,
										"sm63_Timeout", systemTestTimeout, registry, user, password);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P16", log);
							}
							
							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P17", log);

								if (noteDP != null && noteDP.length() != 0) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P18", log);
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest, "sm63_NOTE",
											noteDP, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P19", log);
								}

							}

							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P20", log);
								if (noteError != null && noteError.length() != 0) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P21", log);
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest,
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

							envelope = envelopes.createSoapEndpointXMLDAta(systemTestUrlSecurized, systemTestTimeout, systemTestFlagISPHeader,
									"SystemTest", "", null, sicurezza);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P25", log);
						}

						if (interfaceType.equalsIgnoreCase("REST")) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P26", log);
							envelope = envelopes.createRestEndpointXMLDAta(systemTestUrlSecurized,systemTestTimeout, "SystemTest",
									"", null, sicurezza);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P27", log);
						}

						if (interfaceType.equalsIgnoreCase("CALLABLE")) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P28", log);

							envelope = envelopes.createCallableEndpointXMLDAta(systemTestUrlSecurized, systemTestTimeout,
									"SystemTest", "", null, "sicurezza");
							Reps0WSRR.logMe(">>>>>>WSRRoutine P29", log);
						}
						Reps0WSRR.logMe(">>>>>>WSRRoutine P30", log);
						bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);
						Reps0WSRR.logMe(">>>>>>WSRRoutine P31", log);
						if (bsrURI==null) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P32 "+result, log);
							result=false;
						} else {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P33 "+bsrURI, log);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P33 bis "+bsrURISLD, log);
							result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI, registry,
									user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P34", log);
						}

					}

					bsrURI = null;

					if (result) {
						Reps0WSRR.logMe(">>>>>>WSRRoutine P35", log);
						if (uriendpointProduction != null) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P36", log);
							result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointProduction, "name",
									productionUrlSecurized, registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P37", log);
							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P38", log);
								if (noteUser != null && noteUser.length() != 0) {
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction,
											"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P39", log);
								}
								
								//29052017
								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P40", log);
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction,
											"sm63_Timeout", productionTimeout, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P41", log);
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P42", log);
									if (noteDP != null && noteDP.length() != 0) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P43", log);
										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction,
												"sm63_NOTE", noteDP, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P44", log);
									}
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P45", log);
									if (noteError != null && noteError.length() != 0) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P46", log);

										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction,
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

								envelope = envelopes.createSoapEndpointXMLDAta(productionUrlSecurized, productionTimeout, productionFlagISPHeader,
										"Produzione", "", null, sicurezza);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P50", log);
							}

							if (interfaceType.equalsIgnoreCase("REST")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P51", log);
								envelope = envelopes.createRestEndpointXMLDAta(productionUrlSecurized, productionTimeout,
										"Produzione", "", null, sicurezza);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P52", log);
							}

							if (interfaceType.equalsIgnoreCase("CALLABLE")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P53", log);
								envelope = envelopes.createCallableEndpointXMLDAta(productionUrlSecurized, productionTimeout,
										"Produzione", "", null, sicurezza);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P54", log);
							}
							Reps0WSRR.logMe(">>>>>>WSRRoutine P55", log);
							bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P56 "+bsrURI, log);

							if (bsrURI == null){
								Reps0WSRR.logMe(">>>>>>WSRRoutine P57 "+result, log);
								result = false;
							}
							else {
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
							result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointIndependent, "name",
									independentSecurized, registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P61", log);
							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P62", log);
								if (noteUser != null && noteUser.length() != 0) {
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyIndependent,
											"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P63", log);
								}

								//29052017
								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P64", log);
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyIndependent,
											"sm63_Timeout", independentTimeout, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P65", log);
								}
								
								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P66", log);
									if (noteDP != null && noteDP.length() != 0) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P67", log);

										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyIndependent,
												"sm63_NOTE", noteDP, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P68", log);
									}
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P69", log);
									if (noteError != null && noteError.length() != 0) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P70", log);
										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyIndependent,
												"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P71", log);
									}

								}

							}

						} else {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P72", log);
							String envelope = null;

							if (interfaceType.equalsIgnoreCase("SOAP")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P73", log);

								envelope = envelopes.createSoapEndpointXMLDAta(independentSecurized, independentTimeout, independentFlagISPHeader,
										"IndipendentTest", "", null, sicurezza);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P74", log);
							}

							if (interfaceType.equalsIgnoreCase("REST")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P75", log);

								envelope = envelopes.createRestEndpointXMLDAta(independentSecurized, independentTimeout,
										"IndipendentTest", "", null, sicurezza);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P76", log);
							}

							if (interfaceType.equalsIgnoreCase("CALLABLE")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P77", log);
								envelope = envelopes.createCallableEndpointXMLDAta(independentSecurized, independentTimeout,
										"IndipendentTest", "", null, sicurezza);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P78", log);
							}
							Reps0WSRR.logMe(">>>>>>WSRRoutine P79", log);
							bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P80 "+bsrURI, log);

							if (bsrURI == null){
								result = false;
								Reps0WSRR.logMe(">>>>>>WSRRoutine P81 "+result, log);
							}

							else {	
								Reps0WSRR.logMe(">>>>>>WSRRoutine P82", log);
								result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI,
										registry, user, password);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P83", log);

							}
						}

					}
					//////////////////////////////
					bsrURI = null;

					if (result) {
						Reps0WSRR.logMe(">>>>>>WSRRoutine P85", log);

						if (uriendpointUserAcceptance != null) {
							Reps0WSRR.logMe(">>>>>>WSRRoutine P86", log);

							result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointUserAcceptance, "name",
									userAcceptanceUrlSecurized, registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P87", log);
							if (result) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P88", log);
								if (noteUser != null && noteUser.length() != 0) {
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
											"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P89", log);
								}
								
								//29052017
								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P90", log);
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
											"sm63_Timeout", userAcceptanceTimeout, registry, user, password);
									Reps0WSRR.logMe(">>>>>>WSRRoutine P91", log);
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P92", log);
									if (noteDP != null && noteDP.length() != 0) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P93", log);

										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
												"sm63_NOTE", noteDP, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P94", log);
									}
								}

								if (result) {
									Reps0WSRR.logMe(">>>>>>WSRRoutine P95", log);
									if (noteError != null && noteError.length() != 0) {
										Reps0WSRR.logMe(">>>>>>WSRRoutine P96", log);
										result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyUserAcceptance,
												"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
										Reps0WSRR.logMe(">>>>>>WSRRoutine P97", log);
									}

								}

							}

						} else {

							String envelope = null;
							Reps0WSRR.logMe(">>>>>>WSRRoutine P99", log);

							if (interfaceType.equalsIgnoreCase("SOAP")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P99", log);
								envelope = envelopes.createSoapEndpointXMLDAta(userAcceptanceUrlSecurized, userAcceptanceTimeout, userAcceptanceFlagISPHeader,
										"UserAcceptanceTest", "", null, sicurezza);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P100", log);
							}
							Reps0WSRR.logMe(">>>>>>WSRRoutine P101", log);
							if (interfaceType.equalsIgnoreCase("REST")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P102", log);
								envelope = envelopes.createRestEndpointXMLDAta(userAcceptanceUrlSecurized, userAcceptanceTimeout,
										"UserAcceptanceTest", "", null, sicurezza);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P103", log);
							}

							if (interfaceType.equalsIgnoreCase("CALLABLE")) {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P104", log);
								envelope = envelopes.createCallableEndpointXMLDAta(userAcceptanceUrlSecurized, userAcceptanceTimeout,
										"UserAcceptanceTest", "", null, "sicurezza");
								Reps0WSRR.logMe(">>>>>>WSRRoutine P105", log);
							}
							Reps0WSRR.logMe(">>>>>>WSRRoutine P106", log);
							bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);
							Reps0WSRR.logMe(">>>>>>WSRRoutine P107 "+bsrURI, log);

							if (bsrURI == null){
								Reps0WSRR.logMe(">>>>>>WSRRoutine P108 "+false, log);
								result = false;
							}
							else {
								Reps0WSRR.logMe(">>>>>>WSRRoutine P109", log);
								result = wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI,
										registry, user, password);
								Reps0WSRR.logMe(">>>>>>WSRRoutine P110", log);
							}
						}

					}
					//////////////////////////////
				}

			}
		} catch (Exception ex) {
			result = false;
			Reps0WSRR.logMe(">>>>>>WSRRoutine P111 "+false, log);
			Reps0WSRR.logMe(">>>>>>WSRRoutine P112 "+ex.getMessage(), log);
			Reps0WSRR.logMe(">>>>>>WSRRoutine P113 "+ex.getStackTrace().toString(),log);
		}

		Reps0WSRR.logMe(">>>>>>WSRRoutine P200 "+result, log);
		return result;

	}
	private static void logMe(String data,boolean log) {
		
		if (log) {
			
			//System.out.println(">>>>>WSRRoutine@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println(data);
			//System.out.println("<<<<<WSRRoutine@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			
		}
	}
}
