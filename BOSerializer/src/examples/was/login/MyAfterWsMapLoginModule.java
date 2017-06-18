/*
 * Created on Oct 1, 2003
 *
 * by Keys Botzum (keys@us.ibm.com), IBM Software Services for WebSphere
 * 
 */
package examples.was.login;

import java.security.Principal;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

//import com.ibm.websphere.security.auth.WSSubject;

/**
 * @author keys
 *  
 */
public class MyAfterWsMapLoginModule implements LoginModule
   {
   Subject subject;

   /**
    *  
    */
   public MyAfterWsMapLoginModule()
      {
      super();

      System.out.println( "MYAfterwsMapLoginModule being loaded" );
      }

   /*
    * (non-Javadoc)
    * 
    * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject,
    *      javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
    */
   public void initialize( Subject subject, CallbackHandler arg1, Map sharedState, Map arg3 )
      {
      System.out.println( "MYAfterwsMapLoginModule initialize()" );
      this.subject = subject;
      }

   /*
    * (non-Javadoc)
    * 
    * @see javax.security.auth.spi.LoginModule#login()
    */
   public boolean login() throws LoginException
      {
      System.out.println( "MyAfterWsMapLoginModule login()" );

      return true;
      }

   /*
    * (non-Javadoc)
    * 
    * @see javax.security.auth.spi.LoginModule#commit()
    */
   public boolean commit() throws LoginException
      {
      String id = null;
      System.out.println( "MyAfterWsMapLoginModule commit() " );
      Set stuffset = subject.getPublicCredentials( CustomJAASStuff.class );
      if ( stuffset != null && stuffset.isEmpty() )
         {
         System.out.println( "MyAfterWsMapLoginModule never been called before." );
         CustomJAASStuff stuff = new CustomJAASStuff();
         Set principals = subject.getPrincipals(); 
         if ( ( principals != null ) && ( !principals.isEmpty() ) )
            {
            Iterator iter = principals.iterator();
            Principal p = (Principal)iter.next();
            id = p.getName();
            }
         StringBuffer id2 = null;
         id2 = new StringBuffer( id );
         stuff.setWord1( "Caller principal in caps = " + id.toUpperCase() ); 
         stuff.setWord2( "Caller principal in reverse = " + id2.reverse().toString() ); 
       	 System.out.println( "MyAfterWsMapLoginModule: adding to subject" );
       	 subject.getPublicCredentials().add( stuff );
         }
      else
         System.out.println( "Found existing CustomJAASStuff. Must be a reinvocation." ); 

      return true;
      }

   /*
    * (non-Javadoc)
    * 
    * @see javax.security.auth.spi.LoginModule#abort()
    */
   public boolean abort() throws LoginException
      {
      //nothing to abort since didn't modify Subject.
      System.out.println( "MyAfterWsMapLoginModule abort()" );

      return true;
      }

   /*
    * (non-Javadoc)
    * 
    * @see javax.security.auth.spi.LoginModule#logout()
    */
   public boolean logout() throws LoginException
      {
      System.out.println( "MyAfterWsMapLoginModule logout()" );

      return true;
      }
   }
