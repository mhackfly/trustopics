/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : ConfigFile.java
 * 
 * Trustopics est un utilitaire qui permet de suivre
 * les topics d'un forum, en informant de la présence de nouveaux
 * messages. L'adresse internet ou se situe le forum en question :
 * http://www.trustonme.net, un site d'aide Linux.
 *
 * Copyright (C) 2005 Machmot (machmot@club-internet.fr)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
