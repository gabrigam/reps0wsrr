/**
 * 
 */
package com.intesasanpaolo.qdh.nbp.adapter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer;

//import org.apache.log4j.Logger;
//import org.apache.log4j.MDC;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.pcf.CMQC;
////////import com.intesasanpaolo.nbp.mq.QDHMQClient; ho tolto questa classe che non serviva

import teamworks.TWObjectFactory;
import com.lombardisoftware.core.TWObject;
import teamworks.TWList;
import teamworks.TWObjectFactory;

/**
 * @author U0H2438 modified U0H1963
 * 
 */
@SuppressWarnings({ "deprecation", "unused" })

public class NBPMQAdapter {

	private String _queueMngName = null;
	private String _hostName = null;
	private int _port = 0;
	private String _channel = null;
	private String _queueName = null;
	private String _msgId = null;
	private MQQueueManager _mqQueueManager = null;
	private MQQueue _queue = null;
	private int _maxConnRetry = 0;
	private String _message = null;

	//
	private boolean _operationInError = false;
	private int _returnCode = 0;

	/* Gestione Errori : CompCode e ReasonCode MQ - Return code generale */
	/**
	 * twsAdapterCompletionCode : E' il completion code di un'eccezione MQSeries
	 */
	private int twsAdapterCompletionCode = 0;
	/**
	 * twsAdapterReasonCode : E' il reason code di un'eccezione MQSeries
	 */
	private int twsAdapterReasonCode = 0;
	/**
	 * twsAdapterErrorCode : E' l'errore generale riscontrato
	 */
	private int twsAdapterErrorCode;

	/* Internal control flow operation constants */
	private static final int TWS_ADAPTER_MQCONN_OK = 0;
	private static final int TWS_ADAPTER_MQCONN_RETRY = 110;
	private static final int TWS_ADAPTER_MQCONN_KO = 199;
	private static final int TWS_ADAPTER_MQOPEN_OK = 0;
	private static final int TWS_ADAPTER_MQOPEN_KO = 299;

	/* External control flow operation */
	public static final int TWS_ADAPTER_INIT_OK = 0;
	public static final int TWS_ADAPTER_INIT_KO = 1999;
	public static final int TWS_ADAPTER_CLOSE_OK = 0;
	public static final int TWS_ADAPTER_CLOSE_KO = 2999;
	public static final int TWS_ADAPTER_RESTORABLE_CONNECTION = 10000;
	public static final int TWS_ADAPTER_UNRESTORABLE_CONNECTION = 10999;
	public static final int TWS_ADAPTER_WRITE_OK = 0;
	public static final int TWS_ADAPTER_WRITE_KO = 20999;
	public static final int TWS_ADAPTER_COMMIT_OK = 0;
	public static final int TWS_ADAPTER_COMMIT_KO = 60999;
	public static final int TWS_ADAPTER_ROLLBACK_OK = 0;
	public static final int TWS_ADAPTER_ROLLBACK_KO = 70999;

	private static final int TWS_ADAPTER_IOEXC_KO = 99999;
	private static final int TWS_ADAPTER_INITIALIZATION_KO = 78000; // new
	private static final int TWS_ADAPTER_SETMESSAGECONTENT_KO = 79000; // new

	/**
	 * @param _connectionString
	 * @param _logger
	 * @param _maxConnRetry
	 */
	public NBPMQAdapter(TWObject connectionData) {

		System.out.println("NBPMQAdapter(costruttore start...");

		try {


			this._queueMngName = (String) connectionData.getPropertyValue("QMGR");

			this._hostName = (String) connectionData.getPropertyValue("HOST");

			this._port = Integer.parseInt((String) connectionData.getPropertyValue("PORT"));

			this._channel = (String) connectionData.getPropertyValue("CHANNEL");

			this._queueName = (String) connectionData.getPropertyValue("QUEUE");

			this._msgId = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS").format(new Date());

			int maxConnRetry = Integer.parseInt((String) connectionData.getPropertyValue("MAXCONNRETRY"));

			if (maxConnRetry < 0) {
				System.out.println("maxConnRetry = " + new Integer(maxConnRetry).toString()
						+ ". Non sono accettabili valori Negativi viene impostato  il valore 0.");
				this._maxConnRetry = 0;
			}

			this._maxConnRetry = maxConnRetry;


			/* Impostazione Ambiente MQ */ // Serve ????
			if (this._hostName.equalsIgnoreCase(MQEnvironment.hostname) == false) {

				MQEnvironment.hostname = this._hostName;
			}

			if (this._channel.equalsIgnoreCase(MQEnvironment.channel) == false) {

				MQEnvironment.channel = this._channel;
			}

			if (this._port != MQEnvironment.port) {

				MQEnvironment.port = this._port;
			}

			System.out.println("NBPMQAdapter(costruttore end");

		} catch (Exception e) {
			this.setOperationInError(true);
			this.setReturnCode(TWS_ADAPTER_INITIALIZATION_KO); // new
			System.out.println("Errore durante recupero dati di connessione da strato BPM NBP");
			System.out.println(e.toString());
			System.out.println("END");
		}

	}

