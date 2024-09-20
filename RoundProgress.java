/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : RoundProgress.java
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

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class RoundProgress extends JComponent {
	
	private final int glb_roundProgressFileSize = 500;
	private int glb_roundProgressValue=0;
	private int glb_roundProgressStep;
	
	public RoundProgress() {}

	public void MyUpdate() {
		glb_roundProgressValue++;
		update(getGraphics());
	}
	
	public void Init() {
		glb_roundProgressValue = 0;
		update(getGraphics());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		if (glb_roundProgressValue <= 0) { 
			g2.setPaint(new GradientPaint(getWidth(),1,Defines.UIcolForeGDrawing,0,0,Defines.UIcolBackGDrawing,true));
			g2.fillRect(0,0,getWidth(),getHeight());
			g2.setPaint(Defines.UIcolForeGDrawing);
			g2.fillRect(0,getHeight()/2,getHeight()/2,getHeight()/2);
			g2.setPaint(new GradientPaint(getWidth(),1,Defines.UIcolForeGDrawing,0,0,Defines.UIcolForeGText,true));
			g2.fillArc(0,0,getHeight(),getHeight(),90,180);
		}
		glb_roundProgressStep = glb_roundProgressValue/(glb_roundProgressFileSize/getWidth());
		g2.setPaint(new GradientPaint(getWidth(),1,Defines.UIcolForeGDrawing,0,0,Defines.UIcolForeGText,true));
		g2.fillArc(glb_roundProgressStep,0,getHeight(),getHeight(),270,180);
	}
}
