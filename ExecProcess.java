/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : ExecProcess.java
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

public class ExecProcess extends Thread {
	
	private String glb_url;

	public ExecProcess(String prm_url) {
		glb_url = prm_url;
		start();
	}
	
	public void run() {
		Runtime lcl_rt = Runtime.getRuntime();
		Process lcl_pc = null;
		try {
			ConfigFile lcl_cf = new ConfigFile();
			String lcl_browser = lcl_cf.GetValue("browser");
			if (lcl_browser.equals("aucun")) {
				new TrustException("Le navigateur n'a pas été défini.").Action();
			}
			else {
				lcl_pc = lcl_rt.exec(lcl_browser + " " + glb_url);
			}
		}
		catch (Exception prm_except) {
			new TrustException("L'exécution du navigateur a échoué.").Action();
		}
	}
}
