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
	//public static boolean debug = false;

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
		String command = "DELETE";
		String url = "http://linuxbpm85:9080/rest/bpm/wle/v1/process/1?action=delete&parts=all";
		 */
		String contentFile = null;
		String content = null;
		String userid = "bpmadmin";
		String password = "Password01";
		HashMap<String, String> headerMap = new HashMap<String, String>();
		Options options = null;

		try {

			System.out.println( doRest(command, url, content, headerMap, userid, password,false,100));
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	} // End of main

	public static class RestException extends Exception {

		String codice;
		String messaggio;

		public RestException(String arg0) {
			super(arg0);
		}


		public RestException(String cod,String mess){

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
	public static HashMap<String,String> doRest(String command, String urlString,
			String content, HashMap<String, String> headerMap, String userid,
			String password,boolean debug,int timeout) throws Exception {

		HashMap<String,String> returnVal = new HashMap<String,String>();


		// Check that the command type is known
		if (!command.equals("GET") && !command.equals("POST")
				&& !command.equals("PUT")&& !command.equals("DELETE")) {
			throw new RestException("Unsupported command: " + command
					+ ".  Supported commands are GET, POST, PUT");
		}

		{
			try {

				URL url = new URL(urlString);
				HttpURLConnection httpUrlConnection = (HttpURLConnection) url
						.openConnection();
				httpUrlConnection.setRequestMethod(command);
								
				if (timeout > 0) {
					
					httpUrlConnection.setReadTimeout(timeout*1000); //timeout is in seconds 
				}
					
				////////////////////////////////////////////////////////////////////////
				
				String ispHeader=headerMap.get("X-ISPWebServicesHeader");
				
				HashMap<String,String> ispHeaderMap=Rest.ISPHeaderScomposition(ispHeader);
				
				System.out.println("-------TokenMap----------- " + ispHeaderMap);
				
				if (ispHeaderMap ==null) {	
					
					System.out.println("-------TokenMap---------NULL");
					
					throw new RestException("Error ISPHeader : " + ispHeader
							+ ".  is Malformed or mandatory fields are missing or invalid");
				}
				
				if (debug) {
					if (timeout != -1)
					System.out.println(">> doRest: command=" + command + ", urlString="
							+ urlString + ", content=" + content + ", userid=" + userid+ ", +, X-ISPWebServicesHeader=" + ispHeader+" , timeout="+timeout);
					else
						System.out.println(">> doRest: command=" + command + ", urlString="
								+ urlString + ", content=" + content + ", userid=" + userid+ ", , X-ISPWebServicesHeader=" + ispHeader+" , timeout=-999 Not Applicable");	
				}
					
				////////////////////////////////////////////////////////////////////////

				// If a header map was supplied, add the map name=value pairs as
				// HTTP header values
				if (headerMap != null) {
					Set<String> keySet = headerMap.keySet();
					Iterator<String> it = keySet.iterator();
					while (it.hasNext()) {
						String key = it.next();
						if (!key.equals("X-ISPWebServicesHeader"))
							//skip X-ISPWebServicesHeader
						httpUrlConnection.addRequestProperty(key,
								headerMap.get(key));
					}
										
					
					//add single ISPHeader field as single requestProperty				
					keySet = ispHeaderMap.keySet();
					it = keySet.iterator();
					while (it.hasNext()) {
						String key = it.next();
						String value=headerMap.get(key);
						if (value==null) value="";
						httpUrlConnection.addRequestProperty(key,value);
					}
					
				}

				// If a userid (and password) have been supplied, set the basic Auth
				// info
				if (userid != null && !userid.isEmpty()) {
					String authorization = "Basic "
							+ new String(
									org.apache.commons.codec.binary.Base64
									.encodeBase64(new String(userid + ":"
											+ password).getBytes()));
					httpUrlConnection.setRequestProperty("Authorization",
							authorization);

				}

				// If there is content AND the command is either POST or PUT, then
				// send the request content
				if (content != null
						&& (command.equals("POST") || command.equals("PUT"))) {
					httpUrlConnection.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(
							httpUrlConnection.getOutputStream());
					wr.write(content);
					wr.flush();
					wr.close();
				}else if(command.equals("DELETE")){

					httpUrlConnection.setDoOutput(true);

					/*httpUrlConnection.setRequestProperty(
					    "Content-Type", "application/x-www-form-urlencoded" );*/
				}

				int responseCode = httpUrlConnection.getResponseCode();
				returnVal.put(keycodeMessage, "" + responseCode);
				returnVal.put(keyErrorMessage, "" + httpUrlConnection.getResponseMessage());	


				returnVal.put(keyResponseMessage, "" );

				// Now we wait for the response data
				InputStreamReader inReader = null;

				try{

					inReader = new InputStreamReader(httpUrlConnection.getInputStream());

				}catch (IOException e) {

					InputStream in = 	httpUrlConnection.getErrorStream();

					if(in == null  ){
						if(httpUrlConnection.getResponseCode() == 401)
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

				returnVal.put(keyResponseMessage, "" +  sb.toString());

				return returnVal;

			}catch(IOException e){
				e.printStackTrace();
				throw e;
			}
		}


		// Never reached. We should never reach this line.
	} // End of doRest



	public static HashMap<String,String> doRest(String command, String urlString,
			String content, HashMap<String, String> headerMap,String aliasAuthName,boolean debug,int timeout) throws Exception {

		HashMap<String,String> returnVal = new HashMap<String,String>();
		String userid = "";
		String password = "";
		
		returnVal.put(keycodeMessage, "" );
		returnVal.put(keyErrorMessage, "" );	
		returnVal.put(keyResponseMessage, "" );
		try{
			Map<String,String> userCredentials = getUserCredentials(aliasAuthName);
			userid = userCredentials.get(USER_NAME);
			password = userCredentials.get(USER_PASSWORD);
		}catch(Exception ex){
			returnVal.put(keyResponseMessage, "Impossibile recuperare l'alias. " + ex.getMessage() );
			returnVal.put(keycodeMessage, "9999"  );
			return returnVal;
		}

		return doRest(command, urlString,
				content, headerMap,  userid,
				password, debug,timeout);

		// Never reached. We should never reach this line.
	} // End of doRest


	private static Map<String, String> getUserCredentials(String aliasName)
			throws Exception {

		Map<String, String> userCredentials = new HashMap<String, String>();

		try {
			Map<String, String> map = new HashMap<String, String>();


			map.put(MAPPING_ALIAS, aliasName);
			CallbackHandler handler;

			handler = WSMappingCallbackHandlerFactory.getInstance()
					.getCallbackHandler(map, null);

			Subject subject = new Subject();
			LoginContext lc = new LoginContext("DefaultPrincipalMapping",
					subject, handler);
			lc.login();
			subject = lc.getSubject();

			Set<PasswordCredential> pwdCredentialSet = subject
					.getPrivateCredentials(PasswordCredential.class);
			PasswordCredential cred = (PasswordCredential) pwdCredentialSet
					.iterator().next();
			if (cred != null) {
				userCredentials.put(USER_NAME, new String(cred.getUserName()));
				userCredentials.put(USER_PASSWORD,
						new String(cred.getPassword()));
				return userCredentials;
			} else {
				return null;
			}
		} catch (Exception e) {

			System.out.println("eRestClient : Error while retrieving user alias named "
					+ aliasName);

			throw e;
		}
	}
	/*
	private static String sendDelete(String url, HashMap<String, String> headerMap, String userid,
			String password) throws IOException, AuthenticationException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		//We just change HttpPost > HttpDelete
		HttpDelete deleteRequest = new HttpDelete(url);

		if (headerMap != null) {
			Set<String> keySet = headerMap.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				deleteRequest.addHeader(key,
						headerMap.get(key));
			}
		}

		// If a userid (and password) have been supplied, set the basic Auth
		// info
		if (userid != null && !userid.isEmpty()) {
			String authorization = "Basic "
					+ new String(
							org.apache.commons.codec.binary.Base64
							.encodeBase64(new String(userid + ":"
									+ password).getBytes()));
			   UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userid, password);
				BasicScheme scheme = new BasicScheme();

				Header authorizationHeader = scheme.authenticate(credentials, deleteRequest);
				deleteRequest.addHeader(authorizationHeader);

		}

		HttpResponse response = httpClient.execute(deleteRequest);

	//	if (response.getStatusLine().getStatusCode() != 200) {



		//	throw new RuntimeException("Failed : HTTP error code : "
			//		+ response.getStatusLine().getStatusCode());
		//}

		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));

		System.out.println("Output from Server .... \n");
		StringBuffer result = new StringBuffer();
		String output = "";
		while ((output = br.readLine()) != null) {
			result.append(output);
		}
		httpClient.close();
		return result.toString();
	}*/
	
	 //u0h1963
	private static HashMap<String, String> ISPHeaderScomposition(String ispHeader) {

		Document doc = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		InputSource source = new InputSource(new StringReader(ispHeader));
		HashMap<String, String> tokenMap = new HashMap<String, String>();
		try {
			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			
			try {
				tokenMap.put("ISPWebservicesHeader.RequestInfo.TransactionId",
						xpath.evaluate("/ISPWebservicesHeader/RequestInfo/TransactionId/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.RequestInfo.Timestamp", xpath.evaluate("/ISPWebservicesHeader/RequestInfo/Timestamp/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.RequestInfo.ServiceID", xpath.evaluate("/ISPWebservicesHeader/RequestInfo/ServiceID/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.RequestInfo.ServiceVersion",
						xpath.evaluate("/ISPWebservicesHeader/RequestInfo/ServiceVersion/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.RequestInfo.Language", xpath.evaluate("/ISPWebservicesHeader/RequestInfo/Language/text()", doc));

				tokenMap.put("ISPWebservicesHeader.OperatorInfo.UserID", xpath.evaluate("/ISPWebservicesHeader/OperatorInfo/@UserID", doc));
				
				tokenMap.put("ISPWebservicesHeader.OperatorInfo.ISVirtualUser", xpath.evaluate("/ISPWebservicesHeader/OperatorInfo/@IsVirtualUser", doc));
				
				tokenMap.put("ISPWebservicesHeader.OperatorInfo.NotISPUserID", xpath.evaluate("/ISPWebservicesHeader/OperatorInfo/@NotISPUserID", doc));

				tokenMap.put("ISPWebservicesHeader.CompanyInfo.ISPCallerCompanyIDCode",
						xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/ISPCallerCompanyIDCode/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.CompanyInfo.NotISPCompanyIDCode",	
						xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/NotISPCompanyIDCode/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.CompanyInfo.ISPBranchCode",
						xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/ISPBranchCode/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.CompanyInfo.ISPServiceCompanyIDCode",
						xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/ISPServiceCompanyIDCode/text()", doc));

				
				tokenMap.put("ISPWebservicesHeader.BusinessInfo.CustomerID", xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/CustomerID/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.BusinessInfo.BusinessProcessName",
						xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessProcessName/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.BusinessInfo.BusinessProcessID",
						xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessProcessID/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.BusinessInfo.BusinessOperation",
						xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessOperation/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.BusinessInfo.BusinessFileID",
						xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessFileID/text()", doc));

				
				tokenMap.put("ISPWebservicesHeader.TechnicalInfo.ChannelIDCode",
						xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/ChannelIDCode/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.TechnicalInfo.ApplicationID",
						xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/ApplicationID/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.TechnicalInfo.CallerServerName",
						xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/CallerServerName/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.TechnicalInfo.CallerProgramName",
						xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/CallerProgramName/text()", doc));

				
				tokenMap.put("ISPWebservicesHeader.AdditionalBusinessInfo.CodUnitaOperativa",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodUnitaOperativa/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.AdditionalBusinessInfo.DataContabile",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/DataContabile/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.AdditionalBusinessInfo.FlagPaperless",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/FlagPaperless/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.AdditionalBusinessInfo.CodABI", xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodABI/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.AdditionalBusinessInfo.CodOperatività",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodOperatività/text()", doc));
				
				tokenMap.put("ISPWebservicesHeader.AdditionalBusinessInfo.CodTerminaleCics",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodTerminaleCics/text()", doc));

			} catch (XPathExpressionException e) {
				tokenMap=null;
				e.printStackTrace();
			}

		} catch (XPathExpressionException e) {
			tokenMap=null;
			e.printStackTrace();
		}

		return tokenMap;
	}

	/*
	private static HashMap<String, String> ISPHeaderScomposition(String ispHeader) {

		Document doc = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		InputSource source = new InputSource(new StringReader(ispHeader));
		try {
			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		HashMap<String, String> tokenMap = new HashMap<String, String>();

		try {
			tokenMap.put("TransactionID",
					xpath.evaluate("/ISPWebservicesHeader/RequestInfo/TransactionId/text()", doc));
			tokenMap.put("Timestamp", xpath.evaluate("/ISPWebservicesHeader/RequestInfo/Timestamp/text()", doc));
			tokenMap.put("ServiceID", xpath.evaluate("/ISPWebservicesHeader/RequestInfo/ServiceID/text()", doc));
			tokenMap.put("ServiceVersion",
					xpath.evaluate("/ISPWebservicesHeader/RequestInfo/ServiceVersion/text()", doc));
			tokenMap.put("Language", xpath.evaluate("/ISPWebservicesHeader/RequestInfo/Language/text()", doc));

			tokenMap.put("UserID", xpath.evaluate("/ISPWebservicesHeader/OperatorInfo/@UserID", doc));
			tokenMap.put("ISVirtualUser", xpath.evaluate("/ISPWebservicesHeader/OperatorInfo/@IsVirtualUser", doc));
			tokenMap.put("NotISPUserID", xpath.evaluate("/ISPWebservicesHeader/OperatorInfo/@NotISPUserID", doc));

			tokenMap.put("ISPCallerCompanyIDCode",
					xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/ISPCallerCompanyIDCode/text()", doc));
			tokenMap.put("NotISPCompanyIDCode",
					xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/NotISPCompanyIDCode/text()", doc));
			tokenMap.put("ISPBranchCode",
					xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/ISPBranchCode/text()", doc));
			tokenMap.put("ISPServiceCompanyIDCode",
					xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/ISPServiceCompanyIDCode/text()", doc));

			tokenMap.put("CustomerID", xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/CustomerID/text()", doc));
			tokenMap.put("BusinessProcessName",
					xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessProcessName/text()", doc));
			tokenMap.put("BusinessProcessID",
					xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessProcessID/text()", doc));
			tokenMap.put("BusinessOperation",
					xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessOperation/text()", doc));
			tokenMap.put("BusinessFileID",
					xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessFileID/text()", doc));

			tokenMap.put("ChannelIDCode",
					xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/ChannelIDCode/text()", doc));
			tokenMap.put("ApplicationID",
					xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/ApplicationID/text()", doc));
			tokenMap.put("CallerServerName",
					xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/CallerServerName/text()", doc));
			tokenMap.put("CallerProgramName",
					xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/CallerProgramName/text()", doc));

			tokenMap.put("CodUnitaOperativa",
					xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodUnitaOperativa/text()", doc));
			tokenMap.put("DataContabile",
					xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/DataContabile/text()", doc));
			tokenMap.put("FlagPaperless",
					xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/FlagPaperless/text()", doc));
			tokenMap.put("CodABI", xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodABI/text()", doc));
			tokenMap.put("CodOperatività",
					xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodOperatività/text()", doc));
			tokenMap.put("CodTerminaleCics",
					xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodTerminaleCics/text()", doc));

		} catch (XPathExpressionException e) {
			tokenMap=null;
			e.printStackTrace();
		}

		return tokenMap;
	}
	*/	
} // End of File
