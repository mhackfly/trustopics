/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : RoundProgress.java
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
