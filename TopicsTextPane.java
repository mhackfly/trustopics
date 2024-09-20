/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : TopicsTextPane.java
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class TopicsTextPane extends JTextPane implements HyperlinkListener {

	private Vector glb_vectTopicsList = new Vector();
	private Vector glb_vectTopicsHtmlList = new Vector();
	private String[][] glb_htmlDatas_0 = new String[2][2];
	private String[][] glb_htmlDatas_1 = new String[3][3];
	private String[][] glb_htmlDatas_2 = new String[5][3];
	private boolean glb_htmlDataAdd = false;
	
	private HTMLEditorKit glb_kit;
	private HTMLDocument glb_doc;
	
	/*
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	*/
	
	public TopicsTextPane() {
		InitDatas();
		glb_kit = new HTMLEditorKit();
		glb_doc = (HTMLDocument) (glb_kit.createDefaultDocument());
		addHyperlinkListener(this);
		addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent evt) {
				glb_htmlDataAdd = false;
				UpdateList();
			}
		});
		setEditable(false);
		setEditorKit(glb_kit);
		setDocument(glb_doc);
		setContentType("text/html");
		setBackground(Defines.UIcolBackGDrawing);
		LoadTopics();
		setText(CreateHtmlList());
	}

	public void ExternClear() {
		ConfigTopicsUpdates lcl_ctu = new ConfigTopicsUpdates();
		lcl_ctu.LoadTopicsDatas();
		int lcl_nbTopic = lcl_ctu.GetTotalTopic();
		if (lcl_nbTopic > 0) {
			String[] lcl_topicsDatas = new String[lcl_ctu.GetMaxTopicDatas()];
			for (int i = 0 ; i < lcl_nbTopic ; i++) {
				lcl_topicsDatas = lcl_ctu.GetTopicDatas(1);
				lcl_ctu.DelTopicDatas(lcl_topicsDatas);
				glb_vectTopicsList.removeElementAt(0);
				glb_vectTopicsHtmlList.removeElementAt(0);
			}
			lcl_ctu.SaveTopicsDatas();
			UpdateList();
		}
	}
	
	public void ExternRaz() {
		ConfigTopicsUpdates lcl_ctu = new ConfigTopicsUpdates();
		lcl_ctu.LoadTopicsDatas();
		int lcl_nbTopic = lcl_ctu.GetTotalTopic();
		if (lcl_nbTopic > 0) {
			String[] lcl_topicsDatas = new String[lcl_ctu.GetMaxTopicDatas()];
			for (int i = 0 ; i < lcl_nbTopic ; i++) { 
				lcl_topicsDatas = lcl_ctu.GetTopicDatas(i + 1);
				lcl_ctu.ChangeTopicData(lcl_topicsDatas,0,"1");
				lcl_topicsDatas[0] = "1";
				glb_vectTopicsList.setElementAt(lcl_topicsDatas,i);
				glb_vectTopicsHtmlList.set(i,CreateHtmlLine(i));
			}
			lcl_ctu.SaveTopicsDatas();
			UpdateList();
		}
	}
	
	public void UpdateList() {
		try {
			DefaultHtmlList();
			setText(CreateHtmlList());
			setEditable(false);
			setEditorKit(glb_kit);
			setDocument(glb_doc);
			setContentType("text/html");
		}
		catch(Exception prm_exc) {/* bug pb maj JTextPane ? à résoudre...*/}
	}
	
	public void hyperlinkUpdate(HyperlinkEvent prm_ev) {
		Defines.eventMouseIsActivated = true;
		String lcl_description = prm_ev.getDescription();
		if (prm_ev.getEventType() == HyperlinkEvent.EventType.ENTERED) {
			if (ParamatersColAndLigIsOk(lcl_description)==true) {
				int[] lcl_param = new int[3];
				lcl_param = GetParametersColAndLig(lcl_description);
				if (glb_vectTopicsHtmlList.size() > lcl_param[1]) { 
					glb_vectTopicsHtmlList.set(lcl_param[1],CreateHtmlLine(lcl_param[1],lcl_param[2]));
					setText(CreateHtmlList(lcl_param[1],lcl_param[2]));
				}
			}
			if (lcl_description.equals("add")) {
				glb_htmlDataAdd = true;
				setText(CreateHtmlList());
			}
		}
		if (prm_ev.getEventType() == HyperlinkEvent.EventType.EXITED) {
			if (ParamatersColAndLigIsOk(lcl_description)) {
				int[] lcl_param = new int[3];
				lcl_param = GetParametersColAndLig(lcl_description);
				if (glb_vectTopicsHtmlList.size() > lcl_param[1]) { 
					glb_vectTopicsHtmlList.set(lcl_param[1],CreateHtmlLine(lcl_param[1]));
					setText(CreateHtmlList());
				}
			}
			if (lcl_description.equals("add")) {
				glb_htmlDataAdd = false;
				setText(CreateHtmlList());
			}
		}
		if (prm_ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			if (lcl_description.equals("add")) {
				AddTopic();
				DefaultHtmlList();
				glb_htmlDataAdd = false;
				setText(CreateHtmlList());
			}
			else {
				if (ParamatersColAndLigIsOk(lcl_description)) {
					setText("");
					int[] lcl_param = new int[3];
					lcl_param = GetParametersColAndLig(lcl_description);
					HyperlinkEvent(lcl_param[1],lcl_param[2]);
					DefaultHtmlList();
					setText(CreateHtmlList());
				}
			}
		}
		Defines.eventMouseIsActivated = false;
	}

	public void LoadTopics() {
		glb_vectTopicsList.removeAllElements();
		glb_vectTopicsHtmlList.removeAllElements();
		ConfigTopicsUpdates lcl_ctu = new ConfigTopicsUpdates();
		lcl_ctu.LoadTopicsDatas();
		int lcl_cptTopics = lcl_ctu.GetTotalTopic();
		String[] lcl_topicsDatas = new String[lcl_ctu.GetMaxTopicDatas()];
		for (int i = 0 ; i < lcl_cptTopics ; i++) {
			lcl_topicsDatas = lcl_ctu.GetTopicDatas(i+1);
			glb_vectTopicsList.add(lcl_topicsDatas);
			glb_vectTopicsHtmlList.add(CreateHtmlLine(i));
		}
	}
	
	private void AddTopic() {
		Defines.selectTopicIsInProgress = true;
		SelectTopicUI lcl_st = new SelectTopicUI();
		if (lcl_st.TopicIsSelected()) {
			WebMemory lcl_wm = new WebMemory();
			ConfigTopicsUpdates lcl_ctu = new ConfigTopicsUpdates();
			lcl_ctu.LoadTopicsDatas();
			String[] lcl_topicsDatas = new String[lcl_ctu.GetMaxTopicDatas()];
			lcl_topicsDatas[0] = "1";
			lcl_topicsDatas[1] = Integer.toString(lcl_st.GetCategorie());
			lcl_topicsDatas[2] = lcl_wm.GetTopicValue(lcl_st.GetTopic());
			lcl_topicsDatas[3] = lcl_wm.GetTopicMessage(lcl_st.GetTopic());
			lcl_topicsDatas[4] = lcl_wm.GetTopicAnswer(lcl_st.GetTopic());
			lcl_topicsDatas[5] = lcl_wm.GetTopicView(lcl_st.GetTopic());
			lcl_topicsDatas[6] = lcl_wm.GetTopicAuthor(lcl_st.GetTopic());
			lcl_topicsDatas[7] = lcl_wm.GetTopicAuthorAnswer(lcl_st.GetTopic());
			glb_vectTopicsList.add(lcl_topicsDatas);
			int lcl_line = glb_vectTopicsHtmlList.size();
			String lcl_topicsHtmlDatas = new String();
			lcl_topicsHtmlDatas = glb_htmlDatas_0[0][0]+lcl_line+glb_htmlDatas_0[0][1];
			lcl_topicsHtmlDatas += glb_htmlDatas_1[0][0]+glb_htmlDatas_1[0][1]+""+glb_htmlDatas_1[0][2];
			lcl_topicsHtmlDatas += glb_htmlDatas_2[0][0]+lcl_line+glb_htmlDatas_2[0][1]+lcl_topicsDatas[3]+glb_htmlDatas_2[0][2];
			glb_vectTopicsHtmlList.add(lcl_topicsHtmlDatas);
			lcl_ctu.AddTopicDatas(lcl_topicsDatas);
			lcl_ctu.SaveTopicsDatas();
		}
		Defines.selectTopicIsInProgress = false;
	}

	private void HyperlinkEvent(int prm_lig,int prm_col) {
		switch(prm_col) {
			case 0: {
				ConfigTopicsUpdates lcl_ctu = new ConfigTopicsUpdates();
				lcl_ctu.LoadTopicsDatas();
				String[] lcl_topicsDatas = new String[lcl_ctu.GetMaxTopicDatas()];
				lcl_topicsDatas = lcl_ctu.GetTopicDatas(prm_lig + 1);
				lcl_ctu.DelTopicDatas(lcl_topicsDatas);
				lcl_ctu.SaveTopicsDatas();
				glb_vectTopicsList.removeElementAt(prm_lig);
				glb_vectTopicsHtmlList.removeElementAt(prm_lig);
				break;
			}
			case 1: {
				ConfigTopicsUpdates lcl_ctu = new ConfigTopicsUpdates();
				lcl_ctu.LoadTopicsDatas();
				String[] lcl_topicsDatas = new String[lcl_ctu.GetMaxTopicDatas()];
				lcl_topicsDatas = lcl_ctu.GetTopicDatas(prm_lig + 1);
				lcl_ctu.ChangeTopicData(lcl_topicsDatas,0,"1");
				lcl_ctu.SaveTopicsDatas();
				lcl_topicsDatas[0] = "1";
				glb_vectTopicsList.setElementAt(lcl_topicsDatas,prm_lig);
				glb_vectTopicsHtmlList.set(prm_lig,CreateHtmlLine(prm_lig));
				break;
			}
			case 2: {
				ConfigTopicsUpdates lcl_ctu = new ConfigTopicsUpdates();
				lcl_ctu.LoadTopicsDatas();
				String[] lcl_topicsDatas = new String[lcl_ctu.GetMaxTopicDatas()];
				lcl_topicsDatas = lcl_ctu.GetTopicDatas(prm_lig + 1);
				int lcl_categorie = Integer.parseInt(lcl_topicsDatas[1]) + 1;
				int lcl_topicValue = Integer.parseInt(lcl_topicsDatas[2]);
				String lcl_url;
				try { 
					lcl_url = new WebConnectExtra().GetUrlTopicPage(lcl_categorie,lcl_topicValue);
					if (!lcl_url.equals("")) {
						lcl_topicsDatas[0] = "1";
						new ExecProcess(lcl_url);
					}
					else {
						lcl_topicsDatas[0] = "0";
					}
				}
				catch (TrustException prm_ev) { prm_ev.Action(); }
				lcl_ctu.ChangeTopicData(lcl_topicsDatas,0,lcl_topicsDatas[0]);
				lcl_ctu.SaveTopicsDatas();
				glb_vectTopicsList.setElementAt(lcl_topicsDatas,prm_lig);
				glb_vectTopicsHtmlList.set(prm_lig,CreateHtmlLine(prm_lig));
			}
		}
	}

	private void DefaultHtmlList() {
		for (int i = 0 ; i < glb_vectTopicsHtmlList.size() ; i++) {
			glb_vectTopicsHtmlList.set(i,CreateHtmlLine(i));
		}
	}
	
	private String CreateHtmlLine(int prm_lig) {
		return(CreateHtmlLine(prm_lig,-1));
	}
	
	private String CreateHtmlList() {
		return(CreateHtmlList(-1,-1));
	}
	
	private String CreateHtmlLine(int prm_lig,int prm_colSelect) {
		String lcl_htmlLine = new String();
		int lcl_topicState = Integer.parseInt(GetDataTopicsList(prm_lig,0));
		switch(prm_colSelect) {
			case -1: {
				lcl_htmlLine = glb_htmlDatas_0[0][0] + prm_lig + glb_htmlDatas_0[0][1];
				if (lcl_topicState > 1)
					lcl_htmlLine += glb_htmlDatas_1[1][0] + prm_lig + glb_htmlDatas_1[1][1] + (lcl_topicState-1) + glb_htmlDatas_1[1][2];
				else
					lcl_htmlLine += glb_htmlDatas_1[0][0] + glb_htmlDatas_1[0][1] + glb_htmlDatas_1[0][2];
				switch(lcl_topicState) {
					case 0:	{ lcl_htmlLine += glb_htmlDatas_2[2][0] + glb_htmlDatas_2[2][1] + GetDataTopicsList(prm_lig,3) + glb_htmlDatas_2[2][2]; break; }
					case 1: { lcl_htmlLine += glb_htmlDatas_2[0][0] + prm_lig + glb_htmlDatas_2[0][1] + GetDataTopicsList(prm_lig,3) + glb_htmlDatas_2[0][2]; break; }
					default: { lcl_htmlLine += glb_htmlDatas_2[1][0] + prm_lig + glb_htmlDatas_2[1][1] + GetDataTopicsList(prm_lig,3) + glb_htmlDatas_2[1][2]; }
				}
				break;
			}
			case 0: {
				lcl_htmlLine = glb_htmlDatas_0[1][0]+prm_lig+glb_htmlDatas_0[1][1];
				if (lcl_topicState > 1) {
					lcl_htmlLine += glb_htmlDatas_1[1][0]+prm_lig+glb_htmlDatas_1[1][1] + (lcl_topicState-1) + glb_htmlDatas_1[1][2];
				}
				else {
					lcl_htmlLine += glb_htmlDatas_1[0][0]+glb_htmlDatas_1[0][1]+glb_htmlDatas_1[0][2];
				}
				switch(lcl_topicState) {
					case 0:	{ lcl_htmlLine += glb_htmlDatas_2[2][0]+glb_htmlDatas_2[2][1]+GetDataTopicsList(prm_lig,3)+glb_htmlDatas_2[2][2]; break; }
					case 1: { lcl_htmlLine += glb_htmlDatas_2[0][0]+prm_lig+glb_htmlDatas_2[0][1]+GetDataTopicsList(prm_lig,3)+glb_htmlDatas_2[0][2]; break; }
					default: { lcl_htmlLine += glb_htmlDatas_2[1][0]+prm_lig+glb_htmlDatas_2[1][1]+GetDataTopicsList(prm_lig,3)+glb_htmlDatas_2[1][2]; }
				}
				break;
			}
			case 1: {
				lcl_htmlLine = glb_htmlDatas_0[0][0]+prm_lig+glb_htmlDatas_0[0][1];
				if (lcl_topicState > 1) {
					lcl_htmlLine += glb_htmlDatas_1[2][0]+prm_lig+glb_htmlDatas_1[2][1] + (lcl_topicState-1) + glb_htmlDatas_1[2][2];
				}
				else {
					lcl_htmlLine += glb_htmlDatas_1[0][0]+glb_htmlDatas_1[0][1]+glb_htmlDatas_1[0][2];
				}
				switch(lcl_topicState) {
					case 0:	{ lcl_htmlLine += glb_htmlDatas_2[2][0]+glb_htmlDatas_2[2][1]+GetDataTopicsList(prm_lig,3)+glb_htmlDatas_2[2][2]; break; }
					case 1: { lcl_htmlLine += glb_htmlDatas_2[0][0]+prm_lig+glb_htmlDatas_2[0][1]+GetDataTopicsList(prm_lig,3)+glb_htmlDatas_2[0][2]; break; }
					default: { lcl_htmlLine += glb_htmlDatas_2[1][0]+prm_lig+glb_htmlDatas_2[1][1]+GetDataTopicsList(prm_lig,3)+glb_htmlDatas_2[1][2]; }
				}
				break;
			}
			case 2: {
				lcl_htmlLine = glb_htmlDatas_0[0][0]+prm_lig+glb_htmlDatas_0[0][1];
				if (lcl_topicState > 1) {
					lcl_htmlLine += glb_htmlDatas_1[1][0]+prm_lig+glb_htmlDatas_1[1][1] + (lcl_topicState-1) + glb_htmlDatas_1[1][2];
				}
				else {
					lcl_htmlLine += glb_htmlDatas_1[0][0]+glb_htmlDatas_1[0][1]+glb_htmlDatas_1[0][2];
				}
				switch(lcl_topicState) {
					case 0: { lcl_htmlLine += glb_htmlDatas_2[2][0]+glb_htmlDatas_2[2][1]+GetDataTopicsList(prm_lig,3)+glb_htmlDatas_2[2][2]; break; }
					case 1: { lcl_htmlLine += glb_htmlDatas_2[4][0]+prm_lig+glb_htmlDatas_2[4][1]+GetDataTopicsList(prm_lig,3)+glb_htmlDatas_2[4][2]; break; }
					default: { lcl_htmlLine += glb_htmlDatas_2[3][0]+prm_lig+glb_htmlDatas_2[3][1]+GetDataTopicsList(prm_lig,3)+glb_htmlDatas_2[3][2]; }
				}
			}
		}
		return(lcl_htmlLine);
	}
	
	private String CreateHtmlList(int prm_lig,int prm_col) {
		String lcl_text = new String();
		if (Defines.topicsUpdateIsInProgress) {
			lcl_text = "<html><body>";
			lcl_text += "<font face=\"helvetica\" color=\"green\" size=3><b>Recherche de nouveaux messages ...</b></font>";
			lcl_text += "</body></html>";
			return(lcl_text);
		}
		lcl_text = "<html><body>";
		if (glb_vectTopicsHtmlList.size() > 0) {
			lcl_text = "<table border=\"0\" cellpadding=\"0\">";
			for (int i = 0 ; i < glb_vectTopicsHtmlList.size() ; i++) {
				lcl_text += "<tr>";
				lcl_text += (String)glb_vectTopicsHtmlList.elementAt(i);
				lcl_text += "</tr>";
			}
			lcl_text += "</table>";
		}
		if (prm_col == 2) {
			lcl_text += "<font face=\"helvetica\" color=\"#63619c\" size=2><b>";
			lcl_text += new WebMemory().GetCategories(Integer.parseInt(GetDataTopicsList(prm_lig,1)))+"  -  ";
			lcl_text += GetDataTopicsList(prm_lig,4)+"/"+GetDataTopicsList(prm_lig,5)+"  -  ";
			lcl_text += GetDataTopicsList(prm_lig,6)+"/"+GetDataTopicsList(prm_lig,7);
			lcl_text += "</b></font>";
		}
		else {
			if (!(glb_vectTopicsList.size() >= Defines.MaxTopic)) {
				if (glb_htmlDataAdd == false) {
					lcl_text += "<font face=\"helvetica\" color=\"yellow\" size=3><a href=\"add\" style=\"text-decoration:none;\"><b>ADD</b></a></font>";
				}
				else {
					lcl_text += "<font face=\"helvetica\" color=\"black\" bgcolor=\"yellow\" size=3><a href=\"add\" style=\"text-decoration:none;\"><b>ADD</b></a></font>";
				}
			}
		}
		lcl_text += "</body></html>";
		return(lcl_text);
	}
	
	private void InitDatas() {
		glb_htmlDatas_0[0][0] = "<td><font face=\"helvetica\" color=\"teal\" size=2><a href=\"col=0&lig=";
		glb_htmlDatas_0[0][1] = "\" style=\"text-decoration:none;\"><b>DEL</b></a></font></td>";
		glb_htmlDatas_0[1][0] = "<td><font face=\"helvetica\" color=black bgcolor=\"teal\" size=2><a href=\"col=0&lig=";
		glb_htmlDatas_0[1][1] = "\" style=\"text-decoration:none;\"><b>DEL</b></a></font></td>";
		glb_htmlDatas_1[0][0] = "<td width=\"35\" align=\"center\">";
		glb_htmlDatas_1[0][1] = "";
		glb_htmlDatas_1[0][2] = "</td>";
		glb_htmlDatas_1[1][0] = "<td width=\"40\" align=\"center\"><font face=\"helvetica\" color=\"lime\" size=2><a href=\"col=1&lig=";
		glb_htmlDatas_1[1][1] = "\" style=\"text-decoration:none;\"><b>+";
		glb_htmlDatas_1[1][2] = "</b></a></font></td>";
		glb_htmlDatas_1[2][0] = "<td width=\"40\" align=\"center\"><font face=\"helvetica\" color=\"black\" bgcolor=\"lime\" size=2><a href=\"col=1&lig=";
		glb_htmlDatas_1[2][1] = "\" style=\"text-decoration:none;\">+";
		glb_htmlDatas_1[2][2] = "</b></a></font></td>";
		glb_htmlDatas_2[0][0] = "<td><font face=\"helvetica\" color=\"green\" size=2><a href=\"col=2&lig=";
		glb_htmlDatas_2[0][1] = "\" style=\"text-decoration:none;\"><b>";
		glb_htmlDatas_2[0][2] = "</b></a></font></td>";
		glb_htmlDatas_2[1][0] = "<td><font face=\"helvetica\" color=\"white\" size=2><a href=\"col=2&lig=";
		glb_htmlDatas_2[1][1] = "\" style=\"text-decoration:none;\"><b>";
		glb_htmlDatas_2[1][2] = "</b></a></font></td>";
		glb_htmlDatas_2[2][0] = "<td><font face=\"helvetica\" color=\"olive\" size=2>";
		glb_htmlDatas_2[2][1] = "<strike>";
		glb_htmlDatas_2[2][2] = "</strike></font></td>";
		glb_htmlDatas_2[3][0] = "<td><font face=\"helvetica\" color=black bgcolor=\"white\" size=2><a href=\"col=2&lig=";
		glb_htmlDatas_2[3][1] = "\" style=\"text-decoration:none;\"><b>";
		glb_htmlDatas_2[3][2] = "</b></a></font></td>";
		glb_htmlDatas_2[4][0] = "<td><font face=\"helvetica\" color=black bgcolor=\"green\" size=2><a href=\"col=2&lig=";
		glb_htmlDatas_2[4][1] = "\" style=\"text-decoration:none;\"><b>";
		glb_htmlDatas_2[4][2] = "</b></a></font></td>";
	}
	
	private boolean ParamatersColAndLigIsOk(String prm_description) {
		int[] lcl_param = new int[3];
		lcl_param = GetParametersColAndLig(prm_description);
		if (lcl_param[0] == 0) return(false);
		return(true);
	}
	
	private int[] GetParametersColAndLig(String prm_description) {
		int[] lcl_param = new int[3];
		try {
			lcl_param[1] = Integer.parseInt(new WebParser().GetParameter(prm_description,"lig="));
			lcl_param[2] = Integer.parseInt(new WebParser().GetParameter(prm_description,"col="));
			lcl_param[0] = 1;
		}
		catch (NumberFormatException prm_nfe) {
			lcl_param[0] = 0;
		}
		return(lcl_param);
	}
	
	private String GetDataTopicsList(int prm_lig,int prm_ind) {
		return(((String[])glb_vectTopicsList.elementAt(prm_lig))[prm_ind]);
	}
}
