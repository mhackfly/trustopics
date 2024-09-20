/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : WebWait.java
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

public class WebWait extends Thread {
	
	private final int glb_widthBorder = 6;
	private final int glb_fontSize = 12;
	private final String glb_text = "Connexion en cours...";
	private JTextField glb_textField;
	private JPanelCenter glb_centerPanel;
	private JWindow glb_window;
	private Font glb_font;
	
	public WebWait() { start();	}
	
	public void run() {
		float lcl_cptCloseBox = Defines.WebWaitClose;
		boolean lcl_stop = false;
		int lcl_cpt = 0; while(lcl_cpt < Defines.WebWaitDialog) {
			if (Defines.connectionIsOk) { lcl_stop = true; break; }
			try { sleep(1000); }
			catch(InterruptedException prm_ie) {}
			lcl_cpt ++;
		}
		if (!lcl_stop) {
			OpenBox();
			while(!Defines.connectionIsOk) {
				glb_centerPanel.SetColor(Color.WHITE);
				glb_centerPanel.update(glb_centerPanel.getGraphics());
				try { sleep(250); }
				catch(InterruptedException prm_ie) {}
				glb_centerPanel.SetColor(Color.BLACK);
				glb_centerPanel.update(glb_centerPanel.getGraphics());
				try { sleep(250); }
				catch(InterruptedException prm_ie) {}
				glb_textField.setText(" >> Délai : " + (int)lcl_cptCloseBox + " <<");
				lcl_cptCloseBox -= 0.5;
				if (lcl_cptCloseBox < 0) {
					glb_window.dispose();
					new TrustException("-= ERREUR =-",true).Action();
					System.exit(1);
				}
			}
			CloseBox();
		}
	}
	
	private void CloseBox() {
		glb_window.dispose();
	}
	
	private void OpenBox() {
		glb_font = new Font("Dialog",Font.BOLD,glb_fontSize);
		JPanelTop lcl_topPanel = new JPanelTop();
		lcl_topPanel.setLayout(null);
		lcl_topPanel.setBackground(Defines.UIcolForeGDrawingCDE1);
		lcl_topPanel.setPreferredSize(new Dimension(1,glb_widthBorder));
		JPanelBottom lcl_BottomPanel = new JPanelBottom();
		lcl_BottomPanel.setLayout(null);
		lcl_BottomPanel.setBackground(Defines.UIcolForeGDrawingCDE1);
		lcl_BottomPanel.setPreferredSize(new Dimension(1,glb_widthBorder));
		JPanelRight lcl_topRight = new JPanelRight();
		lcl_topRight.setLayout(null);
		lcl_topRight.setBackground(Defines.UIcolForeGDrawingCDE1);
		lcl_topRight.setPreferredSize(new Dimension(glb_widthBorder,1));
		JPanelLeft lcl_topLeft = new JPanelLeft();
		lcl_topLeft.setLayout(null);
		lcl_topLeft.setBackground(Defines.UIcolForeGDrawingCDE1);
		lcl_topLeft.setPreferredSize(new Dimension(glb_widthBorder,1));
		glb_centerPanel = new JPanelCenter();
		glb_centerPanel.setLayout(null);
		glb_centerPanel.setBackground(Defines.UIcolBackGDrawing);
		glb_window = new JWindow();
		glb_window.setSize(300,100);
		glb_window.setLocationRelativeTo(null);
		glb_window.getContentPane().setLayout(new BorderLayout());
		glb_window.getContentPane().add(lcl_topPanel,BorderLayout.NORTH);
		glb_window.getContentPane().add(lcl_BottomPanel,BorderLayout.SOUTH);
		glb_window.getContentPane().add(lcl_topRight,BorderLayout.EAST);
		glb_window.getContentPane().add(glb_centerPanel,BorderLayout.CENTER);
		glb_window.getContentPane().add(lcl_topLeft,BorderLayout.WEST);
		glb_window.setVisible(true);

	}
	
	public class JPanelTop extends JPanel {
		public JPanelTop() {}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.brighter());
			g2.drawLine(0,0,getWidth(),0);
			g2.drawLine(0,0,0,getHeight());
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.darker());
			g2.drawLine(glb_widthBorder,getHeight()-1,getWidth()-glb_widthBorder,getHeight()-1);
			g2.drawLine(getWidth()-1,1,getWidth()-1,getHeight());
		}
	}

	public class JPanelBottom extends JPanel {
		public JPanelBottom() {}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.brighter());
			g2.drawLine(glb_widthBorder,0,getWidth()-glb_widthBorder,0);
			g2.drawLine(0,0,0,getHeight());
			g2.setPaint(Defines.UIcolForeGDrawingCDE1.darker());
			g2.drawLine(1,getHeight()-1,getWidth()-1,getHeight()-1);
			g2.drawLine(getWidth()-1,0,getWidth()-1,getHeight()-1);
		}
	}

	public class JPanelRight extends JPanel {
		public JPanelRight() {}
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

	public class JPanelLeft extends JPanel {
		public JPanelLeft() {}
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

	public class JPanelCenter extends JPanel {
		private int glb_widthText;
		private int glb_heightText;
		private Color glb_color = Color.WHITE;
		void SetColor(Color prm_color) { glb_color = prm_color; }
		public JPanelCenter() {
			GetTextDimensions();
			glb_textField = new JTextField();
			glb_textField.setEditable(false);
			glb_textField.setBorder(BorderFactory.createEmptyBorder());
			glb_textField.setBackground(Color.BLACK);
			glb_textField.setForeground(Color.ORANGE);
			glb_textField.setFont(new Font("Dialog",Font.BOLD,10));
			glb_textField.setText("");
			add(glb_textField);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
	        glb_textField.setBounds(0,0,getWidth(),16);
			AffineTransform afT = new AffineTransform();
			Font fontAffichage = glb_font.deriveFont(afT);
			AttributedString ats = new AttributedString(glb_text);
			ats.addAttribute(TextAttribute.FONT,fontAffichage);
			AttributedCharacterIterator iter = ats.getIterator();
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout myLayout = new TextLayout(iter, frc);
			g2.setPaint(glb_color);
			myLayout.draw(g2,(getWidth() - glb_widthText) / 2,((getHeight() - glb_heightText) / 2) + (glb_heightText / 2));
		}
		public void GetTextDimensions() {
	        FontMetrics fontMetrics = getFontMetrics(glb_font);
	        glb_widthText = fontMetrics.stringWidth(glb_text);
	        glb_heightText = fontMetrics.getHeight();
		}
	}
}
