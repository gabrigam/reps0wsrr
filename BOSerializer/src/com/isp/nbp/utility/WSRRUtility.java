package com.isp.nbp.utility;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WSRRUtility {

	public static void main(String[] args) throws Exception {
		
		long start_time = System.nanoTime();
		//Thread.sleep(1345);
		long end_time = System.nanoTime();
		double difference = (end_time - start_time)/1e6;
		// void insert test here!!!!
		String wsrr = "https://WIN-MT67KKLQ7LO:9443/WSRR/8.5";

		WSRRUtility wut = new WSRRUtility();
		//wut.getEndpointNameFromBsrUriSLDEnvironmentCheckSecurity("98d59998-78a1-4167.a4d1.8e1ab88ed132", "Application",
		//		"REST", false, "KLL", wsrr, "gabriele", "viviana");
		
	
		
		
	}

	public static String aboutLib() {

		return "lib WSRREnvelopes & utility methods V4.0 May 2017";

	}

	private static String getValueFromJsonObject(JSONObject jso, String key) {

		String result = "";

		try {

			result = jso.getString(key);

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return result;

	}

	public static String getObjectValueFromJSONArrayData(JSONArray jsa, String key) {

		int i = 0;

		int elements = jsa.length();

		String current;

		JSONObject jso;

		String result = "";

		while (i < elements) {

			jso = jsa.getJSONObject(i);

			current = ((String) jso.get("name"));

			if (current.equals(key)) {

				try {

					current = (String) jso.get("value");

				} catch (Exception ex) {

					current = ""; // @TODO eccezione?

					// throw new LIBLKPWSRRTEException(new
					// StringBuffer().append(Messages.ERROR_10).append(key).toString());

				}

				result = current;

				break;

			}

			i++;

		}

		// [[{"name":"name","value":"SLA - CONSUMATORE2 \u202a(00)\u202c"}]]

		return result.replaceAll("\\P{Print}", "");

	}

	public String getGenericObjectByNameAndVersionAndPrimaryType(String name, String version, String primaryType,
			String baseURL, String user, String password) {

		// Create the variable to return
		String bsrURI = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%'%20and%20@primaryType='%PRIMARYTYPE%']&p1=bsrURI";

		if (version == null || version.length() == 0)
			version = "00";

		query = query.replaceAll("%CATALOGNAME%", name);
		query = query.replaceAll("%VERSION%", version);
		query = query.replaceAll("%PRIMARYTYPE%", primaryType);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && bsrURI.equals("[]"))
			bsrURI = null;

		if (bsrURI != null) {
			JSONArray jsa = new JSONArray(bsrURI);
			bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (bsrURI != null) {
				bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}
		return bsrURI;

	}

	public String getGenericObjectByNameAndVersionAndPrimaryTypeExtended(String name, String version,
			String primaryType, String baseURL, String user, String password) {

		// Create the variable to return
		String bsrURI = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%'%20and%20@primaryType='%PRIMARYTYPE%']&p1=bsrURI";

		if (version == null || version.length() == 0)
			version = "00";

		query = query.replaceAll("%CATALOGNAME%", name);
		query = query.replaceAll("%VERSION%", version);
		query = query.replaceAll("%PRIMARYTYPE%", primaryType);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			bsrURI = ">>**ERROR**>>" + e.getMessage();
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && !bsrURI.contains(">>**ERROR**>>")) {

			if (bsrURI != null && bsrURI.equals("[]"))
				bsrURI = null;

			if (bsrURI != null) {
				JSONArray jsa = new JSONArray(bsrURI);
				bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
				if (bsrURI != null) {
					bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
				}
			}
		}
		return bsrURI;

	}

	public boolean existObjectByNameAndVersionAndPrimaryType(String name, String version, String primaryType,
			String baseURL, String user, String password) {

		// Create the variable to return
		boolean result = false;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%'%20and%20@primaryType='%PRIMARYTYPE%']&p1=bsrURI";

		if (version == null || version.length() == 0)
			version = "00";

		query = query.replaceAll("%CATALOGNAME%", name);
		query = query.replaceAll("%VERSION%", version);
		query = query.replaceAll("%PRIMARYTYPE%", primaryType);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				result = true;
			} else {
				result = false;
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return result;

	}

	public String getSLAassociatedToSLDExtended(String consumerName, String consumerVersion, String bsrURISLDProvider,
			String baseURL, String user, String password) {

		// Create the variable to return
		String bsrURI = null;
		String result = null;
		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_consumes(.)&p1=bsrURI";

		if (consumerVersion == null || consumerVersion.length() == 0)
			consumerVersion = "00";

		query = query.replaceAll("%CATALOGNAME%", consumerName);
		query = query.replaceAll("%VERSION%", consumerVersion);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			bsrURI = ">>**ERROR**>>" + e.getMessage();
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && !bsrURI.contains(">>**ERROR**>>")) {
			if (bsrURI != null && bsrURI.equals("[]"))
				result = null;

			else {
				JSONArray jsa = new JSONArray(bsrURI);
				JSONArray jsae = null;
				JSONArray jsae1 = null;
				JSONObject jso = null;
				String bsrURICurrent = null;
				String bsrURISLD = null;
				int i = jsa.length();
				int j = 0;
				while (i > j) {

					try {

						String query1 = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_agreedEndpoints(.)&p1=bsrURI";

						jsae = (JSONArray) jsa.getJSONArray(j);
						jso = (JSONObject) jsae.getJSONObject(0);

						bsrURICurrent = (String) jso.get("value"); // uri SSA

						query1 = query1.replaceAll("%BSRURI%", bsrURICurrent);

						try {
							StringBuffer sb1 = new StringBuffer();
							sb1.append(baseURL).append(query1);
							URL url = new URL(sb1.toString());
							urlConnection = (HttpURLConnection) url.openConnection();
							urlConnection.setRequestMethod("GET");
							urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
							urlConnection.setUseCaches(false);

							if (user != null && password != null) {

								String userPassword = user + ":" + password;

								String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

								urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
							}

							int responsecode = urlConnection.getResponseCode();
							if (responsecode == 200 || (responsecode == 201)) {
								InputStream is = null;
								is = urlConnection.getInputStream();
								int ch;
								sb1.delete(0, sb1.length());
								while ((ch = is.read()) != -1) {
									sb1.append((char) ch);
								}
								bsrURI = sb1.toString();
								is.close();
							} else {
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(urlConnection.getInputStream()));
								StringBuffer stringBuffer = new StringBuffer();
								String line = null;
								while (null != (line = reader.readLine())) {
									stringBuffer.append(line);
								}
								reader.close();
							}
							urlConnection.disconnect();
						} catch (Exception e) {
							bsrURI = ">>**ERROR**>>" + e.getMessage();
							e.printStackTrace();
						}

						finally {
							if (urlConnection != null)
								urlConnection.disconnect();
						}

						if (bsrURI != null && !bsrURI.contains(">>**ERROR**>>")) {

							jsae1 = new JSONArray(bsrURI);

							bsrURISLD = WSRRUtility.getObjectValueFromJSONArrayData((JSONArray) jsae1.get(0), "bsrURI");

							if (bsrURISLD != null && bsrURISLD.equals(bsrURISLDProvider)) {
								result = bsrURICurrent;

								break;
							}

						}

					} catch (Exception ex) {

					}

					j++;
				}

			}
		}

		return result;

	}

	// ** 19042017 sostituisce String getSLAassociatedToSLDExtended
	//23052017 per ora non usato da testare
	public String getSLAassociatedToSLDExtendedNew(String consumerName, String consumerVersion,
			String bsrURISLDProvider, String baseURL, String user, String password) {

		// Create the variable to return
		String bsrURI = null;
		String result = null;
		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_consumes(.)/gep63_agreedEndpoints(.)[@bsrURI='%BSRURISLDPROVIDER%']&p1=bsrURI";

		if (consumerVersion == null || consumerVersion.length() == 0)
			consumerVersion = "00";

		query = query.replaceAll("%CATALOGNAME%", consumerName);
		query = query.replaceAll("%VERSION%", consumerVersion);
		query = query.replaceAll("%BSRURISLDPROVIDER%", bsrURISLDProvider);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				result=bsrURI;
				
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			bsrURI = ">>**ERROR**>>" + e.getMessage();
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && !bsrURI.contains(">>**ERROR**>>")) {
			if (bsrURI != null && bsrURI.equals("[]"))
				result = null;
		}

		return result;

	}

	/// in progress per ora non usata
	public String getSLAassociatedToSLDExtended2(String slaName, String bsrURISLDProvider, String baseURL, String user,
			String password) {

		// Create the variable to return
		String bsrURI = null;
		String result = null;
		String sldURI = null;
		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[matches(@name,'%SLANAME%')]/gep63_agreedEndpoints(.)&p1=bsrURI";

		query = query.replaceAll("%SLANAME%", slaName);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			result = ">>**ERROR**>>" + e.getMessage();
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		JSONArray jsa = new JSONArray(bsrURI);
		JSONArray jsae = null;
		String bsrURICurrent = null;
		int i = jsa.length();
		int j = 0;
		while (i > j) {

			jsae = (JSONArray) jsa.getJSONArray(j);
			sldURI = WSRRUtility.getObjectValueFromJSONArrayData((JSONArray) jsae.get(0), "bsrURI");

			if (sldURI != null && sldURI.equals(bsrURISLDProvider)) {
				result = bsrURICurrent;
				break;
			}

			j++;
		}

		// get associated SLA to SLD

		if (result != null && !result.contains(">>**ERROR**>>")) {
			if (result != null && result.equals("[]"))
				result = null;
		}
		return result;

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// ** 23052017 metodo ottimizzato similmente a getSLAassociatedToSLDExtendedNew  //non funziona
	
	public String getSLAassociatedToSLDWithPrimaryTypeExtendedFake(String consumerName, String consumerVersion,String primaryType,
			String bsrURISLDProvider, String baseURL, String user, String password) {

		// Create the variable to return
		String bsrURI = null;
		String result = null;
		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%'%20and%20@primaryType='%PRIMARYTYPE%']/gep63_consumes(.)/gep63_agreedEndpoints(.)[@bsrURI='%BSRURISLDPROVIDER%']&p1=bsrURI";

		if (consumerVersion == null || consumerVersion.length() == 0)
			consumerVersion = "00";

		query = query.replaceAll("%CATALOGNAME%", consumerName);
		query = query.replaceAll("%VERSION%", consumerVersion);
		query = query.replaceAll("%BSRURISLDPROVIDER%", bsrURISLDProvider);
		query = query.replaceAll("%PRIMARYTYPE%", primaryType);

		System.out.println(">>>>>>>>>>>>>>>>>>> "+query);
		
		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				
				System.out.println(">>>>>>>>>>>>>>>>>>> "+bsrURI);
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			bsrURI = ">>**ERROR**>>" + e.getMessage();
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && !bsrURI.contains(">>**ERROR**>>")) {
			if (bsrURI != null && bsrURI.equals("[]"))
				result = null;
		}

		return result;

	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String getSLAassociatedToSLDWithPrimaryTypeExtended(String consumerName, String consumerVersion,
			String primaryType, String bsrURISLDProvider, String baseURL, String user, String password) {

		// Create the variable to return
		String bsrURI = null;
		String result = null;
		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%'%20and%20@primaryType='%PRIMARYTYPE%']/gep63_consumes(.)&p1=bsrURI";

		if (consumerVersion == null || consumerVersion.length() == 0)
			consumerVersion = "00";

		query = query.replaceAll("%CATALOGNAME%", consumerName);
		query = query.replaceAll("%VERSION%", consumerVersion);
		query = query.replaceAll("%PRIMARYTYPE%", primaryType);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			result = ">>**ERROR**>>" + e.getMessage();
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		JSONArray jsa = new JSONArray(bsrURI);
		JSONArray jsae = null;
		JSONArray jsae1 = null;
		JSONObject jso = null;
		String bsrURICurrent = null;
		String bsrURISLD = null;
		int i = jsa.length();
		int j = 0;
		while (i > j) {

			// jsae = (JSONArray) jsa.getJSONArray(j);

			try {

				String query1 = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_agreedEndpoints(.)&p1=bsrURI";

				jsae = (JSONArray) jsa.getJSONArray(j);
				jso = (JSONObject) jsae.getJSONObject(0);

				bsrURICurrent = (String) jso.get("value"); // uri SSA

				query1 = query1.replaceAll("%BSRURI%", bsrURICurrent);

				try {
					StringBuffer sb1 = new StringBuffer();
					sb1.append(baseURL).append(query1);
					URL url = new URL(sb1.toString());
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
					urlConnection.setUseCaches(false);

					if (user != null && password != null) {

						String userPassword = user + ":" + password;

						String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

						urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
					}

					int responsecode = urlConnection.getResponseCode();
					if (responsecode == 200 || (responsecode == 201)) {
						InputStream is = null;
						is = urlConnection.getInputStream();
						int ch;
						sb1.delete(0, sb1.length());
						while ((ch = is.read()) != -1) {
							sb1.append((char) ch);
						}
						bsrURI = sb1.toString();
						is.close();
					} else {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(urlConnection.getInputStream()));
						StringBuffer stringBuffer = new StringBuffer();
						String line = null;
						while (null != (line = reader.readLine())) {
							stringBuffer.append(line);
						}
						reader.close();
					}
					urlConnection.disconnect();
				} catch (Exception e) {
					result = ">>**ERROR**>>" + e.getMessage();
					e.printStackTrace();
				}

				finally {
					if (urlConnection != null)
						urlConnection.disconnect();
				}

				jsae1 = new JSONArray(bsrURI);

				bsrURISLD = WSRRUtility.getObjectValueFromJSONArrayData((JSONArray) jsae1.get(0), "bsrURI");

				if (bsrURISLD != null && bsrURISLD.equals(bsrURISLDProvider)) {
					result = bsrURICurrent;

					break;
				}
			} catch (Exception ex) {

			}

			j++;
		}

		if (result != null && !result.contains(">>**ERROR**>>")) {
			if (result != null && result.equals("[]"))
				result = null;
		}
		return result;

	}

	public boolean isSLDConsumedByService(String consumerName, String consumerVersion, String bsrURISLDProvider,
			String baseURL, String user, String password) {

		// Create the variable to return
		String resultdata = null;
		boolean result = false;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_consumes(.)/gep63_agreedEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23ServiceLevelDefinition')]&p1=bsrURI";

		if (consumerVersion == null || consumerVersion.length() == 0)
			consumerVersion = "00";

		query = query.replaceAll("%CATALOGNAME%", consumerName);
		query = query.replaceAll("%VERSION%", consumerVersion);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		// [[{"value":"e9475ae9-cfc7-4712.9f8e.5d6adc5d8e31","name":"bsrURI"}],[{"value":"98d59998-78a1-4167.a4d1.8e1ab88ed132","name":"bsrURI"}]]
		JSONArray jsa = new JSONArray(resultdata);
		JSONArray jsae = null;
		JSONObject jso = null;
		String bsrURICurrent = null;
		int i = jsa.length();
		int j = 0;
		while (i > j) {
			jsae = (JSONArray) jsa.getJSONArray(j);
			try {
				jso = (JSONObject) jsae.getJSONObject(0);
				bsrURICurrent = (String) jso.get("value");

				if (bsrURICurrent.equals(bsrURISLDProvider)) {
					result = true;
					break;
				}
			} catch (Exception ex) {
				//
			}

			j++;
		}
		return result;

	}

	public boolean isSLDConsumedByServicePrimaryType(String consumerName, String consumerVersion, String primaryType,
			String bsrURISLDProvider, String baseURL, String user, String password) {

		// Create the variable to return
		String resultdata = null;
		boolean result = false;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%'%20and%20@primaryType='%PRIMARYTYPE%']/gep63_consumes(.)/gep63_agreedEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23ServiceLevelDefinition')]&p1=bsrURI";

		if (consumerVersion == null || consumerVersion.length() == 0)
			consumerVersion = "00";

		query = query.replaceAll("%CATALOGNAME%", consumerName);
		query = query.replaceAll("%VERSION%", consumerVersion);
		query = query.replaceAll("%PRIMARYTYPE%", primaryType);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		// [[{"value":"e9475ae9-cfc7-4712.9f8e.5d6adc5d8e31","name":"bsrURI"}],[{"value":"98d59998-78a1-4167.a4d1.8e1ab88ed132","name":"bsrURI"}]]
		JSONArray jsa = new JSONArray(resultdata);
		JSONArray jsae = null;
		JSONObject jso = null;
		String bsrURICurrent = null;
		int i = jsa.length();
		int j = 0;
		while (i > j) {
			jsae = (JSONArray) jsa.getJSONArray(j);
			try {
				jso = (JSONObject) jsae.getJSONObject(0);
				bsrURICurrent = (String) jso.get("value");

				if (bsrURICurrent.equals(bsrURISLDProvider)) {
					result = true;
					break;
				}
			} catch (Exception ex) {
				//
			}

			j++;
		}
		return result;

	}

	public void serviceVersionwithNoOwningOrganization(String tipo, boolean auth, String baseURL, String user,
			String password) {

		// Create the variable to return
		String resultdata = null;

		String type = "http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23" + tipo
				+ "ServiceVersion";

		// String
		// query="/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@primaryType='%CATALOGTYPE%'%20and%20@version='%VERSION%'%20and%20@primaryType='%PRIMARYTYPE%']/gep63_consumes(.)/gep63_agreedEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23ServiceLevelDefinition')]&p1=bsrURI";

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@primaryType='%CATALOGTYPE%']&p1=bsrURI";

		query = query.replaceAll("%CATALOGTYPE%", type);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (auth) {

				if (user != null && password != null) {

					String userPassword = user + ":" + password;

					String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

					urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
				}

			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		// [[{"value":"e9475ae9-cfc7-4712.9f8e.5d6adc5d8e31","name":"bsrURI"}],[{"value":"98d59998-78a1-4167.a4d1.8e1ab88ed132","name":"bsrURI"}]]
		JSONArray jsa = new JSONArray(resultdata);
		JSONArray jsae = null;
		JSONObject jso = null;
		String bsrURICurrent = null;
		int i = jsa.length();
		int j = 0;
		String owningOrg = null;
		while (i > j) {
			jsae = (JSONArray) jsa.getJSONArray(j);
			try {
				jso = (JSONObject) jsae.getJSONObject(0);
				bsrURICurrent = (String) jso.get("value");

				owningOrg = getOwningOrganizationFromGenericObjectByBsrUri(bsrURICurrent, baseURL, user, password);

				if (owningOrg == null) {

					System.out.println("Oggetto con BsrURI : " + bsrURICurrent + " di tipo : " + tipo
							+ "ServiceVersion NON HA Organizzazione associata");
				}

			} catch (Exception ex) {
				//
			}

			j++;
		}
		// return result;

	}

	public String getSLAAssociatedToSLD(String consumerName, String consumerVersion, String bsrURISLDProvider,
			String baseURL, String user, String password) {

		// Create the variable to return
		String resultdata = null;
		String bsrURI = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_consumes(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceProfileExtensions%23ServiceLevelAgreement')]&p1=bsrURI";
		String query1 = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_agreedEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23ServiceLevelDefinition')]&p1=bsrURI";

		if (consumerVersion == null || consumerVersion.length() == 0)
			consumerVersion = "00";

		query = query.replaceAll("%CATALOGNAME%", consumerName);
		query = query.replaceAll("%VERSION%", consumerVersion);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		JSONArray jsa = new JSONArray(resultdata);
		JSONArray jsae = null;
		JSONObject jso = null;
		String bsrURICurrent = null;
		int i = jsa.length();
		int j = 0;
		while (i > j) {
			jsae = (JSONArray) jsa.getJSONArray(j);
			try {
				jso = (JSONObject) jsae.getJSONObject(0);
				bsrURI = (String) jso.get("value");
				String currentQuery = query1;
				resultdata = null;
				currentQuery = currentQuery.replaceAll("%BSRURI%", bsrURI);

				try {
					StringBuffer sb = new StringBuffer();
					sb.append(baseURL).append(currentQuery);
					URL url = new URL(sb.toString());
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
					urlConnection.setUseCaches(false);

					if (user != null && password != null) {

						String userPassword = user + ":" + password;

						String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

						urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
					}

					int responsecode = urlConnection.getResponseCode();
					if (responsecode == 200 || (responsecode == 201)) {
						InputStream is = null;
						is = urlConnection.getInputStream();
						int ch;
						sb.delete(0, sb.length());
						while ((ch = is.read()) != -1) {
							sb.append((char) ch);
						}
						resultdata = sb.toString();
						is.close();

						JSONArray jsa1 = new JSONArray(resultdata);
						JSONArray jsae1 = null;
						JSONObject jso1 = null;
						jsae1 = (JSONArray) jsa1.getJSONArray(0);
						jso1 = (JSONObject) jsae1.getJSONObject(0);
						bsrURICurrent = (String) jso1.get("value");

					} else {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(urlConnection.getInputStream()));
						StringBuffer stringBuffer = new StringBuffer();
						String line = null;
						while (null != (line = reader.readLine())) {
							stringBuffer.append(line);
						}
						reader.close();
					}
					urlConnection.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}

				finally {
					if (urlConnection != null)
						urlConnection.disconnect();
				}

				if (bsrURICurrent != null && bsrURICurrent.equals(bsrURISLDProvider)) {

					break;

				} else
					bsrURI = null;
			} catch (Exception ex) {
				//
			}

			j++;
		}

		return bsrURI;
	}

	public boolean checkSSAAndAcronimoRelationShip(String acronimo, String bsrURIAcronimo, String bsrURISSA,
			String baseURL, String user, String password) {

		String resultdata = null;
		String bsrURI = null;
		boolean result = false;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[classifiedByAnyOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')/ale63_childOrganizations(.)[@name='%ACRONIMONAME%']]&p1=bsrURI";
		query = query.replaceAll("%ACRONIMONAME%", acronimo);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		JSONArray jsa = new JSONArray(resultdata);
		JSONArray jsae = null;
		JSONObject jso = null;
		int i = jsa.length();
		int j = 0;
		boolean okOnDelete = true;
		boolean relation = false;

		if (i != 0) {

			while (i > j & okOnDelete) {
				jsae = (JSONArray) jsa.getJSONArray(j);
				try {
					jso = (JSONObject) jsae.getJSONObject(0);
					bsrURI = (String) jso.get("value");

					if (!bsrURI.equals(bsrURISSA)) {
						okOnDelete = deleteSpecificRelation(bsrURI, bsrURIAcronimo, "ale63_childOrganizations", baseURL,
								user, password);
					} else
						relation = true;
				} catch (Exception e) {
					e.printStackTrace();
					okOnDelete = false;
				}

				j++;
			}

			result = true;

			if (okOnDelete) {

				if (!relation) {

					result = updateRelationShip(bsrURISSA, "ale63_childOrganizations", bsrURIAcronimo, baseURL, user,
							password);
				}
			} else
				result = false;

		}

		else {
			// ssa & acronimo present without relation particular case
			result = updateRelationShip(bsrURISSA, "ale63_childOrganizations", bsrURIAcronimo, baseURL, user, password);

		}

		return result;
	}

	public String checkSSAAndAcronimoRelationShipVerbose(String acronimo, String bsrURIAcronimo, String bsrURISSA,
			String baseURL, String user, String password) {

		String resultdata = null;
		String bsrURI = null;
		boolean result = false;
		String message = "";

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[classifiedByAnyOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')/ale63_childOrganizations(.)[@name='%ACRONIMONAME%']]&p1=bsrURI";
		query = query.replaceAll("%ACRONIMONAME%", acronimo);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		JSONArray jsa = new JSONArray(resultdata);
		JSONArray jsae = null;
		JSONObject jso = null;
		int i = jsa.length();
		int j = 0;
		boolean okOnDelete = true;
		boolean relation = false;

		if (i != 0) {

			while (i > j & okOnDelete) {
				jsae = (JSONArray) jsa.getJSONArray(j);
				try {
					jso = (JSONObject) jsae.getJSONObject(0);
					bsrURI = (String) jso.get("value");

					if (!bsrURI.equals(bsrURISSA)) {
						okOnDelete = deleteSpecificRelation(bsrURI, bsrURIAcronimo, "ale63_childOrganizations", baseURL,
								user, password);
						message = "deleted relation name ale63_childOrganizations - " + bsrURIAcronimo
								+ " from object organization : " + bsrURI;
					} else
						relation = true;
				} catch (Exception e) {
					e.printStackTrace();
					okOnDelete = false;
				}

				j++;
			}

			result = true;

			if (okOnDelete) {

				if (!relation) {

					result = updateRelationShip(bsrURISSA, "ale63_childOrganizations", bsrURIAcronimo, baseURL, user,
							password);
				}
			} else
				result = false;

		}

		else {
			// ssa & acronimo present without relation particular case
			result = updateRelationShip(bsrURISSA, "ale63_childOrganizations", bsrURIAcronimo, baseURL, user, password);

		}

		if (!result)
			message = null;

		return message;
	}

	public String getGenericObjectByNameAndVersion(String name, String version, String baseURL, String user,
			String password) {

		// Create the variable to return
		String bsrURI = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']&p1=bsrURI";

		if (version == null || version.length() == 0)
			version = "00";

		query = query.replaceAll("%CATALOGNAME%", name);
		query = query.replaceAll("%VERSION%", version);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();

		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && bsrURI.equals("[]"))
			bsrURI = null;

		if (bsrURI != null) {
			JSONArray jsa = new JSONArray(bsrURI);
			bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (bsrURI != null) {
				bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}
		return bsrURI;

	}

	public String getGenericObjectByNameAndVersionExtended(String name, String version, String baseURL, String user,
			String password) {

		// Create the variable to return
		String bsrURI = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']&p1=bsrURI";

		if (version == null || version.length() == 0)
			version = "00";

		query = query.replaceAll("%CATALOGNAME%", name);
		query = query.replaceAll("%VERSION%", version);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			bsrURI = ">>**ERROR**>>" + e.getMessage();
			e.printStackTrace();

		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && !bsrURI.contains(">>**ERROR**>>")) {
			if (bsrURI != null && bsrURI.equals("[]"))
				bsrURI = null;

			if (bsrURI != null) {
				JSONArray jsa = new JSONArray(bsrURI);
				bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
				if (bsrURI != null) {
					bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
				}
			}
		}
		return bsrURI;

	}

	public String getPropertyValueFromGenericObjectByNameAndVersion(String name, String version, String propertyString,
			String baseURL, String user, String password) {

		// Create the variable to return
		String bsrURI = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']";

		if (version == null || version.length() == 0)
			version = "00";
		if (propertyString == null || propertyString.length() == 0)
			propertyString = "&p1=bsrURI";

		query = query.replaceAll("%CATALOGNAME%", name);
		query = query.replaceAll("%VERSION%", version);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query).append(propertyString);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();

		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && bsrURI.equals("[]"))
			bsrURI = null;

		return bsrURI;

	}

	// metodo inserito il 310117

	public String getProducerFromEndpointByUriNoSecurity(String endpointURI, String baseURL, String user,
			String password) {

		// Create the variable to return
		String result = null;

		// String query =
		// "Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[gep63_provides%28.%29/gep63_availableEndpoints%28.%29[matches%28@name,%27%ENDPOINTURI%%27%29%20and%20@sm63_USO_SICUREZZA=%27NO%27]]&p1=name&p2=gep63_ABILITAZ_INFRASTR";
		String query = "Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[gep63_provides%28.%29/gep63_availableEndpoints%28.%29[matches%28@name,%27%ENDPOINTURI%%27%29%20and%20@sm63_USO_SICUREZZA!=%27SI-Datapower%27]]&p1=name&p2=gep63_ABILITAZ_INFRASTR";
		// String query =
		// "Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[gep63_provides%28.%29/gep63_availableEndpoints%28.%29[matches%28@name,%27%ENDPOINTURI%%27%29]]&p1=name&p2=gep63_ABILITAZ_INFRASTR&p3=sm63_USO_SICUREZZA";

		query = query.replaceAll("%ENDPOINTURI%", endpointURI);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				result = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			result = "[" + WSRRUtility.jsonWithError(e.getMessage()) + "]";

		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return result;

	}

	// metodo inserito il 310117

	public String getProducerFromEndpointByUriFromProxyService(String endpointURI, String interfaceType, String baseURL,
			String user, String password) {

		// Create the variable to return
		String result = null;
		String effectiveProxyInterface = "sm63_SOAPProxy";

		if (interfaceType != null) {
			if (interfaceType.equalsIgnoreCase("SOAP"))
				effectiveProxyInterface = "sm63_SOAPProxy";
			if (interfaceType.equalsIgnoreCase("REST"))
				effectiveProxyInterface = "rest80_RESTProxy";
			if (interfaceType.equalsIgnoreCase("CALLABLE"))
				effectiveProxyInterface = "rest80_CALLABLEProxy";
		}
		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[gep63_provides%28.%29/gep63_availableEndpoints%28.%29/%INTERFACERELATION%%28.%29[matches%28@name,%27%ENDPOINTURI%%27%29]]&p1=name&p2=gep63_ABILITAZ_INFRASTR&p3=bsrURI";

		query = query.replaceAll("%INTERFACERELATION%", effectiveProxyInterface);
		query = query.replaceAll("%ENDPOINTURI%", endpointURI);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				result = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			result = "[" + WSRRUtility.jsonWithError(e.getMessage()) + "]";

		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return result;

	}

	// metodo inserito il 21012017

	public String getServiceVersionTipologyByNameAndVersion(String name, String version, String baseURL, String user,
			String password) {

		JSONArray jsa = null;
		JSONObject jso = null;
		String tipology = null;
		String bsrURI = null;
		JSONArray classificationRecord = null;

		bsrURI = this.getPropertyValueFromGenericObjectByNameAndVersion(name, version, null, baseURL, user, password);

		if (bsrURI != null) {
			jsa = new JSONArray(bsrURI);
			jso = (JSONObject) ((JSONArray) jsa.get(0)).get(0);
			bsrURI = WSRRUtility.getValueFromJsonObject(jso, "value");

			classificationRecord = this.getClassificationRecord(bsrURI, baseURL, user, password);

			if (classificationRecord != null && classificationRecord.length() != 0) {

				tipology = WSRRUtility.getObjectValueFromJSONArrayClassification(classificationRecord, "uri",

						"http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel");

				if (tipology != null) {
					tipology = tipology.substring(0, tipology.indexOf("ServiceVersion"));
				}

			}

		}

		return tipology;

	}

	// metodo inserito il 21012017

	public String getServiceVersionTipologyBybsrURI(String bsrURI, String baseURL, String user, String password) {

		String tipology = null;
		JSONArray classificationRecord = null;

		if (bsrURI != null) {

			classificationRecord = this.getClassificationRecord(bsrURI, baseURL, user, password);

			if (classificationRecord != null && classificationRecord.length() != 0) {

				tipology = WSRRUtility.getObjectValueFromJSONArrayClassification(classificationRecord, "uri",

						"http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel");

				if (tipology != null && tipology.contains("ServiceVersion")) {
					tipology = tipology.substring(0, tipology.indexOf("ServiceVersion"));
				}
			}
		}

		return tipology;

	}
	// metodo inserito il 21012017

	public String getServiceVersionSubTipologyBybsrURI(String bsrURI, String baseURL, String user, String password) {

		String subtipology = null;
		JSONArray classificationRecord = null;

		if (bsrURI != null) {

			classificationRecord = this.getClassificationRecord(bsrURI, baseURL, user, password);

			if (classificationRecord != null && classificationRecord.length() != 0) {

				subtipology = WSRRUtility.getObjectValueFromJSONArrayClassification(classificationRecord, "uri",

						"http://isp/");
			}

		}

		return subtipology;
	}

	// metodo inserito il 21012017

	public String getServiceVersionSubTipologyByNameAndVersion(String name, String version, String baseURL, String user,
			String password) {

		JSONArray jsa = null;
		JSONObject jso = null;
		String subtipology = null;
		String bsrURI = null;
		JSONArray classificationRecord = null;

		bsrURI = this.getPropertyValueFromGenericObjectByNameAndVersion(name, version, null, baseURL, user, password);

		if (bsrURI != null) {
			jsa = new JSONArray(bsrURI);
			jso = (JSONObject) ((JSONArray) jsa.get(0)).get(0);
			bsrURI = WSRRUtility.getValueFromJsonObject(jso, "value");

			classificationRecord = this.getClassificationRecord(bsrURI, baseURL, user, password);

			if (classificationRecord != null && classificationRecord.length() != 0) {

				subtipology = WSRRUtility.getObjectValueFromJSONArrayClassification(classificationRecord, "uri",

						"http://isp/");
			}

		}

		return subtipology;

	}

	public String getPropertyValueFromGenericObjectByName(String name, String propertyString, String baseURL,
			String user, String password) {

		// Create the variable to return
		String bsrURI = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%']";

		if (propertyString == null || propertyString.length() == 0)
			propertyString = "&p1=bsrURI";

		query = query.replaceAll("%CATALOGNAME%", name);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query).append(propertyString);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && bsrURI.equals("[]"))
			bsrURI = null;

		return bsrURI;

	}

	public String getOrganizationFromGenericObjectByNameAndVersion(String name, String version, String baseURL,
			String user, String password) {

		// Create the variable to return
		String orgName = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/ale63_owningOrganization(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')]&p1=name";

		query = query.replaceAll("%CATALOGNAME%", name);
		if (version == null || version.length() == 0)
			version = "00";
		query = query.replaceAll("%VERSION%", version);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				orgName = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (orgName != null && orgName.equals("[]"))
			orgName = null;

		if (orgName != null) {
			JSONArray jsa = new JSONArray(orgName);
			orgName = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (orgName != null && orgName != null) {
				orgName = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}

		return orgName;

	}

	public String getOrganizationFromGenericObjectByNameAndVersionExtended(String name, String version, String baseURL,
			String user, String password) {

		// Create the variable to return
		String orgName = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/ale63_owningOrganization(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')]&p1=name";

		query = query.replaceAll("%CATALOGNAME%", name);
		if (version == null || version.length() == 0)
			version = "00";
		query = query.replaceAll("%VERSION%", version);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				orgName = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			orgName = ">>**ERROR**>>" + e.getMessage();
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (orgName != null && !orgName.contains(">>**ERROR**>>")) {

			if (orgName != null && orgName.equals("[]"))
				orgName = null;

			if (orgName != null) {
				JSONArray jsa = new JSONArray(orgName);
				orgName = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
				if (orgName != null && orgName != null) {
					orgName = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
				}
			}
		}

		return orgName;

	}

	public String getChildOrganizationFromGenericObjectByName(String name, String baseURL, String user,
			String password) {

		// Create the variable to return
		String orgName = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%']/ale63_childOrganizations(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')]&p1=name";

		query = query.replaceAll("%CATALOGNAME%", name);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				orgName = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (orgName != null && orgName.equals("[]"))
			orgName = null;

		if (orgName != null) {
			JSONArray jsa = new JSONArray(orgName);
			orgName = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (orgName != null && orgName != null) {
				orgName = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}

		return orgName;

	}

	//
	// get service version associated to the SLA
	//

	public String getConsumersFromSLA(String SLABSRUri, String baseURL, String user, String password) {

		String resultdata = null;

		String classification = "[classifiedByAnyOf(.,'http://isp/%23REG','http://isp/%23SGOPEN','http://isp/%23SAOPEN','http://isp/%23IIBPARAL','http://isp/%23SCIIB','http://isp/%23BSNBP','http://isp/%23MPE','http://isp/%23SAHOST','http://isp/%23RTGEN','http://isp/%23BC')";

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject" + classification
				+ "/gep63_consumes%28.%29[@bsrURI=%27%SLABSRUI%%27]]&p1=bsrURI";

		query = query.replaceAll("%SLABSRUI%", SLABSRUri);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (resultdata != null && resultdata.equals("[]"))
			resultdata = null;

		if (resultdata != null) {
			JSONArray jsa = new JSONArray(resultdata);
			resultdata = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (resultdata != null && resultdata != null) {
				resultdata = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}

		return resultdata;

	}

	public JSONArray getConsumersFromSLAGeneral(String SLABSRUri, String baseURL, String user, String password) {

		JSONArray jsa = null;

		String resultdata = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[gep63_consumes(.)[@bsrURI='%SLABSRURI%']]&p1=bsrURI&p2=name&p3=primaryType";

		query = query.replaceAll("%SLABSRURI%", SLABSRUri);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (resultdata != null && !resultdata.equals("[]")) {

			jsa = new JSONArray(resultdata);

		}
		return jsa;
	}

	//
	// get provider (service Version) from SLD
	//
	//

	public String getProviderFromSLD(String SLADSRUri, String baseURL, String user, String password) {

		String resultdata = null;

		String classification = "[classifiedByAnyOf(.,'http://isp/%23REG','http://isp/%23SGOPEN','http://isp/%23SAOPEN','http://isp/%23IIBPARAL','http://isp/%23SCIIB','http://isp/%23BSNBP','http://isp/%23MPE','http://isp/%23SAHOST','http://isp/%23RTGEN','http://isp/%23BC')";

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject" + classification
				+ "/gep63_provides%28.%29[@bsrURI=%27%SLDBSRUI%%27]]&p1=bsrURI";

		query = query.replaceAll("%SLDBSRUI%", SLADSRUri);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (resultdata != null && resultdata.equals("[]"))
			resultdata = null;

		if (resultdata != null) {
			JSONArray jsa = new JSONArray(resultdata);
			resultdata = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (resultdata != null && resultdata != null) {
				resultdata = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}

		return resultdata;

	}

	//
	// get classification type from service -version
	//
	//

	public String getServiceVersionClassification(String BSRUriServiceVersion, String baseURL, String user,
			String password) {

		String resultdata = null;

		String query = "/Metadata/JSON/%BSRURI%/classifications";

		query = query.replaceAll("%BSRURI%", BSRUriServiceVersion);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (resultdata != null) {

			if (resultdata.contains("#REG"))
				resultdata = "REG";
			if (resultdata.contains("#SGOPEN"))
				resultdata = "SGOPEN";
			if (resultdata.contains("#SAOPEN"))
				resultdata = "SAOPEN";
			if (resultdata.contains("#IIBPARAL"))
				resultdata = "IIBPARAL";
			if (resultdata.contains("#SCIIB"))
				resultdata = "SCIIB";
			if (resultdata.contains("#BSNBP"))
				resultdata = "BSNBP";
			if (resultdata.contains("#MPE"))
				resultdata = "MPE";
			if (resultdata.contains("#SAHOST"))
				resultdata = "SAHOST";
			if (resultdata.contains("#RTGEN"))
				resultdata = "RTGEN";
			if (resultdata.contains("#BC"))
				resultdata = "BC";
			// 211216
			if (resultdata.contains("#SOM"))
				resultdata = "SOM";
			if (resultdata.contains("#EP"))
				resultdata = "EP";
			if (resultdata.contains("#AGGR"))
				resultdata = "AGGR";
			// 110117
			if (resultdata.contains("#SHLIB"))
				resultdata = "SHLIB";
			if (resultdata.contains("#FNZCD"))
				resultdata = "FNZCD";
		}

		return resultdata;

	}

	private JSONArray getClassificationRecord(String BSRUriServiceVersion, String baseURL, String user,
			String password) {

		JSONArray resultdata = null;

		String query = "/Metadata/JSON/%BSRURI%/classifications";

		query = query.replaceAll("%BSRURI%", BSRUriServiceVersion);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = new JSONArray(sb.toString());
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (resultdata != null && resultdata.length() == 0) {

			resultdata = null;
		}

		return resultdata;

	}

	public String checkClassification(String bsrURI, String classification, String baseURL, String user,
			String password) {

		String resultdata = null;

		String result = null;

		String query = "/Metadata/JSON/%BSRURI%/classifications";

		query = query.replaceAll("%BSRURI%", bsrURI);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			resultdata = ">>**ERROR**>>" + e.getMessage();
			e.printStackTrace();

		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (resultdata != null && !resultdata.contains(">>**ERROR**>>")) {

			if (resultdata.contains(classification)) {

				result = classification;
			}

		}

		return result;

	}

	//
	// get SSA from SLA
	//
	// steps: get SLD from SSA ,get provider from SLD , get Acronimo from
	// provider, get SSA from acronimo
	//

	public String getSSAFromSLA(String SLABSRUri, String baseURL, String user, String password) {

		String result = getSLDFromSLA(SLABSRUri, baseURL, user, password); // get
		// SLD

		result = getProviderFromSLD(result, baseURL, user, password); // get
		// Privider

		result = getOwningOrganizationFromGenericObjectByBsrUri(result, baseURL, user, password); // get
		// acronimo

		result = getSSAFromAcronimo(result, baseURL, user, password); // gest
		// SSA

		return result;

	}

	//
	// get SLD associated to SLA
	//

	public String getSLDFromSLA(String SLABSRUri, String baseURL, String user, String password) {

		String resultdata = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%SLABSRURI%']/gep63_agreedEndpoints%28.%29[exactlyClassifiedByAllOf%28.,%27http://www.ibm.com/xmlns/prod/serviceregistry/profile/v6r3/GovernanceEnablementModel%23ServiceLevelDefinition%27%29]&p1=bsrURI";

		query = query.replaceAll("%SLABSRURI%", SLABSRUri);

		// System.out.println(query);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (resultdata != null && resultdata.equals("[]"))
			resultdata = null;

		if (resultdata != null) {
			JSONArray jsa = new JSONArray(resultdata);
			resultdata = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (resultdata != null && resultdata != null) {
				resultdata = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}

		return resultdata;

	}

	//
	// get SSA from Acronimo
	//

	public String getSSAFromAcronimo(String bsrURIAcronimo, String baseURL, String user, String password) {

		String resultdata = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[classifiedByAnyOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')/ale63_childOrganizations(.)[@name='%BSRURIACRONIMO%']]&p1=name";
		query = query.replaceAll("%BSRURIACRONIMO%", bsrURIAcronimo);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (resultdata != null && resultdata.equals("[]"))
			resultdata = null;

		if (resultdata != null) {
			JSONArray jsa = new JSONArray(resultdata);
			resultdata = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (resultdata != null) {
				resultdata = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}
		return resultdata;
	}

	//
	// get all object of type specified by Primary Type
	//

	public JSONArray getAllObjectsSpecifiedByPrimaryType(String primaryType, String baseURL, String user,
			String password) {

		String resultdata = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@primaryType='%PRIMARYTYPE%']&p1=bsrURI";

		query = query.replaceAll("%PRIMARYTYPE%", primaryType);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				resultdata = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		JSONArray jsa = new JSONArray(resultdata);

		return jsa;

	}

	//
	// Check is a object is of particular primarytype if so return jsonArray
	// with selected properties
	//

	public JSONArray getObjectPropertiesData(String bsrURI, String queryString, String baseURL, String user,
			String password) {

		String properties = null;
		JSONArray jsa = null;

		// &p1=name

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']" + queryString;

		query = query.replaceAll("%BSRURI%", bsrURI);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				properties = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (properties != null) {

			jsa = new JSONArray(properties);

		}
		return jsa;

	}

	// 24042017
	public JSONArray getObjectPropertiesDataFromGeneralQuery(String userquery, String queryString, String baseURL,
			String user, String password) {

		String properties = null;
		JSONArray jsa = null;

		// &p1=name

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[%BSRURI%]" + queryString;

		query = query.replaceAll("%BSRURI%", userquery);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				properties = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (properties != null) {

			jsa = new JSONArray(properties);

		}
		return jsa;

	}
	//17052017 metodo utilizzato nel recupero della catena servizio/endpoint/proxy (nel ws di SLa e SSA)
	
	public JSONArray getObjectPropertiesDataFromGeneralQueryExtended(String userquery, String queryString, String baseURL,
			String user, String password) {

		String properties = null;
		JSONArray jsa = null;

		// &p1=name

		String query = "%BSRURI%" + queryString;

		query = query.replaceAll("%BSRURI%", userquery);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				properties = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (properties != null) {

			jsa = new JSONArray(properties);

		}
		return jsa;

	}

	// 24042017
	public String getDataFromGraphQuery(String userquery, String baseURL, String user, String password) {

		String graph = null;

		// &p1=name

		String query = "/Metadata/JSON/GraphQuery?query=/WSRR/GenericObject[%BSRURI%]";

		query = query.replaceAll("%BSRURI%", userquery);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				graph = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return graph;

	}

	//
	// Check is a object is of particular primarytype and name if so return
	// jsonArray with selected properties
	//

	public JSONArray getPropertiesByObjectNameAndPrimaryType(String name, String primaryType, String queryString,
			String baseURL, String user, String password) {

		String properties = null;
		JSONArray jsa = null;

		// &p1=name

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%NAME%'%20and%20@primaryType='%PRIMARYTYPE%']"
				+ queryString;

		query = query.replaceAll("%NAME%", name);
		query = query.replaceAll("%PRIMARYTYPE%", primaryType);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				properties = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (properties != null) {

			jsa = new JSONArray(properties);

		}
		return jsa;

	}

	public JSONArray getPropertiesByURI(String bsrURI, String queryString, String baseURL, String user,
			String password) {

		String properties = null;
		JSONArray jsa = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']" + queryString;

		query = query.replaceAll("%BSRURI%", bsrURI);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				properties = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (properties != null) {

			jsa = new JSONArray(properties);

		}
		return jsa;

	}

	public String getOwningOrganizationFromGenericObjectByBsrUri(String bsrURI, String baseURL, String user,
			String password) {

		// Create the variable to return
		String orgName = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/ale63_owningOrganization(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ALEModel%23Organization')]&p1=name";

		query = query.replaceAll("%BSRURI%", bsrURI);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				orgName = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (orgName != null && orgName.equals("[]"))
			orgName = null;

		if (orgName != null) {
			JSONArray jsa = new JSONArray(orgName);
			orgName = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (orgName != null && orgName != null) {
				orgName = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}

		return orgName;

	}

	public String getGenericObjectByNameAndPrimaryType(String name, String primaryType, String baseURL, String user,
			String password) {

		// Create the variable to return
		String bsrURI = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@primaryType='%PRIMARYTYPE%']&p1=bsrURI";

		query = query.replaceAll("%CATALOGNAME%", name);
		query = query.replaceAll("%PRIMARYTYPE%", primaryType);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && bsrURI.equals("[]"))
			bsrURI = null;

		if (bsrURI != null) {
			JSONArray jsa = new JSONArray(bsrURI);
			bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (bsrURI != null) {
				bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}
		return bsrURI;

	}

	public String getGenericObjectByNameAndPrimaryTypeExtended(String name, String primaryType, String baseURL,
			String user, String password) {

		// Create the variable to return
		String bsrURI = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@primaryType='%PRIMARYTYPE%']&p1=bsrURI";
		// String query =
		// "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[matches(@name,'%CATALOGNAME%')%20and%20@primaryType='%PRIMARYTYPE%']&p1=bsrURI";

		query = query.replaceAll("%CATALOGNAME%", name);
		query = query.replaceAll("%PRIMARYTYPE%", primaryType);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			bsrURI = ">>**ERROR**>>" + e.getMessage();
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && !bsrURI.contains(">>**ERROR**>>")) {

			if (bsrURI != null && bsrURI.equals("[]"))
				bsrURI = null;

			if (bsrURI != null) { // first element
				JSONArray jsa = new JSONArray(bsrURI);
				bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
				if (bsrURI != null) {
					bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
				}
			}
		}
		return bsrURI;

	}
	
	// 29052017
	
	public String generalWSRRQuery(String userquery, String baseURL,String user, String password) {

		String graph = null;

		// &p1=name

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(userquery);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			System.out.println(url);
			
			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				graph = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return graph;

	}

	// 11052017 aggiungo la gestione della specializzazione dell' endpoint

	public String getEndpointInfo(String name, String version, String interfaceType, String environment,
			String specializzazione, String baseURL, String user, String password) {

		String data = null;
		String query = null;
		String environmentQuery = "http://www.ibm.com/xmlns/prod/serviceregistry/6/1/GovernanceProfileTaxonomy%23%ENVIRONMENT%";
		environmentQuery = environmentQuery.replace("%ENVIRONMENT%", environment);

		if (version == null || version.length() == 0)
			version = "00";

		String querySOAP = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_provides(.)/gep63_availableEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel%23SOAPServiceEndpoint')%20and%20exactlyClassifiedByAllOf(.,'%ENVIRONMENT%')]&p1=bsrURI&p2=sm63_DATA_PRIMO_UTILIZZO&p3=sm63_SPECIALIZZAZIONE";
		String queryREST = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_provides(.)/gep63_availableEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/profile/v8r0/RESTModel%23RESTServiceEndpoint')%20and%20exactlyClassifiedByAllOf(.,'%ENVIRONMENT%')]&p1=bsrURI&p2=sm63_DATA_PRIMO_UTILIZZO&p3=sm63_SPECIALIZZAZIONE";

		String queryCICS = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_provides(.)/gep63_availableEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel%23CICSServiceEndpoint')%20and%20exactlyClassifiedByAllOf(.,'%ENVIRONMENT%')]&p1=bsrURI&p2=sm63_DATA_PRIMO_UTILIZZO&p3=sm63_SPECIALIZZAZIONE";
		String queryMQ = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_provides(.)/gep63_availableEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel%23MQServiceEndpoint')%20and%20exactlyClassifiedByAllOf(.,'%ENVIRONMENT%')]&p1=bsrURI&p2=sm63_DATA_PRIMO_UTILIZZO&p3=sm63_SPECIALIZZAZIONE";
		// 110117
		String queryCALLABLE = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_provides(.)/gep63_availableEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel%23CALLABLEServiceEndpoint')%20and%20exactlyClassifiedByAllOf(.,'%ENVIRONMENT%')]&p1=bsrURI&p2=sm63_DATA_PRIMO_UTILIZZO&p3=sm63_SPECIALIZZAZIONE";

		// 24032017
		String queryZRES = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_provides(.)/gep63_availableEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel%23ZRESServiceEndpoint')%20and%20exactlyClassifiedByAllOf(.,'%ENVIRONMENT%')]&p1=bsrURI&p2=sm63_DATA_PRIMO_UTILIZZO&p3=sm63_SPECIALIZZAZIONE";
		String queryWOLA = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_provides(.)/gep63_availableEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel%23WOLAServiceEndpoint')%20and%20exactlyClassifiedByAllOf(.,'%ENVIRONMENT%')]&p1=bsrURI&p2=sm63_DATA_PRIMO_UTILIZZO&p3=sm63_SPECIALIZZAZIONE";
		String querySHC = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_provides(.)/gep63_availableEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/v6r3/ServiceModel%23SHCServiceEndpoint')%20and%20exactlyClassifiedByAllOf(.,'%ENVIRONMENT%')]&p1=bsrURI&p2=sm63_DATA_PRIMO_UTILIZZO&p3=sm63_SPECIALIZZAZIONE";

		if (interfaceType.equals("REST"))
			query = queryREST;
		if (interfaceType.equals("SOAP"))
			query = querySOAP;
		if (interfaceType.equals("CICS"))
			query = queryCICS;
		if (interfaceType.equals("MQ"))
			query = queryMQ;
		// 110117
		if (interfaceType.equals("CALLABLE"))
			query = queryCALLABLE;
		// 24032017
		if (interfaceType.equals("ZRES"))
			query = queryZRES;
		if (interfaceType.equals("WOLA"))
			query = queryWOLA;
		if (interfaceType.equals("SHC"))
			query = querySHC;

		query = query.replaceAll("%CATALOGNAME%", name);
		query = query.replaceAll("%VERSION%", version);
		query = query.replaceAll("%ENVIRONMENT%", environmentQuery);

		HttpURLConnection urlConnection = null;

		// System.out.println(query);

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {

				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				data = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}

				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		// [{"value":"bc8f48bc-d1ab-4b30.9543.876ea38743a1","name":"bsrURI"},{"value":null,"name":"gep63_DATA_PRIMO_UTILIZZO"}]]

		String result = null;

		if (data != null && data.equals("[]"))
			result = null;
		else {
			if (specializzazione == null)
				specializzazione = "";

			JSONArray jsa = new JSONArray(data);
			JSONArray jsae = null;
			JSONObject jso = null;
			int countEP = 0;
			int i = jsa.length();
			int j = 0;
			String specializzazione_ = null;
			while (i > j) {
				jsae = (JSONArray) jsa.getJSONArray(j);
				specializzazione_ = WSRRUtility.getObjectValueFromJSONArrayData(jsae, "sm63_SPECIALIZZAZIONE");
				if (specializzazione_ != null && (specializzazione_.equals(specializzazione))) {
					countEP++;
					result = jsae.toString();
				}
				j++;
			}
			if (countEP != 1)
				result = null; // if more endpoint with same SPECIALIZZAZIONE
			// result=null
		}
		if (result != null) result="["+result+"]"; //23052017  forzo array quindi aggiungo []
		return result; 
	}

	// utilizzata nella funzione caricamento SLA

	// bsrUri interfaccia MQ padre

	public String getManualMQEndpointInfo(String bsrURI, String baseURL, String user, String password) {

		// Create the variable to return
		String data = null;
		String query = null;

		query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/sm63_mqEndpoint(.)&p1=bsrURI&p2=sm63_DATA_PRIMO_UTILIZZO_MQM&p3=sm63_DATA_ULTIMO_UTILIZZO_MQM";

		query = query.replaceAll("%BSRURI%", bsrURI);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {

				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				data = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}

				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (data != null && data.equals("[]"))
			data = null;

		return data;

	}

	// 13.08.2016
	// utilizzo la query nel totalizzatore quando lavoro con associazioni bv e
	// av
	// TOLTO!!!!!!!!!!!

	// 03022017
    // 12052017 aggiunta la gestione della specializzazione
	public String[] getEndpointNameFromBsrUriSLDEnvironmentCheckSecurity(String bsrURI, String environment,
			String interfaceType, boolean security, String specializzazione, String baseURL, String user,
			String password) throws Exception {

		// Create the variable to return
		String data = null;
		String query = null;
		String[] endpoints = new String[] { " ", " ", " ", " ", " ", " ", " ", " ", " ", " " };

		String environmentQuery = "http://www.ibm.com/xmlns/prod/serviceregistry/6/1/GovernanceProfileTaxonomy%23%ENVIRONMENT%";
		environmentQuery = environmentQuery.replaceAll("%ENVIRONMENT%", environment);

		query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_availableEndpoints()[exactlyClassifiedByAllOf(.,'%ENVIRONMENT%')]&p1=bsrURI&p2=name&p3=sm63_USO_SICUREZZA&p4=sm63_SPECIALIZZAZIONE";
		query = query.replaceAll("%BSRURI%", bsrURI);
		query = query.replaceAll("%ENVIRONMENT%", environmentQuery);

		if (specializzazione == null)
			specializzazione = "";

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				data = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("errore: in getEndpointNameFromBsrUriSLDEnvironmentCheckSecurity");
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (data != null) {

			JSONArray jsa = new JSONArray(data);

			JSONArray jsaint = null;
			JSONArray jsaintCurrent = null;
			JSONObject jso = null;

			String localUri = null;
			String sicurezza = null;
			String endpointName = null;
			int c = 0;

			String specializzazione_ = null;
			int specializzazioneCount = 0;

			// ciclo per recuperare l'array con la specializzazione

			for (int i = 0; i < jsa.length(); i++) {
				jsaint = jsa.getJSONArray(i);
				for (int ii = 0; ii < jsaint.length(); ii++) {
					jso = (JSONObject) jsaint.get(ii);

					if (jso.getString("name").equals("sm63_SPECIALIZZAZIONE")) {
						if (!jso.isNull("value"))
							specializzazione_ = (String) jso.get("value");
						else
							specializzazione_ = "";

						if (specializzazione_.equals(specializzazione)) {
							specializzazioneCount++;
							jsaintCurrent = jsaint;
						}
					}

				}
			}

			if (specializzazioneCount >= 2) {
				// trovati piu' EP con stessa specializzazione , lancio
				// eccezione
				throw new Exception(
						"errore: in getEndpointNameFromBsrUriSLDEnvironmentCheckSecurity endpoint incongruente");
			}

			if (jsaintCurrent != null) {
				for (int ii = 0; ii < jsaintCurrent.length(); ii++) {
					jso = (JSONObject) jsaintCurrent.get(ii);

					if (jso.getString("name").equals("bsrURI")) {
						localUri = (String) jso.getString("value");
					}
					if (jso.getString("name").equals("sm63_USO_SICUREZZA")) {

						if (!jso.isNull("value")) {
							sicurezza = (String) jso.get("value");
							if (sicurezza.equals(""))
								sicurezza = "NO";
						} else
							sicurezza = "NO";
					}

					if (jso.getString("name").equals("name")) { // endpoint

						if (!jso.isNull("value"))
							endpointName = (String) jso.get("value");
						else
							endpointName = "";
					}

				}

				if (security) {
					if (sicurezza != null && !sicurezza.equals("NO")) {
						endpointName = this.getProxyEndpointNameFromEndpointFilteredByInterface(localUri, interfaceType,
								baseURL, user, password);
						if (c <= 9) {
							endpoints[c] = endpointName;
							c++;
						}
					}

				} else {

					if (c <= 9) {
						endpoints[c] = endpointName;
						c++;
					}
				}

			}
		}
		return endpoints;

	}

	// 03022017

	public String getProxyEndpointNameFromEndpointFilteredByInterface(String bsrURI, String interfaceType,
			String baseURL, String user, String password) throws Exception {

		// Create the variable to return
		String data = null;
		String query = null;
		String effectiveProxyInterface = null;
		JSONObject jso = null;

		if (interfaceType != null) {
			if (interfaceType.equalsIgnoreCase("SOAP"))
				effectiveProxyInterface = "sm63_SOAPProxy";
			if (interfaceType.equalsIgnoreCase("REST"))
				effectiveProxyInterface = "rest80_RESTProxy";
			if (interfaceType.equalsIgnoreCase("CALLABLE"))
				effectiveProxyInterface = "rest80_CALLABLEProxy";
		}

		query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/%RELATION%()&p1=name";
		query = query.replaceAll("%BSRURI%", bsrURI);
		query = query.replaceAll("%RELATION%", effectiveProxyInterface);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				data = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("errore: in getProxyEndpointNameFromEndpointFilteredByInterface");
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (data != null && !data.equals("[]")) { // fix del 24032017 aggiunto
			// controllo se risultato []
			JSONArray jsona1 = new JSONArray(data);
			JSONArray jsona2 = (JSONArray) jsona1.get(0);
			jso = (JSONObject) jsona2.get(0);
			data = WSRRUtility.getValueFromJsonObject(jso, "value");
		} else
			data = "[]";

		return data;

	}

	// 03022017
	// 12052017 aggiunta la gestione della specializzazione
	//TOLTO

	// 03022017
	public boolean isEndpointSecurityPresentByProviderBsrURI(String bsrURI, String securitytype, String baseURL,
			String user, String password) throws Exception {

		// Create the variable to return
		JSONArray data = null;
		String query = null;
		Boolean result = false;

		query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_provides()/gep63_availableEndpoints()[@sm63_USO_SICUREZZA='%TIPOSICUREZZA%']&p1=bsrURI";
		query = query.replaceAll("%BSRURI%", bsrURI);
		query = query.replaceAll("%TIPOSICUREZZA%", securitytype);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				data = new JSONArray(sb.toString());
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("errore: isEndpointSecurityPresentByProviderBsrURI");
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (data != null) {

			if (data.length() != 0)
				result = true;

		}

		return result;

	}
	// 03022017

	public String getTargetWSRRFromBootstrapRuntime(String catalogQuery, String baseURL, String user, String password)
			throws Exception {

		// Create the variable to return
		String data = null;
		String query = null;
		Boolean result = false;

		query = "Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGQUERY%'%20and%20@version='00']/gep63_provides(.)/gep63_availableEndpoints(.)[exactlyClassifiedByAllOf(.,'http://www.ibm.com/xmlns/prod/serviceregistry/profile/v8r0/RESTModel%23RESTServiceEndpoint')]&p1=name";
		query = query.replaceAll("%CATALOGQUERY%", catalogQuery);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				data = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("errore: getTargetWSRRFromBootstrapRuntime");
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (data != null && !data.equals("[]")) {
			JSONArray jsona1 = new JSONArray(data);
			JSONArray jsona2 = (JSONArray) jsona1.get(0);
			JSONObject jso = (JSONObject) jsona2.get(0);
			data = WSRRUtility.getValueFromJsonObject(jso, "value");
		}

		return data;

	}

	public JSONArray getEndpointInfoFromSLD(String bsrURI, String environment, String baseURL, String user,
			String password) {

		// Create the variable to return
		JSONArray data = null;
		String query = null;

		String environmentQuery = "http://www.ibm.com/xmlns/prod/serviceregistry/6/1/GovernanceProfileTaxonomy%23%ENVIRONMENT%";
		environmentQuery = environmentQuery.replaceAll("%ENVIRONMENT%", environment);

		query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@bsrURI='%BSRURI%']/gep63_availableEndpoints(.)[exactlyClassifiedByAllOf(.,'%ENVIRONMENT%')]&p1=bsrURI&p2=sm63_DATA_PRIMO_UTILIZZO&p3=sm63_DATA_ULTIMO_UTILIZZO";
		query = query.replaceAll("%BSRURI%", bsrURI);
		query = query.replaceAll("%ENVIRONMENT%", environmentQuery);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				data = new JSONArray(sb.toString());
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (data != null && data.equals("[]"))
			data = null;

		return data;

	}

	public JSONArray getAssociatedInterfaces(String name, String version, String baseURL, String user,
			String password) {

		// Create the variable to return

		JSONArray data = null;
		String query = null;

		if (version == null || version.length() == 0)
			version = "00";

		// String
		// query="/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name=%CATALOGNAME%27%20and%20@version=%27VERSION%27]/gep63_provides%28.%29/gep63_availableEndpoints%28.%29&p1=bsrURI&p2=name";

		query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%'%20and%20@version='%VERSION%']/gep63_provides%28.%29/gep63_serviceInterface%28.%29&p1=bsrURI&p2=name";

		query = query.replace("%CATALOGNAME%", name);
		query = query.replace("%VERSION%", version);

		// tipo interfaccia non supportato

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				data = new JSONArray(sb.toString());
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}

				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		// [{"value":"bc8f48bc-d1ab-4b30.9543.876ea38743a1","name":"bsrURI"},{"value":null,"name":"gep63_DATA_PRIMO_UTILIZZO"}]]

		if (data != null && data.equals("[]"))
			data = null;

		return data;

	}

	public String getGenericObjectByName(String name, String baseURL, String user, String password) {

		// Create the variable to return
		String bsrURI = null;

		String query = "/Metadata/JSON/PropertyQuery?query=/WSRR/GenericObject[@name='%CATALOGNAME%']&p1=bsrURI";

		query = query.replaceAll("%CATALOGNAME%", name);

		HttpURLConnection urlConnection = null;

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(baseURL).append(query);
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			int responsecode = urlConnection.getResponseCode();
			if (responsecode == 200 || (responsecode == 201)) {
				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null && bsrURI.equals("[]"))
			bsrURI = null;

		if (bsrURI != null) {
			JSONArray jsa = new JSONArray(bsrURI);
			bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (bsrURI != null) {
				bsrURI = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		}
		return bsrURI;

	}

	public String createWSRRGenericObject(String xmlData, String verb, String createURL, String user, String password) {

		// Create the variable to return
		String bsrURI = null;
		HttpURLConnection urlConnection = null;
		StringBuffer sb = new StringBuffer();
		sb.append(createURL).append("/Content/GenericObject");
		try {
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod(verb);
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);

			}

			byte[] postDataBytes = xmlData.getBytes("UTF-8");
			urlConnection.getOutputStream().write(postDataBytes);

			int returnCode = urlConnection.getResponseCode();

			if (returnCode == 200 || (returnCode == 201)) {

				InputStream is = null;
				is = urlConnection.getInputStream();
				int ch;
				sb.delete(0, sb.length());
				while ((ch = is.read()) != -1) {
					sb.append((char) ch);
				}
				bsrURI = sb.toString();
				is.close();

			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
				throw new Exception("Unable to create WSRR GenericObject " + stringBuffer.toString());
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		if (bsrURI != null)
			bsrURI = WSRRUtility.getbsrURI(bsrURI);

		return bsrURI;
	}

	public boolean updateSinglePropertyXMLFormat(String bsrURIToChange, String propertyName, String propertyValue,
			String createURL, String user, String password) {

		// Create the variable to return
		boolean result = false;
		String query = "/Metadata/XML/%BSRURI%/properties/%PROPERTYNAME%";
		String value = "<property value=\"%VALUE%\"/> ";
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
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
				throw new Exception("Unable to create WSRR GenericObject " + stringBuffer.toString());
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return result;
	}

	public boolean updateRelationShip(String bsrURIToChange, String relationName, String bsrURITarget, String createURL,
			String user, String password) {

		// Create the variable to return
		boolean result = false;
		/// Metadata/JSON/<bsrURI>/properties/Metadata/JSON/<bsrURI>/relationships
		// String
		/// value="[{\"delete\":\"false\",\"relationships\":[{\"name\":\"%RELATIONNAME%\",\"targetBsrURI\":\"%TARGETBSRURI%\"}]}]";
		String query = "/Metadata/XML/%BSRURI%/relationships";

		String value = "<relationships delete=\"false\"><relationship name=\"%RELATIONNAME%\" targetBsrURI=\"%TARGETBSRURI%\"/></relationships>";

		if (bsrURIToChange == null || bsrURIToChange.length() == 0)
			bsrURIToChange = "bsrURI_not_Specified";
		if (relationName == null || relationName.length() == 0)
			relationName = "relationName_not_Specified";
		if (bsrURITarget == null || bsrURITarget.length() == 0)
			bsrURITarget = "bsrUriTarget_not_Specified";

		query = query.replaceAll("%BSRURI%", bsrURIToChange);
		value = value.replaceAll("%RELATIONNAME%", relationName);
		value = value.replaceAll("%TARGETBSRURI%", bsrURITarget);

		HttpURLConnection urlConnection = null;
		StringBuffer sb = new StringBuffer();
		sb.append(createURL).append(query);
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
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
				throw new Exception("Error: updateRelationShip " + stringBuffer.toString());
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return result;
	}

	// 24042017
	public boolean updateEmptyRelationShip(String bsrURIToChange, String relationName, String createURL, String user,
			String password) {

		// Create the variable to return
		boolean result = false;
		/// Metadata/JSON/<bsrURI>/properties/Metadata/JSON/<bsrURI>/relationships
		// String
		/// value="[{\"delete\":\"false\",\"relationships\":[{\"name\":\"%RELATIONNAME%\",\"targetBsrURI\":\"%TARGETBSRURI%\"}]}]";
		String query = "/Metadata/XML/%BSRURI%/relationships";

		String value = "<relationships delete=\"false\"><relationship name=\"%RELATIONNAME%\" /></relationships>";

		if (bsrURIToChange == null || bsrURIToChange.length() == 0)
			bsrURIToChange = "bsrURI_not_Specified";
		if (relationName == null || relationName.length() == 0)
			relationName = "relationName_not_Specified";

		query = query.replaceAll("%BSRURI%", bsrURIToChange);
		value = value.replaceAll("%RELATIONNAME%", relationName);

		HttpURLConnection urlConnection = null;
		StringBuffer sb = new StringBuffer();
		sb.append(createURL).append(query);
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
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
				throw new Exception("Error: updateRelationShip " + stringBuffer.toString());
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return result;
	}

	public boolean updateSinglePropertyJSONFormat(String bsrURIToChange, String propertyName, String propertyValue,
			String createURL, String user, String password) {

		// Create the variable to return
		boolean result = false;
		String query = "/Metadata/JSON/%BSRURI%/properties/%PROPERTYNAME%";
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
				// BufferedReader reader = new BufferedReader(new
				// InputStreamReader(urlConnection.getInputStream()));
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
				throw new Exception("Unable to update WSRR GenericObject " + stringBuffer.toString());
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return result;
	}

	public boolean changeGovernanceState(String bsrUri, String[] transations, String createURL, String user,
			String password) {

		boolean result = false;
		;

		HttpURLConnection urlConnection = null;
		StringBuffer sb = new StringBuffer();

		for (String action : transations) {
			result = false;
			sb.delete(0, sb.length());
			sb.append(createURL).append("/Metadata/XML/").append(bsrUri).append("/governance/").append(action);

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

				int returnCode = urlConnection.getResponseCode();

				if (returnCode == 200 || (returnCode == 201)) {

					InputStream is = null;
					is = urlConnection.getInputStream();
					int ch;
					sb.delete(0, sb.length());
					while ((ch = is.read()) != -1) {
						sb.append((char) ch);
					}
					result = true;

				} else {
					BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					StringBuffer stringBuffer = new StringBuffer();
					String line = null;
					while (null != (line = reader.readLine())) {
						stringBuffer.append(line);
					}
					reader.close();
				}
				urlConnection.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}

			finally {
				if (urlConnection != null)
					urlConnection.disconnect();
			}

			if (!result)
				break;

		}

		return result;
	}

	public boolean deleteWSRRObject(String bsrURI, String createURL, String user, String password) {

		boolean result = false;

		// Create the variable to return
		HttpURLConnection urlConnection = null;
		StringBuffer sb = new StringBuffer();
		sb.append(createURL).append("/Content/").append(bsrURI);
		try {
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("DELETE");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);

			}

			int returnCode = urlConnection.getResponseCode();

			if (returnCode == 200 || (returnCode == 201)) {

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
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
				throw new Exception("Unable to delete WSRR GenericObject " + stringBuffer.toString());
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return result;

	}

	public boolean deleteSpecificRelation(String bsrURIBaseObject, String bsrURSpecific, String relationName,
			String createURL, String user, String password) {

		boolean result = false;

		String query = "/Metadata/XML/%BASEOBJECT%/relationships/%RELATIONNAME%/%TARTGETOBJECT%";
		query = query.replaceAll("%BASEOBJECT%", bsrURIBaseObject);
		query = query.replaceAll("%RELATIONNAME%", relationName);
		query = query.replaceAll("%TARTGETOBJECT%", bsrURSpecific);

		// Create the variable to return
		HttpURLConnection urlConnection = null;
		StringBuffer sb = new StringBuffer();
		sb.append(createURL).append(query);
		try {
			URL url = new URL(sb.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("DELETE");
			urlConnection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);

			if (user != null && password != null) {

				String userPassword = user + ":" + password;

				String encoding = new String(Base64.encodeBase64(userPassword.getBytes()));

				urlConnection.setRequestProperty("Authorization", "Basic " + encoding);

			}

			int returnCode = urlConnection.getResponseCode();

			if (returnCode == 200 || (returnCode == 201)) {

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
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while (null != (line = reader.readLine())) {
					stringBuffer.append(line);
				}
				reader.close();
				throw new Exception("Unable to delete WSRR GenericObject " + stringBuffer.toString());
			}
			urlConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return result;

	}

	public static String extractPropertyValue(String data) {
		String result = null;

		try {
			JSONArray jsa = new JSONArray(data);
			result = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("name");
			if (result != null) {
				result = ((JSONObject) ((JSONArray) jsa.get(0)).get(0)).getString("value");
			}
		} catch (Exception ex) {
			// nothing
		}

		return result;

	}

	private static String getbsrURI(String response) {

		String bsrURI = "";

		if (response != null && response.contains("bsrURI")) {
			int pos = response.indexOf("name=\"bsrURI\" value=\"");// 21
			bsrURI = response.substring(pos + 21, pos + 21 + 36);
		}

		return bsrURI;

	}

	public static String unescape(String string) {
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			Character ch = new Character(string.charAt(i));
			res.append(ch);
		}
		return res.toString();

	}

	// metodo aggiunto il 21012017
	private static String getObjectValueFromJSONArrayClassification(JSONArray jsa, String key, String field) {

		int i = 0;

		int elements = jsa.length();

		String current;

		JSONObject jso;

		String result = "";

		while (i < elements) {

			jso = jsa.getJSONObject(i);

			try {

				current = ((String) jso.get(key));

			} catch (Exception ex) {

				current = "";
			}

			if (current.startsWith(field)) {

				result = WSRRUtility.getData(current);

				break;

			}

			i++;

		}

		return result;

	}

	// metodo aggiunto il 21012017
	private static String getData(String input) {

		return input.substring(input.indexOf("#", 0) + 1, input.length());

	}

	// metodo aggiunto il 31012017

	private static String jsonWithError(String errorMessage) {

		return "{\"chiamata_in_errore\":\"" + errorMessage + "\"}";

	}

	private static String getServ(String k) {
		String result = "";

		int i = 0;

		i = k.indexOf("ServiceVersion");

		return k.substring(0, i);
	}

}
