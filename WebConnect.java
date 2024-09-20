/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : WebConnect.java
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

public class WebConnect {
	
	private WebParser glb_webFiles;

	public WebConnect() {
	}
	
	public String[] GetCategories() throws TrustException {
		Vector lcl_vecteur = new Vector();
		TrustDatas lcl_trustDatas = new TrustDatas();
		WebParser lcl_webFiles = new WebParser();
		int lcl_indice = 0;
		boolean lcl_result;
		String lcl_newUrl;
		if (lcl_webFiles.LoadPage(lcl_trustDatas.GetDatCategorie(0))) 
			throw new TrustException("Problème de lecture de la page des catégories.");
		while(true) {
			lcl_indice ++;
			lcl_newUrl = lcl_webFiles.SetParameter(lcl_trustDatas.GetDatCategorie(1),lcl_trustDatas.GetKeyCategorie(),String.valueOf(lcl_indice));
			lcl_result = lcl_webFiles.SeekPatternLine(lcl_newUrl,lcl_trustDatas.GetDatCategorie(2));
			if (lcl_result) {
				break;
			}
			lcl_vecteur.add(lcl_webFiles.GetPattern());
		}
		if (lcl_vecteur.size() == 0) 
			throw new TrustException("Aucune catégorie trouvée.");
		return(CopyVectorToTable(lcl_vecteur));
	}

	public void GetTopicsFile(int prm_categories,int prm_page) throws TrustException {
		glb_webFiles = new WebParser();
		TrustDatas lcl_trustDatas = new TrustDatas();
		String lcl_urlPage;
		lcl_urlPage = glb_webFiles.SetParameter(lcl_trustDatas.GetTopicsFile(),lcl_trustDatas.GetKeyCategorie(),String.valueOf(prm_categories));
		if (glb_webFiles.LoadPage(glb_webFiles.SetParameter(lcl_urlPage,lcl_trustDatas.GetKeyPage(),String.valueOf(prm_page))))
			throw new TrustException("Problème de lecture d'une page de topics (cat=" + prm_categories + ";page=" + prm_page + ").");
	}
	
	public String[] GetTopicsMessages() throws TrustException {
		Vector lcl_vecteur = new Vector();
		TrustDatas lcl_trustDatas = new TrustDatas();
		String lcl_lineKeysReplace;
		if (glb_webFiles.SeekUrlLines(lcl_trustDatas.GetDatTopic(1),lcl_trustDatas.UrlKeys(lcl_trustDatas.GetDatTopic(1))))
			throw new TrustException("Aucune ligne de type URL n'a été trouvée.");
		for (int i = 0 ; i < glb_webFiles.GetResultsPage().size() ; i++) {
			lcl_lineKeysReplace = glb_webFiles.ReplaceKeys(lcl_trustDatas.GetDatTopic(1),glb_webFiles.GetResultLine(i),lcl_trustDatas.UrlKeys(lcl_trustDatas.GetDatTopic(1)));
			if (glb_webFiles.SeekPattern(glb_webFiles.GetResultLine(i),lcl_lineKeysReplace,lcl_trustDatas.GetDatTopic(2)))
				throw new TrustException("Un message de la liste des résultats n'a pas été trouvé.");
			lcl_vecteur.add(glb_webFiles.GetPattern());
		}
		if (lcl_vecteur.size() == 0) 
			throw new TrustException("Aucun message n'a été trouvé.");
		return(CopyVectorToTable(lcl_vecteur));
	}
	
	public String[] GetTopicsValues() throws TrustException {
		Vector lcl_vecteur = new Vector();
		TrustDatas lcl_trustDatas = new TrustDatas();
		String lcl_topicValue;
		if (glb_webFiles.SeekUrlLines(lcl_trustDatas.GetDatTopic(1),lcl_trustDatas.UrlKeys(lcl_trustDatas.GetDatTopic(1))))
			throw new TrustException("Aucune ligne de type URL n'a été trouvée.");
		for (int i = 0 ; i < glb_webFiles.GetResultsPage().size() ; i++) {
			lcl_topicValue = glb_webFiles.GetParameter(glb_webFiles.GetResultLine(i),lcl_trustDatas.GetKeyTopic());
			lcl_vecteur.add(lcl_topicValue);
		}
		if (lcl_vecteur.size() == 0) 
			throw new TrustException("Aucun numéro de topic n'a été trouvé.");
		return(CopyVectorToTable(lcl_vecteur));
	}
	
	public String[] GetTopicsAnswers() throws TrustException {
		Vector lcl_vecteur = new Vector();
		TrustDatas lcl_trustDatas = new TrustDatas();
		int lcl_indice,j;
		boolean lcl_result;
		if (glb_webFiles.SeekUrlLines(lcl_trustDatas.GetDatTopic(1),lcl_trustDatas.UrlKeys(lcl_trustDatas.GetDatTopic(1))))
			throw new TrustException("Aucune ligne de type URL n'a été trouvée.");
		for (int i = 0 ; i < glb_webFiles.GetIndicesResultsPage().size() ; i++) {
			lcl_indice = Integer.valueOf(glb_webFiles.GetIndicesResultLine(i)).intValue();
			for (j = lcl_indice ; j < glb_webFiles.GetPage().size() ; j++ ) {
				lcl_result = glb_webFiles.SeekPattern(glb_webFiles.GetPageLine(j),lcl_trustDatas.GetDatAnswer(1),lcl_trustDatas.GetDatAnswer(2));
				if (!lcl_result) {
					lcl_vecteur.add(glb_webFiles.GetPattern());
					break;
				}
			}
			if (j == glb_webFiles.GetPage().size()) 
				throw new TrustException("Un nombre de réponse de la liste des résultats n'a pas été trouvé.");
		}
		if (lcl_vecteur.size() == 0) 
			throw new TrustException("Aucun nombre de réponse n'a été trouvé.");
		return(CopyVectorToTable(lcl_vecteur));
	}
	
