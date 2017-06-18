/*
 * Created on Apr 13, 2005
 * 
 * This code is not suitable for production environments. It is intended
 * soley as a simple example for certain techniques and lacks surrounding
 * code that would make it suitable for any other use.
 *
 */
package examples.was.login;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.security.CustomRegistryException;
import com.ibm.websphere.security.EntryNotFoundException;
import com.ibm.websphere.security.PasswordCheckFailedException;
import com.ibm.websphere.security.UserRegistry;
import com.ibm.websphere.security.WebTrustAssociationException;
import com.ibm.websphere.security.WebTrustAssociationFailedException;
import com.ibm.wsspi.security.tai.TAIResult;
import com.ibm.wsspi.security.tai.TrustAssociationInterceptor;
import com.ibm.wsspi.security.token.AttributeNameConstants;

/**
 * @author Bill Hines and Keys Botzum, IBM Software Services for WebSphere
 *  
 */
public class ExampleTAI implements TrustAssociationInterceptor {
	public ExampleTAI() {
		super();
	}

	/*
	 * 
	 * Check here that this request is the correct one for this TAI (i.e you
	 * might have multiple TAI's). Since this TAI requires that it be called
	 * from the special login page which includes the admin question, if that
	 * parameter isn't found, it doesn't execute. WAS will then fall back to
	 * normal behavior. Assuming the application is configured properly with a
	 * login page, that login page will then be displayed.
	 * 
	 * @see com.ibm.wsspi.security.tai.TrustAssociationInterceptor#isTargetInterceptor(javax.servlet.http.HttpServletRequest)
	 */
	public boolean isTargetInterceptor(javax.servlet.http.HttpServletRequest req)
			throws WebTrustAssociationException {
		System.out.println("isTargetInterceptor called");

		String fromLoginJSP = req.getParameter("from_login_jsp");
		System.out.println("Request is from login JSP:" + fromLoginJSP);
		System.out
				.println("Checking to see if this invocation came from the login JSP");
		
		
		if (fromLoginJSP == null || !fromLoginJSP.equals("Y")) {
			// The TAI has been invoked, but the hidden flag that indicates
			// that it is from the login JSP is not in place. This can happen
			// in scenarios such as the user being failed over to another
			// application server, and this new server being
			// unable to retrieve the custom subject via the cache key.
			System.out
					.println("TAI will handle requestbut forced for gabriele/viviana.");
			return true;
		} else {
			System.out.println("TAI will handle request.");
			return true;
		}
	}

	/*
	 * 
	 * @see com.ibm.wsspi.security.tai.TrustAssociationInterceptor#negotiateValidateandEstablishTrust(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public TAIResult negotiateValidateandEstablishTrust(HttpServletRequest req,
			HttpServletResponse resp) throws WebTrustAssociationFailedException {
		String userid = req.getParameter("userid");
		if (userid==null || userid.length()==0) userid="gabriele";
		System.out.println("TAI using userid " + userid);
		String password = req.getParameter("password");
		if (password==null || password.length()==0) password="viviana";
		System.out.println("TAI using password " + password);

		String uniqueid = null;

		try {
			boolean customGroups = false;
			boolean wantsAdmin = false;

			// Compute identity information
			InitialContext ctx = new InitialContext();
			UserRegistry reg = (UserRegistry) ctx.lookup("UserRegistry");

			// Check the user's password, since we have no front-end
			// authentication for this simple
			// example. Typically, you would have a third-party authentication
			// proxy such as
			// Tivoli Access Manager to do the authentication before the TAI
			// gets the request.
			reg.checkPassword(userid, password);

			//get basic info about the user
			uniqueid = reg.getUniqueUserId(userid);
			System.out.println("uniqueid = " + uniqueid);
			List groups = reg.getGroupsForUser(userid);

			//determine if user wants to be admin.
			String adminPriv = req.getParameter("AdminPriv");
			if (adminPriv != null && adminPriv.equals("Y")) {
				System.out.println("User desires admin");
				wantsAdmin = true;
			}

			//go through groups and remove admin group if needed.
			Iterator iter = groups.iterator();
			boolean foundAdmin = false;
			while (iter.hasNext()) {
				String gid = (String) iter.next();
				if (gid.equals("admin")) {
					foundAdmin = true;
					if (wantsAdmin == false) {
						iter.remove();
						customGroups = true;
					}
					break;
				}
			}

			//Now, a quick error check (wanting admin when not an admin)
			if ((!foundAdmin) && wantsAdmin) {
				// Ok, this joker is trying to get admin privs and is not a
				// member of that group!
				System.out
						.println("User tried for admin privs when not a member of admin group ");
				req
						.setAttribute("errorMessage",
								"<b>Error: You are not a member of the admin group.</b>");
				javax.servlet.RequestDispatcher disp = req.getRequestDispatcher("/login.jsp");
				try {
					disp.forward(req, resp);
					return TAIResult
							.create(HttpServletResponse.SC_UNAUTHORIZED);
				} catch (Exception e1) {
					System.out.println("exception in TAI " + e1);
					e1.printStackTrace();
					throw new WebTrustAssociationFailedException(e1
							.getMessage());
				}
			}

			//Now, we have a list of groups that is appropriate for this
			// user based on their input. Let's go ahead and create the correct Subject.

			String key;
			if (customGroups) {
				// Set unique cache key to prevent cache problems
				// (losing custom info, etc)
				// since the info is always the same for the user, we
				// just prefix this with
				// the uniqueid and a static string value.
				// But, if the info was unique to *this login* I'd need
				// a more unique value. 
				key = uniqueid + "ExampleTAIAdminRemoved";
			} else {
				key = uniqueid;
			}

			/*
			 * It may seem odd that we create the Subject even when it just
			 * contains the default group information. We do this to work around
			 * defect #293814: auth cache not matching properly. When a custom
			 * subject is cached, if the same userid is returned from the TAI,
			 * that custom Subject will be used because WAS is matching on the
			 * wrong cache key (userid instead of the full custom key).
			 */
			Subject subject = createSubject(userid, uniqueid,
					convertGroupsToUniqueIds(reg, groups), key);
			
