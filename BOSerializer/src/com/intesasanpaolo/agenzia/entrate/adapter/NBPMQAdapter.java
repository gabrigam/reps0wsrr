/**
 * 
 */
package com.intesasanpaolo.agenzia.entrate.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.apache.log4j.Logger;
//import org.apache.log4j.MDC;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.intesasanpaolo.nbp.mq.QDHMQClient;

import teamworks.TWObjectFactory;
import com.lombardisoftware.core.TWObject;
import teamworks.TWList;
import teamworks.TWObjectFactory;

/**
 * @author U0H2438
 * 
 */
public class NBPMQAdapter {

	private String _queueMngName = null;
	private String _hostName = null;
	private int _port = 0;
	private String _channel = null;
	private String _queueName = null;
	//private Logger _logger = null;
	private String _msgId = null;
	private MQQueueManager _mqQueueManager = null;
	private MQQueue _queue = null;
	private int _maxConnRetry = 0;
	private String _message = null;
	

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
			int maxConnRetry=Integer.parseInt((String)connectionData.getPropertyValue("MAXCONNRETRY"));
			
			if (maxConnRetry < 0) {
				System.out.println("maxConnRetry = "
						+ new Integer(maxConnRetry).toString()
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
			System.out.println("Errore durante recupero dati di connessione da strato BPM NBP");
			System.out.println(e.toString());
			System.out.println("END");
		}


	}



	/**
	 * @param catalogOut
	 * @param fileTransfOut
	 * @param dirOut
	 * @param _connectionString
	 */


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
			this._mqQueueManager = new MQQueueManager(this._queueMngName);
			//MDC.put("SessId", this._mqQueueManager.toString().split("@")[1]);
			this.twsAdapterCompletionCode = MQException.MQCC_OK;
			this.twsAdapterReasonCode = MQException.MQRC_NONE;

			return connStatus = TWS_ADAPTER_MQCONN_OK;
		} catch (MQException mqExc) {
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
			queueOpenOptions = MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING
					| flags;
			// | MQC.MQOO_SET_IDENTITY_CONTEXT
			// MQC.MQOO_SAVE_ALL_CONTEXT

			this._queue = this._mqQueueManager.accessQueue(this._queueName,
					queueOpenOptions);
			this.twsAdapterCompletionCode = MQException.MQCC_OK;
			this.twsAdapterReasonCode = MQException.MQRC_NONE;

			return openQueueStatus = TWS_ADAPTER_MQOPEN_OK;

		} catch (MQException mqExc) {
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
	 */
	public int writeString(String newMessage) {

		this.twsAdapterCompletionCode = MQException.MQCC_OK;
		this.twsAdapterReasonCode = MQException.MQRC_NONE;

		System.out.println("writeString() start...");

		if (newMessage != null) {
			// adding the length of the tws msg to the length of the headers


			MQMessage twsMsg = new MQMessage();
			try {
				twsMsg.writeString(newMessage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			MQPutMessageOptions twsPutMsgOptions = new MQPutMessageOptions();

			twsPutMsgOptions.options = MQC.MQPMO_FAIL_IF_QUIESCING
					| MQC.MQPMO_SYNCPOINT; // | MQC.MQPMO_SET_IDENTITY_CONTEXT;

			twsMsg.replyToQueueManagerName = this._queueMngName;
			twsMsg.persistence = MQC.MQPER_PERSISTENT;
			twsMsg.messageType = MQC.MQMT_DATAGRAM;
			twsMsg.messageId = MQC.MQMI_NONE;
			twsMsg.characterSet = MQC.MQCCSI_Q_MGR;

			try {

				twsMsg.format = MQC.MQFMT_STRING;
				/* restoring of connection if necessary */
				if ((this._queue == null) || (this._mqQueueManager == null)) {
					int retInit = init();
					if (retInit == TWS_ADAPTER_INIT_KO) {
						System.out.println("twsWriteString() end");
						return TWS_ADAPTER_WRITE_KO;
					}
				}

				System.out.println("Data length = "
						+ new Integer(twsMsg.getDataLength()).toString());
				System.out.println("Message length = "
						+ new Integer(twsMsg.getMessageLength()).toString());
				System.out.println("Total message length = "
						+ new Integer(twsMsg.getTotalMessageLength())
								.toString());

				this._queue.put(twsMsg, twsPutMsgOptions);
				this._mqQueueManager.commit();

				this.twsAdapterCompletionCode = MQException.MQCC_OK;
				this.twsAdapterReasonCode = MQException.MQRC_NONE;

				System.out.println("writeString() end");
				return TWS_ADAPTER_WRITE_OK;

			} catch (MQException mqExc) {
				this.twsAdapterCompletionCode = mqExc.completionCode;
				this.twsAdapterReasonCode = mqExc.reasonCode;
				this.twsAdapterErrorCode = mqExc.reasonCode;

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
	/*g
	public Logger get_logger() {
		return _logger;
	}
	*/

	/**
	 * @param _logger
	 *            the _logger to set
	 *G
	public void set_logger(Logger _logger) {
		this._logger = _logger;
	}

	/**
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


	/**
	 * @param applidTWSName
	 *            contains the name of applid TWS that must be executed
	 * @param filePath
	 *            contains the path of file that must be transferred by means
	 *            TWS
	 * @param fileName
	 *            name of file that must be transferred by means TWS
	 */

	/**
	 * @param applidTWSName
	 *            contains the name of applid TWS that must be executed
	 * @param filePath
	 *            contains the path of the files that must be transferred by
	 *            means TWS
	 * @param fileName
	 *            name of first file that must be transferred by means TWS
	 * @param fileName2
	 *            name of second file that must be transferred by means TWS
	 */
	

	/**
	 * @param applidTWSName
	 *            contains the name of applid TWS that must be executed
	 * @param filePath
	 *            contains the path of the files that must be transferred by
	 *            means TWS
	 * @param fileName[]
	 *            array that contains the names of files that must be transferred by means TWS
	 */


	/**
	 * @return the _twsHeader
	 */
	  public static void sendNBPMMeesage(TWObject connectionData,String message) {
		  
		  System.out.println("Il messaggio : "+message);
		  NBPMQAdapter adapter_Instance = new NBPMQAdapter(connectionData);
		  adapter_Instance.init();
		  //adapter_Instance.set_message(message);
		  System.out.println("RC = "+adapter_Instance.writeString(message));
	   
		  	/**
		  	 * 
		  	 * 	TK_MQAppChannel=SYSTEM.DEF.SVRCONN
				TK_MQAppHost=WIN-M248F5G0EIC
				TK_MQAppPassword=
				TK_MQAppPort=1414
				TK_MQAppQueueManager=SQL00001
				TK_MQAppQueueName=DATAGRAM.ZXI.WBPMBSMI
				TK_MQAppUserName=
		  	 */
		 
		  
		 
	    	
	  }

}