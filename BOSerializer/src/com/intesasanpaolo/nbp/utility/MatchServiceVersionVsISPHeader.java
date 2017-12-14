package com.intesasanpaolo.nbp.utility;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class MatchServiceVersionVsISPHeader {
//14122017.
	

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
				if (MatchServiceVersionVsISPHeader.checkTimestamp(tagValue) == null) {
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

				int len=ts.length();
				if (len==20) len=19;
				int difflen = 19 - len;

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
}
