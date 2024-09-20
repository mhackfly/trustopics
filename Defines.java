/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : Defines.java
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

public class Defines {
	static final Color UIcolForeGDrawing = new Color(107,28,115);
	static final Color UIcolBackGDrawing = new Color(0,0,0);
	static final Color UIcolForeGText = new Color(255,203,0);
	static final Color UIcolForeGDrawingCDE1 = new Color(147,118,230);
	static final Color UIcolForeGDrawingCDE2 = new Color(49,73,82);
	
	static final int MaxPage = 2;
	static final int MaxTopic = 8;
	static final int Timer = 1;
	static final int WebWaitDialog = 20;
	static final int WebWaitClose = 30;
	static RoundProgress roundProgress;
	static double elpasedTime;
	static int filesDownload;
	static TrustButton trustButton;
	static boolean roundProgressIsVisible = false;
	static boolean topicsUpdateIsInProgress = false;
	static boolean eventMouseIsActivated = false;
	static boolean selectTopicIsInProgress = false;
	static boolean connectionIsOk = false;
}
