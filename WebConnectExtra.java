/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : WebConnectExtra.java
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

public class WebConnectExtra {
	
	public WebConnectExtra() {
	}

	public String GetUrlTopicPage(int prm_categorie,int prm_topic) throws TrustException {
		boolean lcl_urlFound = false;
		TrustDatas lcl_trustDatas = new TrustDatas();
		WebParser lcl_webFiles = new WebParser();
		String lcl_topicValue;
		String lcl_urlPage;
		String lcl_url = lcl_trustDatas.GetUrlTopicsPage();
		for (int i = 0 ; i < (Defines.MaxPage + 1) ; i++) {
			lcl_urlPage = lcl_webFiles.SetParameter(lcl_trustDatas.GetTopicsFile(),lcl_trustDatas.GetKeyCategorie(),String.valueOf(prm_categorie));
			if (lcl_webFiles.LoadPage(lcl_webFiles.SetParameter(lcl_urlPage,lcl_trustDatas.GetKeyPage(),String.valueOf(i + 1))))
				throw new TrustException("Problème de lecture d'une page de topics (cat=" + prm_categorie + ";page=" + (i + 1) + ").");
			if (lcl_webFiles.SeekUrlLines(lcl_trustDatas.GetDatUrlTopicsPage(1),lcl_trustDatas.UrlKeys(lcl_trustDatas.GetDatUrlTopicsPage(1))))
				throw new TrustException("Aucune ligne de type URL n'a été trouvée.");
			for (int j = 0 ; j < lcl_webFiles.GetResultsPage().size() ; j++) {
				lcl_topicValue = lcl_webFiles.GetParameter(lcl_webFiles.GetResultLine(j),lcl_trustDatas.GetKeyTopic());
				if (lcl_topicValue.equals(String.valueOf(prm_topic)) && !lcl_urlFound) {
					lcl_url = lcl_webFiles.SetParameter(lcl_url,lcl_trustDatas.GetKeyCategorie(),String.valueOf(prm_categorie));
					lcl_url = lcl_webFiles.SetParameter(lcl_url,lcl_trustDatas.GetKeyTopic(),lcl_topicValue);
					lcl_url = lcl_webFiles.SetParameter(lcl_url,lcl_trustDatas.GetKeyPage(),lcl_webFiles.GetParameter(lcl_webFiles.GetResultLine(j),lcl_trustDatas.GetKeyPage()));
					lcl_urlFound = true;
				}
			}
			if (lcl_urlFound) break;
		}
		
		if (!lcl_urlFound) lcl_url = "";
		return(lcl_url);
	}
}
