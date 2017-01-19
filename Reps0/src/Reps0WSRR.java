import com.isp.wsrr.utility.WSRRUtility;

import teamworks.TWList;

public class Reps0WSRR {

	// 150117 prima scrittura
	
	// 180117 versione testata ok
	
	public Reps0WSRR() {
		
		//notes
	}
	
	public boolean updateEndPointAndWSProxyData(String bsrURISLD,String interfaceType, TWList notes,
			TWList endPointbsrURI, TWList endpontProxybsrURI, TWList securizedUrls, String registry, String user,
			String password) {



		
		WSRRUtility wsrrutility = new WSRRUtility();
		WSDLLoaderBPM envelopes = new WSDLLoaderBPM();

		String uriendpointApplication = (String) endPointbsrURI.getArrayData(0);
		String uriendpointSystemTest = (String) endPointbsrURI.getArrayData(1);
		String uriendpointProduction = (String) endPointbsrURI.getArrayData(2);

		String uriproxyApplication = (String) endpontProxybsrURI.getArrayData(0);
		String uriproxySystemTest = (String) endpontProxybsrURI.getArrayData(1);
		String uriproxyProduction = (String) endpontProxybsrURI.getArrayData(2);

		String applicationUrlSecurized = (String) securizedUrls.getArrayData(0);
		String systemTestUrlSecurized = (String) securizedUrls.getArrayData(1);
		String productionUrlSecurized = (String) securizedUrls.getArrayData(2);

		String noteUser = (String) notes.getArrayData(0);
		String noteDP = (String) notes.getArrayData(1);
		String noteError = (String) notes.getArrayData(2);
		
		Boolean result = false;
		String bsrURI = null;

		try {
			
		// rename url application with securized application URL
		result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointApplication, "name", applicationUrlSecurized,
				registry, user, password);

		if (result) {

			if (noteUser != null && noteUser.length() != 0) {
				// update application EndPointProxy sm63_NOTE_GEN_WSPROXY
				result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyApplication, "sm63_NOTE_GEN_WSPROXY",
						noteUser, registry, user, password);
			}

			if (result) {

				if (noteDP != null && noteDP.length() != 0) {
					// update application EndPointProxy sm63_NOTE
					result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyApplication, "sm63_NOTE", noteDP,
							registry, user, password);
				}

			}

			if (result) {

				if (noteError != null && noteError.length() != 0) {
					// update application EndPointProxy
					// sm63_ERRORE_GENERAZIONE_WSPROXY
					result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyApplication,
							"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
				}

			}

			if (result) {

				// check if systemTest enpoint is present

				if (uriendpointSystemTest != null) {

					// rename url sysytem with securized system URL
					result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointSystemTest, "name",
							systemTestUrlSecurized, registry, user, password);

					if (result) {

						if (noteUser != null && noteUser.length() != 0) {
							// update System Test EndPointProxy
							// sm63_NOTE_GEN_WSPROXY
							result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest,
									"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
						}

						if (result) {

							if (noteDP != null && noteDP.length() != 0) {
								// update System Test EndPointProxy sm63_NOTE
								result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest, "sm63_NOTE",
										noteDP, registry, user, password);
							}

						}

						if (result) {

							if (noteError != null && noteError.length() != 0) {
								// update System Test EndPointProxy
								// sm63_ERRORE_GENERAZIONE_WSPROXY
								result = wsrrutility.updateSinglePropertyJSONFormat(uriproxySystemTest,
										"sm63_ERRORE_GENERAZIONE_WSPROXY", noteError, registry, user, password);
							}

						}

					}

				} else {

					// create endpoint systemTest Only

					String envelope = null;

					if (interfaceType.equalsIgnoreCase("SOAP")) {

						envelope = envelopes.createSoapEndpointXMLDAta(systemTestUrlSecurized, "10", "NO", "SystemTest",
								"", null, "SI-Datapower");
					}

					if (interfaceType.equalsIgnoreCase("REST")) {

						envelope = envelopes.createRestEndpointXMLDAta(systemTestUrlSecurized, "10", "SystemTest", "",
								null, "SI-Datapower");
					}

					if (interfaceType.equalsIgnoreCase("CALLABLE")) {

						envelope = envelopes.createCallableEndpointXMLDAta(systemTestUrlSecurized, "10", "SystemTest",
								"", null, "SI-Datapower");
					}

					bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);
					
				}

				if (bsrURI != null) {
					
					result=wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI, registry, user, password);
					

					bsrURI = null;

					if (uriendpointProduction != null && result) {

						// rename url sysytem with securized system URL
						result = wsrrutility.updateSinglePropertyJSONFormat(uriendpointProduction, "name",
								productionUrlSecurized, registry, user, password);

						if (result) {

							if (noteUser != null && noteUser.length() != 0) {
								// update System Test EndPointProxy
								// sm63_NOTE_GEN_WSPROXY
								result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction,
										"sm63_NOTE_GEN_WSPROXY", noteUser, registry, user, password);
							}

							if (result) {

								if (noteDP != null && noteDP.length() != 0) {
									// update System Test EndPointProxy
									// sm63_NOTE
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction, "sm63_NOTE",
											noteDP, registry, user, password);
								}
							}

							if (result) {

								if (noteError != null && noteError.length() != 0) {
									// update System Test EndPointProxy
									// sm63_ERRORE_GENERAZIONE_WSPROXY
									result = wsrrutility.updateSinglePropertyJSONFormat(uriproxyProduction,
											"sm63_ERRORE_GENERAZIONE_WSPROXY", noteDP, registry, user, password);
								}

							}

						}

					} else {

						// create endpoint systemTest Only

						String envelope = null;

						if (interfaceType.equalsIgnoreCase("SOAP")) {

							envelope = envelopes.createSoapEndpointXMLDAta(productionUrlSecurized, "10", "NO",
									"Produzione", "", null, "SI-Datapower");
						}

						if (interfaceType.equalsIgnoreCase("REST")) {

							envelope = envelopes.createRestEndpointXMLDAta(productionUrlSecurized, "10", "Produzione",
									"", null, "SI-Datapower");
						}

						if (interfaceType.equalsIgnoreCase("CALLABLE")) {

							envelope = envelopes.createCallableEndpointXMLDAta(productionUrlSecurized, "10",
									"Produzione", "", null, "SI-Datapower");
						}

						bsrURI = wsrrutility.createWSRRGenericObject(envelope, "POST", registry, user, password);

						if (bsrURI == null)
							result = false;
						else {
							result=wsrrutility.updateRelationShip(bsrURISLD, "gep63_availableEndpoints", bsrURI, registry, user, password);
						}
					}

				} else
					result = false;

			}

		}
		}
		catch(Exception ex ){
			result=false; //error
		}

		return result;

	}

}
