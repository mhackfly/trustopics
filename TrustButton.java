/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : TrustButton.java
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
