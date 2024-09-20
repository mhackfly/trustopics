/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : Defines.java
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
