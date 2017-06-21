 package examples.was.login;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import javax.security.auth.Subject;

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;

public class CustomSecurity {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public Hashtable getInfo() {
		Hashtable results = new Hashtable();
		try {
			Subject subject = WSSubject.getCallerSubject();
			Set<Hashtable> set = subject.getPublicCredentials(Hashtable.class);
			if (set != null) {
				for (Hashtable hashtable : set) {
					results.putAll(hashtable);
				}
			}
		} catch (WSSecurityException e) {
			// Unable to find subject
			e.printStackTrace();
		}

		return results;
	}
	
	public static Properties getTaiProperties() {
		Properties taiProperties = null;
		try {
			Set<Properties> set = WSSubject.getCallerSubject().getPublicCredentials(Properties.class);
			if ((set != null) && (!set.isEmpty())) {
				// subject appears to have Properties in it
				taiProperties = (Properties) set.iterator().next();
			}
		} catch (WSSecurityException e) {
			// Unable to find subject
			e.printStackTrace();
		}

		return taiProperties;
	}
	
	public static String getRawToken() {
		Properties prop = getTaiProperties();
		
		if (prop==null) return null;
		
		return prop.getProperty("rawToken");
	}
	
	public static String getClearToken() {
			
		Properties prop = getTaiProperties();
		
		if (prop==null) return null;
		
		return prop.getProperty("clearToken");
	}

	public static String getGroups() {
		Properties prop = getTaiProperties();
		
		return prop.getProperty("groups");
	}

	public static HashMap<String, String> getCustomSecurityProfileAttributesMap() {
		Properties prop = getTaiProperties();

		Set<Object> keySets = prop.keySet();
		String propertyValue;
		HashMap<String, String> propMap = new HashMap<String, String>();
		
		for (Object propertyName: keySets) {
			propertyValue = prop.getProperty((String) propertyName);
			propMap.put((String) propertyName, propertyValue);
		}
		
		return propMap;
	}
}