/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : PhpSessId.java
 */

public class PhpSessId {
	
	public PhpSessId() {}

	public String Del(String lcl_string) {
		int lcl_result;
		lcl_result = lcl_string.indexOf("\">");
		if (lcl_result == -1) return(lcl_string);
		return(lcl_string.substring(lcl_result + 2,lcl_string.length()));
	}
}
