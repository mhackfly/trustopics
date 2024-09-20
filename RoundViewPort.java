/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : RoundViewPort.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RoundViewPort extends JPanel {

	private int glb_widthBar = 40;
	private int glb_heightBar = 0;
	private int glb_YMousePressed;
	private int glb_YMouse;
	private JTextArea glb_textArea;
	private JViewport glb_viewPort;
	private ScrollZone glb_scrollZone;
	private ScrollBar glb_scrollBar;
	
	public RoundViewPort(JTextArea prm_textArea) {
		glb_textArea = prm_textArea;
		glb_viewPort = new JViewport();
		glb_scrollZone = new ScrollZone();
		glb_viewPort.add(glb_textArea);
		glb_viewPort.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				glb_scrollZone.update(glb_scrollZone.getGraphics());
			}
		});
		setLayout(new BorderLayout());
		add(glb_viewPort,BorderLayout.CENTER);
		add(glb_scrollZone,BorderLayout.EAST);
	}

	class ScrollZone extends JPanel {
		public ScrollZone () {
			setLayout(null);
			glb_scrollBar = new ScrollBar();
			setBackground(Color.BLACK);
			add(glb_scrollBar);
		}
	    public Dimension getPreferredSize() {
	        return new Dimension(glb_widthBar,1);
	    }
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolForeGDrawing);
			g2.fillRect(0,0,glb_widthBar,glb_widthBar/2);
			g2.fillRect(0,getHeight() - glb_widthBar / 2,glb_widthBar,glb_widthBar/2);
			g2.setPaint(Defines.UIcolBackGDrawing);
			g2.fillRoundRect(0,0,glb_widthBar,glb_widthBar,glb_widthBar,glb_widthBar);
			g2.fillRoundRect(0,getHeight() - glb_widthBar,glb_widthBar,glb_widthBar,glb_widthBar,glb_widthBar);
			
			float lcl_scrollZoneGetHeight = glb_scrollZone.getHeight();
			float lcl_textAreaGetHeight = glb_textArea.getHeight();
			float lcl_calc = (((lcl_scrollZoneGetHeight / lcl_textAreaGetHeight) * 100) * lcl_scrollZoneGetHeight) / 100;
			glb_heightBar = (int)lcl_calc;
			if (glb_heightBar < glb_widthBar) glb_heightBar = glb_widthBar;
			if (glb_heightBar > glb_textArea.getHeight()) glb_heightBar = glb_textArea.getHeight();

			if (glb_YMouse < 0) {
				glb_scrollBar.setBounds(0,0,glb_widthBar,glb_heightBar);
			}
			else {
				if (glb_YMouse > glb_scrollZone.getHeight() - glb_heightBar) {
					glb_scrollBar.setBounds(0,glb_scrollZone.getHeight() - glb_heightBar,glb_widthBar,glb_heightBar);
				}
				else {
					glb_scrollBar.setBounds(0,glb_YMouse,glb_widthBar,glb_heightBar);
				}
			}
		}
	}
	
	class ScrollBar extends JComponent {
		public ScrollBar () {
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent prm_evt) {
					glb_YMousePressed = prm_evt.getY();
				}
			});
			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent prm_evt) {
					int lcl_xViewPort = 0;
					int lcl_yViewPort = 0;
					//glb_XMouse = getX() + prm_evt.getX() - glb_XMousePressed;
					glb_YMouse = getY() + prm_evt.getY() - glb_YMousePressed;
					if (glb_YMouse < 0) {
						lcl_yViewPort = 0;
					}
					else {
						if (glb_YMouse > glb_scrollZone.getHeight() - glb_heightBar) {
							lcl_yViewPort = glb_textArea.getHeight() - glb_scrollZone.getHeight();
						}
						else {
							float lcl_scrollZoneGetHeight = glb_scrollZone.getHeight();
							float lcl_textAreaGetHeight = glb_textArea.getHeight();
							float lcl_step = ((lcl_textAreaGetHeight - lcl_scrollZoneGetHeight) / (lcl_scrollZoneGetHeight - glb_heightBar)) * glb_YMouse;
							lcl_yViewPort = (int)lcl_step;
						}
					}
					glb_viewPort.setViewPosition(new Point(lcl_xViewPort,lcl_yViewPort));
					setLocation(0,glb_YMouse);
				}
			});
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolForeGDrawing);
			g2.fillRoundRect(0,0,glb_widthBar,glb_heightBar,glb_widthBar,glb_widthBar);
		}
	}
}
