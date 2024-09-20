/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : TrustButton.java
 */

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TrustButton extends JLabel {
	private ImageIcon glb_logo;
	int GetWidth() {return(glb_logo.getIconWidth());}
	int GetHeight() {return(glb_logo.getIconHeight());}
	public TrustButton() {
		try {
			glb_logo = new ImageIcon(new URL(new TrustDatas().GetTrustButton()));
			setIcon(glb_logo);
		}
	    catch (Exception ex) {
	    }
	}
}
