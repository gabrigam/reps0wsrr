package com.intesasanpaolo.nbp.mq;




import java.io.IOException;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

/**
 *
 * Assumes that the queue is empty before being run.
 *
 * Does not make use of JNDI for ConnectionFactory and/or Destination definitions.
 * Gets the message from Queue.
 *
 * 
 */
@SuppressWarnings("deprecation")
public class QDHMQClient {
  

	 static
	int openOptions =  MQC.MQOO_INPUT_EXCLUSIVE | MQC.MQOO_BROWSE | MQC.MQOO_INQUIRE ;

/**
   * Main method
   *
   * @param args
   */
  public static void main(String[] args) {
   
	  	QDHMQClient t = new QDHMQClient();
   
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
	   t.getMQMessage("WIN-M248F5G0EIC",1414,"SYSTEM.DEF.SVRCONN","SQL00001","DATAGRAM.ZXI.WBPMBSMI");
	  
	 
    	
  }
  
      
    	 public String getMQMessage(String hostname,Integer port, String channel, String qManager, String qName){
    		 String msgText = null;
    		 try {
    			 MQEnvironment.hostname = hostname; // host to connect to
    	    	 MQEnvironment.port = port; // port to connect to.  If not set, this
    	    	 MQEnvironment.channel = channel; // the CASE-SENSITIVE name of the SVRCONN channel on the queue manager
    	    	 MQQueueManager qMgr = new MQQueueManager(qManager);
    	    	 if(qMgr.isConnected())
    	    	 {
    	    	
    	    	 System.out.println("debug true");
    	    	 }else
    	    	 {
    	    		 System.out.println("debug false selse");;
    	    	 }
    	    	 MQQueue myQueue= qMgr.accessQueue(qName, openOptions);
    	    	
    	    	 System.out.println("debug");
    	    	    
    	    	 System.out.println(myQueue.isOpen() + "");;
    	    	 System.out.println("Number"+ myQueue.getCurrentDepth());
    	    	if(myQueue.getCurrentDepth()>0) {
    	    	 
    	    	 MQMessage getMessage = new MQMessage();  
    	    	 MQGetMessageOptions gmo = new MQGetMessageOptions();  
    	    	 gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_BROWSE_FIRST;
    	    	
    	    	 myQueue.get(getMessage,gmo);
    	    	 msgText = getMessage.readStringOfByteLength(getMessage.getTotalMessageLength());
    	    	
    	    	 gmo.options = MQC.MQGMO_MSG_UNDER_CURSOR; 
    	    	 myQueue.get(getMessage, gmo);
    	    	}
    	    	 myQueue.close();
    	    	 qMgr.disconnect();
    	    	 System.out.println("\\nSUCCESS\\n" + msgText);
    	    	 
    	    	
    		 }
    		 catch (MQException e){
    			 System.out.println(e);
    		 }
    		 
    		 catch (IOException p){
    			 System.out.println(p);
    		 }
			return msgText;
    		 
    	 }
    	 
  
      
      
      
      
  
}