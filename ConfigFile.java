/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : ConfigFile.java
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFile {

	private static final String glb_headerFile="Trustopics Properties";
	private static final String glb_fileName = new String(System.getProperty("user.home")+"/.trustopics");
	private static Properties glb_properties;

	public ConfigFile() {}
	
	public void Open() throws TrustException {
		glb_properties = new Properties();
		if (LoadProps()) new TrustException("Lecture des propriétés impossible.");
	}

	public void Save() throws TrustException {
		if (SaveProps()) new TrustException("Sauvegarde des propriétés impossible.");
	}
	
	public String GetValue(String prm_cle) {
		return(glb_properties.getProperty(prm_cle));
	}
	
	public void SetValue(String prm_cle,String prm_value) {
		glb_properties.put(prm_cle,prm_value);
	}
	
	public void DelValue(String prm_cle) {
		glb_properties.remove(prm_cle);
	}
	
	private boolean LoadProps() {
		try { 
			FileInputStream lcl_fis = new FileInputStream(glb_fileName);
			if (lcl_fis != null) {
				glb_properties.load(lcl_fis);
				lcl_fis.close();
			}
			else return(true);
		}
		catch (FileNotFoundException except) {}
		catch (IOException except) { return(true); }
		return(false);
	}
	
	private boolean SaveProps() {
		try {
			FileOutputStream lcl_fos = new FileOutputStream(glb_fileName);
			if (lcl_fos != null) {
				glb_properties.store(lcl_fos,glb_headerFile);
				lcl_fos.close();
			}
			else return(true);
		}
		catch (IOException except) { return(true); }
		return (false);
	}
}
