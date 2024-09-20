/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : Start.java
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

public class Start {
	public static void main(String[] args) {
		new WebWaitExtra().Start();
		try { new UrlConnect();	}
		catch (TrustException prm_ev) {	prm_ev.Action(); }
		new WebWaitExtra().Stop();
		try { 
			new ConfigFile().Open();
			new ConfigFile().Save();
			ConfigFile lcl_cf = new ConfigFile();
			if (lcl_cf.GetValue("browser") == null) { lcl_cf.SetValue("browser","firefox"); }
			if (lcl_cf.GetValue("xwin") == null) { lcl_cf.SetValue("xwin","200"); }
			if (lcl_cf.GetValue("ywin") == null) { lcl_cf.SetValue("ywin","200"); }
			lcl_cf.Save();
		}
		catch (TrustException prm_ev) { prm_ev.Action(); }
		Defines.trustButton = new TrustButton();
		new WebMemory().UpdateCategories();
		new TrustopicsUI();
	}
}
