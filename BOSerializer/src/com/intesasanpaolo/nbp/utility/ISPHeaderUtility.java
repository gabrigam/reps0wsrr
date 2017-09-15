package com.intesasanpaolo.nbp.utility;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import teamworks.TWList;
import teamworks.TWObjectFactory;
import teamworks.TWObject;

public class ISPHeaderUtility {

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

	@SuppressWarnings("unchecked")
	private static HashMap ISPHeaderScompositionLocal(String ispHeader) {

		Document doc = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		
		InputSource source = new InputSource(new StringReader(ispHeader));
		try {
			doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap tokenMap = new HashMap();

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

			int countag = Integer
					.parseInt(xpath.evaluate("count(/ISPWebservicesHeader/AdditionalBusinessInfo/Param)", doc));

			String tagValue = null;
			for (int ii = 1; ii <= countag; ii++) {

				tagValue = xpath.evaluate(
						"/ISPWebservicesHeader/AdditionalBusinessInfo/Param[" + String.valueOf(ii) + "]/@Name", doc);
				// System.out.println(xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/Param["+String.valueOf(ii)+"]/@Name",
				// doc));
				if (tagValue != null && tagValue.length() != 0)
					tokenMap.put(tagValue, xpath.evaluate(
							"/ISPWebservicesHeader/AdditionalBusinessInfo/Param[" + String.valueOf(ii) + "]/@Value",
							doc));
			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			tokenMap.put("ISPHEADERMALFORMED", "TRUE");
			e.printStackTrace();
		}

		return tokenMap;
	}

	@SuppressWarnings("unchecked")

	public static TWObject ISPHeaderTagValues(String ispHeader) {

		Document doc = null;
		TWObject ISPHeaderValues = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		if (ispHeader != null) {

			InputSource source = new InputSource(new StringReader(ispHeader));
			try {
				ISPHeaderValues = (TWObject) TWObjectFactory.createObject();
				doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
			} catch (XPathExpressionException e) {

				ISPHeaderValues.setPropertyValue("ERROR", e.getMessage());

				e.printStackTrace();
			}

			catch (Exception e1) {

				ISPHeaderValues.setPropertyValue("ERROR", e1.getMessage());

				e1.printStackTrace();
			}

			try {

				ISPHeaderValues.setPropertyValue("TransactionId",
						xpath.evaluate("/ISPWebservicesHeader/RequestInfo/TransactionId/text()", doc));
				ISPHeaderValues.setPropertyValue("Timestamp", ISPHeaderUtility
						.checkTimestamp(xpath.evaluate("/ISPWebservicesHeader/RequestInfo/Timestamp/text()", doc)));
				ISPHeaderValues.setPropertyValue("ServiceID",
						xpath.evaluate("/ISPWebservicesHeader/RequestInfo/ServiceID/text()", doc));
				ISPHeaderValues.setPropertyValue("ServiceVersion",
						xpath.evaluate("/ISPWebservicesHeader/RequestInfo/ServiceVersion/text()", doc));
				ISPHeaderValues.setPropertyValue("Language",
						xpath.evaluate("/ISPWebservicesHeader/RequestInfo/Language/text()", doc));

				ISPHeaderValues.setPropertyValue("UserID",
						xpath.evaluate("/ISPWebservicesHeader/OperatorInfo/@UserID", doc));
				ISPHeaderValues.setPropertyValue("IsVirtualUser",
						xpath.evaluate("/ISPWebservicesHeader/OperatorInfo/@IsVirtualUser", doc));
				ISPHeaderValues.setPropertyValue("NotISPUserID",
						xpath.evaluate("/ISPWebservicesHeader/OperatorInfo/@NotISPUserID", doc));

				ISPHeaderValues.setPropertyValue("ISPCallerCompanyIDCode",
						xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/ISPCallerCompanyIDCode/text()", doc));
				ISPHeaderValues.setPropertyValue("NotISPCompanyIDCode",
						xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/NotISPCompanyIDCode/text()", doc));
				ISPHeaderValues.setPropertyValue("ISPBranchCode",
						xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/ISPBranchCode/text()", doc));
				ISPHeaderValues.setPropertyValue("ISPServiceCompanyIDCode",
						xpath.evaluate("/ISPWebservicesHeader/CompanyInfo/ISPServiceCompanyIDCode/text()", doc));

				ISPHeaderValues.setPropertyValue("CustomerID",
						xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/CustomerID/text()", doc));
				ISPHeaderValues.setPropertyValue("BusinessProcessName",
						xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessProcessName/text()", doc));
				ISPHeaderValues.setPropertyValue("BusinessProcessID",
						xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessProcessID/text()", doc));
				ISPHeaderValues.setPropertyValue("BusinessOperation",
						xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessOperation/text()", doc));
				ISPHeaderValues.setPropertyValue("BusinessFileID",
						xpath.evaluate("/ISPWebservicesHeader/BusinessInfo/BusinessFileID/text()", doc));

				ISPHeaderValues.setPropertyValue("ChannelIDCode",
						xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/ChannelIDCode/text()", doc));
				ISPHeaderValues.setPropertyValue("ApplicationID",
						xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/ApplicationID/text()", doc));
				ISPHeaderValues.setPropertyValue("CallerServerName",
						xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/CallerServerName/text()", doc));
				ISPHeaderValues.setPropertyValue("CallerProgramName",
						xpath.evaluate("/ISPWebservicesHeader/TechnicalInfo/CallerProgramName/text()", doc));

				/**
				ISPHeaderValues.setPropertyValue("CodUnitaOperativa",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodUnitaOperativa/text()", doc));
				ISPHeaderValues.setPropertyValue("DataContabile",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/DataContabile/text()", doc));
				ISPHeaderValues.setPropertyValue("FlagPaperless",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/FlagPaperless/text()", doc));
				ISPHeaderValues.setPropertyValue("CodABI",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodABI/text()", doc));
				ISPHeaderValues.setPropertyValue("CodOperatività",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodOperatività/text()", doc));
				ISPHeaderValues.setPropertyValue("CodTerminaleCics",
						xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/CodTerminaleCics/text()", doc));
                **/
				
				String tagValue = null;

				int countag = Integer
						.parseInt(xpath.evaluate("count(/ISPWebservicesHeader/AdditionalBusinessInfo/Param)", doc));
				String tagName = null;

				if (countag != 0) {

					for (int ii = 1; ii <= countag; ii++) {

						tagName = xpath.evaluate(
								"/ISPWebservicesHeader/AdditionalBusinessInfo/Param[" + String.valueOf(ii) + "]/@Name",
								doc);
						if (tagName != null && tagName.length() != 0) {
							tagValue = xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo/Param["
									+ String.valueOf(ii) + "]/@Value", doc);
							ISPHeaderValues.setPropertyValue(tagName, tagValue);

						}
					}
				}

				else {
					
					// *deprecated*
					
					countag = Integer
							.parseInt(xpath.evaluate("count(/ISPWebservicesHeader/AdditionalBusinessInfo)", doc));
					tagName = null;

					if (countag != 0) {

						for (int ii = 1; ii <= countag; ii++) {

							tagName = xpath.evaluate(
									"/ISPWebservicesHeader/AdditionalBusinessInfo[" + String.valueOf(ii) + "]/@Name",
									doc);
							if (tagName != null && tagName.length() != 0) {
								tagValue = xpath.evaluate("/ISPWebservicesHeader/AdditionalBusinessInfo["
										+ String.valueOf(ii) + "]/@Value", doc);
								ISPHeaderValues.setPropertyValue(tagName, tagValue);

							}
						}
					}
					
				}

			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				ISPHeaderValues.setPropertyValue("ERROR", e.getMessage());
				e.printStackTrace();
			}
		} else {
			try {
				ISPHeaderValues = (TWObject) TWObjectFactory.createObject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ISPHeaderValues.setPropertyValue("ERROR", "ISP Header Passato == null");
		}
		return ISPHeaderValues;

	}

	public static Object testByte(String data) throws Exception {
		return data.getBytes();
	}

	public static void main(String[] args) {

		String serviceID = null;
		String applicationID = null;
		String timeStamp = null;
		String timeStamp_ = null;
		boolean error = false;

		HashMap ispHeaderMap = new HashMap();

		// String ispHeader="<ISPWebservicesHeader><!--You may enter the
		// following 6 items in any order--> <RequestInfo><!--You may enter the
		// following 5 items in any
		// order--><TransactionId>TID</TransactionId><Timestamp>20170630125040000252</Timestamp><ServiceID>SID</ServiceID><ServiceVersion>SERVICEVERION</ServiceVersion><!--Optional:--><Language>IT</Language></RequestInfo><OperatorInfo
		// UserID=\"U0H1963\" IsVirtualUser=\"VIUSER\"
		// NotISPUserID=\"NOTVIU\"/><CompanyInfo><!--You may enter the following
		// 4 items in any
		// order--><ISPCallerCompanyIDCode>[ISPCallerCompanyIDCode]</ISPCallerCompanyIDCode><!--Optional:--><NotISPCompanyIDCode>[NotISPCompanyIDCode]</NotISPCompanyIDCode><!--Optional:--><ISPBranchCode>[ISPBranchCode]</ISPBranchCode><ISPServiceCompanyIDCode>[ISPServiceCompanyIDCode]</ISPServiceCompanyIDCode></CompanyInfo><BusinessInfo><!--You
		// may enter the following 5 items in any
		// order--><CustomerID>[CustomerID]</CustomerID><!--Optional:--><BusinessProcessName>[BusinessProcessName]</BusinessProcessName><!--Optional:--><BusinessProcessID>[BusinessProcessID]</BusinessProcessID><!--Optional:--><BusinessOperation>[BusinessOperation]</BusinessOperation><!--Optional:--><BusinessFileID>[BusinessFileID]</BusinessFileID></BusinessInfo>
		// <TechnicalInfo> <!--You may enter the following 4 items in any
		// order--> <ChannelIDCode>[ChannelIDCode]</ChannelIDCode>
		// <ApplicationID>[ApplicationID]</ApplicationID>
		// <CallerServerName>[CallerServerName]</CallerServerName>
		// <CallerProgramName>[CallerProgramName]</CallerProgramName>
		// </TechnicalInfo> <AdditionalBusinessInfo> <!--1 or more
		// repetitions:-->
		// <CodUnitaOperativa>[CodUnitaOperativa]</CodUnitaOperativa><DataContabile>[DataContabile]</DataContabile><FlagPaperless>[FlagPaperless>]</FlagPaperless>
		// <CodABI>[CodABI]</CodABI><CodOperatività>[CodOperarività]</CodOperatività><CodTerminaleCics>[CodTerminaleCics]</CodTerminaleCics></AdditionalBusinessInfo></ISPWebservicesHeader>";
		// String ispHeader = "<ISPWebservicesHeader><!--You may enter the
		// following 6 items in any order--> <RequestInfo><!--You may enter the
		// following 5 items in any
		// order--><TransactionId>TID</TransactionId><Timestamp>20170630125040000252</Timestamp><ServiceID_>SID</ServiceID_><ServiceVersion>SERVICEVERION</ServiceVersion><!--Optional:--><Language>IT</Language></RequestInfo><OperatorInfo
		// UserID=\"U0H1963\" IsVirtualUser=\"VIUSER\"
		// NotISPUserID=\"NOTVIU\"/><CompanyInfo><!--You may enter the following
		// 4 items in any
		// order--><ISPCallerCompanyIDCode>[ISPCallerCompanyIDCode]</ISPCallerCompanyIDCode><!--Optional:--><NotISPCompanyIDCode>[NotISPCompanyIDCode]</NotISPCompanyIDCode><!--Optional:--><ISPBranchCode>[ISPBranchCode]</ISPBranchCode><ISPServiceCompanyIDCode>[ISPServiceCompanyIDCode]</ISPServiceCompanyIDCode></CompanyInfo><BusinessInfo><!--You
		// may enter the following 5 items in any
		// order--><CustomerID>[CustomerID]</CustomerID><!--Optional:--><BusinessProcessName>[BusinessProcessName]</BusinessProcessName><!--Optional:--><BusinessProcessID>[BusinessProcessID]</BusinessProcessID><!--Optional:--><BusinessOperation>[BusinessOperation]</BusinessOperation><!--Optional:--><BusinessFileID>[BusinessFileID]</BusinessFileID></BusinessInfo>
		// <TechnicalInfo> <!--You may enter the following 4 items in any
		// order--> <ChannelIDCode>[ChannelIDCode]</ChannelIDCode>
		// <ApplicationID>[ApplicationID]</ApplicationID>
		// <CallerServerName>[CallerServerName]</CallerServerName>
		// <CallerProgramName>[CallerProgramName]</CallerProgramName>
		// </TechnicalInfo> <AdditionalBusinessInfo> <!--1 or more
		// repetitions:-->
		// <CodUnitaOperativa>[CodUnitaOperativa]</CodUnitaOperativa><DataContabile>[DataContabile]</DataContabile><FlagPaperless>[FlagPaperless>]</FlagPaperless>
		// <CodABI>[CodABI]</CodABI><CodOperatività>[CodOperarività]</CodOperatività><CodTerminaleCics>[CodTerminaleCics]</CodTerminaleCics></AdditionalBusinessInfo></ISPWebservicesHeader>";
		String ispHeader = "<ISPWebservicesHeader><!--You may enter the following 6 items in any order--> <RequestInfo><!--You may enter the following 5 items in any order--><TransactionId>TID</TransactionId><Timestamp>20170630125040000252</Timestamp><ServiceVersion>SERVICEVERION</ServiceVersion><!--Optional:--><Language>IT</Language></RequestInfo><OperatorInfo UserID=\"U0H1963\" IsVirtualUser=\"VIUSER\" NotISPUserID=\"NOTVIU\"/><CompanyInfo><!--You may enter the following 4 items in any order--><!--Optional:--><!--Optional:--><ISPBranchCode>[ISPBranchCode]</ISPBranchCode><ISPServiceCompanyIDCode>[ISPServiceCompanyIDCode]</ISPServiceCompanyIDCode></CompanyInfo><BusinessInfo><!--You may enter the following 5 items in any order--><CustomerID>[CustomerID]</CustomerID><!--Optional:--><BusinessProcessName>[BusinessProcessName]</BusinessProcessName><!--Optional:--><BusinessProcessID>[BusinessProcessID]</BusinessProcessID><!--Optional:--><BusinessOperation>[BusinessOperation]</BusinessOperation><!--Optional:--><BusinessFileID>[BusinessFileID]</BusinessFileID></BusinessInfo>         <TechnicalInfo>            <!--You may enter the following 4 items in any order-->            <ChannelIDCode>[ChannelIDCode]</ChannelIDCode>            <ApplicationID>[ApplicationID]</ApplicationID>            <CallerServerName>[CallerServerName]</CallerServerName>            <CallerProgramName>[CallerProgramName]</CallerProgramName>         </TechnicalInfo>         <AdditionalBusinessInfo>            <!--1 or more repetitions:-->            <CodUnitaOperativa>[CodUnitaOperativa]</CodUnitaOperativa><DataContabile>[DataContabile]</DataContabile><FlagPaperless>[FlagPaperless>]</FlagPaperless>	<CodABI>[CodABI]</CodABI><CodOperatività>[CodOperarività]</CodOperatività><CodTerminaleCics>[CodTerminaleCics]</CodTerminaleCics></AdditionalBusinessInfo></ISPWebservicesHeader>";

		ispHeader = "<ISPWebservicesHeader><RequestInfo><TransactionId>BPMTKMQ2017-07</TransactionId><Timestamp>20170703180828371</Timestamp><ServiceID>XINBPTE1</ServiceID><ServiceVersion>01</ServiceVersion><Language>E</Language></RequestInfo><OperatorInfo UserID=\"U0H2438\" IsVirtualUser=\"false\" NotISPUserID=\"U900389\"/><CompanyInfo><ISPCallerCompanyIDCode>01</ISPCallerCompanyIDCode><NotISPCompanyIDCode>99</NotISPCompanyIDCode><ISPBranchCode>02841</ISPBranchCode><ISPServiceCompanyIDCode>02</ISPServiceCompanyIDCode></CompanyInfo><BusinessInfo><CustomerID>ABCDEF80L09B248Z</CustomerID><BusinessProcessName>BusinessProcessName</BusinessProcessName><BusinessProcessID>123</BusinessProcessID><BusinessOperation>BusinessOperation</BusinessOperation><BusinessFileID>BusinessFileID</BusinessFileID></BusinessInfo><TechnicalInfo><ChannelIDCode>99</ChannelIDCode><ApplicationID>IXPG0</ApplicationID><CallerServerName>CallerServerName</CallerServerName><CallerProgramName>BPMPA</CallerProgramName></TechnicalInfo><AdditionalBusinessInfo><Param Name=\"CodUnitaOperativa\" Value=\"14493\"/><Param Name=\"DataContabile\" Value=\"03072017\"/><Param Name=\"FlagPaperless\" Value=\"S\"/><Param Name=\"CodABI\" Value=\"03069\"/><Param Name=\"CodOperatività\" Value=\"N\"/><Param Name_=\"CodTerminaleCics\" Value=\"9999\"/></AdditionalBusinessInfo></ISPWebservicesHeader>";
		ispHeader="<int:ISPWebservicesHeader xmlns:int=\"http://eric.van-der-vlist.com/ns/person\">         <RequestInfo>            <TransactionId>GKBG0_20170913113655000769</TransactionId>            <Timestamp>20170913113655000769</Timestamp>            <ServiceID>GKMGDECPLF</ServiceID>            <ServiceVersion>00</ServiceVersion>              <Language>it</Language>         </RequestInfo>         <OperatorInfo UserID=\"U0G4356\" IsVirtualUser=\"false\"     NotISPUserID=\"\"   />         <CompanyInfo>            <ISPCallerCompanyIDCode>01</ISPCallerCompanyIDCode>            <ISPServiceCompanyIDCode>01</ISPServiceCompanyIDCode>            <ISPBranchCode>00700</ISPBranchCode>            <NotISPCompanyIDCode></NotISPCompanyIDCode>         </CompanyInfo>         <BusinessInfo>            <CustomerID>U0G4356</CustomerID>            <BusinessProcessName>Richiesta Pratica Mogeni</BusinessProcessName>            <BusinessProcessID>76828</BusinessProcessID>            <BusinessOperation></BusinessOperation>            <BusinessFileID></BusinessFileID>         </BusinessInfo>         <TechnicalInfo>            <ChannelIDCode>01</ChannelIDCode>            <ApplicationID>GKBG0</ApplicationID>            <CallerServerName>localhost</CallerServerName>            <CallerProgramName>GKBG0-BPM</CallerProgramName>         </TechnicalInfo>         <AdditionalBusinessInfo>    <Param Name=\"CodABI\" Value=\"01025\"/>  </AdditionalBusinessInfo></int:ISPWebservicesHeader>";
	
		ispHeaderMap = ISPHeaderUtility.ISPHeaderScompositionLocal(ispHeader);

		if (ispHeaderMap != null) {

			// errore?

			if (ispHeaderMap.get("ISPHEADERMALFORMED") == null) {

				serviceID = (String) ispHeaderMap.get("ServiceID");
				applicationID = (String) ispHeaderMap.get("ApplicationID");
				timeStamp = (String) ispHeaderMap.get("Timestamp");

				if (serviceID == null || applicationID == null)
					error = true;
				else {

					if (timeStamp == null)
						error = true;
					else {

						timeStamp_ = ISPHeaderUtility.checkTimestamp(timeStamp);

						if (timeStamp_ == null)
							System.out.println("TSNV");

					}
				}

			} else {

			}
		}

	}

}
