/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : TrustopicsUI.java
 * 
 * Trustopics est un utilitaire qui permet de suivre
 * les topics d'un forum, en informant de la prsence de nouveaux
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Timer;

import javax.swing.JPanel;
import javax.swing.JWindow;

public class TrustopicsUI extends JWindow {
	private final int glb_widthBorder = 4;
	private final int glb_widthLeftBorder = 32;
	private final int glb_widthStroke = 32;
	private final int glb_widthStrokeRound = 23;
	private final int glb_widthStrokeBorder = 95;
	private TopicsTextPane glb_topicsTextPane;
	private int glb_XMouse;
	private int glb_YMouse;
	public TrustopicsUI() {
		new Logs().CreateWin();
		new Options().CreateWin();
		glb_topicsTextPane = new TopicsTextPane();
		TopicsUpdates lcl_topicsUpdates = new TopicsUpdates(glb_topicsTextPane);
		Timer lcl_timer = new Timer();
		lcl_timer.schedule(lcl_topicsUpdates,5000,(Defines.Timer*60000));
		setBackground(Defines.UIcolBackGDrawing);
		setSize(460,242);
		ConfigFile lcl_cf = new ConfigFile();
		int lcl_xWin = Integer.valueOf(lcl_cf.GetValue("xwin")).intValue();
		int lcl_yWin = Integer.valueOf(lcl_cf.GetValue("ywin")).intValue();
		setLocation(lcl_xWin,lcl_yWin);
		getContentPane().setLayout(new BorderLayout());
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent prm_evt) {
				glb_XMouse = prm_evt.getX();
				glb_YMouse = prm_evt.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent prm_evt) {
				if ((prm_evt.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
					if (DragIsOk()) {
						int dx = getX() + prm_evt.getX() - glb_XMouse;
						int dy = getY() + prm_evt.getY() - glb_YMouse;
						setLocation(dx, dy);
						try { 
							ConfigFile lcl_cf = new ConfigFile();
							lcl_cf.SetValue("xwin",String.valueOf(dx));
							lcl_cf.SetValue("ywin",String.valueOf(dy));
							lcl_cf.Save();
						}
						catch (TrustException prm_ev) { prm_ev.Action(); }
					}
				}
			}
		});
		MouseActions lcl_mouseActions = new MouseActions();
		JPanelCenter lcl_topCenter = new JPanelCenter(lcl_mouseActions);
		lcl_topCenter.setLayout(null);
		lcl_topCenter.setBackground(Defines.UIcolBackGDrawing);
		getContentPane().add(lcl_topCenter,BorderLayout.CENTER);
		JPanelTop lcl_topPanel = new JPanelTop();
		lcl_topPanel.setLayout(null);
		lcl_topPanel.setBackground(Defines.UIcolForeGDrawingCDE1);
		lcl_topPanel.setPreferredSize(new Dimension(1,glb_widthBorder));
		getContentPane().add(lcl_topPanel,BorderLayout.NORTH);
		JPanelBottom lcl_BottomPanel = new JPanelBottom();
		lcl_BottomPanel.setLayout(null);
		lcl_BottomPanel.setBackground(Defines.UIcolForeGDrawingCDE1);
		lcl_BottomPanel.setPreferredSize(new Dimension(1,glb_widthBorder));
		getContentPane().add(lcl_BottomPanel,BorderLayout.SOUTH);
		JPanelRight lcl_topRight = new JPanelRight();
		lcl_topRight.setLayout(null);
		lcl_topRight.setBackground(Defines.UIcolForeGDrawingCDE1);
		lcl_topRight.setPreferredSize(new Dimension(glb_widthBorder,1));
		getContentPane().add(lcl_topRight,BorderLayout.EAST);
		JPanelLeft lcl_topLeft = new JPanelLeft(lcl_timer);
		lcl_topLeft.setLayout(null);
		lcl_topLeft.setBackground(Defines.UIcolForeGDrawingCDE1);
		lcl_topLeft.setPreferredSize(new Dimension(glb_widthLeftBorder,1));
		getContentPane().add(lcl_topLeft,BorderLayout.WEST);
		//setAlwaysOnTop(false);
		setVisible(true);
	}

	public class MouseActions implements ActionListener {
		public void actionPerformed(ActionEvent prm_ev) {
			String lcl_cmd = prm_ev.getActionCommand();
			if (lcl_cmd.equals("Trustonme") && !Defines.topicsUpdateIsInProgress) {
				new ExecProcess(new TrustDatas().GetTrustSite());
			}
			if (lcl_cmd.equals("logs") && !Defines.topicsUpdateIsInProgress) {
				new Logs().OpenWin();
			}
			if (lcl_cmd.equals("Options") && !Defines.topicsUpdateIsInProgress) {
				new Options().OpenWin();
			}
			if (lcl_cmd.equals("raz") && !Defines.topicsUpdateIsInProgress) {
				glb_topicsTextPane.ExternRaz();
			}
			if (lcl_cmd.equals("vider") && !Defines.topicsUpdateIsInProgress) {
				glb_topicsTextPane.ExternClear();
			}
		}
	}
	
	public class JPanelCenter extends JPanel {
		
		private RoundButton glb_roundButtonTrustonme;
		private RoundButton glb_roundButtonOptions;
		private RoundButton glb_roundButtonLogs;
		private RoundButton glb_roundButtonRAZ;
		private RoundButton glb_roundButtonVider;
		
		public JPanelCenter(MouseActions prm_mouseActions) {
			add(glb_topicsTextPane);
			glb_roundButtonTrustonme = new RoundButton("Trustonme",Defines.UIcolForeGDrawing,Color.GREEN,12);
			glb_roundButtonTrustonme.setBackground(Defines.UIcolForeGDrawing);
			glb_roundButtonTrustonme.setActionCommand("Trustonme");
			glb_roundButtonTrustonme.addActionListener(prm_mouseActions);
			add(glb_roundButtonTrustonme);
			glb_roundButtonOptions = new RoundButton("Options",Defines.UIcolForeGDrawing,Color.GREEN,12);
			glb_roundButtonOptions.setBackground(Defines.UIcolForeGDrawing);
			glb_roundButtonOptions.setActionCommand("Options");
			glb_roundButtonOptions.addActionListener(prm_mouseActions);
			add(glb_roundButtonOptions);
			glb_roundButtonLogs = new RoundButton("logs",Defines.UIcolForeGDrawing,Color.LIGHT_GRAY,12);
			glb_roundButtonLogs.setBackground(Defines.UIcolForeGDrawing);
			glb_roundButtonLogs.setActionCommand("logs");
			glb_roundButtonLogs.addActionListener(prm_mouseActions);
			add(glb_roundButtonLogs);
			glb_roundButtonRAZ = new RoundButton("raz",Defines.UIcolForeGDrawing,Color.LIGHT_GRAY,12);
			glb_roundButtonRAZ.setBackground(Defines.UIcolForeGDrawing);
			glb_roundButtonRAZ.setActionCommand("raz");
			glb_roundButtonRAZ.addActionListener(prm_mouseActions);
			add(glb_roundButtonRAZ);
			glb_roundButtonVider = new RoundButton("vider",Defines.UIcolForeGDrawing,Color.LIGHT_GRAY,12);
			glb_roundButtonVider.setBackground(Defines.UIcolForeGDrawing);
			glb_roundButtonVider.setActionCommand("vider");
			glb_roundButtonVider.addActionListener(prm_mouseActions);
			add(glb_roundButtonVider);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			CreateStroke(g2);
			glb_topicsTextPane.setBounds(glb_widthStroke,glb_widthStroke,getWidth()-glb_widthStroke,getHeight()-(2*glb_widthStroke));
			int lcl_xButT,lcl_yButT,lcl_wButT,lcl_hButT;
			lcl_xButT = getWidth() - ((glb_widthStrokeBorder / 2) + glb_roundButtonTrustonme.GetWidthText() / 2);
			lcl_yButT = glb_widthStroke - ((glb_widthStroke / 2) + glb_roundButtonTrustonme.GetHeightText() / 2);
			lcl_wButT = glb_roundButtonTrustonme.GetWidthText();
			lcl_hButT = glb_roundButtonTrustonme.GetHeightText();
			glb_roundButtonTrustonme.setBounds(lcl_xButT,lcl_yButT,lcl_wButT,lcl_hButT);
			lcl_xButT = getWidth() - ((glb_widthStrokeBorder / 2) + glb_roundButtonOptions.GetWidthText() / 2);
			lcl_yButT = getHeight() - ((glb_widthStroke / 2) + glb_roundButtonOptions.GetHeightText() / 2);
			lcl_wButT = glb_roundButtonOptions.GetWidthText();
			lcl_hButT = glb_roundButtonOptions.GetHeightText();
			glb_roundButtonOptions.setBounds(lcl_xButT,lcl_yButT,lcl_wButT,lcl_hButT);
			int lcl_nbBut = 3;
			int lcl_step = glb_widthStrokeBorder;
			int lcl_xBut,lcl_yBut,lcl_wBut,lcl_hBut;
			RoundButton[] lcl_roundButton = new RoundButton[lcl_nbBut];
			lcl_roundButton[0] = glb_roundButtonLogs;
			lcl_roundButton[1] = glb_roundButtonRAZ;
			lcl_roundButton[2] = glb_roundButtonVider;
			for (int i = 0 ; i < lcl_nbBut ; i++) {
				lcl_xBut = getWidth() - lcl_step - lcl_roundButton[i].GetWidthText();
				lcl_yBut = getHeight()-lcl_roundButton[i].GetHeightText()-2;
				lcl_wBut = lcl_roundButton[i].GetWidthText();
				lcl_hBut = lcl_roundButton[i].GetHeightText(); 
				lcl_roundButton[i].setBounds(lcl_xBut,lcl_yBut,lcl_wBut,lcl_hBut);
				lcl_step += lcl_roundButton[i].GetWidthText();
			}
			AffineTransform afT = new AffineTransform();
			Font fontAffichage = new Font("Monospaced",Font.BOLD,10).deriveFont(afT);
			g2.setPaint(Color.CYAN);
			AttributedString ats = new AttributedString("Trustopics");
			ats.addAttribute(TextAttribute.FONT,fontAffichage);
			AttributedCharacterIterator iter = ats.getIterator();
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout myLayout = new TextLayout(iter, frc);
			myLayout.draw(g2,22,14);
			ats = new AttributedString("http://www.trustonme.net");
			ats.addAttribute(TextAttribute.FONT,fontAffichage);
			iter = ats.getIterator();
			frc = g2.getFontRenderContext();
			myLayout = new TextLayout(iter, frc);
			g2.setPaint(Color.CYAN);
			myLayout.draw(g2,22,227);
		}
		
		private void CreateStroke(Graphics2D prm_G2D) {
			prm_G2D.setPaint(Defines.UIcolForeGDrawing);
			prm_G2D.fillRect(glb_widthStroke,0,getWidth(),glb_widthStroke);
			prm_G2D.fillRect(glb_widthStroke,getHeight() - glb_widthStroke,getWidth(),getHeight());
			prm_G2D.fillRect(0,0,glb_widthStroke,getHeight());
			int lcl_widthRound = glb_widthStrokeRound / 2;
			int lcl_topStkX = glb_widthStroke - lcl_widthRound;
			int lcl_topStkY = lcl_topStkX;
			int lcl_topStkW = getWidth() - (glb_widthStrokeBorder + (glb_widthStroke - lcl_widthRound));
			int lcl_topStkH = glb_widthStrokeRound;
			int lcl_botStkX = lcl_topStkX;
			int lcl_botStkY = getHeight() - (glb_widthStroke + lcl_widthRound);
			int lcl_botStkW = lcl_topStkW;
			int lcl_botStkH = lcl_topStkH;
			int lcl_leftStkX = lcl_topStkX;
			int lcl_leftStkY = lcl_topStkY;
			int lcl_leftStkW = glb_widthStrokeRound;
			int lcl_leftStkH = getHeight() - (2 * glb_widthStroke) + (2 * lcl_widthRound);
			prm_G2D.setPaint(Defines.UIcolBackGDrawing);
			prm_G2D.fillRoundRect(lcl_topStkX,lcl_topStkY,lcl_topStkW,lcl_topStkH,glb_widthStrokeRound,glb_widthStrokeRound);
			prm_G2D.fillRoundRect(lcl_botStkX,lcl_botStkY,lcl_botStkW,lcl_botStkH,glb_widthStrokeRound,glb_widthStrokeRound);
			prm_G2D.fillRoundRect(lcl_leftStkX,lcl_leftStkY,lcl_leftStkW,lcl_leftStkH,glb_widthStrokeRound,glb_widthStrokeRound);
			prm_G2D.setPaint(new GradientPaint(glb_widthStrokeBorder - 12,1,Color.ORANGE,0,0,Defines.UIcolForeGDrawing,true));
			prm_G2D.fillRect(getWidth() - glb_widthStrokeBorder,glb_widthStroke - 2,glb_widthStrokeBorder,2);
			prm_G2D.fillRect(getWidth() - glb_widthStrokeBorder,getHeight() - glb_widthStroke,glb_widthStrokeBorder,2);
		}
	}

	public class JPanelLeft extends JPanel {
		
		private ExitButton glb_exitButton;
		
		public JPanelLeft(Timer prm_timer) {
			glb_exitButton = new ExitButton(prm_timer);
			add(glb_exitButton);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.brighter());
			g2.drawLine(0,0,0,getHeight());
			g2.drawLine(glb_widthLeftBorder-glb_widthBorder,1,glb_widthLeftBorder-glb_widthBorder,glb_widthLeftBorder-(2*glb_widthBorder));
			g2.drawLine(glb_widthBorder+1,glb_widthLeftBorder-(2*glb_widthBorder),glb_widthLeftBorder-glb_widthBorder,glb_widthLeftBorder-(2*glb_widthBorder));
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.darker());
			g2.drawLine(getWidth()-1,0,getWidth()-1,getHeight()-1);
			g2.drawLine(glb_widthBorder,0,glb_widthLeftBorder-glb_widthBorder,0);
			g2.drawLine(glb_widthBorder,0,glb_widthBorder,glb_widthLeftBorder-(2*glb_widthBorder));
			glb_exitButton.setBounds(glb_widthBorder+1,1,(glb_widthLeftBorder-(2*glb_widthBorder))-1,(glb_widthLeftBorder-(2*glb_widthBorder))-1);
			for(int y = glb_widthLeftBorder-glb_widthBorder ; y < getHeight()-2 ; y+=4) {
				for(int x = glb_widthBorder ; x < getWidth()-glb_widthBorder ; x+=4) {
					motif(g2,x,y);
				}
			}
		}
		private void motif(Graphics2D prm_g2,int prm_x,int prm_y) {
			prm_g2.setPaint(Defines.UIcolForeGDrawingCDE1.darker());
			prm_g2.drawLine(prm_x,prm_y,prm_x+1,prm_y);
			prm_g2.drawLine(prm_x,prm_y,prm_x,prm_y+1);
			prm_g2.setPaint(Defines.UIcolForeGDrawingCDE1.brighter());
			prm_g2.drawLine(prm_x+2,prm_y+1,prm_x+2,prm_y+2);
			prm_g2.drawLine(prm_x+1,prm_y+2,prm_x+2,prm_y+2);
		}
	}

	public class JPanelTop extends JPanel {
		
		public JPanelTop() {
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.brighter());
			g2.drawLine(0,0,getWidth(),0);
			g2.drawLine(0,0,0,getHeight());
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.darker());
			g2.drawLine(0+glb_widthLeftBorder,getHeight()-1,getWidth()-glb_widthBorder,getHeight()-1);
			g2.drawLine(getWidth()-1,1,getWidth()-1,getHeight());
		}
	}

	public class JPanelBottom extends JPanel {
		
		public JPanelBottom() {
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.brighter());
			g2.drawLine(glb_widthLeftBorder,0,getWidth()-glb_widthBorder,0);
			g2.drawLine(0,0,0,getHeight());
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.darker());
			g2.drawLine(1,getHeight()-1,getWidth()-1,getHeight()-1);
			g2.drawLine(getWidth()-1,0,getWidth()-1,getHeight()-1);
		}
	}

	public class JPanelRight extends JPanel {
		
		public JPanelRight() {
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.brighter());
			g2.drawLine(0,0,0,getHeight());
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.darker());
			g2.drawLine(getWidth()-1,0,getWidth()-1,getHeight()-1);
		}
	}
	
	private boolean DragIsOk() {
		if (glb_XMouse > glb_widthBorder && glb_XMouse < glb_widthLeftBorder-glb_widthBorder) {
			if (glb_YMouse > glb_widthLeftBorder && glb_YMouse < getHeight()-glb_widthBorder) {
				return(true);
			}
		}
		return(false);
	}
}