	public String[] GetTopicsViews() throws TrustException {
		Vector lcl_vecteur = new Vector();
		TrustDatas lcl_trustDatas = new TrustDatas();
		int lcl_indice,j;
		boolean lcl_result1,lcl_result2;
		if (glb_webFiles.SeekUrlLines(lcl_trustDatas.GetDatTopic(1),lcl_trustDatas.UrlKeys(lcl_trustDatas.GetDatTopic(1))))
			throw new TrustException("Aucune ligne de type URL n'a été trouvée.");
		for (int i = 0 ; i < glb_webFiles.GetIndicesResultsPage().size() ; i++) {
			lcl_indice = Integer.valueOf(glb_webFiles.GetIndicesResultLine(i)).intValue();
			for (j = lcl_indice ; j < glb_webFiles.GetPage().size() ; j++ ) {
				lcl_result1 = glb_webFiles.SeekPattern(glb_webFiles.GetPageLine(j),lcl_trustDatas.GetDatAnswer(1),lcl_trustDatas.GetDatAnswer(2));
				lcl_result2 = glb_webFiles.SeekPattern(glb_webFiles.GetPageLine(j+1),lcl_trustDatas.GetDatView(1),lcl_trustDatas.GetDatView(2));
				if (!lcl_result1 && !lcl_result2) {
					lcl_vecteur.add(glb_webFiles.GetPattern());
					break;
				}
			}
			if (j == glb_webFiles.GetPage().size()) 
				throw new TrustException("Un nombre de vue de la liste des résultats n'a pas été trouvé.");
		}
		if (lcl_vecteur.size() == 0) 
			throw new TrustException("Aucun nombre de vue n'a été trouvé.");
		return(CopyVectorToTable(lcl_vecteur));
	}

	public String[] GetTopicsAuthors() throws TrustException {
		Vector lcl_vecteur = new Vector();
		TrustDatas lcl_trustDatas = new TrustDatas();
		int lcl_indice,j;
		boolean lcl_result;
		if (glb_webFiles.SeekUrlLines(lcl_trustDatas.GetDatTopic(1),lcl_trustDatas.UrlKeys(lcl_trustDatas.GetDatTopic(1))))
			throw new TrustException("Aucune ligne de type URL n'a été trouvée.");
		for (int i = 0 ; i < glb_webFiles.GetIndicesResultsPage().size() ; i++) {
			lcl_indice = Integer.valueOf(glb_webFiles.GetIndicesResultLine(i)).intValue();
			for (j = lcl_indice ; j < glb_webFiles.GetPage().size() ; j++ ) {
				lcl_result = glb_webFiles.SeekPattern(glb_webFiles.GetPageLine(j),lcl_trustDatas.GetDatTopicAuthor(1),lcl_trustDatas.GetDatTopicAuthor(2));
				if (!lcl_result) {
					lcl_vecteur.add(glb_webFiles.GetPattern());
					break;
				}
			}
			if (j == glb_webFiles.GetPage().size()) 
				throw new TrustException("Un auteur de topic de la liste des résultats n'a pas été trouvé.");
		}
		if (lcl_vecteur.size() == 0) 
			throw new TrustException("Aucun auteur de topic n'a été trouvé.");
		return(CopyVectorToTable(lcl_vecteur));
	}
	
	public String[] GetTopicsAnswersAuthors() throws TrustException {
		Vector lcl_vecteur = new Vector();
		TrustDatas lcl_trustDatas = new TrustDatas();
		String lcl_lineKeysReplace;
		int lcl_testValue;
		boolean lcl_testValueOk;
		if (glb_webFiles.SeekUrlLines(lcl_trustDatas.GetDatAnswerAuthor(1),lcl_trustDatas.UrlKeys(lcl_trustDatas.GetDatAnswerAuthor(1))))
			throw new TrustException("Aucune ligne de type URL n'a été trouvée.");
		for (int i = 0 ; i < glb_webFiles.GetResultsPage().size() ; i++) {
			lcl_lineKeysReplace = glb_webFiles.ReplaceKeys(lcl_trustDatas.GetDatAnswerAuthor(1),glb_webFiles.GetResultLine(i),lcl_trustDatas.UrlKeys(lcl_trustDatas.GetDatAnswerAuthor(1)));
			if (glb_webFiles.SeekPattern(glb_webFiles.GetResultLine(i),lcl_lineKeysReplace,lcl_trustDatas.GetDatAnswerAuthor(2)))
				throw new TrustException("Un auteur du dernier message de la liste des résultats n'a pas été trouvé.");
			lcl_testValueOk = false;
			try { lcl_testValue = Integer.valueOf(glb_webFiles.GetPattern()).intValue(); }
			catch(NumberFormatException err) { lcl_testValueOk = true; }
			if (lcl_testValueOk == true) lcl_vecteur.add(glb_webFiles.GetPattern());
		}
		if (lcl_vecteur.size() == 0) 
			throw new TrustException("Aucun auteur du dernier message n'a été trouvé.");
		return(CopyVectorToTable(lcl_vecteur));
	}
	
	private String[] CopyVectorToTable(Vector prm_vecteur) {
		String lcl_table[] = new String[prm_vecteur.size()];
		for (int i = 0 ; i < prm_vecteur.size() ; i++) {
			lcl_table[i] = (String)prm_vecteur.elementAt(i);
		}
		return(lcl_table);
	}
}
