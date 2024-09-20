/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : UrlConnect.java
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
