/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : WebParser.java
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class WebParser {
	
	private Vector glb_vecteurPage = null;
	private Vector glb_vecteurResultsPage = null;
	private Vector glb_vecteurIndicesResultsPage = null;
	private String glb_pattern;
	private String glb_line;
	private int glb_lineIndice;
	private double glb_startTime;
	
	public WebParser() {}
	
	public boolean LoadPage(String prm_page) {
		glb_vecteurPage = new Vector();
	    String lcl_line = new String();
	    try {
	    	URLConnection lcl_connection = new URL(prm_page).openConnection();
	    	BufferedReader lcl_sin = new BufferedReader(new InputStreamReader(lcl_connection.getInputStream()));
	    	if (!Defines.roundProgressIsVisible) glb_startTime = System.currentTimeMillis();
	    	while((lcl_line = lcl_sin.readLine()) != null) {
	    		glb_vecteurPage.add(new String(lcl_line));
	    		if (Defines.roundProgressIsVisible) Defines.roundProgress.MyUpdate();
	    	}
	    	if (!Defines.roundProgressIsVisible) {
	    		Defines.elpasedTime += System.currentTimeMillis() - glb_startTime;
	    		Defines.filesDownload ++;
	    	}
		    lcl_sin.close();
		    if (Defines.roundProgressIsVisible) Defines.roundProgress.Init();
	    }
	    catch (Exception ex) {
	    	return(true);
	    }
	    if (glb_vecteurPage.size() <= 0) return(true);
	    return (false);
	}

	public void PrintPage() {
		for(int i = 0; i < glb_vecteurPage.size(); i++) {
			System.out.println(glb_vecteurPage.elementAt(i));
		}
	}

	public void PrintResultsPage() {
		for(int i = 0; i < glb_vecteurResultsPage.size(); i++) {
			System.out.println(glb_vecteurResultsPage.elementAt(i));
		}
	}

	public boolean TestKey(String prm_url,String prm_key) {
		if (prm_url.indexOf(prm_key) == -1) return (true);
		return(false);
	}

	public boolean TestKeys(String prm_lineVector,String[] prm_keys) {
		int lcl_total = prm_keys.length;
		int lcl_compteur = 0;
		for (int i = 0 ; i < prm_keys.length ; i ++) {
			if (!TestKey(prm_lineVector,prm_keys[i])) lcl_compteur ++; 
		}
		if (lcl_total != lcl_compteur) return(true);
		return(false);
	}

	public String ReplaceKeys(String prm_lineStart,String prm_lineVector,String[] prm_keys) {
		String lcl_lineStart = prm_lineStart;
		for (int i = 0 ; i < prm_keys.length ; i ++) {
			lcl_lineStart = SetParameter(lcl_lineStart,prm_keys[i],GetParameter(prm_lineVector,prm_keys[i]));
		}
		return(lcl_lineStart);
	}
	
	public String GetParameter(String prm_url,String prm_key) {
		String lcl_value = "";
		char lcl_car;
		int lcl_index;
		int i = 0;
		while(true) {
			lcl_index = prm_url.indexOf(prm_key) + prm_key.length() + i;
			if (lcl_index >= prm_url.length()) break;
			lcl_car = prm_url.charAt(lcl_index);
			if (lcl_car >= '0' && lcl_car <= '9') lcl_value += lcl_car;
			else break;
			i ++;
		}
		return(lcl_value);
	}
	
	public String SetParameter(String prm_url,String prm_key,String prm_value) {
		int lcl_keyIndice;
		StringBuffer lcl_url = new StringBuffer(prm_url);
		lcl_keyIndice = prm_url.indexOf(prm_key) + prm_key.length();
		lcl_url.replace(lcl_keyIndice,lcl_keyIndice + 1,prm_value);
		return(lcl_url.toString());
	}

	public boolean SeekLines(String prm_start) {
		String lcl_empty[] = new String[0];
		SeekUrlLines(prm_start,lcl_empty);
		return(false);
	}
	
	public boolean SeekUrlLines(String prm_start,String[] prm_keys) {
		glb_vecteurResultsPage = new Vector();
		glb_vecteurIndicesResultsPage = new Vector();
		String lcl_lineVector;
		String lcl_lineStart;
		for(int i = 0;i < glb_vecteurPage.size();i ++) {
			lcl_lineVector = ((String)glb_vecteurPage.elementAt(i));
			if (prm_keys.length > 0 && !TestKeys(lcl_lineVector,prm_keys)) {
				lcl_lineStart = ReplaceKeys(prm_start,lcl_lineVector,prm_keys);
			}
			else {
				lcl_lineStart = prm_start;
			}
			for(int j = 0;j < lcl_lineVector.length();j ++) {
				if (lcl_lineVector.indexOf(lcl_lineStart) != -1) {
					glb_vecteurResultsPage.add(new String(lcl_lineVector));
					glb_vecteurIndicesResultsPage.add(new String(String.valueOf(i)));
		    		break;
				}
			}
		}
		if (glb_vecteurResultsPage.size() == 0) return(true);
		return(false);
	}

	public boolean SeekPatternLine(String prm_start,String prm_end) {
		if (!SeekLine(prm_start)) {
			if (!SeekPattern(glb_line,prm_start,prm_end)) {
				return(false);
			}
		}
		return(true);
	}
	
	public boolean SeekLine(String prm_start) {
		glb_line = "NULL";
		glb_lineIndice = 0;
		String lcl_line;
		for(int i = 0;i < glb_vecteurPage.size();i ++) {
			lcl_line = ((String)glb_vecteurPage.elementAt(i));
			for(int j = 0;j < lcl_line.length();j ++) {
				if (lcl_line.regionMatches(true,j,prm_start,0,prm_start.length())) {
					glb_line = lcl_line;
					glb_lineIndice = i;
					return(false);
				}
			}
		}
		return(true);
	}

	public boolean SeekPattern(String prm_line,String prm_start,String prm_end) {
		glb_pattern = "NULL";
		int lcl_start;
		int lcl_end;
		lcl_start = prm_line.indexOf(prm_start);
		lcl_end = prm_line.indexOf(prm_end);
		if ( lcl_start == -1 || lcl_end == -1) return(true);
		glb_pattern = new PhpSessId().Del(prm_line.substring(lcl_start + prm_start.length(),lcl_end));
		return(false);
	}
	
	public Vector GetPage() {
		return(glb_vecteurPage);
	}

	public String GetPageLine(int prm_indice) {
		return((String)glb_vecteurPage.elementAt(prm_indice));
	}

	public Vector GetResultsPage() {
		return(glb_vecteurResultsPage);
	}

	public String GetResultLine(int prm_indice) {
		return((String)glb_vecteurResultsPage.elementAt(prm_indice));
	}
	
	public Vector GetIndicesResultsPage() {
		return(glb_vecteurIndicesResultsPage);
	}

	public String GetIndicesResultLine(int prm_indice) {
		return((String)glb_vecteurIndicesResultsPage.elementAt(prm_indice));
	}
	
	public String GetPattern() {
		return(glb_pattern);
	}
	
	public String GetPatternLine() {
		return(glb_line);
	}

	public int GetIndicePatternLine() {
		return(glb_lineIndice);
	}
}
