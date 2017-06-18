package examples.was.taitest;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.security.auth.Subject;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import examples.was.login.CustomJAASStuff;
import examples.was.login.CustomSecurity;

import com.ibm.websphere.security.UserRegistry;
import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.cred.WSCredential;

/**
 * @author Bill Hines, IBM Software Services for WebSphere (based on code from
 *         Keys Botzum)
 *  
 */
public class PrintUserInfo extends HttpServlet implements Servlet {
	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public PrintUserInfo() {
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest arg0,
	 *      HttpServletResponse arg1)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest arg0,
	 *      HttpServletResponse arg1)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req.getRemoteUser() == null) {
			req
					.setAttribute("errorMessage",
							"<b>Error: Please log in before accessing PrintUserInfo.<b>");
			RequestDispatcher disp = getServletContext().getRequestDispatcher(
					"/login.jsp");
			disp.forward(req, resp);
			return;
		}
		resp.setContentType("text/html");
		PrintWriter out = new PrintWriter(resp.getOutputStream());

		String id = WSSubject.getCallerPrincipal();
		out.println("The WAS Subject layer thinks you are " + id);

		try {
			Context ic = new InitialContext();
			Object objRef = ic.lookup("UserRegistry");
			UserRegistry userReg = (UserRegistry) PortableRemoteObject.narrow(
					objRef, UserRegistry.class);
			out.println("<BR><BR>The user registry says your display name is: "
					+ userReg.getUserDisplayName(req.getUserPrincipal()
							.getName()));

			Subject subject = WSSubject.getCallerSubject();
			Set credSet = subject.getPublicCredentials(WSCredential.class);
			//should be impossible.
			if (credSet.size() > 1) {
				System.out
						.println("Expected only one WSCredential in Subject set");
				throw new Exception("Expected one WSCredential, found "
						+ credSet.size());
			}
			//is it empty?
			if (credSet.isEmpty()) {
				System.out.println("Credential set is empty");
				throw new Exception("Found no credentials");
			}
			//get one and only one element of Set
			Iterator iter = credSet.iterator();
			WSCredential creds = (WSCredential) iter.next();
			out.println("<BR><BR>Looking into your Subject your userid is "
					+ creds.getSecurityName());
			out.println("<BR><BR>Looking into your Subject your uniqueid is "
					+ creds.getUniqueSecurityName());
			out
					.println("<BR><BR>Looking into your Subject I see these groups: ");
			//List groups = helper.getGroups();
			List groups = creds.getGroupIds();
			iter = groups.iterator();
			while (iter.hasNext()) {
				String gid = (String) iter.next();
				out.println("<BR>Group ID: " + gid);
			}
			out.println("<BR><BR>By comparison the registry says you are in these groups:");
			groups = userReg.getGroupsForUser(req.getUserPrincipal().getName());
			//If you want to print out the group ids, use this
			//groups = userReg.getUniqueGroupIds(creds.getUniqueSecurityName());
			iter = groups.iterator();
			while (iter.hasNext()) {
				String gid = (String) iter.next();
				out.println("<BR>Group name = " + gid);
			}
		} catch (Exception e) {
			out.println("<BR>PrintUserInfo failed with " + e);
			e.printStackTrace();
		}
		Set set;
		out.println("<BR><BR>Checking for CustomJAASStuff in the Subject");
		try {
			set = WSSubject.getCallerSubject().getPublicCredentials(
					CustomJAASStuff.class);
			if ((set != null) && (!set.isEmpty())) {
				Iterator iter = set.iterator();
				out
						.println("<BR>Your subject appears to have CustomJAASStuff in it");
				CustomJAASStuff stuff = (CustomJAASStuff) iter.next();
				out.println("<BR>word1 = " + stuff.getWord1());
				out.println("<BR>word2 = " + stuff.getWord2());
				out.println("<BR>date = " + stuff.getDate());
			}
		} catch (WSSecurityException e1) {
			out.println("getCallerSubject() failed w/ " + e1);
			e1.printStackTrace();
		}

		if (req.isUserInRole("PrintUserInfoRole"))
			out
					.println("<BR><BR>You are in the PrintUserInfoRole security role");
		else
			out
					.println("<BR><BR>You are not in the PrintUserInfoRole security role");
		
		System.out.println("The Size "+CustomSecurity.getTaiProperties().size());

		out.flush();
	}
}