	/**
	 * init method
	 * 
	 */
	public int init() {
		int retInitRc = TWS_ADAPTER_INIT_KO;
		int twsAdapterRC = this.openMqConnection();

		System.out.println("init() start...");
		if (twsAdapterRC == TWS_ADAPTER_MQCONN_OK) {
			twsAdapterRC = this.openMqQueues(0);
			if (twsAdapterRC == TWS_ADAPTER_MQOPEN_OK)
				return retInitRc = TWS_ADAPTER_INIT_OK;
		}
		System.out.println("init() end");

		return retInitRc;
	}

	/**
	 * init method
	 * 
	 * @param flag
	 */
	public int init(int flag) {
		int retInitRc = TWS_ADAPTER_INIT_KO;
		int twsAdapterRC = this.openMqConnection();

		System.out.println("init(flag) start...");
		if (twsAdapterRC == TWS_ADAPTER_MQCONN_OK) {
			twsAdapterRC = this.openMqQueues(flag);
			if (twsAdapterRC == TWS_ADAPTER_MQOPEN_OK)
				return retInitRc = TWS_ADAPTER_INIT_OK;
		}
		System.out.println("init(flag) end");
		return retInitRc;
	}

	protected int openMqConnection() {

		int connStatus;

		System.out.println("openMqConnection() start...");

		try {
			this._mqQueueManager = new MQQueueManager(this._queueMngName); // nuovo

			// MDC.put("SessId", this._mqQueueManager.toString().split("@")[1]);
			this.twsAdapterCompletionCode = MQException.MQCC_OK;
			this.twsAdapterReasonCode = MQException.MQRC_NONE;

			return connStatus = TWS_ADAPTER_MQCONN_OK;
		} catch (MQException mqExc) {
			
			System.out.println("Error completioncode "+mqExc.completionCode);
			System.out.println("Error reasoncode "+mqExc.reasonCode);
			
			this.twsAdapterCompletionCode = mqExc.completionCode;
			this.twsAdapterReasonCode = mqExc.reasonCode;
			this.twsAdapterErrorCode = mqExc.reasonCode;

			if (mqExc.completionCode == MQException.MQCC_FAILED) {
				if (checkMQException(mqExc.reasonCode) == TWS_ADAPTER_RESTORABLE_CONNECTION)
					connStatus = TWS_ADAPTER_MQCONN_RETRY;
				else
					connStatus = TWS_ADAPTER_MQCONN_KO;
			} else {
				connStatus = TWS_ADAPTER_MQCONN_OK;
			}
			return connStatus;
		} finally {
			System.out.println("openMqConnection() end");
		}

	}

