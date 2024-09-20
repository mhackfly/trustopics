/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : TopicsUpdates.java
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

import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

public class TopicsUpdates extends TimerTask {

	private double glb_startTime;
	private double glb_elpasedTime;
	private ArrayList glb_arrayList;
	private TopicsTextPane glb_topicsTextPane;

	public class TopicDatasStruct {
		public boolean isOk;
		public String state;
		public String categorie;
		public String value;
		public String message;
		public String currentAnswer;
		public String newAnswer;
		public String view;
		public String author;
		public String authorAnswer;
	}
	
	public TopicsUpdates(TopicsTextPane prm_roundTextPane) {
		glb_topicsTextPane = prm_roundTextPane;
	}
	
	public void run() {
		if (Defines.selectTopicIsInProgress) return;
		Logs lcl_logs = new Logs();
		lcl_logs.Clear();
		lcl_logs.AddTab(0);
		lcl_logs.Add("Mise à jour des topics. (" + new Date() + ")");
		lcl_logs.AddLine("~");
		while(true) { if (!Defines.eventMouseIsActivated) break; }
		int lcl_topicNb;
		ConfigTopicsUpdates lcl_ctu;
		lcl_ctu = new ConfigTopicsUpdates();
		lcl_ctu.LoadTopicsDatas();
		lcl_topicNb = lcl_ctu.GetTotalTopic();
		if (lcl_topicNb <= 0) return;
		int lcl_topicsTt = 0;
		Defines.elpasedTime = 0;
		Defines.filesDownload = 0;
		glb_startTime = System.currentTimeMillis();
		Defines.topicsUpdateIsInProgress = true;
		glb_topicsTextPane.UpdateList();
		String[] lcl_topicsDatasFile = new String[lcl_ctu.GetMaxTopicDatas()];
		glb_arrayList = new ArrayList();
		for (int i = 0 ; i < lcl_topicNb ; i++) {
			lcl_topicsDatasFile = lcl_ctu.GetTopicDatas(i + 1);
			TopicDatasStruct lcl_tds = new TopicDatasStruct();
			lcl_tds.isOk = false;
			lcl_tds.state = lcl_topicsDatasFile[0]; 
			lcl_tds.categorie = lcl_topicsDatasFile[1]; 
			lcl_tds.value = lcl_topicsDatasFile[2]; 
			lcl_tds.message = lcl_topicsDatasFile[3]; 
			lcl_tds.currentAnswer = lcl_topicsDatasFile[4]; 
			lcl_tds.newAnswer = "0";
			lcl_tds.view = lcl_topicsDatasFile[5];
			lcl_tds.author = lcl_topicsDatasFile[6];
			lcl_tds.authorAnswer = lcl_topicsDatasFile[7];
			glb_arrayList.add(lcl_tds);
		}
		String lcl_temp;
		boolean lcl_topicsExist = false;
		boolean lcl_categoriePageIsOk = false;
		WebMemory lcl_wm = new WebMemory();
		TopicDatasStruct lcl_tds = new TopicDatasStruct();
		for (int bcl_categorie = 0 ; bcl_categorie < new WebMemory().GetCategoriesSize() ; bcl_categorie ++) {
			for (int bcl_pageNb = 0 ; bcl_pageNb < (Defines.MaxPage + 1) ; bcl_pageNb ++) {
				for (int bcl_topicNbLocal = 0 ; bcl_topicNbLocal < lcl_topicNb ; bcl_topicNbLocal ++) {
					lcl_tds = (TopicDatasStruct)glb_arrayList.get(bcl_topicNbLocal);
					if (lcl_tds.categorie.equals(String.valueOf(bcl_categorie)) && !lcl_tds.isOk && !lcl_categoriePageIsOk) {
						lcl_categoriePageIsOk = true;
						new WebWaitExtra().Start();
						lcl_wm.UpdateTopics((Integer.parseInt(lcl_tds.categorie) + 1),(bcl_pageNb + 1));
						new WebWaitExtra().Stop();
						lcl_logs.AddTab(1);
						lcl_logs.AddEmpty();
						lcl_logs.Add("Lecture [Catégorie/Page] : " + new WebMemory().GetCategories(Integer.parseInt(lcl_tds.categorie)) + " / " + (bcl_pageNb + 1));
						lcl_logs.AddLine("-");
						lcl_logs.AddTab(2);
						lcl_logs.AddEmpty();
						lcl_logs.Add(">> Données recueillies : ");
						lcl_logs.Add("[Message/Identifiant/Nombre réponse/Nombre vue/Auteur topic/Auteur Message]");
						lcl_logs.AddEmpty();
						for (int i = 0 ; i < lcl_wm.GetTopicsAnswersSize() ; i++) {
							lcl_temp = lcl_wm.GetTopicMessage(i) + " / ";
							lcl_temp += lcl_wm.GetTopicValue(i) + " / ";
							lcl_temp += lcl_wm.GetTopicAnswer(i) + " / ";
							lcl_temp += lcl_wm.GetTopicView(i) + " / ";
							lcl_temp += lcl_wm.GetTopicAuthor(i) + " / ";
							lcl_temp += lcl_wm.GetTopicAuthorAnswer(i);
							lcl_logs.Add(lcl_temp);
						}
						lcl_logs.AddTab(3);
						lcl_logs.AddEmpty();
						lcl_logs.Add(">> Topics traités : ");
					}
					if (lcl_categoriePageIsOk) {
						for (int bcl_memoryResult = 0 ; bcl_memoryResult < lcl_wm.GetTopicsValuesSize() ; bcl_memoryResult ++) {
							if (lcl_tds.value.equals(lcl_wm.GetTopicValue(bcl_memoryResult))) {
								lcl_tds.isOk = true;
								lcl_tds.newAnswer = lcl_wm.GetTopicAnswer(bcl_memoryResult);
								lcl_tds.view = lcl_wm.GetTopicView(bcl_memoryResult);
								lcl_tds.authorAnswer = lcl_wm.GetTopicAuthorAnswer(bcl_memoryResult);
								if (lcl_tds.state.equals("0")) {
									lcl_tds.state = "1";
									lcl_ctu.ChangeTopicData(lcl_topicsDatasFile,0,lcl_tds.state);
								}
								lcl_logs.AddEmpty();
								lcl_logs.Add("Le topic a été traité : " + lcl_tds.isOk);
								lcl_logs.Add("L'état du topic [0,1,>1] : " + lcl_tds.state);
								lcl_logs.Add("Indice categorie : " + lcl_tds.categorie);
								lcl_logs.Add("Identifiant : " + lcl_tds.value);
								lcl_logs.Add("Message : " + lcl_tds.message);
								lcl_logs.Add("Nombre de réponse avant mise à jour : " + lcl_tds.currentAnswer);
								lcl_logs.Add("Nombre de réponse aprés mise à jour : " + lcl_tds.newAnswer);
								lcl_logs.Add("Nombre de vue : " + lcl_tds.view);
								lcl_logs.Add("Auteur du Topic : " + lcl_tds.author);
								lcl_logs.Add("Auteur du dernier message : " + lcl_tds.authorAnswer);
								lcl_logs.Add("Nombre de nouveau message : >> " + (Integer.parseInt(lcl_tds.newAnswer) - Integer.parseInt(lcl_tds.currentAnswer)) + " <<");
								lcl_topicsTt ++;
								lcl_topicsExist = true;
								break;
							}
						}
					}
				}
				if (!lcl_topicsExist && lcl_categoriePageIsOk) {
					lcl_logs.AddTab(3);
					lcl_logs.AddEmpty();
					lcl_logs.Add("Aucun");
				}
				lcl_categoriePageIsOk = false;
				lcl_topicsExist = false;
			}
		}
		lcl_logs.AddTab(1);
		lcl_logs.AddEmpty();
		lcl_logs.Add("Topics non traités");
		lcl_logs.AddLine("-");
		lcl_logs.AddTab(2);
		lcl_topicsExist = false;
		for (int i = 0 ; i < glb_arrayList.size() ; i++) {
			lcl_tds = (TopicDatasStruct)glb_arrayList.get(i);
			lcl_topicsDatasFile = lcl_ctu.GetTopicDatas(i + 1);
			if (!lcl_tds.isOk) {
				lcl_ctu.ChangeTopicData(lcl_topicsDatasFile,0,"0");
				lcl_logs.AddEmpty();
				lcl_logs.Add("Le topic a été traité : " + lcl_tds.isOk);
				lcl_logs.Add("L'état du topic [0,1,>1] : " + "0");
				lcl_logs.Add("Indice categorie : " + lcl_tds.categorie + " ( " + new WebMemory().GetCategories(Integer.parseInt(lcl_tds.categorie)) + " )");
				lcl_logs.Add("Identifiant : " + lcl_tds.value);
				lcl_logs.Add("Message : " + lcl_tds.message);
				lcl_logs.Add("Nombre de réponse : " + lcl_tds.currentAnswer);
				lcl_logs.Add("Nombre de vue : " + lcl_tds.view);
				lcl_logs.Add("Auteur du Topic : " + lcl_tds.author);
				lcl_logs.Add("Auteur du dernier message : " + lcl_tds.authorAnswer);
				lcl_topicsExist = true;
			}
			else {
				int lcl_newAnswer = Integer.parseInt(lcl_tds.newAnswer) - Integer.parseInt(lcl_tds.currentAnswer);
				int lcl_state = Integer.parseInt(lcl_tds.state);
				if (lcl_newAnswer > 0 && lcl_state >= 1) {
					lcl_state += lcl_newAnswer;
					lcl_ctu.ChangeTopicData(lcl_topicsDatasFile,0,String.valueOf(lcl_state));
					lcl_ctu.ChangeTopicData(lcl_topicsDatasFile,4,lcl_tds.newAnswer);
				}
				lcl_ctu.ChangeTopicData(lcl_topicsDatasFile,5,lcl_tds.view);
				lcl_ctu.ChangeTopicData(lcl_topicsDatasFile,7,lcl_tds.authorAnswer);
			}
		}
		if (!lcl_topicsExist) {
			lcl_logs.AddEmpty();
			lcl_logs.Add("Aucun");
		}
		lcl_ctu.SaveTopicsDatas();
		glb_topicsTextPane.LoadTopics();
		Defines.topicsUpdateIsInProgress = false;
		glb_topicsTextPane.UpdateList();
		glb_elpasedTime = System.currentTimeMillis() - glb_startTime;
		lcl_logs.AddTab(1);
		lcl_logs.AddEmpty();
		lcl_logs.Add("Infos sur la mise à jour.");
		lcl_logs.AddLine("-");
		lcl_logs.AddEmpty();
		lcl_logs.AddTab(2);
		lcl_logs.Add("Durée de la mise à jour : " + (glb_elpasedTime / 1000) + " secondes");
		lcl_logs.Add("Nombre de fichiers téléchargés : " + Defines.filesDownload);
		lcl_logs.Add("Temps de téléchargement des fichiers : " + (Defines.elpasedTime / 1000) + " secondes");
		lcl_logs.Add("Topics traités : " + lcl_topicsTt + "/" + lcl_topicNb);
		lcl_logs.Add("Fichiers téléchargés / Nombre de topics : " + (float)Defines.filesDownload/(float)lcl_topicNb + " (<= 1)");
	}
}
