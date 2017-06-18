/*
 * Created on May 24, 2005
 *
 * Note: sas.jar must be on build path for this to compile in WAS 5.1.1.
 *
 * by Bill Hines (based on code from Keys Botzum), IBM Software Services for WebSphere
 * 
 */
package examples.was.login;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import com.ibm.websphere.security.UserRegistry;
import com.ibm.wsspi.security.auth.callback.WSTokenHolderCallback;
import com.ibm.wsspi.security.token.AttributeNameConstants;

/**
 * @author keys
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MyBeforeLTPALoginModule implements LoginModule {
	private Subject subject;

	CallbackHandler callbackHandler;

	/**
	 *  
	 */
	public MyBeforeLTPALoginModule() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject,
	 *      javax.security.auth.callback.CallbackHandler, java.util.Map,
	 *      java.util.Map)
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map arg2, Map arg3) {
		System.out.println("MyBeforeLTPALoginModule initialize()");
		this.subject = subject;
		this.callbackHandler = callbackHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	public boolean login() throws LoginException {
		System.out.println("MyBeforeLTPALoginModule login()");

		Callback callbacks[] = new Callback[3];
		try {
			callbacks[0] = new WSTokenHolderCallback("");
			callbacks[1] = new NameCallback("User:");
			callbacks[2] = new PasswordCallback("Password:", false);
			callbackHandler.handle(callbacks);
		} catch (Exception e) {
			System.out.println("Login Module failed: " + e);
			e.printStackTrace(System.out);
			throw new LoginException(e.getMessage());
		}

		boolean requiresLogin = ((WSTokenHolderCallback) callbacks[0])
				.getRequiresLogin();
		if (requiresLogin) {
			System.out
					.println("MyBeforeLTPALoginModule: Need to do stuff for an initial login");
			String username = ((NameCallback) callbacks[1]).getName();
			String password = new String(((PasswordCallback) callbacks[2])
					.getPassword());
			((PasswordCallback) callbacks[2]).clearPassword();
			Hashtable hashtable = new Hashtable();
			String uniqueid = null;
			try {
				InitialContext ctx = new InitialContext();
				UserRegistry reg = (UserRegistry) ctx.lookup("UserRegistry");
				uniqueid = reg.getUniqueUserId(username);
				reg.checkPassword(username, password);
			} catch (Exception e1) {
				System.out.println("Login Module failed: " + e1);
				e1.printStackTrace(System.out);
				throw new LoginException(e1.getMessage());
			}
			System.out.println("uniqueid = " + uniqueid);
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_UNIQUEID,
					uniqueid);
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_SECURITYNAME,
					username);

			// Assert the user as belonging to only this group. This is done
			// only as an
			// example of how to manipulate the user's roles dynamically. We
			// could also
			// have looked up the user's groups in the registry with
			// reg.getUniqueGroupIds()
			// and added this one, or subtracted others from that list.
			ArrayList groups = new ArrayList();
			groups.add(0, "nomemb");
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_GROUPS, groups);

			// Set unique cache key to prevent cache problems (losing custom
			// info, etc)
			// If the info was unique to *this login* I'd need a more
			// unique value. Be aware that this Subject is cached in the CSIv2
			// session
			// between the client and server. Thus, it will last until that
			// ends.
			// This also means that if I go to a second application server, that
			// this custom
			// Subject will have to be recreated.
			System.out.println("Using new cache key");
			hashtable.put(AttributeNameConstants.WSCREDENTIAL_CACHE_KEY,
					uniqueid + "ExampleBeforeLTPALoginModule");
			subject.getPublicCredentials().add(hashtable);
			return true;
		} else {
			System.out
					.println("MyBeforeLTPALoginModule: This is a repeat login, nothing to do.");
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	public boolean commit() throws LoginException {
		System.out.println("MyBeforeLTPALoginModule commit()");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		System.out.println("MyBeforeLTPALoginModule abort()");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	public boolean logout() throws LoginException {
		System.out.println("MyBeforeLTPALoginModule logout()");
		return false;
	}
}
