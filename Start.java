/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : Start.java
 */

public class Start {
	public static void main(String[] args) {
		new WebWaitExtra().Start();
		try { new UrlConnect();	}
		catch (TrustException prm_ev) {	prm_ev.Action(); }
		new WebWaitExtra().Stop();
		try { 
			new ConfigFile().Open();
			new ConfigFile().Save();
			ConfigFile lcl_cf = new ConfigFile();
			if (lcl_cf.GetValue("browser") == null) { lcl_cf.SetValue("browser","firefox"); }
			if (lcl_cf.GetValue("xwin") == null) { lcl_cf.SetValue("xwin","200"); }
			if (lcl_cf.GetValue("ywin") == null) { lcl_cf.SetValue("ywin","200"); }
			lcl_cf.Save();
		}
		catch (TrustException prm_ev) { prm_ev.Action(); }
		Defines.trustButton = new TrustButton();
		new WebMemory().UpdateCategories();
		new TrustopicsUI();
	}
}
