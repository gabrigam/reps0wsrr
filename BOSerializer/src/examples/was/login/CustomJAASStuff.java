/*
 * Created on Oct 1, 2003
 *
 * by Keys Botzum (keys@us.ibm.com), IBM Software Services for WebSphere
 * 
 */
package examples.was.login;

import java.util.Date;

/**
 * @author keys
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CustomJAASStuff implements java.io.Serializable {
	int num;
	String word1;
	String word2;
	Date date = new Date();

	/**
	 * 
	 */
	public CustomJAASStuff() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @return
	 */
	public String getWord1() {
		return word1;
	}

	/**
	 * @return
	 */
	public String getWord2() {
		return word2;
	}

	/**
	 * @param i
	 */
	public void setNum(int i) {
		num = i;
	}

	/**
	 * @param string
	 */
	public void setWord1(String string) {
		word1 = string;
	}

	/**
	 * @param string
	 */
	public void setWord2(String string) {
		word2 = string;
	}

	/**
	 * @return
	 */
	public Date getDate() {
		return date;
	}

}
