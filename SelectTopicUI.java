/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : SelectTopicUI.java
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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class SelectTopicUI {
	
	private final int glb_topPanelHeight = 40;
	private final int glb_bottomPanelHeight = 40;
	private final int glb_RoundProgressWidthComponent = 120;
	private final int glb_RoundProgressHeightComponent = 20;
	private int glb_page = -1;
	private int glb_categorie = -1;
	private int glb_topic = -1;
	public int GetPage() { return(glb_page); }
	public int GetCategorie() { return(glb_categorie); }
	public int GetTopic() { return(glb_topic); }
	public boolean TopicIsSelected() { if (glb_topic != -1) return(true); return(false); }
	private JTextPane glb_textPane;
	private JDialog glb_dialog;
	private boolean glb_topicsList[];
	
	public SelectTopicUI() {
		JFrame lcl_frame = new JFrame();
		glb_dialog = new JDialog(lcl_frame,"Selection topic",true);
		glb_dialog.setBackground(Defines.UIcolBackGDrawing);
		glb_dialog.getContentPane().setLayout(new BorderLayout());
		glb_dialog.setSize(670,400);
		glb_dialog.setResizable(false);
		glb_dialog.setLocationRelativeTo(null);
		glb_dialog.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent evt) { Defines.roundProgressIsVisible = false;glb_dialog.dispose(); }});
		MouseActions lcl_mouseActions = new MouseActions();
		JPanelTop lcl_topPanel = new JPanelTop(lcl_mouseActions);
		lcl_topPanel.setLayout(null);
		lcl_topPanel.setBackground(Defines.UIcolForeGDrawing);
		lcl_topPanel.setPreferredSize(new Dimension(1,glb_topPanelHeight));
		glb_dialog.getContentPane().add(lcl_topPanel,BorderLayout.NORTH);
		JPanelCenter lcl_centerPanel = new JPanelCenter(lcl_mouseActions);
		lcl_centerPanel.setLayout(null);
		lcl_centerPanel.setBackground(Defines.UIcolBackGDrawing);
		glb_dialog.getContentPane().add(lcl_centerPanel,BorderLayout.CENTER);
		JPanelBottom lcl_bottomPanel = new JPanelBottom(lcl_mouseActions);
		lcl_bottomPanel.setLayout(null);
		lcl_bottomPanel.setBackground(Defines.UIcolForeGDrawing);
		lcl_bottomPanel.setPreferredSize(new Dimension(1,glb_bottomPanelHeight));
		glb_dialog.getContentPane().add(lcl_bottomPanel,BorderLayout.SOUTH);
		glb_dialog.setVisible(true);
	}
	
	public class MouseActions implements ActionListener,HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent prm_ev) {
			if (prm_ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				glb_topic = Integer.parseInt(prm_ev.getDescription()); 
				glb_textPane.setText(UpdateTopicsList(glb_topic));
				return;
			}
			if (prm_ev.getEventType() == HyperlinkEvent.EventType.EXITED) {
				glb_textPane.setText(UpdateTopicsList(-1));
				return;
			}
			if (prm_ev.getEventType() == HyperlinkEvent.EventType.ENTERED) {
				glb_textPane.setText(UpdateTopicsList(Integer.parseInt(prm_ev.getDescription())));
				return;
			}
		}
		public void actionPerformed(ActionEvent prm_ev) {
			String lcl_cmd = prm_ev.getActionCommand();
			if (lcl_cmd.equals("ButtonOk")) { Defines.roundProgressIsVisible = false;glb_dialog.dispose(); }
			if (lcl_cmd.equals("ButtonAnnuler")) { Defines.roundProgressIsVisible = false;glb_topic = -1;glb_dialog.dispose(); }
			for (int i = 0 ; i < new WebMemory().GetCategoriesSize() ; i++) {
				if (lcl_cmd.equals("ButtonCategorie"+i)) {
					glb_categorie = i;
					glb_topic = -1; glb_page = 0;
					if (glb_categorie != -1 && glb_page != -1) {
						new WebMemory().UpdateTopics(glb_categorie+1,glb_page+1);
						TopicsListSelected();
					}
					glb_textPane.setText(UpdateTopicsList(-1));
					return;
				}
			}
			for (int i = 0 ; i < Defines.MaxPage ; i++) {
				if (lcl_cmd.equals("ButtonPage"+i)) {
					glb_page = i;
					glb_topic = -1;
					if (glb_categorie != -1 && glb_page != -1) {
						new WebMemory().UpdateTopics(glb_categorie+1,glb_page+1);
						TopicsListSelected();
					}
					glb_textPane.setText(UpdateTopicsList(-1));
					return;
				}
			}
		}
	}
	
	public class JPanelCenter extends JPanel {
		final int glb_separatorWidth = 4;
		final float[] glb_separatorStyle = {12,6};
		RoundButton glb_roundButtonsCategories[];
		int glb_xBut,glb_yBut;
		public JPanelCenter(MouseActions prm_mouseActions) {
			glb_roundButtonsCategories = new RoundButton[new WebMemory().GetCategoriesSize()];
			for (int i = 0 ; i < new WebMemory().GetCategoriesSize() ; i++) {
				glb_roundButtonsCategories[i] = new RoundButton(new WebMemory().GetCategories(i));
				glb_roundButtonsCategories[i].setBackground(Defines.UIcolBackGDrawing);
				glb_roundButtonsCategories[i].setActionCommand("ButtonCategorie"+String.valueOf(i));
				glb_roundButtonsCategories[i].addActionListener(prm_mouseActions);
				add(glb_roundButtonsCategories[i]);
			}
			HTMLEditorKit kit = new HTMLEditorKit();
			HTMLDocument doc = (HTMLDocument) (kit.createDefaultDocument());
			glb_textPane = new JTextPane();
			glb_textPane.addMouseListener(new MouseAdapter() {
				public void mouseExited(MouseEvent evt) {
					glb_textPane.setText(UpdateTopicsList(-1));
				}
			});
			glb_textPane.addHyperlinkListener(prm_mouseActions);
			glb_textPane.setEditable(false);
			glb_textPane.setEditorKit(kit);
			glb_textPane.setDocument(doc);
			glb_textPane.setContentType("text/html");
			glb_textPane.setBackground(Defines.UIcolBackGDrawing);
			glb_textPane.setText(UpdateTopicsList(-1));
			add(glb_textPane);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolForeGText);
			g2.setStroke( new BasicStroke(glb_separatorWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,10.0f,glb_separatorStyle,14));
			g2.drawLine(getWidth()/2,0,getWidth()/2,getHeight()-16);
			glb_xBut = 0;glb_yBut = 24;
			for (int i = 0 ; i < new WebMemory().GetCategoriesSize() ; i++) {
				glb_xBut = (getWidth()/2 - glb_roundButtonsCategories[i].GetWidthText())-glb_separatorWidth;
				glb_roundButtonsCategories[i].setBounds(glb_xBut,glb_yBut,glb_roundButtonsCategories[i].GetWidthText(),glb_roundButtonsCategories[i].GetHeightText());
				glb_yBut += glb_roundButtonsCategories[i].GetHeightText(); 
			}
			glb_textPane.setBounds((getWidth()/2)+(glb_separatorWidth*2),0,(getWidth()/2)-glb_separatorWidth,getHeight());
		}
	}
	
	public class JPanelTop extends JPanel {
		int glb_x,glb_y,glb_w,glb_h;
		RoundButton glb_roundButtonsPage[];
		public JPanelTop(MouseActions prm_mouseActions) {
			glb_roundButtonsPage = new RoundButton[Defines.MaxPage];
			for (int i = 0 ; i < Defines.MaxPage ; i++) {
				glb_roundButtonsPage[i] = new RoundButton(Integer.toString(i+1));
				glb_roundButtonsPage[i].setBackground(Defines.UIcolBackGDrawing);
				glb_roundButtonsPage[i].setActionCommand("ButtonPage"+String.valueOf(i));
				glb_roundButtonsPage[i].addActionListener(prm_mouseActions);
				add(glb_roundButtonsPage[i]);
			}
			add(Defines.trustButton);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolBackGDrawing);
			g2.fillRoundRect(getWidth()/2,getHeight()-glb_roundButtonsPage[0].GetHeightText(),getWidth(),getHeight(),getHeight()/2,getHeight()/2);
			g2.setPaint(new GradientPaint(getWidth()/2,1,Defines.UIcolForeGDrawing,0,0,Color.ORANGE,true));
			g2.fillRect(getWidth()-(getWidth()/2),(getHeight()-glb_roundButtonsPage[0].GetHeightText())-2,getWidth(),2);
			glb_x = getWidth();
			for (int i = 0 ; i < Defines.MaxPage ; i++) {
				glb_w = glb_roundButtonsPage[i].GetWidthText();
				glb_h = glb_roundButtonsPage[i].GetHeightText();
				glb_x -= glb_w;
				glb_y = getHeight()-glb_h;
				glb_roundButtonsPage[i].setBounds(glb_x,glb_y,glb_w,glb_h);
			}
			int lcl_y = (getHeight()- Defines.trustButton.GetHeight())/2;
			Defines.trustButton.setBounds(lcl_y,lcl_y,Defines.trustButton.GetWidth(),Defines.trustButton.GetHeight());
		}
	}
	
	public class JPanelBottom extends JPanel {
		int glb_w,glb_h;
		RoundButton glb_roundButtonOk;
		RoundButton glb_roundButtonAnnuler;
		public JPanelBottom(MouseActions prm_mouseActions) {
			glb_roundButtonOk = new RoundButton(" Ok ");
			glb_roundButtonOk.setBackground(Defines.UIcolBackGDrawing);
			glb_roundButtonOk.setActionCommand("ButtonOk");
			glb_roundButtonOk.addActionListener(prm_mouseActions);
			add(glb_roundButtonOk);
			glb_roundButtonAnnuler = new RoundButton(" Annuler ",Defines.UIcolForeGDrawing,Color.LIGHT_GRAY,14);
			glb_roundButtonAnnuler.setBackground(Defines.UIcolForeGDrawing);
			glb_roundButtonAnnuler.setActionCommand("ButtonAnnuler");
			glb_roundButtonAnnuler.addActionListener(prm_mouseActions);
			add(glb_roundButtonAnnuler);
			Defines.roundProgress = new RoundProgress();
			Defines.roundProgressIsVisible = true;
			add(Defines.roundProgress);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			glb_w = glb_roundButtonOk.GetWidthText();
			glb_h = glb_roundButtonOk.GetHeightText();
			glb_roundButtonOk.setBounds((getWidth()/2)-(glb_w/2),(getHeight()/2)-(glb_h/2),glb_w,glb_h);
			glb_roundButtonAnnuler.setBounds(8,getHeight()-glb_roundButtonAnnuler.GetHeightText()-11,glb_roundButtonAnnuler.GetWidthText(),glb_roundButtonAnnuler.GetHeightText());
			Defines.roundProgress.setBounds(getWidth()-(glb_RoundProgressWidthComponent + 40),0,glb_RoundProgressWidthComponent,glb_RoundProgressHeightComponent);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolBackGDrawing);
			g2.fillRoundRect(200,0,getWidth()-400,getHeight(),getHeight(),getHeight());
			g2.setPaint(new GradientPaint(200,1,Defines.UIcolForeGDrawing,0,0,Color.ORANGE,true));
			g2.fillRect(0,0,200,2);
		}
	}
	
	private String UpdateTopicsList(int prm_topicSelected) {
		if (glb_categorie == -1) return("");
		WebMemory lcl_wm = new WebMemory();
		String lcl_topic;
		String lcl_text = new String();
		lcl_text = "<html><font face=\"helvetica\" color=\"#ffa000\" size=3><body>";
		lcl_text += "<font color=white size=4><b>"+lcl_wm.GetCategories(glb_categorie)+"</b> ("+(glb_page+1)+") "+"<br></font>";
		for (int i = 0 ; i < lcl_wm.GetTopicsMessagesSize() ; i++) {
			lcl_topic = lcl_wm.GetTopicMessage(i);
			if (i == prm_topicSelected) {
				if (i == glb_topic)
					lcl_text += "<font color=black bgcolor=white>" + GetHRefStart(i) + lcl_topic+"<br>" + GetHRefEnd(i) + "</font>";
				else
					lcl_text += "<font color=black bgcolor=\"#ffa000\">" + GetHRefStart(i) + lcl_topic+"<br>" + GetHRefEnd(i) + "</font>";
			}
			else {
				if (i == glb_topic) {
					lcl_text += "<font color=white>" + GetHRefStart(i) + lcl_topic+"<br>" + GetHRefEnd(i) + "</font>";
				}
				else {
					lcl_text +=  GetHRefStart(i) + lcl_topic+"<br>" + GetHRefEnd(i);
				}
			}
		}
		if (prm_topicSelected != -1) {
			lcl_text += "<font face=\"helvetica\" color=\"#63619c\" size=2><b>";
			lcl_text += lcl_wm.GetTopicAnswer(prm_topicSelected) + "/" + lcl_wm.GetTopicView(prm_topicSelected) + " - ";
			lcl_text += lcl_wm.GetTopicAuthor(prm_topicSelected) + "/" + lcl_wm.GetTopicAuthorAnswer(prm_topicSelected);
			lcl_text += "</b></font>";
		}
		lcl_text += "</body></font>></html>";
		return(lcl_text);
	}
	
	private String GetHRefStart(int lcl_indexMessage) {
		if (!glb_topicsList[lcl_indexMessage]) return("<a href=" + lcl_indexMessage + " style=\"text-decoration:none ;\">");
		return("<font color=gray>");
	}
	
	private String GetHRefEnd(int lcl_indexMessage) {
		if (!glb_topicsList[lcl_indexMessage]) return("</a>");
		return("</font>");
	}
	
	private void TopicsListSelected() {
		WebMemory lcl_wm = new WebMemory();
		glb_topicsList = new boolean[lcl_wm.GetTopicsMessagesSize()];
		for (int i = 0 ; i < lcl_wm.GetTopicsMessagesSize() ; i++) glb_topicsList[i] = false;
		int lcl_topicNb;
		ConfigTopicsUpdates lcl_ctu;
		lcl_ctu = new ConfigTopicsUpdates();
		lcl_ctu.LoadTopicsDatas();
		lcl_topicNb = lcl_ctu.GetTotalTopic();
		if (lcl_topicNb <= 0) return;
		String[] lcl_topicsDatasFile = new String[lcl_ctu.GetMaxTopicDatas()];
		for (int i = 0 ; i < lcl_wm.GetTopicsMessagesSize() ; i++) {
			for (int j = 0 ; j < lcl_topicNb ; j++) {
				lcl_topicsDatasFile = lcl_ctu.GetTopicDatas(j + 1);
				if (lcl_topicsDatasFile[1].equals(String.valueOf(glb_categorie)) && lcl_topicsDatasFile[2].equals(lcl_wm.GetTopicValue(i)))
					glb_topicsList[i] = true;
			}
		}
	}
}