			return TAIResult.create(HttpServletResponse.SC_OK, "notused",
					subject);

		} catch (PasswordCheckFailedException e) {
			System.out.println("Password check failed exception in TAI " + e);
			req.setAttribute("errorMessage",
					"<b>Error: Login failed. Please try again.</b>");
			RequestDispatcher disp = req.getRequestDispatcher("/login.jsp");
			try {
				disp.forward(req, resp);
				return TAIResult.create(HttpServletResponse.SC_UNAUTHORIZED);
			} catch (Exception e1) {
				System.out.println("exception in TAI " + e);
				e.printStackTrace();
				throw new WebTrustAssociationFailedException(e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("exception in TAI " + e);
			e.printStackTrace();
			throw new WebTrustAssociationFailedException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.wsspi.security.tai.TrustAssociationInterceptor#initialize(java.util.Properties)
	 */
	public int initialize(Properties arg0)
			throws WebTrustAssociationFailedException {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.wsspi.security.tai.TrustAssociationInterceptor#getVersion()
	 */
	public String getVersion() {
		return "1.0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.wsspi.security.tai.TrustAssociationInterceptor#getType()
	 */
	public String getType() {
		return this.getClass().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.wsspi.security.tai.TrustAssociationInterceptor#cleanup()
	 */
	public void cleanup() {
	}

	private List convertGroupsToUniqueIds(UserRegistry reg, List groups)
			throws EntryNotFoundException, CustomRegistryException,
			RemoteException {
		Iterator iter = groups.iterator();
		ArrayList answer = new ArrayList();

		while (iter.hasNext()) {
			String ugroup = reg.getUniqueGroupId((String) iter.next());
			answer.add(ugroup);
		}
		return answer;
	}

	private Subject createSubject(String userid, String uniqueid, List groups,
			String key) {
		Subject subject = new Subject();
		Hashtable hashtable = new Hashtable();
		hashtable.put(AttributeNameConstants.WSCREDENTIAL_UNIQUEID, uniqueid);
		hashtable.put(AttributeNameConstants.WSCREDENTIAL_SECURITYNAME, userid);
		hashtable.put(AttributeNameConstants.WSCREDENTIAL_GROUPS, groups);
		System.out.println("Subject cache key is " + key);
		hashtable.put(AttributeNameConstants.WSCREDENTIAL_CACHE_KEY, key);
		subject.getPublicCredentials().add(hashtable);

		// add custom stuff to Subject just for fun
		CustomJAASStuff stuff = new CustomJAASStuff();
		String word1 = userid.toUpperCase();
		System.out.println("TAI using word " + word1);
		stuff.setWord1(word1);
		subject.getPublicCredentials().add(stuff);

		return subject;
	}
	

	
}