	protected int openMqQueues(int flags) {
		int openQueueStatus;
		int queueOpenOptions = 0;

		System.out.println("openMqQueues(flags) start...");

		try {
			queueOpenOptions = MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING | flags;
			// | MQC.MQOO_SET_IDENTITY_CONTEXT
			// MQC.MQOO_SAVE_ALL_CONTEXT

			this._queue = this._mqQueueManager.accessQueue(this._queueName, queueOpenOptions);
			this.twsAdapterCompletionCode = MQException.MQCC_OK;
			this.twsAdapterReasonCode = MQException.MQRC_NONE;

			return openQueueStatus = TWS_ADAPTER_MQOPEN_OK;

		} catch (MQException mqExc) {
			
			System.out.println("Error completioncode "+mqExc.completionCode);
			System.out.println("Error reasoncode "+mqExc.reasonCode);
			
			this.twsAdapterCompletionCode = mqExc.completionCode;
			this.twsAdapterReasonCode = mqExc.reasonCode;
			this.twsAdapterErrorCode = mqExc.reasonCode;

			if (mqExc.completionCode == MQException.MQCC_FAILED) {
				openQueueStatus = TWS_ADAPTER_MQOPEN_KO;
			} else {
				openQueueStatus = TWS_ADAPTER_MQOPEN_OK;
			}
			return openQueueStatus;
		} finally {
			System.out.println("openMqQueues(flags) end");
		}
	}

