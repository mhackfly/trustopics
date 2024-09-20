/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : RoundTextField.java
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RoundTextField extends JPanel {

	private int glb_textFieldSize;
	private String glb_label;
	private String glb_text;
	private int glb_widthLabel;
	private int glb_heightLabel;
	private int glb_widthRoundTextField;
	private int glb_heightRoundTextField;
	private Font glb_font;
	private final int glb_fontSize = 14;
	private RTextField glb_rTextField;

	public String GetText() { return(glb_text); }
	public void SetText(String prm_text) { glb_rTextField.setText(prm_text); }
	public int GetWidthText() { return(glb_widthRoundTextField); }
	public int GetHeightText() { return(glb_heightRoundTextField); }

	public RoundTextField(String prm_label,String prm_text,int prm_textFieldSize) {
		glb_label = prm_label;
		glb_text = prm_text;
		glb_textFieldSize = prm_textFieldSize;
		glb_font = new Font("Dialog",Font.BOLD,glb_fontSize);
		setLayout(null);
		setBorder(BorderFactory.createEmptyBorder());
		GetLabelDimensions();
		glb_widthRoundTextField = (glb_widthLabel + glb_textFieldSize) + glb_heightLabel;
		glb_heightRoundTextField = glb_heightLabel;
		setPreferredSize(new Dimension(glb_widthRoundTextField,glb_heightRoundTextField));
		glb_rTextField = new RTextField();
		add(glb_rTextField);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(Defines.UIcolBackGDrawing);
		g2.fillRect(0,0,glb_widthRoundTextField,glb_heightRoundTextField);
		g2.setPaint(Defines.UIcolForeGDrawing);
		g2.fillRoundRect(glb_widthLabel,0,glb_textFieldSize + glb_heightLabel,glb_heightLabel,glb_heightLabel,glb_heightLabel);
		AffineTransform afT = new AffineTransform();
		Font fontAffichage = glb_font.deriveFont(afT);
		AttributedString ats = new AttributedString(glb_label);
		ats.addAttribute(TextAttribute.FONT,fontAffichage);
		AttributedCharacterIterator iter = ats.getIterator();
		FontRenderContext frc = g2.getFontRenderContext();
		TextLayout myLayout = new TextLayout(iter, frc);
		g2.setPaint(Color.WHITE);
		myLayout.draw(g2,0,glb_heightRoundTextField-4);
		glb_rTextField.setBounds(glb_widthLabel + (glb_heightLabel / 2),0,glb_textFieldSize,glb_heightLabel);
	}
	
	class RTextField extends JTextField {
		public RTextField () {
			setBorder(BorderFactory.createEmptyBorder());
			setBackground(Defines.UIcolForeGDrawing);
			setForeground(Color.ORANGE);
			setFont(glb_font);
			setCaretColor(Color.WHITE);
			setText(glb_text);
			setCaretPosition(glb_text.length());
			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == 10) {
						glb_text = getText();
						RoundTextField.this.requestFocus();
					}
				}
			});
		}
	}
	
	public void GetLabelDimensions() {
        FontMetrics fontMetrics = getFontMetrics(glb_font);
        glb_widthLabel = fontMetrics.stringWidth(glb_label);
        glb_heightLabel = fontMetrics.getHeight();
	}
}
