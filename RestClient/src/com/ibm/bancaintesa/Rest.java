package com.ibm.bancaintesa;
 
/**
 * REST Request
 * Perform a REST service call against a target.
 * Update kolban source..
 * History:
 * 2015-05-06: Added command line processing for testing.
 */

//180717 u0h1963 X-ISPWebServicesHeader and timeout

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.cli.Options;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.ibm.wsspi.security.auth.callback.WSMappingCallbackHandlerFactory;

public class Rest {
	// public static boolean debug = false;

	private static String keyResponseMessage = "responseMessage";
	private static String keycodeMessage = "codeMessage";
	private static String keyErrorMessage = "errorMessage";
	private static String MAPPING_ALIAS = com.ibm.wsspi.security.auth.callback.Constants.MAPPING_ALIAS;
	private static String USER_NAME = "USER_NAME";
	private static String USER_PASSWORD = "USER_PASSWORD";

	@SuppressWarnings("static-access")
	public static void main(String args[]) {

		String command = "GET";
		String url = "http://linuxbpm85:9080/rest/bpm/wle/v1/systems";

		/*
		 * String command = "DELETE"; String url =
		 * "http://linuxbpm85:9080/rest/bpm/wle/v1/process/1?action=delete&parts=all";
		 */
		String contentFile = null;
		String content = null;
		String userid = "bpmadmin";
		String password = "Password01";
		HashMap<String, String> headerMap = new HashMap<String, String>();
		Options options = null;

		String ISPHeader = "<int:ISPWebservicesHeader xmlns:int=\"http://eric.van-der-vlist.com/ns/person\">         <RequestInfo>            <TransactionId>GKBG0_20170913113655000769</TransactionId>            <Timestamp>20170913113655000769</Timestamp>            <ServiceID>GKMGDECPLF</ServiceID>            <ServiceVersion>00</ServiceVersion>              <Language>it</Language>         </RequestInfo>         <OperatorInfo UserID=\"U0G4356\" IsVirtualUser=\"false\"     NotISPUserID=\"\"   />         <CompanyInfo>            <ISPCallerCompanyIDCode>01</ISPCallerCompanyIDCode>            <ISPServiceCompanyIDCode>01</ISPServiceCompanyIDCode>            <ISPBranchCode>00700</ISPBranchCode>            <NotISPCompanyIDCode></NotISPCompanyIDCode>         </CompanyInfo>         <BusinessInfo>            <CustomerID>U0G4356</CustomerID>            <BusinessProcessName>Richiesta Pratica Mogeni</BusinessProcessName>            <BusinessProcessID>76828</BusinessProcessID>            <BusinessOperation></BusinessOperation>            <BusinessFileID></BusinessFileID>         </BusinessInfo>         <TechnicalInfo>            <ChannelIDCode>01</ChannelIDCode>            <ApplicationID>GKBG0</ApplicationID>            <CallerServerName>localhost</CallerServerName>            <CallerProgramName>GKBG0-BPM</CallerProgramName>         </TechnicalInfo>         <AdditionalBusinessInfo>    <Param Name=\"CodABI\" Value=\"01025\"/>  </AdditionalBusinessInfo></int:ISPWebservicesHeader>";

		Rest.ISPHeaderScomposition(ISPHeader, true);

		try {

			System.out.println(doRest(command, url, content, headerMap, userid, password, false, 100, "", false));
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	} // End of main

	public static class RestException extends Exception {

		String codice;
		String messaggio;

		public RestException(String arg0) {
			super(arg0);
		}

		public RestException(String cod, String mess) {

			codice = cod;
			messaggio = mess;

		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}; // End of RestException

	/**
	 * Make a REST call
	 * 
	 * @param command
	 *            The REST command to use. Choices are GET, POST and PUT
	 * @param urlString
	 *            The URL string
	 * @param content
	 *            Content used for POST and PUT
	 * @param headerMap
	 *            Optional HTTP headers
	 * @param userid
	 *            Userid for Basic Auth
	 * @param password
	 *            Password for Basic Auth
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, String> doRest(String command, String urlString, String content,
			HashMap<String, String> headerMap, String userid, String password, boolean debug, int timeout,
			String interfaccia, boolean servizioIIB) throws Exception {

		HashMap<String, String> returnVal = new HashMap<String, String>();

		System.out.println("fxVer 01 Parametri passati :  Interfaccia=" + interfaccia + " ServizioIIB=" + servizioIIB);

		// Check that the command type is known
		if (!command.equals("GET") && !command.equals("POST") && !command.equals("PUT") && !command.equals("DELETE")) {
			throw new RestException("Unsupported command: " + command + ".  Supported commands are GET, POST, PUT");
		}

		{
			try {

				URL url = new URL(urlString);
				HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
				httpUrlConnection.setRequestMethod(command);

				if (timeout > 0) {

					httpUrlConnection.setReadTimeout(timeout * 1000); // timeout
					// is in
					// //
					// milliseconds
				}

				String ispHeader = headerMap.get("X-ISPWebServicesHeader");

				HashMap<String, String> ispHeaderMap = null;

				if (ispHeader != null && !ispHeader.equals("#NOTREQUIRED#")) {

					ispHeaderMap = Rest.ISPHeaderScomposition(ispHeader, debug);

					if (ispHeaderMap == null) {
						System.out.println(
								"###################################################################################################");
						System.out.println("Error ISPHeader : " + ispHeader
								+ " is Malformed or Mandatory fields ( </Timestamp>,<ServiceID > or </ApplicationID>) are void or </Timestamp> contains invalid data");
						System.out.println(
								"###################################################################################################");
						throw new RestException("Error ISPHeader : " + ispHeader
								+ " is Malformed or Mandatory fields ( </Timestamp>,<ServiceID > or </ApplicationID>) are void or </Timestamp> contains invalid data");
					}

					// If a header map was supplied, add the map name=value
					// pairs as
					// HTTP header values
					if (debug) {
						System.out.println(
								"###################################################################################################");
						System.out.println(
								">> doRest MAP  key -value http-header-request-property alpahbetical list order; ");
						System.out.println(
								"###################################################################################################");
					}

				}

				if (debug) {
					if (timeout != -1) {
						System.out.println(
								"###################################################################################################");
						System.out.println(">> doRest: command=" + command + ", urlString=" + urlString + ", content="
								+ content + ", userid=" + userid + ", +, X-ISPWebServicesHeader=" + ispHeader
								+ " , timeout=" + timeout + " , isIIBService=" + servizioIIB + " , ServiceInterface="
								+ interfaccia);
						System.out.println(
								"###################################################################################################");
					} else {
						System.out.println(
								"###################################################################################################");
						System.out.println(">> doRest: command=" + command + ", urlString=" + urlString + ", content="
								+ content + ", userid=" + userid + ", , X-ISPWebServicesHeader=" + ispHeader
								+ " , isIIBService=" + servizioIIB + " , ServiceInterface=" + interfaccia);
						System.out.println(
								"###################################################################################################");
					}
				}

				if (headerMap != null) {
					Set<String> keySet = headerMap.keySet();
					Iterator<String> it = keySet.iterator();
					while (it.hasNext()) {
						String key = it.next();
						if (!key.equals("X-ISPWebServicesHeader") && !key.equals("X-ISP-Security")
								&& !key.equals("Authorization"))
							// skip X-ISPWebServicesHeader
							httpUrlConnection.addRequestProperty(key, headerMap.get(key));
						if (debug)
							System.out.println(">> doRest requestProperty : " + key + " = " + headerMap.get(key));
					}
					// add single ISPHeader field as single requestProperty
					if (debug) {
						System.out.println(
								"###################################################################################################");
						System.out.println(">> doRest ISPHEADER key -value http-header-request-property list ; ");
						System.out.println(
								"###################################################################################################");
					}

					if (ispHeaderMap != null) {
						String encodedValue = null;
						String encodedKey = null;
						keySet = ispHeaderMap.keySet();
						it = keySet.iterator();
						while (it.hasNext()) {
							String key = it.next();
							String value = ispHeaderMap.get(key);
							if (value == null)
								value = "";
							encodedValue = URLEncoder.encode(value, "UTF-8");
							encodedKey = URLEncoder.encode(key, "UTF-8");
							httpUrlConnection.addRequestProperty(encodedKey, encodedValue);

							if (debug)
								System.out.println(">> doRest requestProperty : " + key + " = " + value);
						}

					}

				}

				// If a userid (and password) have been supplied, set the basic
				// Auth
				// info
				if (userid != null && !userid.isEmpty()) {
					String authorization = "Basic " + new String(org.apache.commons.codec.binary.Base64
							.encodeBase64(new String(userid + ":" + password).getBytes()));
					httpUrlConnection.setRequestProperty("Authorization", authorization);

					System.out.println(
							"###################################################################################################");
					System.out.println(
							">> doRest: service invoked with user/password , Security Token IF present will be ignored");
					System.out.println(
							"###################################################################################################");

				} else {

					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// IIB Interface Clear Token b64 Raw token EnvelopSoap
					// Header X-ISP-Security Header Authorization
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// yes REST yes yes NA
					// no REST no yes NA
					// yes SOAP no yes ISPHeader + WsseSecurity
					// no SOAP no no ISPHeader + WsseSecurity
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

					String clearToken = headerMap.get("X-ISP-Security");
					String clearTokenb64 = null;

					// if present clear Token execute b64 trasformation and pass
					// it in header only if is IIB service of type REST
					if (clearToken != null) {
						clearTokenb64 = new String(
								org.apache.commons.codec.binary.Base64.encodeBase64(clearToken.getBytes()));

						if (servizioIIB & interfaccia.equalsIgnoreCase("REST"))

							httpUrlConnection.setRequestProperty("X-ISP-Security", clearTokenb64);

						if (debug) {
							System.out.println(
									"###################################################################################################");
							System.out.println(">> doRest FOUND - Security Clear Token \n" + clearToken);
							System.out.println(
									"###################################################################################################");
							System.out.println(">> doRest B64   - Security ClearToken \n" + clearTokenb64);
							System.out.println(
									"###################################################################################################");

							if (servizioIIB & interfaccia.equalsIgnoreCase("REST"))

								System.out.println("created http header: X-ISP-Security with Clear Token b64");
						}

					} else {

						if (debug) {
							System.out.println(
									"###################################################################################################");
							System.out.println(">> doRest Security Clear Token NOT Present");
							System.out.println(
									"###################################################################################################");
						}

					}

					String rawToken = headerMap.get("Authorization");

					// If present raw Token then creation of http header only if
					// is a IIB Service or interface is of type Rest

					if (rawToken != null) {

						if (servizioIIB | interfaccia.equalsIgnoreCase("REST"))
							httpUrlConnection.setRequestProperty("Authorization", rawToken);

						if (debug) {
							System.out.println(
									"###################################################################################################");
							System.out.println(">> doRest FOUND - Security Raw Token \n" + rawToken);
							System.out.println(
									"###################################################################################################");

							if (servizioIIB | interfaccia.equalsIgnoreCase("REST"))

								System.out.println("Aggiunto header Authorization con Raw Token");

						}

					} else

						if (debug) {
							System.out.println(
									"###################################################################################################");
							System.out.println(">> doRest Security Raw Token NOT Present");
							System.out.println(
									"###################################################################################################");
						}
				}

				// If there is content AND the command is either POST or PUT,
				// then
				// send the request content
				if (content != null && (command.equals("POST") || command.equals("PUT"))) {
					httpUrlConnection.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(httpUrlConnection.getOutputStream());
					wr.write(content);
					wr.flush();
					wr.close();
				} else if (command.equals("DELETE")) {

					httpUrlConnection.setDoOutput(true);

					/*
					 * httpUrlConnection.setRequestProperty( "Content-Type",
					 * "application/x-www-form-urlencoded" );
					 */
				}

				int responseCode = httpUrlConnection.getResponseCode();
				returnVal.put(keycodeMessage, "" + responseCode);
				returnVal.put(keyErrorMessage, "" + httpUrlConnection.getResponseMessage());

				returnVal.put(keyResponseMessage, "");

				// Now we wait for the response data
				InputStreamReader inReader = null;

				try {

					inReader = new InputStreamReader(httpUrlConnection.getInputStream());

				} catch (IOException e) {

					InputStream in = httpUrlConnection.getErrorStream();

					if (in == null) {
						if (httpUrlConnection.getResponseCode() == 401)
							return returnVal;
						else
							throw e;
					}
					inReader = new InputStreamReader(in);

				}

				BufferedReader bufferedReader = new BufferedReader(inReader);
				StringBuffer sb = new StringBuffer();
				String line = bufferedReader.readLine();
				while (line != null) {
					sb.append(line);
					line = bufferedReader.readLine();
				}
				bufferedReader.close();

				httpUrlConnection.disconnect();

				if (debug) {
					System.out.println("doRest: result is " + sb.toString());
				}

				returnVal.put(keyResponseMessage, "" + sb.toString());

				return returnVal;

			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}

