/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : ConfigTopicsUpdates.java
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

public class ConfigTopicsUpdates {
	
	private static final String[] glb_topicKeys = {"state","category","value","message","answer","view","author","authorAnswer"};
	public int GetMaxTopicDatas() { return(glb_topicKeys.length); }
	private static final String glb_indexKey = "index";
	
	public ConfigTopicsUpdates() {}
	
	public void LoadTopicsDatas() {
		try { new ConfigFile().Open(); }
		catch (TrustException prm_ev) { prm_ev.Action(); }
	}
	
	public void SaveTopicsDatas() {
		try { new ConfigFile().Save(); }
		catch (TrustException prm_ev) { prm_ev.Action(); }
	}

	public int GetTotalTopic() {
		ConfigFile lcl_configFile = new ConfigFile();
		int lcl_cpt = 0;
		while(true) {
			if (lcl_configFile.GetValue(glb_indexKey+(lcl_cpt+1)) == null) break;
			lcl_cpt++;
		}
		return(lcl_cpt);
	}
	
	public void ChangeTopicData(String[] prm_topicValues,int prm_keyIndice,String prm_value) {
		String lcl_keyValue = prm_topicValues[1]+"."+prm_topicValues[2];
		ConfigFile lcl_configFile = new ConfigFile();
		lcl_configFile.SetValue(lcl_keyValue+"."+glb_topicKeys[prm_keyIndice],prm_value);
	}

	public String[] GetTopicDatas(int prm_index) {
		ConfigFile lcl_configFile = new ConfigFile();
		String lcl_keyValue = lcl_configFile.GetValue(glb_indexKey+prm_index);
		String[] lcl_topicDatas = new String[GetMaxTopicDatas()];
		for (int i = 0 ; i < GetMaxTopicDatas() ; i++)
			lcl_topicDatas[i] = lcl_configFile.GetValue(lcl_keyValue+"."+glb_topicKeys[i]); 
		return(lcl_topicDatas);
	}
	
	public void AddTopicDatas(String[] prm_topicValues) {
		String lcl_keyValue = prm_topicValues[1]+"."+prm_topicValues[2];
		AddIndex(lcl_keyValue);
		ConfigFile lcl_configFile = new ConfigFile();
		for (int i = 0 ; i < GetMaxTopicDatas() ; i++)
			lcl_configFile.SetValue(lcl_keyValue+"."+glb_topicKeys[i],prm_topicValues[i]);
	}
	
	public void DelTopicDatas(String[] prm_topicValues) {
		String lcl_keyValue = prm_topicValues[1]+"."+prm_topicValues[2];
		DelIndex(lcl_keyValue);
		ConfigFile lcl_configFile = new ConfigFile();
		for (int i = 0 ; i < GetMaxTopicDatas() ; i++)
			lcl_configFile.DelValue(lcl_keyValue+"."+glb_topicKeys[i]);
	}
	
	private void AddIndex(String prm_keyValue) {
		new ConfigFile().SetValue(glb_indexKey+(GetTotalTopic()+1),prm_keyValue);
	}
	
	private void DelIndex(String prm_keyValue) {
		int lcl_totalTopic = GetTotalTopic();
		String[] lcl_topicKeys = new String[lcl_totalTopic];
		ConfigFile lcl_configFile = new ConfigFile();
		for (int i = 0 ; i < lcl_totalTopic ; i++) {
			lcl_topicKeys[i] = lcl_configFile.GetValue(glb_indexKey+(i+1));
			lcl_configFile.DelValue(glb_indexKey+(i+1));
		}
		int lcl_cpt = 1;
		for (int i = 0 ; i < lcl_totalTopic ; i++) {
			if (!lcl_topicKeys[i].equals(prm_keyValue)) {
				lcl_configFile.SetValue(glb_indexKey+lcl_cpt,lcl_topicKeys[i]);
				lcl_cpt++;
			}
		}
	}
}
 