	/**
	 * writeString sends a msg to TWS (String format)
	 * 
	 * @return the following error codes: - TWSAdapter.TWS_ADAPTER_WRITE_OK se
	 *         l'operazione si è completata in modo positivo. -
	 *         TWSAdapter.TWS_ADAPTER_WRITE_KO se l'oggetto è nullo, se si è
	 *         verificato un errore di scrittura a livello MQ -
	 *         TWSAdapter.TWS_ADAPTER_IOEXC_KO se operazione non completata con
	 *         errori di conversione dati Stringa
	 * @throws EncodingException 
	 * @throws UnsupportedEncodingException 
	 */
	public int writeString(String newMessage)  {

		this.twsAdapterCompletionCode = MQException.MQCC_OK;
		this.twsAdapterReasonCode = MQException.MQRC_NONE;

		System.out.println("writeString() start...");

		if (newMessage != null) {
			
			System.out.println("message encoded " + newMessage);

			try {

				byte [] bytesEBCDIC = NBPMQAdapter.creatByteArrayFromTokenizer(newMessage);

				MQMessage twsMsg = new MQMessage();

				//twsMsg.writeString(new String(bytesEBCDIC));
				
				twsMsg.write(bytesEBCDIC, 0, bytesEBCDIC.length);
				
				//twsMsg.writeString(NBPMQAdapter.toHex(bytesEBCDIC));

				MQPutMessageOptions twsPutMsgOptions = new MQPutMessageOptions();

				twsPutMsgOptions.options = MQC.MQPMO_FAIL_IF_QUIESCING | MQC.MQPMO_SYNCPOINT;
				// twsPutMsgOptions.options = MQC.MQPMO_FAIL_IF_QUIESCING
				// QC.MQPMO_SYNCPOINT;
				// twsPutMsgOptions.options = MQC.MQPMO_FAIL_IF_QUIESCING;

				twsMsg.replyToQueueManagerName = this._queueMngName;
				twsMsg.persistence = MQC.MQPER_PERSISTENT;
				twsMsg.messageType = MQC.MQMT_DATAGRAM;
				twsMsg.messageId = MQC.MQMI_NONE;

				twsMsg.characterSet=MQC.MQCCSI_Q_MGR;
				
				twsMsg.characterSet = 500;
				twsMsg.encoding=785;
				twsMsg.format = MQC.MQFMT_NONE;			
			

				try {

					//twsMsg.format = MQC.MQFMT_STRING;
					//twsMsg.format = MQC.MQFMT_STRING;

					/* restoring of connection if necessary */
					if ((this._queue == null) || (this._mqQueueManager == null)) {
						int retInit = init();
						if (retInit == TWS_ADAPTER_INIT_KO) {
							System.out.println("twsWriteString() end");
							return TWS_ADAPTER_WRITE_KO;
						}
					}

					System.out.println("Data length = " + new Integer(twsMsg.getDataLength()).toString());
					System.out.println("Message length = " + new Integer(twsMsg.getMessageLength()).toString());
					System.out.println(
							"Total message length = " + new Integer(twsMsg.getTotalMessageLength()).toString());

					this._queue.put(twsMsg, twsPutMsgOptions);

					// this._mqQueueManager.commit();

					this.twsAdapterCompletionCode = MQException.MQCC_OK;
					this.twsAdapterReasonCode = MQException.MQRC_NONE;

					System.out.println("writeString() end");
					return TWS_ADAPTER_WRITE_OK;

				} catch (MQException mqExc) {
					this.twsAdapterCompletionCode = mqExc.completionCode;
					this.twsAdapterReasonCode = mqExc.reasonCode;
					this.twsAdapterErrorCode = mqExc.reasonCode;
					
					System.out.println("Error completioncode "+mqExc.completionCode);
					System.out.println("Error reasoncode "+mqExc.reasonCode);

					/* check if error code related to connection */
					if (checkMQException(mqExc.reasonCode) == TWS_ADAPTER_RESTORABLE_CONNECTION) {
						/* reconnect */
						int retInit = init();
						if (retInit == TWS_ADAPTER_INIT_KO) {
							System.out.println("writeString() end");
							return TWS_ADAPTER_WRITE_KO;
						}
						writeString(newMessage); // send again

						System.out.println("writeString() end");
						return TWS_ADAPTER_WRITE_OK;

						/* delete of msg from queue */
					} else {
						try {
							this._mqQueueManager.backout();
						} catch (MQException e) {
							e.printStackTrace();
							System.out.println(e.getMessage());
						}

						System.out.println("writeString() end");
						return TWS_ADAPTER_WRITE_KO;
					}
				} catch (IOException ioExc) {
					try {
						this._mqQueueManager.backout();
					} catch (MQException e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}

					System.out.println("writeString() end");
					return TWS_ADAPTER_IOEXC_KO;
				}
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
				return TWS_ADAPTER_SETMESSAGECONTENT_KO; ////////////////// errore
				////////////////// dati
			}
		} else { // newMessage == null
			try {
				this._mqQueueManager.backout();
			} catch (MQException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

			System.out.println("writeString() end");
			return TWS_ADAPTER_WRITE_KO;
		}
	}

	public int close() {

		System.out.println("close start...");

		try {
			if (this._mqQueueManager != null) {
				if (this._queue != null) {
					this._queue.close();
				}
				this._mqQueueManager.disconnect();
				this._mqQueueManager.close();
			}
			return TWS_ADAPTER_CLOSE_OK;
		} catch (MQException mqExc) {
			System.out.println("Error completioncode "+mqExc.completionCode);
			System.out.println("Error reasoncode "+mqExc.reasonCode);
			System.out.println(mqExc.getMessage());
			mqExc.printStackTrace();
			return TWS_ADAPTER_CLOSE_KO;
		} finally {
			System.out.println("close end");
		}
	}

	/**
	 * rollback Executes the rollback of last processed message without managing
	 * "quadratura" Used to force re-processing of message in a next reading
	 * 
	 * @return the following values: - TWS_ADAPTER_ROLLBACK_OK for rollback
	 *         executed properly - TWS_ADAPTER_ROLLBACK_KO in case of errors
	 */
	public int rollback() {

		System.out.println("rollback() start...");

		int retval = TWS_ADAPTER_ROLLBACK_OK;

		try {
			if (this._mqQueueManager != null) {
				this._mqQueueManager.backout();
			}
			retval = TWS_ADAPTER_ROLLBACK_OK;
		} catch (MQException mqExc) {
			System.out.println("Error completioncode "+mqExc.completionCode);
			System.out.println("Error reasoncode "+mqExc.reasonCode);
			System.out.println(mqExc.getMessage());
			mqExc.printStackTrace();
			retval = TWS_ADAPTER_ROLLBACK_KO;
		}

		System.out.println("rollback() end");
		return retval;
	}

	protected int checkMQException(int reasonCode) {
		int checkMQExcRc = TWS_ADAPTER_UNRESTORABLE_CONNECTION;

		switch (reasonCode) {
		case MQException.MQRC_CONNECTION_BROKEN:
			checkMQExcRc = TWS_ADAPTER_RESTORABLE_CONNECTION;
			break;
		case MQException.MQRC_HCONN_ERROR:
			checkMQExcRc = TWS_ADAPTER_RESTORABLE_CONNECTION;
			break;
		case MQException.MQRC_MAX_CONNS_LIMIT_REACHED:
			checkMQExcRc = TWS_ADAPTER_RESTORABLE_CONNECTION;
			break;
		case MQException.MQRC_Q_MGR_NOT_AVAILABLE:
			checkMQExcRc = TWS_ADAPTER_RESTORABLE_CONNECTION;
			break;
		case MQException.MQRC_Q_MGR_QUIESCING:
			checkMQExcRc = TWS_ADAPTER_RESTORABLE_CONNECTION;
			break;
		case MQException.MQRC_Q_MGR_STOPPING:
			checkMQExcRc = TWS_ADAPTER_RESTORABLE_CONNECTION;
			break;
		case MQException.MQRC_UNEXPECTED_ERROR:
			checkMQExcRc = TWS_ADAPTER_RESTORABLE_CONNECTION;
			break;
		case MQException.MQRC_CONNECTION_QUIESCING:
			checkMQExcRc = TWS_ADAPTER_RESTORABLE_CONNECTION;
			break;
		case MQException.MQRC_CONNECTION_STOPPING:
			checkMQExcRc = TWS_ADAPTER_RESTORABLE_CONNECTION;
			break;
		case MQException.MQRC_ALREADY_CONNECTED:
			checkMQExcRc = TWS_ADAPTER_RESTORABLE_CONNECTION;
			break;
		default:
			checkMQExcRc = TWS_ADAPTER_UNRESTORABLE_CONNECTION;
			break;
		}

		return checkMQExcRc;
	}

	/**
	 * @return the _queueMngName
	 */
	public String get_queueMngName() {
		return _queueMngName;
	}

	/**
	 * @param _queueMngName
	 *            the _queueMngName to set
	 */
	public void set_queueMngName(String _queueMngName) {
		this._queueMngName = _queueMngName;
	}

	/**
	 * @return the _hostName
	 */
	public String get_hostName() {
		return _hostName;
	}

	/**
	 * @param _hostName
	 *            the _hostName to set
	 */
	public void set_hostName(String _hostName) {
		this._hostName = _hostName;
	}

	/**
	 * @return the _port
	 */
	public int get_port() {
		return _port;
	}

	/**
	 * @param _port
	 *            the _port to set
	 */
	public void set_port(int _port) {
		this._port = _port;
	}

	/**
	 * @return the _channel
	 */
	public String get_channel() {
		return _channel;
	}

	/**
	 * @param _channel
	 *            the _channel to set
	 */
	public void set_channel(String _channel) {
		this._channel = _channel;
	}

	/**
	 * @return the _logger
	 */
	/*
	 * g public Logger get_logger() { return _logger; }
	 */

	/**
	 * @param _logger
	 *            the _logger to set G public void set_logger(Logger _logger) {
	 *            this._logger = _logger; }
	 * 
	 *            /**
	 * @return the _msgId
	 */
	public String get_msgId() {
		return _msgId;
	}

	/**
	 * @param _msgId
	 *            the _msgId to set
	 */
	public void set_msgId(String _msgId) {
		this._msgId = _msgId;
	}

	/**
	 * @return the _maxConnRetry
	 */
	public int get_maxConnRetry() {
		return _maxConnRetry;
	}

	/**
	 * @param _maxConnRetry
	 *            the _maxConnRetry to set
	 */
	public void set_maxConnRetry(int _maxConnRetry) {
		this._maxConnRetry = _maxConnRetry;
	}

	/**
	 * @return the _message
	 */
	public String get_message() {
		return _message;
	}

	/**
	 * @param _message
	 *            the _message to set
	 */
	public void set_message(String _message) {
		this._message = _message;
	}

	public boolean isOperationInError() {
		return _operationInError;
	}

	public void setOperationInError(boolean operationInError) {
		this._operationInError = operationInError;
	}

	public int getReturnCode() {
		return _returnCode;
	}

	public void setReturnCode(int returnCode) {
		this._returnCode = returnCode;
	}

	public static int sendMQNBPMessage(TWObject NBPApplData, TWObject NBPQDHData) {

		int rc = -1;

		NBPMQAdapter adapter_Appl_Instance = new NBPMQAdapter(NBPApplData);

		try {

			if (adapter_Appl_Instance != null && !adapter_Appl_Instance.isOperationInError()) {

				rc = adapter_Appl_Instance.init();

				if (rc == 0) {

					rc = adapter_Appl_Instance.writeString((String) NBPApplData.getPropertyValue("DATA"));

					if (rc == 0) {

						NBPMQAdapter adapter_QDH_Instance = new NBPMQAdapter(NBPQDHData);

						if (adapter_QDH_Instance != null && !adapter_QDH_Instance.isOperationInError()) {

							rc = adapter_QDH_Instance.init();

							if (rc == 0) {
								

								rc = adapter_QDH_Instance.writeString((String) NBPQDHData.getPropertyValue("DATA"));


								if (rc == 0) {

									try {

										adapter_Appl_Instance._mqQueueManager.commit();

										try {

											adapter_QDH_Instance._mqQueueManager.commit();

										} catch (MQException mqExc1) {

											System.out.println(mqExc1.getMessage());

											rc = mqExc1.completionCode;

											System.out.println("Errore Commit messaggio QDH  "+rc);

											// rollback messaggio QDH

											//rc = adapter_QDH_Instance.rollback();

										}
									} catch (MQException mqExc2) {

										System.out.println(mqExc2.getMessage());

										rc = mqExc2.completionCode;

										System.out.println("Errore Commit messaggio applicativo "+rc);

										// rollback messaggio Applicativo

										//rc = adapter_Appl_Instance.rollback();

									}

								} else {

									// eseguo rollback messaggio applicativo

									adapter_Appl_Instance.rollback();

								}

							} else {
								
								adapter_Appl_Instance.rollback();
							  }
							

						} else rc = adapter_QDH_Instance.getReturnCode();

					}
				}
			} else  rc = adapter_Appl_Instance.getReturnCode();


			System.out.println("Result RC : "+rc);

		}

		catch( Exception ex) {

			System.out.println(ex.getMessage());

		}

		return rc;

	}
	
	private static byte[] creatByteArrayFromTokenizer(String input) {
		
		StringTokenizer stk = new StringTokenizer(input,"_");
		byte[] byteData=new byte[stk.countTokens()];
		int i=0;
		while (stk.hasMoreElements()) {
			byteData[i]=Byte.parseByte((String) stk.nextToken());
			i++;
		}
		
		return byteData;
	}
	/**
	public static void main(String [] args) throws EncodingException, UnsupportedEncodingException {

		byte ffff=0;
		
	
		byte a[]="A0".getBytes("Cp500");
		byte[] qq=NBPMQAdapter.intToBytes(48);
		//Byte.decode("223");
		
		
		byte[] AA = "ciao".getBytes();
		byte[] data=NBPMQAdapter.creatByteArrayFromTokenizer("1_2_3_4_0_0_0_0_0_0_0_0_100_101");
		System.out.println(new String(data));
		
		String newMessage= "T:";
		byte[] bytesEBCDIC = newMessage.getBytes("Cp500");
		String  base = NBPMQAdapter.toHex(bytesEBCDIC);
		byte[] bytesEBCDIC__ = newMessage.getBytes("UTF-8"); 
		byte[] bytesEBCDIC___ = newMessage.getBytes("UTF-16"); 
		int[] intEBCDIC= new int[bytesEBCDIC.length];
		byte[] bytesEBCDIC____ =new String(bytesEBCDIC__).getBytes("Cp500"); 
		
	       for(int i=0; i<newMessage.length(); i++){
	    	    byte b=bytesEBCDIC[i];
	    	    if (b<0) intEBCDIC[i]=(bytesEBCDIC[i]+256);
	    	    else intEBCDIC[i]=bytesEBCDIC[i];
	            System.out.println("ASCII "+newMessage.charAt(i)+"  <-->  "+b +"  <-->  "+intEBCDIC[i]);
	       }
	       
	    
	       
	       String KK=Arrays.toString(intEBCDIC).toString();
	       String KK1=new String(bytesEBCDIC,"Cp500");
	       //String KK1g=new String()
	       
	       int j=0;



		//byte[] bytes=Encoder.stringToBytes("ciao", "Cp500");



		//System.out.println(new String(data, "Cp1047")); // convert into readable string       

		//String data_coverted=Encoder.bytesToString(data, "Cp500");
		//System.out.println("DOPO C "+data.toString());
		
	}
	
	**/

}