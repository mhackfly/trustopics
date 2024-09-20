/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : UrlConnect.java
 */

import java.net.MalformedURLException;
import java.net.URL;

public class UrlConnect {

	public UrlConnect() throws TrustException {
		TrustDatas lcl_trustDatas = new TrustDatas();
		WebParser lcl_webFiles = new WebParser();
		if (TestUrl(lcl_trustDatas)) throw
			new TrustException("L'URL n'est pas valable.");
		if (TestUrlConnection(lcl_webFiles,lcl_trustDatas)) throw
			new TrustException("Problème de connexion.");
		if (TestUrlTrustonme(lcl_webFiles,lcl_trustDatas)) throw
			new TrustException("Problème de lecture de la page d'acceuil de Trustonme.");
	}

	private boolean TestUrl(TrustDatas lcl_trustDatas) {
		URL lcl_page = null;
		try {
			lcl_page = new URL(lcl_trustDatas.GetTrustSite());
		} 
		catch (MalformedURLException mue) {
			return(true);
		}
		return(false);
	}
	
	private boolean TestUrlConnection(WebParser prm_webFiles,TrustDatas prm_trustDatas) {
		if (prm_webFiles.LoadPage(prm_trustDatas.GetTrustSite())) {
			return(true);
		}
		return(false);
	}
	
	private boolean TestUrlTrustonme(WebParser prm_webFiles,TrustDatas prm_trustDatas) {
		boolean lcl_result = true;
		lcl_result = prm_webFiles.SeekPatternLine(prm_trustDatas.GetDatTitle(1),prm_trustDatas.GetDatTitle(2));
		if (lcl_result || !prm_trustDatas.GetTrustTitle().equals(prm_webFiles.GetPattern()))
			return(true);
		return(false);
	}
}