		// Never reached. We should never reach this line.
	} // End of doRest

	public static HashMap<String, String> doRest(String command, String urlString, String content,
			HashMap<String, String> headerMap, String aliasAuthName, boolean debug, int timeout, String interfaccia,
			boolean servizioIIB) throws Exception {

		HashMap<String, String> returnVal = new HashMap<String, String>();
		String userid = "";
		String password = "";

		returnVal.put(keycodeMessage, "");
		returnVal.put(keyErrorMessage, "");
		returnVal.put(keyResponseMessage, "");
		try {
			Map<String, String> userCredentials = getUserCredentials(aliasAuthName);
			userid = userCredentials.get(USER_NAME);
			password = userCredentials.get(USER_PASSWORD);
		} catch (Exception ex) {
			returnVal.put(keyResponseMessage, "Impossibile recuperare l'alias. " + ex.getMessage());
			returnVal.put(keycodeMessage, "9999");
			return returnVal;
		}

		return doRest(command, urlString, content, headerMap, userid, password, debug, timeout, interfaccia,
				servizioIIB);

		// Never reached. We should never reach this line.
	} // End of doRest

	private static Map<String, String> getUserCredentials(String aliasName) throws Exception {

		Map<String, String> userCredentials = new HashMap<String, String>();

		try {
			Map<String, String> map = new HashMap<String, String>();

			map.put(MAPPING_ALIAS, aliasName);
			CallbackHandler handler;

			handler = WSMappingCallbackHandlerFactory.getInstance().getCallbackHandler(map, null);

			Subject subject = new Subject();
			LoginContext lc = new LoginContext("DefaultPrincipalMapping", subject, handler);
			lc.login();
			subject = lc.getSubject();

			Set<PasswordCredential> pwdCredentialSet = subject.getPrivateCredentials(PasswordCredential.class);
			PasswordCredential cred = (PasswordCredential) pwdCredentialSet.iterator().next();
			if (cred != null) {
				userCredentials.put(USER_NAME, new String(cred.getUserName()));
				userCredentials.put(USER_PASSWORD, new String(cred.getPassword()));
				return userCredentials;
			} else {
				return null;
			}
		} catch (Exception e) {

			System.out.println("RestClient : Error while retrieving user alias named " + aliasName);

			throw e;
		}
	}
	/*
	 * private static String sendDelete(String url, HashMap<String, String>
	 * headerMap, String userid, String password) throws IOException,
	 * AuthenticationException { CloseableHttpClient httpClient =
	 * HttpClientBuilder.create().build(); //We just change HttpPost >
	 * HttpDelete HttpDelete deleteRequest = new HttpDelete(url);
	 * 
	 * if (headerMap != null) { Set<String> keySet = headerMap.keySet();
	 * Iterator<String> it = keySet.iterator(); while (it.hasNext()) { String
	 * key = it.next(); deleteRequest.addHeader(key, headerMap.get(key)); } }
	 * 
	 * // If a userid (and password) have been supplied, set the basic Auth //
	 * info if (userid != null && !userid.isEmpty()) { String authorization =
	 * "Basic " + new String( org.apache.commons.codec.binary.Base64
	 * .encodeBase64(new String(userid + ":" + password).getBytes()));
	 * UsernamePasswordCredentials credentials = new
	 * UsernamePasswordCredentials(userid, password); BasicScheme scheme = new
	 * BasicScheme();
	 * 
	 * Header authorizationHeader = scheme.authenticate(credentials,
	 * deleteRequest); deleteRequest.addHeader(authorizationHeader);
	 * 
	 * }
	 * 
	 * HttpResponse response = httpClient.execute(deleteRequest);
	 * 
	 * // if (response.getStatusLine().getStatusCode() != 200) {
	 * 
	 * 
	 * 
	 * // throw new RuntimeException("Failed : HTTP error code : " // +
	 * response.getStatusLine().getStatusCode()); //}
	 * 
	 * BufferedReader br = new BufferedReader( new
	 * InputStreamReader((response.getEntity().getContent())));
	 * 
	 * System.out.println("Output from Server .... \n"); StringBuffer result =
	 * new StringBuffer(); String output = ""; while ((output = br.readLine())
	 * != null) { result.append(output); } httpClient.close(); return
	 * result.toString(); }
	 */

	// u0h1963
	private static HashMap<String, String> ISPHeaderScomposition(String ispHeader, boolean logMe) {

		Document doc = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		InputSource source = new InputSource(new StringReader(ispHeader));
		HashMap<String, String> tokenMap = new HashMap<String, String>();
		String tagValue = null;

		if (logMe) {

			System.out.println("######################################  ISPHEADER ###################################");
			System.out.println(ispHeader);
			System.out.println("######################################  ISPHEADER ###################################");
		}

		try {
			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

			try {

				// tokenMap.put("TransactionId",xpath.evaluate("//*[local-name()='ISPWebservicesHeader']//*[local-name()='RequestInfo']//*[local-name()='TransactionId']",
				// doc));

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='RequestInfo']//*[local-name()='TransactionId']",
						doc);
				tokenMap.put("ISPWebservicesHeader.RequestInfo.TransactionId", tagValue);
				if (logMe)
					System.out.println("TransactionId --> " + tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='RequestInfo']//*[local-name()='Timestamp']",
						doc);
				if (logMe)
					System.out.println("Timestamp --> " + tagValue);
				if (tagValue == null || tagValue.length() == 0) {
					System.out.println(
							"######################################  ISPHEADER ###################################");
					System.out.println(ispHeader);
					System.out.println(
							"######################################  ISPHEADER ###################################");
					System.out.println("Timestamp --> value is mandatory");
					throw new Exception();
				}
				if (Rest.checkTimestamp(tagValue) == null) {
					System.out.println(
							"######################################  ISPHEADER ###################################");
					System.out.println(ispHeader);
					System.out.println(
							"######################################  ISPHEADER ###################################");
					System.out.println("Timestamp --> " + tagValue + " invalid value detected");
					throw new Exception();
				}
				tokenMap.put("ISPWebservicesHeader.RequestInfo.Timestamp", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='RequestInfo']//*[local-name()='ServiceID']",
						doc);
				if (logMe)
					System.out.println("ServiceID --> " + tagValue);
				if (tagValue == null || tagValue.length() == 0) {
					System.out.println(
							"######################################  ISPHEADER ###################################");
					System.out.println(ispHeader);
					System.out.println(
							"######################################  ISPHEADER ###################################");
					System.out.println("ServiceID --> value is mandatory");
					throw new Exception();
				}
				tokenMap.put("ISPWebservicesHeader.RequestInfo.ServiceID", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='RequestInfo']//*[local-name()='ServiceVersion']",
						doc);
				if (logMe)
					System.out.println("ServiceVersion --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.RequestInfo.ServiceVersion", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='RequestInfo']//*[local-name()='Language']",
						doc);
				if (logMe)
					System.out.println("Language --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.RequestInfo.Language", tagValue);

				/// @*[local-name()='UserID']
				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='OperatorInfo']/@*[local-name()='UserID']",
						doc);
				if (logMe)
					System.out.println("UserID --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.OperatorInfo.UserID", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='OperatorInfo']/@*[local-name()='IsVirtualUser']",
						doc);
				if (logMe)
					System.out.println("IsVirtualUser --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.OperatorInfo.ISVirtualUser", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='OperatorInfo']/@*[local-name()='NotISPUserID']",
						doc);
				if (logMe)
					System.out.println("NotISPUserID --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.OperatorInfo.NotISPUserID", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='CompanyInfo']//*[local-name()='ISPCallerCompanyIDCode']",
						doc);
				if (logMe)
					System.out.println("ISPCallerCompanyIDCode --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.CompanyInfo.ISPCallerCompanyIDCode", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='CompanyInfo']//*[local-name()='NotISPCompanyIDCode']",
						doc);
				if (logMe)
					System.out.println("NotISPCompanyIDCode --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.CompanyInfo.NotISPCompanyIDCode", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='CompanyInfo']//*[local-name()='ISPBranchCode']",
						doc);
				if (logMe)
					System.out.println("ISPBranchCode --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.CompanyInfo.ISPBranchCode", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='CompanyInfo']//*[local-name()='ISPServiceCompanyIDCode']",
						doc);
				if (logMe)
					System.out.println("ISPServiceCompanyIDCode --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.CompanyInfo.ISPServiceCompanyIDCode", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='BusinessInfo']//*[local-name()='CustomerID']",
						doc);
				if (logMe)
					System.out.println("CustomerID --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.BusinessInfo.CustomerID", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='BusinessInfo']//*[local-name()='BusinessProcessName']",
						doc);
				if (logMe)
					System.out.println("BusinessProcessName --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.BusinessInfo.BusinessProcessName", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='BusinessInfo']//*[local-name()='BusinessProcessID']",
						doc);
				if (logMe)
					System.out.println("BusinessProcessID --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.BusinessInfo.BusinessProcessID", tagValue);
 
				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='BusinessInfo']//*[local-name()='BusinessOperation']",
						doc);
				if (logMe)
					System.out.println("BusinessOperation --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.BusinessInfo.BusinessOperation", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='BusinessInfo']//*[local-name()='BusinessFileID']",
						doc);
				if (logMe)
					System.out.println("BusinessFileID --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.BusinessInfo.BusinessFileID", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='TechnicalInfo']//*[local-name()='ChannelIDCode']",
						doc);
				if (logMe)
					System.out.println("ChannelIDCode --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.TechnicalInfo.ChannelIDCode", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='TechnicalInfo']//*[local-name()='ApplicationID']",
						doc);
				if (logMe)
					System.out.println("ApplicationID --> " + tagValue);
				if (tagValue == null || tagValue.length() == 0) {
					System.out.println(
							"######################################  ISPHEADER ###################################");
					System.out.println(ispHeader);
					System.out.println(
							"######################################  ISPHEADER ###################################");
					System.out.println("ApplicationID --> value is mandatory");
					throw new Exception();
				}
				tokenMap.put("ISPWebservicesHeader.TechnicalInfo.ApplicationID", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='TechnicalInfo']//*[local-name()='CallerServerName']",
						doc);
				if (logMe)
					System.out.println("CallerServerName --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.TechnicalInfo.CallerServerName", tagValue);

				tagValue = xpath.evaluate(
						"//*[local-name()='ISPWebservicesHeader']//*[local-name()='TechnicalInfo']//*[local-name()='CallerProgramName']",
						doc);
				if (logMe)
					System.out.println("CallerProgramName --> " + tagValue);
				tokenMap.put("ISPWebservicesHeader.TechnicalInfo.CallerProgramName", tagValue);

				int countag = Integer.parseInt(xpath.evaluate(
						"count(//*[local-name()='ISPWebservicesHeader']//*[local-name()='AdditionalBusinessInfo']//*[local-name()='Param'])",
						doc));
				String tagName = null;

				if (countag != 0) {

					for (int ii = 1; ii <= countag; ii++) {

						tagName = xpath.evaluate(
								"//*[local-name()='ISPWebservicesHeader']//*[local-name()='AdditionalBusinessInfo']/Param["
										+ String.valueOf(ii) + "]//@*[local-name()='Name']",
										doc);

						if (tagName != null && tagName.length() != 0) {

							tagValue = xpath.evaluate(
									"//*[local-name()='ISPWebservicesHeader']//*[local-name()='AdditionalBusinessInfo']/Param["
											+ String.valueOf(ii) + "]//@*[local-name()='Value']",
											doc);
							tokenMap.put("ISPWebservicesHeader.AdditionalBusinessInfo." + tagName, tagValue);
							if (logMe)
								System.out.println(tagName + " --> " + tagValue);
						}
					}

				}

			} catch (Exception e) {
				tokenMap = null;
				e.printStackTrace();
			}

		} catch (Exception e) {
			tokenMap = null;
			e.printStackTrace();
		}

		return tokenMap;
	}

	private static String checkTimestamp(String input) {

		boolean iscorrect = false;

		String ts = null;

		String other = null;

		if (input != null) {

			input = input.replace("-", "");
			input = input.replace(":", "");
			input = input.replace(".", "");

		}

		if (input != null && input.length() >= 17 & input.length() <= 20) {

			String zero = "00000000";

			ts = input.substring(0, 14);

			other = input.substring(14, input.length());

			DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

			formatter.setLenient(false);

			try {

				formatter.parse(ts);

				Integer.parseInt(other);

				ts = ts + other;

				int difflen = 20 - ts.length();

				ts = ts.concat(zero.substring(0, difflen));

				iscorrect = true;

			} catch (Exception e) {

				iscorrect = false;

			}

		}

		if (iscorrect)
			return ts;

		else
			return null;

	}
} // End of File
