/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : RoundButton.java
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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class RoundButton extends JButton implements MouseListener {
	
	private String glb_text;
	private int glb_widthText;
	private int glb_heightText;
	private Color glb_colorFontText;
	private Color glb_colorFontBackRound;
	private Font glb_font;
	private int glb_fontSize = 15;
	private Color glb_backButtonColor = Defines.UIcolBackGDrawing;
	private Color glb_foreButtonColor = Defines.UIcolForeGText;
	private Color glb_clickButtonColor = Defines.UIcolForeGText.brighter();

	public RoundButton(String prm_text,Color prm_backButtonColor,Color prm_foreButtonColor,int prm_fontSize) {
		glb_backButtonColor = prm_backButtonColor;
		glb_foreButtonColor = prm_foreButtonColor;
		glb_clickButtonColor = prm_foreButtonColor.brighter();
		glb_fontSize = prm_fontSize;
		InitButton(prm_text);
	}
	
	public RoundButton(String prm_text) {
		InitButton(prm_text);
	}
	
	public void InitButton(String prm_text) {
		glb_text = prm_text;
		glb_font = new Font("Dialog",Font.BOLD,glb_fontSize);
		glb_colorFontText = glb_foreButtonColor;
		glb_colorFontBackRound = glb_backButtonColor;
		GetTextDimensions();
		addMouseListener(this);
		setBorder(BorderFactory.createEmptyBorder());
	}
	
	public int GetWidthText() { return(glb_widthText); }
	
	public int GetHeightText() { return(glb_heightText); }
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(getBackground());
		g2.fillRect(0,0,glb_widthText,glb_heightText);
		g2.setPaint(glb_colorFontBackRound);
		g2.fillRoundRect(0,0,glb_widthText,glb_heightText,glb_heightText,glb_heightText);
		AffineTransform afT = new AffineTransform();
		Font fontAffichage = glb_font.deriveFont(afT);
		AttributedString ats = new AttributedString(glb_text);
		ats.addAttribute(TextAttribute.FONT,fontAffichage);
		AttributedCharacterIterator iter = ats.getIterator();
		FontRenderContext frc = g2.getFontRenderContext();
		TextLayout myLayout = new TextLayout(iter, frc);
		g2.setPaint(glb_colorFontText);
		myLayout.draw(g2,glb_heightText/2,glb_heightText-4);
	}	

	public void mouseEntered(MouseEvent evt) {
		glb_colorFontText = glb_backButtonColor;
		glb_colorFontBackRound = glb_foreButtonColor;
		update(getGraphics());
	}
	
	public void mouseExited(MouseEvent evt) {
		glb_colorFontText = glb_foreButtonColor;
		glb_colorFontBackRound = glb_backButtonColor;
		update(getGraphics());
	}
	
	public void mouseClicked(MouseEvent evt) {
	}
	
	public void mousePressed(MouseEvent evt) {
		glb_colorFontText = glb_backButtonColor;
		glb_colorFontBackRound = glb_clickButtonColor;
		setBorder(BorderFactory.createEmptyBorder());
		update(getGraphics());
	}
	
	public void mouseReleased(MouseEvent evt) {
		if (glb_colorFontBackRound != glb_backButtonColor) {
			glb_colorFontText = glb_backButtonColor;
			glb_colorFontBackRound = glb_foreButtonColor;
			if (getGraphics() != null)
				update(getGraphics());
		}
	}
	
	public void GetTextDimensions() {
        FontMetrics fontMetrics = getFontMetrics(glb_font);
        glb_widthText = fontMetrics.stringWidth(glb_text);
        glb_heightText = fontMetrics.getHeight();
        glb_widthText += glb_heightText;
        glb_heightText++;
	}
	
	public void ClearButton() {
		glb_colorFontText = glb_foreButtonColor;
		glb_colorFontBackRound = glb_backButtonColor;
		update(getGraphics());
	}
}
