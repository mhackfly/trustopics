/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : ExitButton.java
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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class ExitButton extends JButton implements MouseListener,ActionListener {
	
	private final Color glb_colText = Defines.UIcolForeGDrawingCDE1.brighter();
	private final Color glb_colBack = Defines.UIcolForeGDrawingCDE2.darker();
	private final int glb_widthStroke = 6;
	private Color glb_colTextBak;
	private Color glb_colBackBak;
	private Timer glb_timer;
	
	public ExitButton(Timer prm_timer) {
		glb_timer = prm_timer;
		glb_colTextBak = glb_colText;
		glb_colBackBak = glb_colBack;
		addMouseListener(this);
		addActionListener(this);
		setBorder(BorderFactory.createEmptyBorder());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(glb_colBackBak);
		g2.fillRect(0,0,getWidth(),getHeight());
		g2.setStroke( new BasicStroke(glb_widthStroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2.setPaint(glb_colTextBak);
		g2.drawLine(glb_widthStroke,glb_widthStroke,getWidth()-glb_widthStroke,getHeight()-glb_widthStroke);
		g2.drawLine(getWidth()-glb_widthStroke,glb_widthStroke,glb_widthStroke,getHeight()-glb_widthStroke);
	}
	
	public void mouseEntered(MouseEvent prm_evt) {
		glb_colTextBak = glb_colBack;
		glb_colBackBak = glb_colText;
		update(getGraphics());
	}
	
	public void mouseExited(MouseEvent prm_evt) {
		glb_colTextBak = glb_colText;
		glb_colBackBak = glb_colBack;
		update(getGraphics());
	}
	
	public void mouseClicked(MouseEvent prm_evt) {
	}
	
	public void mousePressed(MouseEvent prm_evt) {
		glb_colTextBak = glb_colBack;
		glb_colBackBak = glb_colText.brighter();
	}
	
	public void mouseReleased(MouseEvent prm_evt) {
	}
	
	public void actionPerformed(ActionEvent prm_evt) {
		if (!Defines.topicsUpdateIsInProgress) {
			glb_timer.cancel();
			System.exit(0);
		}
	}
}
