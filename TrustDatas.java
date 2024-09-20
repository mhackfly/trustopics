/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : TrustDatas.java
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

import java.util.Vector;

public class TrustDatas {

	private String glb_trustSite;
	private String glb_trustTitle;
	private String glb_topicsFile;
	private String glb_urlTopicsPage;
	private String glb_trustLogo;
	private String glb_trustButton;
	private String glb_keyCategorie;
	private String glb_keyTopic;
	private String glb_keyPage;
	private String glb_datTitle[] = new String[3];
	private String glb_datCategories[] = new String[3];
	private String glb_datTopics[] = new String[3];
	private String glb_datAnswers[] = new String[3];
	private String glb_datViews[] = new String[3];
	private String glb_datTopicsAuthors[] = new String[3];
	private String glb_datAnswersAuthors[] = new String[3];
	private String glb_datUrlTopicsPage[] = new String[3];
	
	public TrustDatas() {
		glb_trustSite = "http://www.trustonme.net";
		glb_trustTitle = "Trustonme - GNU/Linux pour tous";
		glb_topicsFile = "http://www.trustonme.net/forum/public/topic.php?cat=0&page=0";
		glb_urlTopicsPage = "http://www.trustonme.net/forum/public/post.php?cat=0&topic=0&page=0#bas";
		glb_trustButton = "http://www.trustonme.net/images/Trustonme_88px.png";
		glb_keyCategorie = "cat=";
		glb_keyTopic = "topic=";
		glb_keyPage = "page=";
		glb_datTitle[0] = "http://www.trustonme.net";
		glb_datTitle[1] = "<title>";
		glb_datTitle[2] = "</title>";
		
		
		glb_datCategories[0] = "http://www.trustonme.net/forum/index.php";
		glb_datCategories[1] = "<a href=\"./public/topic.php?cat=0&PHPSESSID";
		glb_datCategories[2] = "</a>";
		glb_datTopics[0] = "http://www.trustonme.net/forum/public/topic.php?cat=0&page=0";
		glb_datTopics[1] = "<a href=\"./post.php?cat=0&amp;topic=0&PHPSESSID";
		glb_datTopics[2] = "</a>";
		glb_datAnswers[0] = "http://www.trustonme.net/forum/public/topic.php?cat=0&page=0";
		glb_datAnswers[1] = "<td class=\"gris2 centre ver10\">";
		glb_datAnswers[2] = "</td>";
		glb_datViews[0] = "http://www.trustonme.net/forum/public/topic.php?cat=0&page=0";
		glb_datViews[1] = "<td class=\"gris2 centre ver10\">";
		glb_datViews[2] = "</td>";
		glb_datTopicsAuthors[0] = "http://www.trustonme.net/forum/public/topic.php?cat=0&page=0";
		glb_datTopicsAuthors[1] = "<td class=\"gris1 centre ver12\">";
		glb_datTopicsAuthors[2] = "</td>";
		glb_datAnswersAuthors[0] = "http://www.trustonme.net/forum/public/topic.php?cat=0&page=0";
		glb_datAnswersAuthors[1] = "<a href=\"./post.php?cat=0&amp;topic=0&amp;page=0&PHPSESSID";
		glb_datAnswersAuthors[2] = "</a>";
		glb_datUrlTopicsPage[0] = "http://www.trustonme.net/forum/public/topic.php?cat=0&page=0";
		glb_datUrlTopicsPage[1] = "<a href=\"./post.php?cat=0&amp;topic=0&amp;page=0&PHPSESSID";
		glb_datUrlTopicsPage[2] = "</a>";
	}
	
	public String[] UrlKeys(String prm_urlLine) {
		Vector lcl_vecteur = new Vector();
		if (prm_urlLine.indexOf(glb_keyCategorie) != -1) lcl_vecteur.add(new String(glb_keyCategorie));
		if (prm_urlLine.indexOf(glb_keyTopic) != -1) lcl_vecteur.add(new String(glb_keyTopic));
		if (prm_urlLine.indexOf(glb_keyPage) != -1) lcl_vecteur.add(new String(glb_keyPage));
		// new keys ...
		String lcl_keys[] = new String[lcl_vecteur.size()];
		for (int i = 0 ; i < lcl_vecteur.size() ; i++) {
			lcl_keys[i] = ((String)lcl_vecteur.elementAt(i));
		}
		return(lcl_keys);
	}
	
	public String GetTrustSite() { return(glb_trustSite); }
	public String GetTrustTitle() { return(glb_trustTitle); }
	public String GetTopicsFile() { return(glb_topicsFile); }
	public String GetUrlTopicsPage() { return(glb_urlTopicsPage);	}
	public String GetTrustLogo() { return(glb_trustLogo); }
	public String GetTrustButton() { return(glb_trustButton); }
	public String GetKeyCategorie() { return(glb_keyCategorie);	}
	public String GetKeyTopic() { return(glb_keyTopic);	}
	public String GetKeyPage() { return(glb_keyPage); }
	public String GetDatTitle(int prm_indice) {	return(glb_datTitle[prm_indice]); }
	public String GetDatCategorie(int prm_indice) {	return(glb_datCategories[prm_indice]); }
	public String GetDatTopic(int prm_indice) {	return(glb_datTopics[prm_indice]); }
	public String GetDatAnswer(int prm_indice) { return(glb_datAnswers[prm_indice]); }
	public String GetDatView(int prm_indice) { return(glb_datViews[prm_indice]); }
	public String GetDatTopicAuthor(int prm_indice) { return(glb_datTopicsAuthors[prm_indice]);	}
	public String GetDatAnswerAuthor(int prm_indice) { return(glb_datAnswersAuthors[prm_indice]); }
	public String GetDatUrlTopicsPage(int prm_indice) { return(glb_datUrlTopicsPage[prm_indice]);	}
}
